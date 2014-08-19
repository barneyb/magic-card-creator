package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface LevelerCard extends CreatureCard {

    static interface Level {

        /**
         * The start of the range this level applies to.  If null, <tt>"0"</tt>
         * is assumed.
         */
        String getStart()

        /**
         * The end of the range this level applies to.  If null, it is assumed
         * to be unbounded.
         */
        String getEnd()

        String getPower()

        String getToughness()

        List<List<BodyItem>> getBody()

    }

    List<Level> getLevels()

}