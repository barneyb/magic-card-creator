package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.CardSet
import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.api.Statistic
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.stats.AverageCMC
import com.barneyb.magic.creator.stats.AverageDevotion
import com.barneyb.magic.creator.stats.CMCHistogram
import com.barneyb.magic.creator.stats.CardCount
import com.barneyb.magic.creator.stats.CardTypeHistogram
import com.barneyb.magic.creator.stats.ColorHistogram
import com.barneyb.magic.creator.stats.CreatureTypeHistogram
import com.barneyb.magic.creator.stats.DevotionHistogram
import com.barneyb.magic.creator.stats.DevotionToHistogram
import com.barneyb.magic.creator.stats.RarityHistogram
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.google.gson.Gson

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

    @Parameter(names = ["-f", "--format"], description = "Format to emit stats in (text or json)")
    Format format = Format.text

    @Override
    void execute(MainCommand main, JCommander jc) {
        def cs = loadDescriptor()
        def stats = [
            new CardCount(cs),
            new AverageCMC(cs),
            new AverageDevotion(cs),
            new CMCHistogram(cs),
            new DevotionHistogram(cs),
            new DevotionToHistogram(cs),
            new RarityHistogram(cs),
            new ColorHistogram(cs),
            new CardTypeHistogram(cs),
            new CreatureTypeHistogram(cs)
        ]
        ({switch (format) {
            case Format.text:
                return new TextEmitter()
            case Format.json:
                return new JsonEmitter()
        }})().emit(cs, stats, System.out)
    }

    protected int devotion(Card c, ManaColor color = null) {
        c.castingCost.findAll {
            it.colored && (it.colors.size() > 1 || it.color != ManaColor.COLORLESS)
        }.sum 0, { Symbol it ->
            color == null ? (it.colors - ManaColor.COLORLESS).size() : it.colors.contains(color) ? 1 : 0
        }
    }

    protected static interface Emitter {
        void emit(CardSet cs, Collection<Statistic> stats, OutputStream out)
    }

    protected static class JsonEmitter implements Emitter {
        @Override
        void emit(CardSet cs, Collection<Statistic> stats, OutputStream os) {
            PrintStream out = os instanceof PrintStream ? os : new PrintStream(os)
            println new Gson().toJson(stats, out)
        }
    }

    protected static class TextEmitter implements Emitter {

        @Override
        void emit(CardSet cs, Collection<Statistic> stats, OutputStream os) {
            def out = os instanceof PrintStream ? os : new PrintStream(os)
            out.println "stats for '$cs.title' ($cs.key)"
            stats.each this.&emit.curry(out, (stats*.label
                + Rarity.enumConstants*.name()
                + ManaColor.enumConstants*.name()
                + CardTypeHistogram.TYPES
            )*.length().max())
        }

        protected void emit(PrintStream out, int fcWidth, Statistic.Numeric s) {
            out.println '  ' + s.label.padRight(fcWidth) + ' : '  + s.n
        }

        protected void emit(PrintStream out, int fcWidth, Statistic.Histogram s) {
            def max = s.values*.n.max()
            def maxDigits = Math.ceil(Math.log10((double) max))
            def labelWidth = fcWidth + 8 + maxDigits
            def barWidth = OUTPUT_WIDTH - labelWidth
            out.println '-' * OUTPUT_WIDTH
            out.println s.label
            s.values.each { v ->
                out.println '  ' + v.label.padRight(fcWidth) + ' : '  + v.n.toString().padLeft(maxDigits) + ' : ' + '#' * Math.round(((double) v.n) / max * barWidth)
            }
        }

    }

}
