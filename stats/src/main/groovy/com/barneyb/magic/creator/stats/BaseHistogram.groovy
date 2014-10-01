package com.barneyb.magic.creator.stats

import com.barneyb.magic.creator.api.Statistic
import groovy.transform.TupleConstructor

/**
 *
 *
 * @author barneyb
 */
abstract class BaseHistogram implements Statistic.Histogram {

    @TupleConstructor
    static class Numeric implements Statistic.Numeric {

        String label
        Number n

        @Override
        String getKey() {
            Util.toKey(label)
        }

    }

    final List<Statistic.Numeric> values

    BaseHistogram(Map<Object, Number> values) {
        this.values = values.collect { lbl, n ->
            new Numeric(lbl.toString(), n)
        }
    }

    @Override
    String getKey() {
        Util.toKey(Util.toLabel(getClass().simpleName))
    }

    @Override
    String getLabel() {
        Util.toLabel(getClass().simpleName.replaceFirst(/Histogram$/, ''))
    }
}
