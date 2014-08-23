package com.barneyb.magic.creator.icon

/**
 *
 *
 * @author barneyb
 */
class SimpleIcon implements Icon {

    String id
    String color = '#CAC5C0'
    String body

    String getShadowed() {
        """\
<svg xmlns="http://www.w3.org/2000/svg" width="45" height="47">
    <circle r="22" cx="22" cy="25" fill="#000" opacity="0.75" />
    <circle r="22" cx="23" cy="22" fill="$color" />
    <g transform="translate(-2 0)">
        $body
    </g>
</svg>
"""
    }

    String getFlat() {
        """\
<svg xmlns="http://www.w3.org/2000/svg" width="44" height="44">
    <circle r="22" cx="22" cy="22" fill="$color" />
    <g transform="translate(-2 0)">
        $body
    </g>
</svg>
"""
    }

    @Override
    String getBare() {
        """\
<svg xmlns="http://www.w3.org/2000/svg" width="44" height="44">
    <g transform="translate(-2 0)">
        $body
    </g>
</svg>
"""
    }
}
