package com.barneyb.magic.creator.util

import java.awt.*

/**
 *
 *
 * @author barneyb
 */
class ColorUtils {

    static Color fromHex(String hex) {
        if (hex.length() == 6 && hex.matches(~/[a-fA-f0-9]{6}/)) {
            hex = "#$hex"
        }
        try {
            Color.decode(hex)
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Malformed hex color '$hex'.", nfe)
        }
    }

    static String toHex(Color c) {
        toHex(c.red, c.green, c.blue)
    }

    static String toHex(int r, int g, int b) {
        "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b)
    }

    private static String toBrowserHexValue(int number) {
        def builder = new StringBuilder(Integer.toHexString(number & 0xff))
        while (builder.length() < 2) {
            builder.append("0")
        }
        builder.toString().toLowerCase()
    }

}
