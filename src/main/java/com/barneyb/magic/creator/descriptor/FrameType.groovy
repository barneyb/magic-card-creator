package com.barneyb.magic.creator.descriptor

/**
 *
 * @author bboisvert
 */
enum FrameType implements AssetKeyed {

    ARTIFACT,
    ARTIFACT_CREATURE,
    BLACK,
    BLACK_CREATURE,
    BLUE,
    BLUE_CREATURE,
    GOLD,
    GOLD_CREATURE,
    GREEN,
    GREEN_CREATURE,
    LAND,
    LAND_CREATURE,
    RED,
    RED_CREATURE,
    WHITE,
    WHITE_CREATURE

    String getAssetKey() {
        name().toLowerCase()
    }

}