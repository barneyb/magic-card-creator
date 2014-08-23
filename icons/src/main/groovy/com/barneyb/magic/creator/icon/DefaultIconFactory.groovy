package com.barneyb.magic.creator.icon

import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolIconFactory
import com.barneyb.magic.creator.util.XmlUtils
import org.w3c.dom.svg.SVGDocument

/**
 * I am the default SymbolIconFactory creating symbol icons a la Magic 2014.
 *
 * @author barneyb
 */
class DefaultIconFactory implements SymbolIconFactory {

    public static final String DESCRIPTOR_PATH = 'default-icons.txt'
    protected Map<String, Icon> icons = [:]

    def DefaultIconFactory() {
        load()
    }

    protected addIcon(Icon i) {
        icons[i.id] = i
    }

    protected addNumericIcon(SimpleIcon base, int n) {
        addIcon new SimpleIcon(id: n, body: base.body.replace('${num}', n.toString()))
    }

    protected void load() {
        getIconDescriptor().text.trim().readLines().findAll {
            ! it.trim().startsWith('#')
        }.join('\n').split('\n\n')*.trim().each {
            def lines = it.readLines()
            def top = lines.first().trim().tokenize()
            def i = new SimpleIcon(id: top.first().toUpperCase(), body: lines.tail().join('\n'))
            if (top.size() > 1) {
                i.color = top.get(1)
            }
            addIcon i
        }

    }

    protected InputStream getIconDescriptor() {
        getClass().classLoader.getResourceAsStream(DESCRIPTOR_PATH)
    }

    Icon getIconInternal(String symbol) {
        if (icons.containsKey(symbol)) {
            // preexisting
            return icons[symbol]
        } else if (symbol.isInteger()) {
            // numeric
            int n = symbol.toInteger()
            if (n >= 0 && n < 10) {
                return addNumericIcon(icons["DIGIT"], n)
            } else if (n >= 10 && n < 100) {
                return addNumericIcon(icons["DIGITDIGIT"], n)
            }
        } else if (symbol.matches(~/^[2WUBRG]\/[WUBRG]$/)) {
            // hybrid/ icons
            return addIcon(new HybridIcon(getIconInternal(symbol.substring(0, 1)), getIconInternal(symbol.substring(2))))
        }
        throw new IllegalArgumentException("Cannot create an icon for symbol '{$symbol}'.")
    }

    @Override
    SVGDocument getIcon(Symbol symbol) {
        XmlUtils.read getIconInternal(symbol.symbol).flat
    }

    @Override
    SVGDocument getShadowedIcon(Symbol symbol) {
        XmlUtils.read getIconInternal(symbol.symbol).shadowed
    }

    @Override
    SVGDocument getBareIcon(Symbol symbol) {
        XmlUtils.read getIconInternal(symbol.symbol).bare
    }

}
