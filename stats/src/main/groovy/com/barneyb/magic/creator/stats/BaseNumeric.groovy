package com.barneyb.magic.creator.stats

import com.barneyb.magic.creator.api.Statistic

/**
 *
 *
 * @author barneyb
 */
abstract class BaseNumeric implements Statistic.Numeric {

    final Number n

    BaseNumeric(Number n) {
        this.n = n
    }

    String getKey() {
        Util.toKey(label)
    }

    String getLabel() {
        Util.toLabel(getClass().simpleName)
    }

}
