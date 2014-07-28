package com.barneyb.magic.creator.asset
import groovy.transform.PackageScope

import java.awt.*
/**
 *
 * @author bboisvert
 */
class RenderSet {

    String key

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

    TextBox titlebar

    Rectangle artwork

    TextBox typebar

    TextBox textbox

    TextBox powertoughness

    TextBox artist

    TextBox footer

}
