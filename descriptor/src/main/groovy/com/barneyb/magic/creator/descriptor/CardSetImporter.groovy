package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.api.SymbolFactory
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
class CardSetImporter {

    SymbolFactory symbolFactory = new DefaultSymbolFactory()

    TextParser textParser = new TextParser()

    CardSet fromUrl(URL url) {
        fromReader(url, url.newReader())
    }

    CardSet fromUrl(String url) {
        fromUrl(new URL(url))
    }

    CardSet fromUri(URI uri) {
        fromUrl(uri.toURL())
    }

    CardSet fromUri(String uri) {
        fromUri(new URI(uri))
    }

    CardSet fromFile(File descriptor) {
        fromReader(descriptor.toURI().toURL(), descriptor.newReader())
    }

    CardSet fromStream(URL base, InputStream descriptor) {
        fromReader(base, new InputStreamReader(descriptor))
    }

    CardSet fromReader(URL base, Reader descriptor) {
        def jc = JAXBContext.newInstance("com.barneyb.magic.creator.descriptor.schema")
        def u = jc.createUnmarshaller()
        u.schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(getClass().classLoader.getResourceAsStream("card-descriptor.xsd")))
        fromCardSetType(base, (u.unmarshal(descriptor) as JAXBElement).getValue() as CardSetType)
    }

    protected CardSet fromCardSetType(URL base, CardSetType csel) {
        def cs = new DefaultCardSet(csel.title, csel.key, csel.copyright)
        cs.cards = csel.cards.collect { el->
            DefaultCard c
            if (el instanceof FuseType) {
                c = fromFuseType(base, el)
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
                coreProps(base, el, c)
            }
            c.rarity = Rarity.valueOf(el.rarity.name())
            c.cardSet = cs
            c.cardNumber = csel.cards.indexOf(el) + 1 // one-indexed
            c
        }
        cs
    }

    protected void coreProps(URL base, BaseCardType el, DefaultCard c) {
        c.title = el.title
        if (el.colorIndicator) {
            c.colors = getColors(el.colorIndicator)
            c.colorExplicit = true
        } else {
            c.colorExplicit = false
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
        c.colors = [ManaColor.COLORLESS]
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

    protected DefaultFusedCard fromFuseType(URL base, FuseType el) {
        def c = new DefaultFusedCard()
        c.fusedCards = [
            fromSpellType(base, el.spell.first()),
            fromSpellType(base, el.spell.last())
        ]
        c
    }

    protected DefaultCard fromSpellType(URL base, FusedSpellType el) {
        def c = new DefaultCard()
        populateCost(c, el)
        c.typeParts = el.type?.tokenize()
        c.subtypeParts = el.subtype?.tokenize()
        coreProps(base, el, c)
        c
    }

    protected List<ManaColor> getColors(String spec) {
        symbolFactory.getCost(spec)*.colors.flatten().unique() - ManaColor.COLORLESS
    }

    protected void populateCost(DefaultCard c, BaseCardType el) {
        c.castingCost = symbolFactory.getCost(el.castingCost)
        c.colors = c.castingCost*.colors.flatten().unique()
        if (c.colors.size() > 1 && c.colors.contains(ManaColor.COLORLESS)) {
            c.colors -= ManaColor.COLORLESS
        }
    }

}
