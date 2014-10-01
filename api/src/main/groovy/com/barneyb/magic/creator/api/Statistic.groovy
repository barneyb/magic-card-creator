package com.barneyb.magic.creator.api

/**
 * I represent a single statistic for a CardSet.  Implementations should
 * provide a constructor that takes a single CardSet parameter to analyze.
 */
interface Statistic {

    String getKey()
    String getLabel()

    interface Numeric extends Statistic {
        Number getN()
    }

    interface Histogram extends Statistic {
        List<Numeric> getValues()
    }

}
