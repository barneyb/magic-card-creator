package com.barneyb.magic.creator.descriptor

/**
 * This enum lists the 6 colors of mana in their canonical order.  Any time
 * multiple colors are used together (casting cost symbols, hybrid borders,
 * etc.), they should be ordered according to this enum's constants' order.
 *
 * @author bboisvert
 */
enum ManaColor {
    COLORLESS,
    WHITE,
    BLUE,
    BLACK,
    RED,
    GREEN
}
