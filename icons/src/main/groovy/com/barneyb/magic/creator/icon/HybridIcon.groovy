package com.barneyb.magic.creator.icon

import groovy.transform.PackageScope

/**
 *
 *
 * @author barneyb
 */
@PackageScope
class HybridIcon implements Icon {

    SimpleIcon top
    SimpleIcon bottom
    String body

    def HybridIcon(SimpleIcon t, SimpleIcon b) {
        this.top = t
        this.bottom = b
        this.body = """\
            <g transform="translate(10 10)">
                ${top.body}
            </g>
            <g transform="translate(45 45)">
                ${bottom.body}
            </g>
        """
    }

    String getId() {
        top.id + '_' + bottom.id
    }

    String getShadowed() {
        """\
<svg xmlns="http://www.w3.org/2000/svg" width="45" height="47">
    <defs>
        <clipPath id="top-half">
            <rect width="47" height="22" />
        </clipPath>
    </defs>
    <circle r="22" cx="22" cy="25" fill="#000" opacity="0.75" />
    <g transform="rotate(-45 23 22)">
        <circle r="22" cx="23" cy="22" fill="$bottom.color" />
        <circle r="22" cx="23" cy="22" fill="$top.color" clip-path="url(#top-half)" />
    </g>
    <g transform="scale(.45)">
        $body
    </g>
</svg>
"""
    }

    String getFlat() {
        """\
<svg xmlns="http://www.w3.org/2000/svg" width="44" height="44">
    <defs>
        <clipPath id="top-half">
            <rect width="44" height="22" />
        </clipPath>
    </defs>
    <g transform="rotate(-45 22 22)">
        <circle r="22" cx="22" cy="22" fill="$bottom.color" />
        <circle r="22" cx="22" cy="22" fill="$top.color" clip-path="url(#top-half)" />
    </g>
    <g transform="scale(.45)">
        $body
    </g>
</svg>
"""
    }

    String getBare() {
        """\
<svg xmlns="http://www.w3.org/2000/svg" width="44" height="44">
    <g transform="scale(.45)">
        $body
    </g>
</svg>
"""
    }

}
