package com.barneyb.magic.creator.validate

import com.barneyb.magic.creator.api.BodyText
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CreatureCard
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolGroup
import com.barneyb.magic.creator.api.ValidationMessage
/**
 *
 *
 * @author barneyb
 */
class CardValidator extends BaseValidator<Card> {

    static final Collection<String> KNOWN_TYPES = [
        'land',
        'instant',
        'sorcery',
        'enchantment',
        'artifact',
        'creature',
        'planeswalker'
    ]

    Collection<ValidationMessage<Card>> validate(Card card) {
        def ctx = new Ctx<Card>(card)
        if (card.fused) {
            if (card.fusedCards.size() != 2) {
                ctx.error(null, "Fused cards must have exactly two halves")
            }
            card.fusedCards.each {
                def sctx = new Ctx<Card>(it)
                if (! it.isType('instant') && ! it.isType('sorcery')) {
                    sctx.error('type', "Only instants and sorceries may be fused.")
                }
                coreValidation(sctx)
                ctx.messages.addAll(sctx.messages)
            }
        } else {
            coreValidation(ctx)
            validateRarity(ctx)
        }
        ctx.messages
    }

    protected coreValidation(Ctx<Card> ctx) {
        validateTitle(ctx)
        validateCastingCost(ctx)
        validateArt(ctx)
        validateType(ctx)
        validateSubtype(ctx)
        validateColorIndicator(ctx)
        validateText(ctx)
        validatePT(ctx)
    }

    protected void validateTitle(Ctx<Card> ctx) {
        ctx.prop = 'title'
        nullOrEmpty(ctx.&error, 'Title', ctx.item.title)
    }

    protected void validateCastingCost(Ctx<Card> ctx) {
        ctx.prop = 'castingCost'
        def cost = ctx.item.castingCost
        if (ctx.item.isType("land")) {
            if (cost != null && ! cost.empty) {
                ctx.error("Lands MUST NOT have a casting cost")
            }
        } else {
            if (cost == null || cost.empty) {
                ctx.error("Spells MUST have a casting cost")
            } else {
                if (cost*.symbol.contains('T')) {
                    ctx.error("The tap symbol cannot be used in casting costs.")
                }
                if (cost*.symbol.contains('Q')) {
                    ctx.error("The untap symbol cannot be used in casting costs.")
                }
                def sorted = cost.sort()
                if (cost != sorted) {
                    ctx.warning("The casting cost is not in canonical order (expected ${sorted.toString()} but was ${cost.toString()})")
                }
            }
        }
    }

    protected void validateType(Ctx<Card> ctx) {
        ctx.prop = 'type'
        def type = ctx.item.typeParts
        if (type == null || type.empty) {
            ctx.error("Cards must specify a type")
        } else {
            if (type.any { it.allWhitespace }) {
                ctx.warning("Malformed type(s)")
            }
            type = type*.toLowerCase()
            def unknown = type - KNOWN_TYPES
            if (unknown == type) {
                ctx.error("Cards must have at least one base type: $KNOWN_TYPES")
            }
            if (type.contains('instant') || type.contains('sorcery')) {
                if (type.size() - unknown.size() > 1) {
                    ctx.error("Instants and sorceries cannot have multiple types")
                } else if (type.size() > 1) {
                    ctx.warning("Instants and sorceries cannot have type modifiers.")
                }
            } else {
                def known = type.findAll KNOWN_TYPES.&contains
                def sorted = KNOWN_TYPES.findAll known.&contains
                if (type.contains('land')) {
                    if (type.size() - unknown.size() > 1) {
                        ctx.error("Lands cannot have multiple types")
                    }
                } else {
                    if (known != sorted) {
                        ctx.warning("The type modifiers are not in canonical order (expected ${sorted.join(' ')} but was ${known.join(' ')})")
                    }
                }
                sorted = unknown + known
                if (type != sorted) {
                    ctx.warning("The type(s) and modifier(s) are not in canonical order (expected ${sorted.join(' ')} but was ${type.join(' ')})")
                }
            }
        }
    }

    protected void validateSubtype(Ctx<Card> ctx) {
        ctx.prop = 'subtype'
        def type = ctx.item.typeParts ?: []
        def subtype = ctx.item.subtypeParts
        if (subtype?.any { it.allWhitespace }) {
            ctx.warning("Malformed subtype(s)")
        }
        if (type.contains('creature')) {
            if (subtype == null || subtype.empty) {
                ctx.error("Creature cards must specify a subtype")
            }
        } else if (type.contains('instant') || type.contains('sorcery')) {
            if (subtype != null && ! subtype.empty) {
                ctx.error("Instants and sorceries cannot declare subtypes")
            }
        }
    }

    protected void validatePT(Ctx<Card> ctx) {
        def card = ctx.item
        if (! (card instanceof CreatureCard)) {
            return
        }
        [
            power: card.power,
            toughness: card.toughness
        ].each { p, String v ->
            ctx.prop = p
            if (v == null || v.allWhitespace) {
                ctx.error('Creatures must have a $p value')
            } else if (v.isInteger()) {
                if (v.toInteger() < 0) {
                    ctx.warning("A negative $p is allowed, but is rather unusual")
                }
            } else if (v.isNumber()) {
                ctx.error("Fractional $p values are not allowed")
            } else if (v != 'x' && v != 'X' && v != '*') {
                ctx.error("Only integers, X, and * are allowed for $p, not '$v'")
            }
        }
    }

    protected void validateArt(Ctx<Card> ctx) {
        ctx.prop = 'artwork'
        def art = ctx.item.artwork
        if (art == null) {
            ctx.error("No artwork has been defined")
        } else {
            ctx.prop = 'artist'
            def over = ctx.item.overArtwork
            if (art.artist == null || art.artist.allWhitespace) {
                ctx.warning("No artist has been defined")
            } else if (over && over.artist != null && over.artist != art.artist) {
                ctx.warning("The over artwork's attributed artist is different than the main artwork's")
            }
        }
    }

    protected void validateColorIndicator(Ctx<Card> ctx) {
        if (! ctx.item.colorExplicit) {
            // any issues with implicit colors are software bugs, not user error
            return
        }
        ctx.prop = 'colorIndicator'
        def colors = ctx.item.colors.unique(false)
        if (colors != ctx.item.colors) {
            ctx.error("The color indicator includes duplicate colors")
        }
        if (colors.contains(ManaColor.COLORLESS)) {
            ctx.error("The color indicator includes 'colorless'")
        } else {
            def sorted = ManaColor.sort(colors)
            if (colors != sorted) {
                ctx.warning("The color indicator is not in canonical order (expected ${sorted*.symbol.join('')} but was ${colors*.symbol.join('')})")
            }
        }
    }

    protected void validateText(Ctx<Card> ctx) {
        ctx.prop = 'text'
        def card = ctx.item
        def empty
        empty = {
            it == null || (it instanceof Collection && it.every(empty))
        }
        if (card.isType('instant') || card.isType('sorcery')) {
            if (empty(card.rulesText)) {
                ctx.error("Instants and sorceries must specify rules text")
            }
        } else {
            if (empty(card.rulesText) && empty(card.flavorText)) {
                ctx.warning("Cards should rarely have neither rules nor flavor text")
            }
        }

        // check symbol group ordering
        def walk
        walk = { work, it ->
            if (it instanceof SymbolGroup) {
                work(it)
            } else if (it instanceof Collection) {
                it.each walk.curry(work)
            }
        }
        def groupOrder = walk.curry {
            def sorted = it.sort()
            if (it != sorted) {
                ctx.warning("Cost symbols are not in canonical order (expected ${sorted.toString()} but was ${it.toString()})")
            }
        }
        groupOrder card.rulesText
        groupOrder card.flavorText

        // check tap/untap location
        def tapUntap = walk.curry { SymbolGroup it ->
            if (it.size() > 1) {
                if (it*.symbol.contains('T')) {
                    ctx.warning("Tap symbols should be separated from mana costs by a comma and space (e.g., {1}{W}, {T}")
                } else if (it*.symbol.contains('Q')) {
                    ctx.warning("Untap symbols should be separated from mana costs by a comma and space (e.g., {1}{W}, {Q}")
                }
            }
        }
        tapUntap card.rulesText
        tapUntap card.flavorText

        // check total length
        walk = { work, it ->
            if (it instanceof Collection) {
                it.each walk.curry(work)
            } else {
                work(it)
            }
        }
        def totalLen = 0
        def lenCheck = walk.curry {
            if (it instanceof Symbol) {
                totalLen += 2
            } else if (it instanceof BodyText) {
                totalLen += it.text.length()
            }
        }
        lenCheck card.rulesText
        lenCheck card.flavorText
        if (totalLen >= 200) {
            ctx.warning("Your body text is pretty long.  It'll be shrunk down to fit, but there's a lot of it.")
        }

    }

    protected void validateRarity(Ctx<Card> ctx) {
        ctx.prop = 'rarity'
        if (ctx.item.rarity == null) {
            ctx.warning("No rarity is specified")
        }
    }

}
