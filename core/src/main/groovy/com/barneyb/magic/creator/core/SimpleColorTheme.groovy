package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.ColorTheme
import com.barneyb.magic.creator.api.Texture
import groovy.transform.TupleConstructor

import java.awt.Color

/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class SimpleColorTheme implements ColorTheme {

    Color borderColor

    Texture frameTexture

    Texture barTexture

    Texture textboxTexture

    Texture manaTextboxTexture

}
