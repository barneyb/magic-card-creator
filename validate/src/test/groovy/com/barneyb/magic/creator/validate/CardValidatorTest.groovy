package com.barneyb.magic.creator.validate
import com.barneyb.magic.creator.api.LineBreak
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.NonNormativeText
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.api.RulesText
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolFactory
import com.barneyb.magic.creator.api.SymbolGroup
import com.barneyb.magic.creator.core.DefaultCard
import com.barneyb.magic.creator.core.DefaultCreatureCard
import com.barneyb.magic.creator.core.DefaultLineBreak
import com.barneyb.magic.creator.core.DefaultNonNormativeText
import com.barneyb.magic.creator.core.DefaultRulesText
import com.barneyb.magic.creator.core.SimpleArtwork
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory
import org.junit.Before
import org.junit.Test

import static com.barneyb.magic.creator.api.ManaColor.*
import static com.barneyb.magic.creator.api.ValidationMessage.Level.*
/**
 *
 *
 * @author barneyb
 */
class CardValidatorTest extends BaseValidatorTest {

    SymbolFactory symbolFactory = new DefaultSymbolFactory()

    CardValidator validator

    protected Symbol s(String key) {
        symbolFactory.getSymbol(key)
    }

    protected SymbolGroup sg(String keys) {
        symbolFactory.getCost(keys)
    }

    protected RulesText rt(String text) {
        new DefaultRulesText(text)
    }

    protected NonNormativeText nnt(String text) {
        new DefaultNonNormativeText(text)
    }

    protected LineBreak lb() {
        new DefaultLineBreak()
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
        check null, ERROR, "null"
        check '', ERROR, "empty"
        check '   \t \n  ', ERROR, "empty"
        check 'Fred'
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Test
    void castingCost() {
        def check = this.&check.curry(validator, 'castingCost')
        check new DefaultCard(), ERROR, 'Spells MUST have'
        check new DefaultCard(typeParts: ['Land'])
        check new DefaultCard(typeParts: ['Land'], castingCost: sg('g')), ERROR, "Lands MUST NOT have"
        check new DefaultCard(castingCost: sg('t')), ERROR, 'tap'
        check new DefaultCard(castingCost: sg('q')), ERROR, 'untap'
        check new DefaultCard(castingCost: sg('uw')), WARNING, 'canonical order', '{W}{U}'
        check new DefaultCard(castingCost: sg('x4w'))
        check new DefaultCard(castingCost: sg('4xw')), WARNING, 'canonical order', '{X}{4}{W}'
    }

    @Test
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
        check 'instant sorcery', ERROR, "multiple types"
        check 'legendary instant', WARNING, "type modifiers"
        check 'enchantment instant', ERROR, "multiple types"
        check 'artifact land', ERROR, "multiple types"
        check "creature artifact", WARNING, "order", 'artifact creature'
        check "artifact legendary", WARNING, "order", 'legendary artifact'
        check "enchantment artifact creature legendary", WARNING, "order", 'legendary enchantment artifact creature'
    }

    @Test
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

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Test
    void text() {
        def check = this.&check.curry(validator, 'text')
        // spells must have rules
        check new DefaultCard(typeParts: ["Instant"], rulesText: [[rt('die')]])
        check new DefaultCard(typeParts: ["Instant"]), ERROR, "rules text"
        check new DefaultCard(typeParts: ["Instant"], flavorText: [[nnt('die?')]]), ERROR, "rules text"
        // cards must have rules or flavor
        check new DefaultCard(typeParts: ["Creature"]), WARNING, "rules", "flavor"
        check new DefaultCard(typeParts: ["Creature"], rulesText: [[rt('die')]])
        check new DefaultCard(typeParts: ["Creature"], flavorText: [[nnt('die?')]])
        // warn about non-standard order in symbol groups
        check new DefaultCard(rulesText: [[sg("rw")]])
        check new DefaultCard(rulesText: [[sg("1gu")]])
        check new DefaultCard(rulesText: [[sg("1ug")]]), WARNING, "canonical", "order", "{1}{U}{G}"
        check new DefaultCard(rulesText: [[sg("wr")]]), WARNING, "canonical", "order", "{R}{W}"
        // error if tap/untap is not at the end of a symbol group
        // warn about tap/untap w/in a multi-symbol group
        check new DefaultCard(rulesText: [[sg("wt")]]), WARNING, "tap", "separated"
        check new DefaultCard(rulesText: [[sg("wq")]]), WARNING, "untap", "separated"
        check new DefaultCard(rulesText: [[sg("tw")]]), WARNING, "canonical", "order", "{W}{T}", WARNING, "tap", "separated"
        check new DefaultCard(rulesText: [[sg("qw")]]), WARNING, "canonical", "order", "{W}{Q}", WARNING, "untap", "separated"
        // warn if more than "a bunch" of text is present
        check new DefaultCard(rulesText: [[rt('a ' * 124)]])
        check new DefaultCard(rulesText: [[rt('b ' * 125)]]), WARNING, "long"
    }

    @Test
    void power() {
        def check = { power, Object... tests ->
            check(validator, 'power', new DefaultCreatureCard(power: power.toString()), tests)
        }
        // non-negative integer, asterisk, or X
        check 0
        check 1
        check 100
        check 1.5, ERROR, 'fractional'
        check(-1, WARNING, "negative")
        check 'x'
        check 'X'
        check '*'
        check 'y', ERROR, 'y'
        check '$', ERROR, '$'
    }

    @Test
    void toughness() {
        // non-negative integer, asterisk, or X
        def check = { toughness, Object... tests ->
            check(validator, 'toughness', new DefaultCreatureCard(toughness: toughness.toString()), tests)
        }
        // non-negative integer, asterisk, or X
        check '', ERROR
        check 0
        check 1
        check 100
        check 1.5, ERROR, 'fractional'
        check(-1, WARNING, "negative")
        check 'x'
        check 'X'
        check '*'
        check 'y', ERROR, 'y'
        check '$', ERROR, '$'
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Test
    void artwork() {
        def check = this.&check.curry(validator, 'artwork')
        check(new DefaultCard(), ERROR, "artwork")
        check(new DefaultCard(artwork: new SimpleArtwork(null, "fred")))
    }

    @Test
    void artist() {
        def check = { String artist, Object... tests ->
            this.check(validator, 'artist', new DefaultCard(artwork: new SimpleArtwork(null, artist)), tests)
        }
        // artist should be present
        check null, WARNING, "artist"
        check '', WARNING
        check '   \t \n  ', WARNING
        check 'Tom'
    }

    @Test
    void overartist() {
        def check = { String artist, String over, Object... tests ->
            this.check(validator, 'artist', new DefaultCard(
                artwork: new SimpleArtwork(null, artist),
                overArtwork: over == null ? null : new SimpleArtwork(null, over),
            ), tests)
        }
        // overartist and artist should match
        check "fred", null
        check "fred", "fred"
        check "fred", "", WARNING, "over", "different"
        check "fred", "sally", WARNING, "over", "different"
    }

    @Test
    void colorIndicator() {
        def check = { List<ManaColor> colors, Object... tests ->
            this.check(validator, 'colorIndicator', new DefaultCard(colors: colors, colorExplicit: true), tests)
        }
        check([RED])
        check([WHITE])
        check([WHITE, BLUE, BLACK])
        // cannot have duplicates
        check([WHITE, BLUE, BLUE], ERROR, "duplicate")
        // cannot have colorless
        check([WHITE, BLUE, COLORLESS], ERROR, "colorless")
        // should be in order
        check([WHITE, BLUE, GREEN], WARNING, "order", 'GWU')
    }

    @Test
    void rarity() {
        def check = { Rarity rarity, Object... tests ->
            this.check(validator, 'rarity', new DefaultCard(rarity: rarity), tests)
        }
        // should be present
        check Rarity.COMMON
        check Rarity.MYTHIC_RARE
        check null, WARNING, "rarity"
    }

}
