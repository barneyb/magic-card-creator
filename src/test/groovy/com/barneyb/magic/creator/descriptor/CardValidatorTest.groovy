package com.barneyb.magic.creator.descriptor

import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 *
 * @author bboisvert
 */
class CardValidatorTest {

    CardValidator validator

    protected validate(Card c) {
        validator.validate(c)
    }

    @Before
    void _makeValidator() {
        validator = new CardValidator()
    }

    @Test
    void allInOneCreature() {
        def es = validator.validate(new Card(
            title: null,
            costString: null,
            artwork: null,
            type: "garbage creature",
            subtype: null,
            abilities: null,
            flavor: null,
            power: null,
            toughness: null,
            artist: null
        ))
        assertEquals([
            'title',
            'costString',
            'type',
            'subtype',
            'power',
            'toughness',
            'artist'
        ], es*.propertyName)
    }

    @Test
    void allInOneNonCreature() {
        def es = validator.validate(new Card(
            title: null,
            costString: null,
            artwork: null,
            type: "monster",
            subtype: null,
            abilities: null,
            flavor: null,
            power: null,
            toughness: null,
            artist: null
        ))
        assertEquals([
            'title',
            'costString',
            'type',
            'artist'
        ], es*.propertyName)
    }

    @Test
    void title() {
        def check = { n, v ->
            assertEquals("'$v' failed", n, validator.validateTitle(v).size())
        }
        check 1, null
        check 1, ""
        check 0, "fred"
    }

    @Test
    void costString() {
        def check = { n, v ->
            assertEquals("'$v' failed", n, validator.validateCost(v).size())
        }
        check 1, null
        check 1, ""
        check 1, "fred"
        check 0, "2u"
        check 1, "ut"
        check 1, "q"
    }

    @Test
    void type() {
        def check = { n, t->
            assertEquals("'$t' failed", n, validator.validateType(t).size())
        }
        check 1, null
        check 1, ""
        check 1, "fred"
        check 0, "creature"
        check 0, "legendary creature"
        check 0, "enchantment creature"
        check 0, "legendary enchantment creature"
        check 0, "enchantment"
        check 0, "legendary enchantment"
        check 0, "artifact"
        check 0, "artifact creature"
        check 0, "land"
        check 0, "legendary land"
        check 0, "instant"
        check 0, "sorcery"
    }

    @Test
    void creatureSubtype() {
        def check = { n, t ->
            assertEquals("'Creature - $t' failed", n, validator.validateCreatureSubtype(t).size())
        }
        check 1, null
        check 1, ""
        check 0, "fred"
    }

    @Test
    void bodyText() {
        def check = { n, t ->
            assertEquals("'$t' failed", n, validator.validateBody(t).size())
        }
        check 1, null
        check 1, ''
        check 0, "Height"
        check 0, """Height

and you are short."""
        check 0, """Height

{and you are short.}"""
        check 0, "{u}{b}, {t}: summon plague of locusts."
        check 0, "{u}{b}, {t}: summon plague of locusts {(If you have PoL in hand, play it without paying.)}."
        check 1, "extra }"
        check 1, "extra {"
        check 2, "{nested {}" // nested and missing close
        check 1, "{nested }}" // extra close
        check 1, "{nested {}}" // nested
    }

    @Test
    void powerToughness() {
        // these are the same (at least for the moment)
        def check = { n, v ->
            assertEquals("'$v' failed as power", n, validator.validatePower(v).size())
            assertEquals("'$v' failed as toughness", n, validator.validateToughness(v).size())
        }
        check 0, "1"
        check 0, "X"
        check 1, "x"
        check 0, "*"
        check 1, "-1"
        check 1, "g"
    }

    @Test
    void artist() {
        def check = { n, v ->
            assertEquals("'$v' failed", n, validator.validateArtist(v).size())
        }
        check 1, null
        check 1, ""
        check 0, "fred"
    }

}
