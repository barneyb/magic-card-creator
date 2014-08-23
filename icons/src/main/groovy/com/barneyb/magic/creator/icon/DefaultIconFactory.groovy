package com.barneyb.magic.creator.icon

import com.barneyb.magic.creator.api.Symbol
import com.barneyb.magic.creator.api.SymbolIconFactory
import com.barneyb.magic.creator.util.XmlUtils
import org.apache.batik.svggen.SVGGraphics2D
import org.w3c.dom.Element
import org.w3c.dom.svg.SVGDocument

import java.awt.font.TextAttribute
import java.awt.geom.AffineTransform
import java.text.AttributedString

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

    protected Element withGraphics(Closure work) {
        def doc = XmlUtils.create()
        SVGGraphics2D g = new SVGGraphics2D(doc)
        work(g)
        g.topLevelGroup
    }

    Icon getIconInternal(String symbol) {
        if (icons.containsKey(symbol)) {
            // preexisting
            return icons[symbol]
        } else if (symbol == 'X') {
            // X colorless
            return addIcon(new SimpleIcon(id: symbol, body: XmlUtils.write(withGraphics { SVGGraphics2D g ->
                def attrStr = new AttributedString("X", [
                    (TextAttribute.FAMILY): "Norasi", // todo: Goudy Old Style
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
                        (TextAttribute.FAMILY): "Norasi", // todo: Goudy Old Style
                        (TextAttribute.SIZE): 53,
                    ])
                    if (n < 10) {
                        attrStr.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD)
                        g.drawString(attrStr.iterator, 12, 39)
                    } else {
                        attrStr.addAttribute(TextAttribute.TRANSFORM, AffineTransform.getScaleInstance(0.58, 1))
                        g.drawString(attrStr.iterator, 8.5f, 40)
                    }
                })))
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
