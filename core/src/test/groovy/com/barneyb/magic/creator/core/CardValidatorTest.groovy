package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.Symbol
import org.junit.Before
import org.junit.Test

import static com.barneyb.magic.creator.api.ValidationMessage.Level.*
/**
 *
 *
 * @author barneyb
 */
class CardValidatorTest extends BaseValidatorTest {

    CardValidator validator

    protected Symbol s(String key) {
        [
            getSymbol: { -> key }
        ] as Symbol
    }

    @Before
    void _makeValidator() {
        validator = new CardValidator()
    }

    @Test
    void title() {
        def check = { String title, Object... tests ->
            this.check(validator, 'title', new DefaultCard(title: title), tests)
        }
        check null, ERROR, "must"
        check '', ERROR
        check '   \t \n  ', ERROR
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Test
    void castingCost() {
        def check = this.&check.curry(validator, 'castingCost')
        check new DefaultCard(), ERROR, 'Spells MUST have'
        check new DefaultCard(typeParts: ['Land'])
        check new DefaultCard(typeParts: ['Land'], castingCost: [s('g')]), ERROR, "Lands MUST NOT have"
        check new DefaultCard(castingCost: [s('t')]), ERROR, 'tap'
        check new DefaultCard(castingCost: [s('q')]), ERROR, 'untap'
        check new DefaultCard(castingCost: [s('u'), s('w')]), WARNING, 'order'
        check new DefaultCard(castingCost: [s('2'), s('4'), s('w')]), WARNING, 'multiple colorless'
        check new DefaultCard(castingCost: [s('x'), s('4'), s('w')])
        check new DefaultCard(castingCost: [s('4'), s('x'), s('w')])
    }

    void type() {
        def check = { String type, Object... tests ->
            this.check(validator, 'type', new DefaultCard(typeParts: type?.tokenize()), tests)
        }
        check null, ERROR, "must"
        check '', ERROR, "must"
        check '  \t ', ERROR, "must"
        check 'Legendary', ERROR, "base type"
        check 'Creature'
        check 'legendary enchantment artifact creature'
        check 'instant sorcery', ERROR, "multiple"
        check 'legendary instant', WARNING, "non-permanent"
        check 'enchantment instant', WARNING, "non-permanent"
        check 'artifact land', WARNING, "multiple"
        check "creature artifact", WARNING, "order"
        check "artifact legendary", WARNING, "order"
    }

    void subtype() {
        def check = { String type, String subtype, Object... tests ->
            this.check(validator, 'subtype', new DefaultCard(typeParts: type?.tokenize(), subtypeParts: subtype?.tokenize()), tests)
        }
        // some stuff can have nothing
        check 'instant', null
        check 'sorcery', null
        check 'enchantment', null
        check 'artifact', null
        check 'land', null
        check 'creature', null, ERROR, "must"

        // some stuff cannot have nothing
        check 'instant', 'ape', ERROR, 'cannot'
        check 'sorcery', 'ape', ERROR, 'cannot'
        check 'enchantment', 'aura'
        check 'artifact', 'equipment'
        check 'land', 'forest'
        check 'creature', 'ape'
    }

    void body() {
        // spells must have rules
        // cards must have rules or flavor
        // warn about non-standard order in symbol groups
        // error if tap/untap is not at the end of a symbol group
        // warn about tap/untap w/in a symbol group
        // warn if more than "a bunch" of text is present
    }

    void power() {
        // non-negative integer, asterisk, or X
    }

    void toughness() {
        // non-negative integer, asterisk, or X
    }

    void artist() {
        // artist should be present
    }

    void overartist() {
        // overartist and artist should match
    }

    void colorIndicator() {
        // cannot have duplicates
        // cannot have colorless
    }

    void copyright() {
        // should be present
    }

    void rarity() {
        // should be present
    }

    void fusedCards() {
        // should be null or size two
        // if non-null, a bunch of other stuff must be nulled
    }

}
