package com.barneyb.magic.creator.descriptor

/**
 *
 * @author bboisvert
 */
class CardValidator {

    static final Collection<String> KNOWN_TYPES = [
        // core
        'creature',
        'land',
        'sorcery',
        'enchantment',
        'instant',
        'artifact',
        // mods
        'legendary'
    ] as Set

    Collection<ValidationException> validate(Card c) {
        def props = [
            title     : validateTitle(c.title)
        ]
        if (c.land) {
            props.costString = c.costString == null || c.costString.allWhitespace ? [] : ["Lands may not have casting costs."]
        } else {
            props.costString = validateCost(c.costString)
        }
        props.type = validateType(c.type)
        if (c.creature) {
            props.subtype = validateCreatureSubtype(c.subtype)
        }
        props.body = validateBody(c.body)
        if (c.creature) {
            props.power = validatePower(c.power)
            props.toughness = validateToughness(c.toughness)
        }
        props.artist = validateArtist(c.artist)
        def exs = []
        props.each { prop, msgs ->
            msgs.each {
                exs << new ValidationException(c, prop, it)
            }
        }
        exs
    }

    Collection<String> validateTitle(String title) {
        def msgs = []
        if (title == null || title.allWhitespace) {
            msgs << "Titles may not be null or empty."
        }
        msgs
    }

    Collection<String> validateCost(String cost) {
        def msgs = []
        if (cost == null || cost.allWhitespace) {
            msgs << "Casting costs may not be null or empty except for lands (use '0' for a no-cost spell)."
        }
        def parts = CostParser.parts(cost)
        if (parts.any { ! CostType.isSymbol(it) }) {
            msgs << "The cost '$cost' contains an invalid symbol."
        }
        if (parts.any { CostType.isSymbol(it) && CostType.fromSymbol(it) == CostType.TAP }) {
            msgs << "Casting costs cannot contain the tap symbol."
        }
        msgs
    }

    Collection<String> validateType(String type) {
        def msgs = []
        if (type == null || type.allWhitespace) {
            msgs << "Types may not be null or empty."
        } else {
            def typeParts = type.toLowerCase().tokenize(' ')
            (typeParts - KNOWN_TYPES).each {
                msgs << "Type '$it' is not recognized."
            }
        }
        msgs
    }

    Collection<String> validateCreatureSubtype(String subtype) {
        def msgs = []
        if (subtype == null || subtype.allWhitespace) {
            msgs << "Creatures must have a subtype."
        }
        msgs
    }

    Collection<String> validateBody(String body) {
        def msgs = []
        if (body == null || body.allWhitespace) {
            msgs << "Cards must ave body text."
        }
        int depth = 0
        for (char c : body) {
            if (c == '{') {
                depth += 1
                if (depth > 1) {
                    msgs << "Invalid brace nesting (nested open) $depth"
                }
            } else if (c == '}') {
                depth -= 1
                if (depth < 0) {
                    msgs << "Invalid brace nesting (extra close) $depth"
                }
            }
        }
        if (depth > 0) {
            msgs << "Invalid brace nesting (missing close) $depth"
        }
        msgs
    }

    Collection<String> validatePower(String power) {
        validatePowerOrToughness('Power', power)
    }

    Collection<String> validateToughness(String toughness) {
        validatePowerOrToughness('Toughness', toughness)
    }

    Collection<String> validatePowerOrToughness(String label, String value) {
        def msgs = []
        if (value == null || value.allWhitespace) {
            msgs << "$label may not be null or empty."
        } else if (value != '*' && value != 'X' && ! value.isInteger()) {
            msgs << "$label must be a number, '*', or 'X'."
        } else if (value.isInteger() && value.toInteger() < 0) {
            msgs << "$label must be a non-negative integer."
        }
        msgs
    }

    Collection<String> validateArtist(String artist) {
        def msgs = []
        if (artist == null || artist.allWhitespace) {
            msgs << "Artists may not be null or empty."
        }
        msgs
    }
    
}
