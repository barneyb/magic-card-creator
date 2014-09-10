package com.barneyb.magic.creator.icon

import com.barneyb.magic.creator.api.IconGroup
import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolGroup
import com.barneyb.magic.creator.api.SymbolIconFactory
import com.barneyb.magic.creator.core.DefaultIcon
import com.barneyb.magic.creator.core.DefaultIconGroup
import com.barneyb.magic.creator.util.FontLoader
import com.barneyb.magic.creator.util.XmlUtils
import org.apache.batik.svggen.SVGGraphics2D

import java.awt.font.TextAttribute
import java.awt.geom.AffineTransform
import java.text.AttributedString

import static com.barneyb.magic.creator.util.SvgUtils.*

/**
 * I am the default SymbolIconFactory creating symbol icons a la Magic 2014.
 *
 * @author barneyb
 */
class DefaultSymbolIconFactory implements SymbolIconFactory {

    public static final String DESCRIPTOR_PATH = 'default-icons.txt'
    protected Map<String, Icon> icons = [:]

    static {
        FontLoader.fromClasspath(
            "fonts/GoudyOldStyle-Regular.ttf"
        )
    }

    def DefaultSymbolIconFactory() {
        load()
    }

    protected addIcon(Icon i) {
        icons[i.id] = i
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
        } else if (symbol == 'X') {
            // X colorless
            return addIcon(new SimpleIcon(id: symbol, body: XmlUtils.write(withGraphics { SVGGraphics2D g ->
                def attrStr = new AttributedString("X", [
                    (TextAttribute.FAMILY): "Goudy Old Style",
                    (TextAttribute.SIZE): 45,
                    (TextAttribute.TRANSFORM): AffineTransform.getScaleInstance(0.8, 1)
                ])
                g.drawString(attrStr.iterator, 13, 37)
            })))
        } else if (symbol.isInteger()) {
            // numeric
            int n = symbol.toInteger()
            if (n >= 0 && n < 100) {
                return addIcon(new SimpleIcon(id: symbol, body: XmlUtils.write(withGraphics { SVGGraphics2D g ->
                    def attrStr = new AttributedString(symbol, [
                        (TextAttribute.FAMILY): "Goudy Old Style",
                        (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD,
                    ])
                    if (n < 10) {
                        attrStr.addAttribute(TextAttribute.SIZE, 49)
                        // this BS transform seems to be needed to get Batik to convert the text to path
                        attrStr.addAttribute(TextAttribute.TRANSFORM, AffineTransform.getScaleInstance(1, 1))
                        g.drawString(attrStr.iterator, 11, 38)
                    } else {
                        attrStr.addAttribute(TextAttribute.SIZE, 45)
                        attrStr.addAttribute(TextAttribute.TRANSFORM, AffineTransform.getScaleInstance(0.58, 1))
                        g.drawString(attrStr.iterator, 10, 38)
                    }
                })))
            }
        } else if (symbol.matches(~/^[2WUBRG]\/[WUBRG]$/)) {
            // hybrid/ icons
            return addIcon(new HybridIcon((SimpleIcon) getIconInternal(symbol.substring(0, 1)), (SimpleIcon) getIconInternal(symbol.substring(2))))
        } else if (symbol.matches(~/^[2WUBRG]\/P$/)) {
            return addIcon(new SimpleIcon(id: symbol, color: getIconInternal(symbol.substring(0, 1)).color, body: getIconInternal("P").body))
        }
        throw new IllegalArgumentException("Cannot create an icon for symbol '{$symbol}'.")
    }

    @Override
    com.barneyb.magic.creator.api.Icon getIcon(Symbol symbol) {
        new DefaultIcon(symbol.symbol, getIconInternal(symbol.symbol).flat)
    }

    @Override
    com.barneyb.magic.creator.api.Icon getShadowedIcon(Symbol symbol) {
        new DefaultIcon(symbol.symbol, getIconInternal(symbol.symbol).shadowed)
    }

    @Override
    com.barneyb.magic.creator.api.Icon getBareIcon(Symbol symbol) {
        new DefaultIcon(symbol.symbol, getIconInternal(symbol.symbol).bare)
    }

    @Override
    IconGroup getIcons(SymbolGroup symbols) {
        new DefaultIconGroup(symbols.collect(this.&getIcon))
    }

    @Override
    IconGroup getShadowedIcons(SymbolGroup symbols) {
        new DefaultIconGroup(symbols.collect(this.&getShadowedIcon))
    }

    @Override
    IconGroup getBareIcons(SymbolGroup symbols) {
        new DefaultIconGroup(symbols.collect(this.&getBareIcon))
    }
}
