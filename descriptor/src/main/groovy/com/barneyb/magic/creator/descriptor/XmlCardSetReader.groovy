package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.CardSetReader
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.api.SymbolFactory
import com.barneyb.magic.creator.core.DefaultCard
import com.barneyb.magic.creator.core.DefaultCardSet
import com.barneyb.magic.creator.core.DefaultCreatureCard
import com.barneyb.magic.creator.core.DefaultCreatureLevel
import com.barneyb.magic.creator.core.DefaultFusedCard
import com.barneyb.magic.creator.core.DefaultIcon
import com.barneyb.magic.creator.core.DefaultLoyaltyAbility
import com.barneyb.magic.creator.core.DefaultPlaneswalkerCard
import com.barneyb.magic.creator.core.SimpleArtwork
import com.barneyb.magic.creator.descriptor.schema.CardSetType
import com.barneyb.magic.creator.descriptor.schema.CreatureType
import com.barneyb.magic.creator.descriptor.schema.FuseType
import com.barneyb.magic.creator.descriptor.schema.FusedSpellType
import com.barneyb.magic.creator.descriptor.schema.LandType
import com.barneyb.magic.creator.descriptor.schema.PlaneswalkerType
import com.barneyb.magic.creator.descriptor.schema.RulesTextType
import com.barneyb.magic.creator.descriptor.schema.SpellType
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory

import javax.xml.XMLConstants
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBElement
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
/**
 *
 *
 * @author barneyb
 */
class XmlCardSetReader implements CardSetReader {

    SymbolFactory symbolFactory = new DefaultSymbolFactory()

    TextParser textParser = new TextParser()

    final URL base
    final Reader reader

    def XmlCardSetReader(URL url) {
        this(url, url.newReader())
    }

    def XmlCardSetReader(URI uri) {
        this(uri.toURL())
    }

    def XmlCardSetReader(File file) {
        this(file.toURI().toURL(), file.newReader())
    }

    def XmlCardSetReader(URL base, InputStream inputStream) {
        this(base, new InputStreamReader(inputStream))
    }

    def XmlCardSetReader(URL base, Reader reader) {
        this.base = base
        this.reader = reader
    }

    CardSet read() {
        def jc = JAXBContext.newInstance("com.barneyb.magic.creator.descriptor.schema")
        def u = jc.createUnmarshaller()
        u.schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(getClass().classLoader.getResourceAsStream("card-descriptor.xsd")))
        fromCardSetType((u.unmarshal(reader) as JAXBElement).getValue() as CardSetType)
    }

    void close() {
        reader?.close()
    }

    protected CardSet fromCardSetType(CardSetType csel) {
        def cs = new DefaultCardSet(csel.title, csel.key, csel.copyright)
        if (csel?.icon?.symbolSvg) {
            cs.iconSymbol = new DefaultIcon("cardset-$csel.key-symbol", new URL(base, csel.icon.symbolSvg))
            if (csel?.icon?.fieldSvg) {
                cs.iconField = new DefaultIcon("cardset-$csel.key-field", new URL(base, csel.icon.fieldSvg))
            }
        }
        cs.cards = csel.cards.collect { el->
            DefaultCard c
            if (el instanceof FuseType) {
                c = fromFuseType(el)
            } else {
                if (el instanceof PlaneswalkerType) {
                    c = fromPlaneswalkerType(el)
                } else if (el instanceof CreatureType) {
                    c = fromCreatureType(el)
                } else if (el instanceof SpellType) {
                    c = fromSpellType(el)
                } else if (el instanceof LandType) {
                    c = fromLandType(el)
                } else {
                    throw new IllegalArgumentException("Unknown card type: ${el.getClass()}")
                }
                coreProps(el, c)
            }
            c.rarity = Rarity.valueOf(el.rarity.name())
            c.set = cs
            c.cardNumber = csel.cards.indexOf(el) + 1 // one-indexed
            c
        }
        cs
    }

    protected void coreProps(BaseCardType el, DefaultCard c) {
        c.title = el.title
        if (el.colorIndicator) {
            c.colors = getColors(el.colorIndicator)
            c.colorExplicit = true
        }
        c.artwork = new SimpleArtwork(
            new URL(base, el.artwork.src),
            el.artwork.artist
        )
        if (el.overArtwork) {
            c.overArtwork = new SimpleArtwork(
                new URL(base, el.overArtwork.src),
                el.overArtwork.artist
            )
        }
        c.flavorText = textParser.getNonNormativeText(el.flavorText)
        c.rulesText = textParser.getRulesText(el.rulesText as RulesTextType)
        c.watermarkName = el.watermark
    }

    protected DefaultCard fromLandType(LandType el) {
        def c = new DefaultCard()
        if (el.alliedColors != null) {
            c.alliedColors = getColors(el.alliedColors)
        }
        c.typeParts = (el.typeModifiers?.tokenize() ?: []) + "Land"
        c.subtypeParts = el.subtype?.tokenize()
        c
    }

    protected DefaultCard fromSpellType(SpellType el) {
        def c = new DefaultCard()
        populateCost(c, el)
        c.typeParts = el.type?.tokenize()
        c.subtypeParts = el.subtype?.tokenize()
        c
    }

    protected DefaultCreatureCard fromCreatureType(CreatureType el) {
        def c = new DefaultCreatureCard()
        populateCost(c, el)
        c.typeParts = (el.typeModifiers?.tokenize() ?: []) + "Creature"
        c.subtypeParts = el.subtype?.tokenize()
        c.power = el.power
        c.toughness = el.toughness
        if (el.levels.size() > 0) {
            c.levels = el.levels.collect { lel ->
                def l = new DefaultCreatureLevel(lel.levels, lel.power, lel.toughness)
                l.rulesText = textParser.getRulesText(lel.rulesText)
                l
            }
        }
        c
    }

    protected DefaultPlaneswalkerCard fromPlaneswalkerType(PlaneswalkerType el) {
        def c = new DefaultPlaneswalkerCard()
        populateCost(c, el)
        c.typeParts = ["Planeswalker"]
        c.subtypeParts = el.subtype.tokenize()
        c.loyalty = el.loyalty
        c.loyaltyAbilities = el.loyaltyAbilities.collect { ael ->
            def a = new DefaultLoyaltyAbility(ael.cost)
            a.rulesText = textParser.getRulesText(ael.rulesText)
            a
        }
        c
    }

    protected DefaultFusedCard fromFuseType(FuseType el) {
        def c = new DefaultFusedCard()
        c.fusedCards = [
            fromSpellType(el.spell.first()),
            fromSpellType(el.spell.last())
        ]
        c
    }

    protected DefaultCard fromSpellType(FusedSpellType el) {
        def c = new DefaultCard()
        populateCost(c, el)
        c.typeParts = el.type?.tokenize()
        c.subtypeParts = el.subtype?.tokenize()
        coreProps(el, c)
        c
    }

    protected List<ManaColor> getColors(String spec) {
        symbolFactory.getCost(spec)*.colors.flatten().unique() - ManaColor.COLORLESS
    }

    protected void populateCost(DefaultCard c, BaseCardType el) {
        c.castingCost = symbolFactory.getCost(el.castingCost)
    }

}
