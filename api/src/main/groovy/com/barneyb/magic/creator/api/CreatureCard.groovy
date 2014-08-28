package com.barneyb.magic.creator.api

/**
 *
 * @author bboisvert
 */
interface CreatureCard extends Card {

    static interface Level {

        /**
         * The label for this level or range of levels.
         */
        String getLabel()

        String getPower()

        String getToughness()

        List<List<BodyItem>> getRulesText()

    }

    String getPower()

    String getToughness()

    boolean isLeveler()

    List<Level> getLevels()

}
