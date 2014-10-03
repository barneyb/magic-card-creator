package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.api.Artwork
import com.barneyb.magic.creator.api.BodyItem
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.CardSetWriter
import com.barneyb.magic.creator.api.CreatureCard
import com.barneyb.magic.creator.api.Icon
import com.barneyb.magic.creator.api.PlaneswalkerCard
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.core.DefaultIcon
import com.barneyb.magic.creator.descriptor.schema.ArtworkType
import com.barneyb.magic.creator.descriptor.schema.CardSetType
import com.barneyb.magic.creator.descriptor.schema.CreatureType
import com.barneyb.magic.creator.descriptor.schema.FuseType
import com.barneyb.magic.creator.descriptor.schema.FusedSpellType
import com.barneyb.magic.creator.descriptor.schema.LandType
import com.barneyb.magic.creator.descriptor.schema.NonNormativeTextType
import com.barneyb.magic.creator.descriptor.schema.ObjectFactory
import com.barneyb.magic.creator.descriptor.schema.PlaneswalkerType
import com.barneyb.magic.creator.descriptor.schema.RarityEnum
import com.barneyb.magic.creator.descriptor.schema.RulesTextType
import com.barneyb.magic.creator.descriptor.schema.SetIconType
import com.barneyb.magic.creator.descriptor.schema.SpellType
import com.barneyb.magic.creator.descriptor.support.ICoreType
import groovy.util.logging.Log

import javax.xml.XMLConstants
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

/**
 *
 *
 * @author barneyb
 */
@Log
class XmlCardSetWriter implements CardSetWriter {

    final URL base
    final Writer writer
    protected factory = new ObjectFactory()

    TextParser textParser = new TextParser()

    boolean formatOutput = true

    XmlCardSetWriter(File file) {
        this(file.toURI().toURL(), file.newWriter())
    }

    XmlCardSetWriter(URL base, OutputStream out) {
        this(base, out.newWriter())
    }

    XmlCardSetWriter(URL base, Writer out) {
        this.base = base.toURI().resolve('./').toURL()
        writer = out
    }

    @Override
    void write(CardSet cs) {
        def jc = JAXBContext.newInstance("com.barneyb.magic.creator.descriptor.schema")
        def m = jc.createMarshaller()
        m.schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema( // comment out to skip validation
            new StreamSource(getClass().classLoader.getResourceAsStream("card-descriptor.xsd"))
        )
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatOutput)
        m.marshal(factory.createCardSet(fromCardSet(cs)), writer)
    }

    protected CardSetType fromCardSet(CardSet cs) {
        def csel = factory.createCardSetType()
        csel.title = cs.title
        csel.key = cs.key
        csel.copyright = cs.copyright
        csel.icon = fromCardSetIcons(cs.iconSymbol, cs.iconField)
        cs.cards.each { c ->
            def el
            if (c.fused) {
                el = fromFusedCard(c)
            } else {
                if (c instanceof PlaneswalkerCard) {
                    el = fromPlaneswalkerCard(c)
                } else if (c instanceof CreatureCard) {
                    el = fromCreatureCard(c)
                } else if (c.isType("Land")) {
                    el = fromLandCard(c)
                } else {
                    el = fromSpellCard(c)
                }
                coreProps(c, el)
            }
            el.rarity = c.rarity in [null, Rarity.COMMON] ? null : RarityEnum.valueOf(c.rarity.name())
            csel.cards << el
        }
        csel
    }

    void coreProps(Card c, ICoreType el) {
        el.title = c.title
        if (c.colorExplicit) {
            el.colorIndicator = c.colors*.symbol.join('')
        }
        el.artwork = fromArtwork(c.artwork)
        el.overArtwork = fromArtwork(c.overArtwork)
        el.flavorText.addAll(fromText(c.flavorText, NonNormativeTextType))
        el.rulesText.addAll(fromText(c.rulesText, RulesTextType))
        el.watermark = c.watermarkName
    }

    protected <T> List<T> fromText(List<List<BodyItem>> body, Class<T> typeClass) {
        body.collect {
            textParser.unparse(it, typeClass)
        }
    }

    protected ArtworkType fromArtwork(Artwork art) {
        if (art == null) {
            return null
        }
        def ael = factory.createArtworkType()
        ael.src = relativize(art.url)
        ael.artist = art.artist
        ael
    }

    protected SpellType fromSpellCard(Card c) {
        def el = factory.createSpellType()
        el.castingCost = castingCost(c)
        el.type = c.typeParts?.join(' ')
        el.subtype = c.subtypeParts?.join(' ')
        el
    }

    protected LandType fromLandCard(Card c) {
        def el = factory.createLandType()
        el.alliedColors = c.alliedColors ? c.alliedColors*.symbol.join('') : null
        def tmod = c.typeParts - 'Land'
        el.typeModifiers = tmod.size() > 0 ? tmod.join(' ') : null
        el.subtype = c.subtypeParts?.join(' ')
        el
    }

    protected CreatureType fromCreatureCard(CreatureCard c) {
        def el = factory.createCreatureType()
        el.castingCost = castingCost(c)
        def tmod = c.typeParts - 'Creature'
        el.typeModifiers = tmod.size() > 0 ? tmod.join(' ') : null
        el.subtype = c.subtypeParts.join(' ')
        el.power = c.power
        el.toughness = c.toughness
        if (c.leveler) {
            c.levels.each { l ->
                def lel = factory.createLevelType()
                lel.levels = l.label
                lel.power = l.power
                lel.toughness = l.toughness
                lel.rulesText.addAll(fromText(l.rulesText, RulesTextType))
                el.levels << lel
            }
        }
        el
    }

    protected PlaneswalkerType fromPlaneswalkerCard(PlaneswalkerCard c) {
        def el = factory.createPlaneswalkerType()
        el.castingCost = castingCost(c)
        el.subtype = c.subtypeParts.join(' ')
        el.loyalty = c.loyalty
        c.loyaltyAbilities.each { l ->
            def lel = factory.createLoyaltyAbilityType()
            lel.cost = l.cost
            lel.rulesText = textParser.unparse(l.rulesText, RulesTextType)
            el.loyaltyAbilities << lel
        }
        el
    }

    protected FuseType fromFusedCard(Card c) {
        def el = factory.createFuseType()
        c.fusedCards.each {
            el.spell << fromFusedSpellCard(it)
        }
        el
    }

    protected FusedSpellType fromFusedSpellCard(Card c) {
        def el = factory.createFusedSpellType()
        el.castingCost = castingCost(c)
        el.type = c.typeParts.join(' ')
        el.subtype = c.subtypeParts?.join(' ')
        coreProps(c, el)
        el
    }

    protected SetIconType fromCardSetIcons(Icon symbol, Icon field) {
        SetIconType icon = null
        if (symbol instanceof DefaultIcon) {
            icon = factory.createSetIconType()
            icon.symbolSvg = relativize(symbol.__url)
            if (field instanceof DefaultIcon) {
                icon.fieldSvg = relativize(field.__url)
            }
        } else if (symbol != null) {
            log.severe("non-default set icons (${symbol.getClass().name}) cannot be exported")
        }
        icon
    }

    protected String castingCost(Card c) {
        c.castingCost.collect {
            it.symbol.length() == 1 ? it.symbol : it.toString()
        }.join('')
    }

    protected String relativize(URL url) {
        base.toURI().relativize(url.toURI())
    }

    @Override
    void close() {
        writer.close()
    }

}
