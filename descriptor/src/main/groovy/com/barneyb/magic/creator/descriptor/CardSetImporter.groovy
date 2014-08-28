package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.descriptor.schema.*

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

    protected CardSet fromCardSetType(URL base, CardSetType csEl) {
        def cs = new DefaultCardSet(csEl.title, csEl.key, csEl.copyright)
        cs.cards = csEl.cards.collect { cEl->
            DefaultCard c
            if (cEl instanceof PlaneswalkerType) {
                c = fromPlaneswalkerType(cEl)
            } else if (cEl instanceof CreatureType) {
                c = fromCreatureType(cEl)
            } else if (cEl instanceof SpellType) {
                c = fromSpellType(cEl)
            } else if (cEl instanceof LandType) {
                c = fromLandType(cEl)
            } else {
                throw new IllegalArgumentException("Unknown card type: ${cEl.getClass()}")
            }
            c.cardSet = cs
            c.title = cEl.title
            if (cEl.colorIndicator) {
                c.colors // todo: whatever was set off casting cost needs to be overwritten
                c.colorExplicit = true
            } else {
                c.colorExplicit = false
            }
            // dereference off 'base'
            c.artwork // todo
            c.overArtwork // todo
            c.cardNumber = csEl.cards.indexOf(cEl) + 1 // one-indexed
            c.rarity = Rarity.valueOf(cEl.rarity.name())
            c.flavorText // todo
            c.rulesText // todo
            c.watermarkName // todo
            c
        }
        cs
    }

    DefaultCard fromLandType(LandType el) {
        def c = new DefaultCard()
        c.colors = [ManaColor.COLORLESS]
        c.typeParts = (el.typeModifiers?.tokenize() ?: []) + "Land"
        c.subtypeParts = el.subtype?.tokenize() ?: []
        c
    }

    DefaultCard fromSpellType(SpellType el) {
        def c = new DefaultCard()
        c.castingCost // todo
        c.colors = [ManaColor.COLORLESS] // todo: parse out of cast cost
        c.typeParts = el.type?.tokenize() ?: []
        c.subtypeParts = el.subtype?.tokenize() ?: []
        c
    }

    DefaultCreatureCard fromCreatureType(CreatureType el) {
        def c = new DefaultCreatureCard()
        c.castingCost // todo
        c.colors = [ManaColor.COLORLESS] // todo: parse out of cast cost
        c.typeParts = (el.typeModifiers?.tokenize() ?: []) + "Creature"
        c.subtypeParts = el.subtype?.tokenize() ?: []
        c.power // todo
        c.toughness // todo
        c.levels // todo
        c
    }

    DefaultPlaneswalkerCard fromPlaneswalkerType(PlaneswalkerType el) {
        def c = new DefaultPlaneswalkerCard()
        c.castingCost // todo
        c.colors = [ManaColor.COLORLESS] // todo: parse out of cast cost
        c.typeParts = ["Planeswalker"]
        c.subtypeParts = el.subtype.tokenize()
        c.loyalty // todo
        c.loyaltyAbilities // todo
        c
    }

}
