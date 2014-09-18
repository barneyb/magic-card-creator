package com.barneyb.magic.creator.symbol

import com.barneyb.magic.creator.api.ManaColor
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolGroup
/**
 *
 *
 * @author barneyb
 */
class DefaultSymbolGroup implements SymbolGroup {

    @Delegate
    final protected List<Symbol> _list

    def DefaultSymbolGroup(List<Symbol> list) {
        this._list = list
    }

    SymbolGroup sort() {
        def byColor = this.groupBy {
            it.colors.size() == 0 ? null : it.colors.first()
        }
        def nulls = byColor.remove(null)
        def result = new DefaultSymbolGroup([])
        ManaColor.sort(byColor.keySet()).each { c ->
            result.addAll byColor[c].sort()
        }
        if (nulls != null) {
            result.addAll nulls.sort()
        }
        result
    }

    @Override
    int hashCode() {
        _list.hashCode()
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof DefaultSymbolGroup) {
            _list == obj._list
        } else if (obj instanceof List) {
            _list == obj
        } else {
            false
        }
    }

    @Override
    String toString() {
        _list*.toString().join('')
    }

}
