package com.barneyb.magic.creator.api

/**
 * This enum lists the 6 colors of mana and sorts them in their canonical order.
 * Any time multiple colors are used together (casting cost symbols, hybrid
 * borders, etc.), they MUST be ordered by this enum's 'sort' method.  The
 * natural order of the constants is NOT a replacement for the 'sort' method, as
 * sorting colors is not just a comparison-based operation, it depends on both
 * the individual items AND the whole set being sorted.
 *
 * @author bboisvert
 */
enum ManaColor {

    COLORLESS,
    WHITE('w'),
    BLUE('u'),
    BLACK('b'),
    RED('r'),
    GREEN('g')

    final String symbol

    def ManaColor(String symbol=null) {
        this.symbol = symbol
    }

    static ManaColor fromSymbol(String symbol) {
        def c = ManaColor.enumConstants.find {
            it.symbol == symbol
        }
        if (c == null) {
            throw new IllegalArgumentException("There is no '$symbol' mana color.")
        }
        c
    }

    /*
     from http://archive.wizards.com/Magic/magazine/article.aspx?x=mtgcom/askwizards/0604
     (article from June 29, 2004)

     Our current (and final!) system for ordering mana symbols is pretty simple.
     If you look at the back of a Magic card, you'll see the pentagon of colors.
     Going clockwise, the colors are white, blue, black, red, green, white, blue,
     black . . . . To order a pair of mana symbols, find them in that list, and
     then put them in whichever order puts the fewest colors between them. For
     example, white/red has two colors in the middle (blue and black), but
     red/white has only one (green). That's why Goblin Legionnaire's mana cost
     is RW.

     Then came the Apocalypse 'wedge' cards. Our system breaks down when you're
     trying to order two friendly colors and their common enemy, and Apocalypse
     has five rares with mana costs that fall into that category. For Lightning
     Angel's mana cost, 1RWU and 1WBR are equally valid options. In the end, I
     decided to put the enemy color pair first.
     */

    /*
     The algorithm for implementing the above is non-trivial, because of the
     circular nature of the prioritization.  Doing simple sorting (i.e., compare
     two values) is trivial.  But the number of edge cases when there are three,
     four, or five values is substantial.  So past there, just use a lookup
     since there aren't that many.
     */

    private static final Collection<List<ManaColor>> canonicals = [
        // the tri-color wedges (a pair of allies, and their common enemy)
        [RED, WHITE, BLUE], // numot, the devastator
        [BLACK, GREEN, WHITE], // teneb, the harvester
        [BLUE, RED, GREEN], // intet, the dreamer
        [WHITE, BLACK, RED], // oros, the avenger
        [GREEN, BLUE, BLACK], // vorosh, the hunter
        // the four-color crescents
        [BLACK, RED, GREEN, WHITE], // dune-brood nephilim
        [BLUE, BLACK, RED, GREEN], // glint-eye nephilim
        [RED, GREEN, WHITE, BLUE], // ink-treader nephilim
        [GREEN, WHITE, BLUE, BLACK], // witch-maw nephilim
        [WHITE, BLUE, BLACK, RED], // yore-tiller nephilim
    ]
    /**
     * I will sort the passed Collection of mana colors and return them as a
     * List.  If the passed Colleciton is already a List and <tt>mutate</tt> is
     * passed as 'true', then the list will be mutated in place.  Otherwise a
     * new list will be created and returned.
     */
    static List<ManaColor> sort(Collection<ManaColor> all, boolean mutate=false) {
        def colors = all.groupBy { it }
        def unique = colors.keySet()
        def hasColorless = unique.contains(COLORLESS)
        if (hasColorless) {
            unique -= COLORLESS
        }
        def result = (hasColorless ? colors[COLORLESS] : []) + ({
            switch (unique.size()) {
                case 5:
                    // natural order
                    return [WHITE, BLUE, BLACK, RED, GREEN]
                case 4:
                case 3:
                    def canon = canonicals.find {
                        unique.size() == it.size() && unique.containsAll(it)
                    }
                    if (canon != null) {
                        return canon
                    }
                    // fall through
                case 2:
                    return unique.sort { ManaColor a, ManaColor b ->
                        if (a == COLORLESS || b == COLORLESS) {
                            return a.compareTo(b)
                        }
                        def normalize = { n ->
                            while (n < 0) {
                                n += 5
                            }
                            n
                        }
                        def cw = normalize((a.ordinal() - b.ordinal()) % 5)
                        def ccw = normalize((b.ordinal() - a.ordinal()) % 5)
                        ccw - cw
                    }
                case 1:
                default:
                    return unique // no idea...
            }
        }()).collect {
            colors[it]
        }.flatten()
        if (mutate && all instanceof List) {
            all.removeAll(all)
            all.addAll(result)
            all
        } else {
            result
        }
    }

}
