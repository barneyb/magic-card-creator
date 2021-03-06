package com.barneyb.magic.creator.core
import com.barneyb.magic.creator.api.NonNormativeText
import groovy.transform.Immutable
/**
 *
 *
 * @author barneyb
 */
@Immutable
class DefaultNonNormativeText implements NonNormativeText {

    String text

    String toString() {
        "nnt($text)"
    }
    
}
