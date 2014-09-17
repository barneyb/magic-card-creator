package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.Keyed

/**
 *
 *
 * @author barneyb
 */
class ServiceUtils {

    static <T> Iterator<T> load(Class<T> type) {
        ServiceLoader.load(type).iterator()
    }

    static <T extends Keyed> T load(Class<T> type, String key) {
        load(type).find {
            it.key == key
        }
    }

}
