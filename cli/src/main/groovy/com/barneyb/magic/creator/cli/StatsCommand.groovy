package com.barneyb.magic.creator.cli
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.api.Symbol
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.google.gson.Gson
import groovy.transform.TupleConstructor
/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "stats", commandDescription = "view stats about a cardset", separators = "=")
class StatsCommand extends BaseDescriptorCommand implements Executable {

    static enum Format {
        text,
        json
    }

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

    @Parameter(names = ["-f", "--format"], description = "Format to emit stats in (text or json)")
    Format format = Format.text

    @Override
    void execute(MainCommand main, JCommander jc) {
        def cs = loadDescriptor()
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
            new Histogram('CMC', cs.cards.findAll {
                it.castingCost
            }.collect(this.&cmc).countBy { it }.sort().collect { cmc, int n ->
                new Numeric(cmc.toString(), n)
            }),
            new Histogram('Devotion', cs.cards.findAll {
                it.castingCost
            }.collect(this.&devotion).countBy { it }.sort().collect { cmc, int n ->
                new Numeric(cmc.toString(), n)
            }),
            new Histogram('Devotion to', (ManaColor.enumConstants - ManaColor.COLORLESS).collectEntries {
                [it, cs.cards.sum(0, this.&devotion.rcurry(it))]
            }.collect { ManaColor c, int n ->
                new Numeric(c.name(), n)
            }),
            new Histogram('Rarity', (Rarity.enumConstants.collectEntries {
                [it, 0]
            } + cs.cards.countBy { it.rarity ?: Rarity.COMMON }).collect { r, int n ->
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
        ({switch (format) {
            case Format.text:
                return new TextEmitter()
            case Format.json:
                return new JsonEmitter()
        }})().emit(cs, stats, System.out)
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

    protected static interface Emitter {
        void emit(CardSet cs, Collection<Stat> stats, OutputStream out)
    }

    protected static class JsonEmitter implements Emitter {
        @Override
        void emit(CardSet cs, Collection<Stat> stats, OutputStream os) {
            PrintStream out = os instanceof PrintStream ? os : new PrintStream(os)
            println new Gson().toJson(stats, out)
        }
    }

    protected static class TextEmitter implements Emitter {

        @Override
        void emit(CardSet cs, Collection<Stat> stats, OutputStream os) {
            def out = os instanceof PrintStream ? os : new PrintStream(os)
            out.println "stats for '$cs.title' ($cs.key)"
            stats.each this.&emit.curry(out, (stats*.label
                + Rarity.enumConstants*.name()
                + ManaColor.enumConstants*.name()
                + TYPES
            )*.length().max())
        }

        protected void emit(PrintStream out, int fcWidth, Numeric s) {
            out.println '  ' + s.label.padRight(fcWidth) + ' : '  + s.n
        }

        protected void emit(PrintStream out, int fcWidth, Histogram s) {
            double max = s.values*.n.max()
            def maxDigits = Math.ceil(Math.log10((double) max))
            def labelWidth = fcWidth + 8 + maxDigits
            def barWidth = OUTPUT_WIDTH - labelWidth
            out.println '-' * OUTPUT_WIDTH
            out.println s.label
            s.values.each { v ->
                out.println '  ' + v.label.padRight(fcWidth) + ' : '  + v.n.toString().padLeft(maxDigits) + ' : ' + '#' * Math.round(v.n / max * barWidth)
            }
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
