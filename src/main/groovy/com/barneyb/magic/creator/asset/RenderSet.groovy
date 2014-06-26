package com.barneyb.magic.creator.asset
import groovy.transform.PackageScope

import java.awt.*
/**
 *
 * @author bboisvert
 */
class RenderSet {

    String key

    String compositor

    @PackageScope
    Assets assets

    AssetSet getFrames() {
        assets.frames
    }

    AssetSet getLarge() {
        assets.large
    }

    AssetSet getSmall() {
        assets.small
    }

    Rectangle titlebar

    Rectangle artwork

    Rectangle typebar

    Rectangle textbox

    Rectangle powertoughness

    Rectangle artist

    Rectangle footer

}
