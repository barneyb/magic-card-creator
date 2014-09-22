package com.barneyb.magic.creator.cli
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.api.Symbol
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameters
import groovy.transform.TupleConstructor
/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "stats", commandDescription = "view stats about a cardset")
class StatsCommand extends BaseDescriptorCommand implements Executable {

    static final int OUTPUT_WIDTH = 79

    static final List<String> TYPES = [
        'Land',
        'Instant',
        'Sorcery',
        'Enchantment',
        'Artifact',
        'Creature',
        'Planeswalker'
    ]

    @Override
    void execute(MainCommand main, JCommander jc) {
        def cs = loadDescriptor()
        println "stats for '$cs.title' ($cs.key)"
        def stats = [
            new Numeric('Total Cards', cs.cards.size()),
            new Numeric('Average CMC', mean(cs, { it.castingCost }, this.&cmc)),
            new Numeric('Average Devotion', mean(cs, {
                if (it.castingCost == null) {
                    return false
                }
                def colors = it.castingCost*.colors.flatten()
                colors.size() > 1 || colors.contains(ManaColor.COLORLESS)
            }, this.&devotion)),
            new Histogram('Cost', cs.cards.findAll {
                it.castingCost
            }.collect(this.&cmc).countBy { it }.sort().collect { cmc, int n ->
                new Numeric(cmc.toString(), n)
            }),
            new Histogram('Devotion', cs.cards.findAll {
                it.castingCost
            }.collect(this.&devotion).countBy { it }.sort().collect { cmc, int n ->
                new Numeric(cmc.toString(), n)
            }),
            // todo new Histogram('Devotion To', ),
            new Histogram('Rarity', (Rarity.enumConstants.collectEntries {
                [it, 0]
            } + cs.cards.countBy { it.rarity }).collect { r, int n ->
                new Numeric(r.name(), n)
            }),
            new Histogram('Color', (ManaColor.enumConstants.collectEntries {
                [it, 0]
            } + cs.cards*.colors.flatten().countBy { it }).collect { c, int n ->
                new Numeric(c.name(), n)
            }),
            new Histogram('Type', (TYPES.collectEntries {
                [it, 0]
            } + cs.cards*.typeParts.flatten().countBy { it }).findAll { t, n ->
                TYPES.contains(t)
            }.collect { t, int n ->
                new Numeric(t.toString(), n)
            }),
            new Histogram('Creature Type', cs.cards.findAll {
                it.isType('creature')
            }*.subtypeParts.flatten().countBy { it }.collect { t, int n ->
                new Numeric(t.toString(), n)
            })
        ]
        stats.each this.&emit.curry((stats*.label
            + Rarity.enumConstants*.name()
            + ManaColor.enumConstants*.name()
            + TYPES
        )*.length().max())
    }

    protected void emit(int fcWidth, Numeric s) {
        println '  ' + s.label.padRight(fcWidth) + ' : '  + s.n
    }

    protected void emit(int fcWidth, Histogram s) {
        def max = s.values*.n.max()
        def maxDigits = Math.ceil(Math.log10((double) max))
        def labelWidth = fcWidth + 8 + maxDigits
        def barWidth = OUTPUT_WIDTH - labelWidth
        println '-' * OUTPUT_WIDTH
        println s.label
        s.values.each { v ->
            println '  ' + v.label.padRight(fcWidth) + ' : '  + v.n.toString().padLeft(maxDigits) + ' : ' + '#' * Math.round(((double) v.n) / max * barWidth)
        }
    }

    protected float mean(CardSet cs, filter, num) {
        def cards = cs.cards.findAll(filter)
        cards.collect(num).sum(0) / cards.size()
    }

    protected int cmc(Card c) {
        c.castingCost.findAll {
            it.colored
        }.sum 0, { Symbol it ->
            (it.colors.size() > 1 || it.color != ManaColor.COLORLESS) ? 1 : it.symbol.isInteger() ? it.symbol.toInteger() : 0
        }
    }

    protected int devotion(Card c, ManaColor color = null) {
        c.castingCost.findAll {
            it.colored && (it.colors.size() > 1 || it.color != ManaColor.COLORLESS)
        }.sum 0, { Symbol it ->
            color == null ? (it.colors - ManaColor.COLORLESS).size() : it.colors.contains(color) ? 1 : 0
        }
    }

    static abstract class Stat {
        String label
    }

    @TupleConstructor(includeSuperProperties = true)
    static class Numeric extends Stat {
        Number n
    }

    @TupleConstructor(includeSuperProperties = true)
    static class Histogram extends Stat {
        List<Numeric> values
    }

}
