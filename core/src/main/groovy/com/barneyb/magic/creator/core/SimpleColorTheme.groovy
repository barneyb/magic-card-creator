package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.ColorTheme
import com.barneyb.magic.creator.api.Texture
import groovy.transform.TupleConstructor

import java.awt.*

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class SimpleColorTheme implements ColorTheme {

    Color baseColor

    Texture frameTexture

    Texture barTexture

    Texture textboxTexture

    Texture manaTextboxTexture

}
