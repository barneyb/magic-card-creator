package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface CreatureCard extends Card {

    static interface Level {

        /**
         * The start of the range this level applies to.  If null, <tt>"0"</tt>
         * is assumed, which is silly (because that'd be the default state).
         */
        String getStart()

        /**
         * The end of the range this level applies to.  If null, it is assumed
         * to be unbounded.
         */
        String getEnd()

        String getPower()

        String getToughness()

        List<List<BodyItem>> getRulesText()

    }

    String getPower()

    String getToughness()

    boolean isLeveler()

    List<Level> getLevels()

}
