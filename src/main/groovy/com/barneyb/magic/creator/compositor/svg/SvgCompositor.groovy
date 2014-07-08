package com.barneyb.magic.creator.compositor.svg
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.Align
import com.barneyb.magic.creator.compositor.Compositor
import com.barneyb.magic.creator.compositor.RenderModel
import org.apache.batik.dom.svg.SVGDOMImplementation
import org.w3c.dom.Element
import org.w3c.dom.svg.SVGDocument

import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import java.awt.*
import java.awt.font.FontRenderContext
import java.awt.font.TextAttribute
import java.awt.font.TextLayout
import java.text.AttributedString
/**
 *
 * @author bboisvert
 */
class SvgCompositor implements Compositor {

    public static final String FONT_NAME = "Goudy Old Style"

    @Override
    void compose(RenderModel model, RenderSet rs, OutputStream dest) {
        def impl = SVGDOMImplementation.getDOMImplementation();
        def svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        def doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

        composeInto(doc, rs, model)

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(doc), new StreamResult(dest));
        dest.close()
    }

    protected void composeInto(SVGDocument doc, RenderSet rs, RenderModel model) {
        doc.rootElement.setAttributeNS(null, "width", rs.frames.size.width.toString())
        doc.rootElement.setAttributeNS(null, "height", rs.frames.size.height.toString())

        def gbx = el(doc.rootElement, 'g', [
            fill          : "none",
            'stroke-width': 1,
            stroke        : "#ebebeb"
        ])

        xmlBox(gbx, rs.titlebar)
        xmlBox(gbx, rs.artwork)
        xmlBox(gbx, rs.typebar)
        xmlBox(gbx, rs.textbox)
        if (model.isPowerToughnessVisible()) {
            xmlBox(gbx, rs.powertoughness)
        }
        xmlBox(gbx, rs.artist)
        xmlBox(gbx, rs.footer)

        g = el(doc.rootElement, 'g', [
        def gtx = el(doc.rootElement, 'g', [
            fill          : "black",
            'stroke-width': 0,
            'font-family' : FONT_NAME,
            'font-weight' : "bold",
        ])

        xmlText(gtx, titleBox, model.title)
        xmlText(gtx, rs.typebar, model.type)
        if (model.isPowerToughnessVisible()) {
            xmlText(gtx, rs.powertoughness, model.powerToughness, Align.CENTER)
        }
        xmlText(gtx, rs.artist, model.artist)
        xmlText(el(gtx, 'g', ['font-weight': "normal"]), rs.footer, model.footer, Align.LEADING)
    }

    protected Element xmlText(Element parent, Rectangle box, String text, Align align=Align.LEADING) {
        float fontSize = box.height
        def frc = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_ON, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        def font = new Font([
            (TextAttribute.FAMILY): FONT_NAME,
            (TextAttribute.SIZE)  : fontSize,
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD,
        ])
        def lm = font.getLineMetrics(text, frc)
        fontSize = fontSize / (lm.ascent + lm.descent) * box.height
        def attstr = new AttributedString(text, font.attributes + [
            (TextAttribute.SIZE): fontSize
        ])
        def tl = new TextLayout(attstr.iterator, frc)
        float y = box.y + tl.ascent + (box.height - tl.ascent - tl.descent) / 2
        float x = box.x
        float w = tl.advance
        if (align == Align.CENTER && w < box.width) {
            x += (box.width - w) / 2
        }
        def container = this.el(parent, "g", [
            transform: "translate($x $y)",
            'font-size': fontSize + "px"
        ])

        def el = this.el(container, "text")
        el.appendChild(parent.ownerDocument.createTextNode(text))
        if (w > box.width) {
            // wrap it with a transform
            def g = this.el(parent, "g", [
                transform: "scale(${(float) box.width / w} 1)"
            ])
            g.appendChild(el) // this moves the text w/in this new element
            container.appendChild(g)
        }
        el
    }

    protected Element xmlBox(Element parent, Rectangle box) {
        el(parent, "rect", [
            x: box.x,
            y: box.y,
            width: box.width,
            height: box.height
        ])
    }

    protected Element el(Element parent, String name, Map<String, ?> attrs=[:]) {
        def el = parent.ownerDocument.createElementNS(parent.namespaceURI, name)
        attrs.each { n, v ->
            el.setAttributeNS(null, n, v.toString())
        }
        parent.appendChild(el)
    }

}
