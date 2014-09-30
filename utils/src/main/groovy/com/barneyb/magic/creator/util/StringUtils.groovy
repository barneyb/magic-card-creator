package com.barneyb.magic.creator.util

import java.util.regex.Pattern

/**
 *
 * @author bboisvert
 */
class StringUtils {

    private static final Pattern ESCAPE = ~/(?<!\\)\\(t|u([0-9A-Fa-f]{4}))/

    static String decodeEscapes(String s) {
        def sb = new StringBuilder()
        def m = ESCAPE.matcher(s)
        def any = false
        def nextStart = 0
        while (m.find()) {
            any = true
            if (m.start() > nextStart) {
                sb.append(s.substring(nextStart, m.start()))
            }
            switch (m.group(1).substring(0, 1)) {
                case 't':
                    sb.append("\t")
                    break
                case 'u':
                    sb.append(Character.toString((char) Integer.parseInt(m.group(2), 16)))
                    break
                default:
                    // unrecognized escape
                    sb.append("{").append(m.group()).append("}")
            }
            nextStart = m.end()
        }
        if (! any) {
            return s
        }
        if (nextStart < s.length()) {
            sb.append(s.substring(nextStart))
        }
        sb.toString()
    }

}
