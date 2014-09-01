package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.Icon
import com.barneyb.magic.creator.api.IconGroup

/**
 *
 *
 * @author barneyb
 */
class DefaultIconGroup implements IconGroup {

    @Delegate
    final protected List<Icon> _list

    def DefaultIconGroup(List<Icon> list) {
        this._list = list
    }

    @Override
    int hashCode() {
        _list.hashCode()
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof DefaultIconGroup) {
            _list == obj._list
        } else if (obj instanceof List) {
            _list == obj
        } else {
            false
        }
    }

    @Override
    String toString() {
        _list.toString()
    }

}
