package com.barneyb.magic.creator.descriptor

import groovy.transform.Immutable

import java.awt.*
/**
 *
 * @author bboisvert
 */
@Immutable
class RenderSet {

    Assets assets

    AssetSet getFrame() {
        assets.frame
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
