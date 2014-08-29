package com.barneyb.magic.creator.core
import com.barneyb.magic.creator.api.RulesText
import groovy.transform.Immutable
/**
 *
 *
 * @author barneyb
 */
@Immutable
class DefaultRulesText implements RulesText {

    String text

    String toString() {
        "rt($text)"
    }

}
