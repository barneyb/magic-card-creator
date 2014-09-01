package com.barneyb.magic.creator.textlayout
import com.ibm.icu.text.RuleBasedBreakIterator

import java.text.BreakIterator
/**
 *
 * @author bboisvert
 */
class MagicBreakIteratorProvider {

    public static final String RULES

    static {
        def rules = new StringBuilder(RuleBasedBreakIterator.lineInstance.toString())
        def i = rules.indexOf('!!forward;')
        rules.insert(i, '$PowerToughness = [+-]?([0-9]+|[xX*])\\/[+-]?([0-9]+|[xX*]);')
        i = rules.indexOf('!!reverse;')
        rules.insert(i, '$PowerToughness {500};')
        RULES = rules.toString()
    }

    static BreakIterator getLineInstance() {
        new ICUBreakIteratorAdapter(new RuleBasedBreakIterator(RULES))
    }

}
