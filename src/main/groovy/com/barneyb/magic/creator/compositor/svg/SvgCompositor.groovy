package com.barneyb.magic.creator.compositor.svg
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.Align
import com.barneyb.magic.creator.compositor.Compositor
import com.barneyb.magic.creator.compositor.RenderModel
import org.apache.batik.dom.svg.SVGDOMImplementation
import org.apache.batik.svggen.SVGGraphics2D
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
        compose_dom(model, rs, dest)
    }

    void compose_dom(RenderModel model, RenderSet rs, OutputStream dest) {
        // Create an SVG document.
        def impl = SVGDOMImplementation.getDOMImplementation();
        def svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        def doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);
        doc.rootElement.setAttributeNS(null, "width", rs.frames.size.width.toString())
        doc.rootElement.setAttributeNS(null, "height", rs.frames.size.height.toString())

        def g = el(doc.rootElement, 'g', [
            fill: "none",
            'stroke-width': 1,
            stroke: "#ebebeb"
        ])

        xmlBox(g, rs.titlebar)
        xmlBox(g, rs.artwork)
        xmlBox(g, rs.typebar)
        xmlBox(g, rs.textbox)
        xmlBox(g, rs.powertoughness)
        xmlBox(g, rs.artist)
        xmlBox(g, rs.footer)

        g = el(doc.rootElement, 'g', [
            fill: "black",
            'stroke-width': 0,
            font: FONT_NAME,
            'font-weight': "bold",
        ])

        xmlText(g, rs.titlebar, model.title)
        xmlText(g, rs.typebar, model.type)
        if (model.isPowerToughnessVisible()) {
            xmlText(g, rs.powertoughness, model.powerToughness, Align.CENTER)
        }
        xmlText(el(g, 'g', ['font-weight': "normal"]), rs.artist, model.artist)
        xmlText(g, rs.footer, model.footer)

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(doc), new StreamResult(dest));
        dest.close()
    }

    protected Element xmlText(Element parent, Rectangle box, String text, Align align=Align.LEADING) {
        def fontSize = box.height

        def attstr = new AttributedString(text, [
            (TextAttribute.FAMILY): FONT_NAME,
            (TextAttribute.SIZE)  : fontSize,
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD,
        ])

        def frc = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_ON, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        def tl = new TextLayout(attstr.iterator, frc)
        def bounds = tl.bounds
        def y = box.y // + tl.ascent
        def x = box.x
        def w = bounds.width
        if (align == Align.CENTER && w < box.width) {
            x += (box.width - w) / 2
        }

        def container = this.el(parent, "g", [
            transform: "translate($x $y)",
            'font-size': fontSize
        ])
        this.el(container, 'rect', [
            x: bounds.x,
            y: bounds.y,
            width: bounds.width,
            height: bounds.height,
            fill: "#f00",
            stroke: "#000",
            'stroke-width': 1,
            opacity: 0.3
        ])
        [(bounds.y): "#000", (bounds.y + tl.ascent): "#f00", (bounds.y + tl.ascent + tl.descent): "#0f0", (bounds.y + bounds.height): "#00f"].each { n, c ->
            this.el(container, 'line', [
                x1: bounds.x - 20,
                x2: bounds.x + bounds.width + 20,
                y1: n,
                y2: n,
                stroke: c,
                'stroke-width': 1,
                opacity: 0.3
            ])
        }
        def el = this.el(container, "text")
        el.appendChild(parent.ownerDocument.createTextNode(text))
        if (w > box.width) {
            // wrap it with a transform
            def g = this.el(parent, "g", [
                transform: "scale(${box.width / w} 1)"
            ])
            g.appendChild(el) // this moves the text w/in this new element
            this.el(g, 'rect', [
                x: bounds.x,
                y: bounds.y,
                width: bounds.width,
                height: bounds.height,
                fill: "#0f0",
                stroke: "#000",
                'stroke-width': 1,
                opacity: 0.3
            ])
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

    void compose_graphics(RenderModel model, RenderSet rs, OutputStream dest) {



        // Create an SVG document.
        def impl = SVGDOMImplementation.getDOMImplementation();
        def svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        def doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

        // Create a converter for this document.
        def g = new SVGGraphics2D(doc);
        g.setSVGCanvasSize(rs.frames.size)

//        g.drawImage(ImageIO.read(model.frame.inputStream), new AffineTransformOp(new AffineTransform(), AffineTransformOp.TYPE_BICUBIC), 0, 0)
        g.color = Color.LIGHT_GRAY
        g.draw(rs.titlebar)
        g.draw(rs.artwork)
        g.draw(rs.typebar)
        g.draw(rs.textbox)
        g.draw(rs.powertoughness)
        g.draw(rs.artist)
        g.draw(rs.footer)
        g.color = Color.BLACK

        drawText(g, rs.titlebar, model.title)
        drawText(g, rs.typebar, model.type)
        drawText(g, rs.artist, model.artist)
        drawText(g, rs.footer, model.footer)
        if (model.isPowerToughnessVisible()) {
            drawText(g, rs.powertoughness, model.powerToughness, Align.CENTER)
        }

        g.stream(new OutputStreamWriter(dest), false, false)
        dest.close()
    }

    void compose_print(RenderModel model, RenderSet rs, OutputStream dest) {
        def out = new PrintWriter(dest)
        out.println('<svg xmlns="http://www.w3.org/2000/svg" width="' + rs.frames.size.width + '" height="' + rs.frames.size.height + '">')
        out.println('<text x="10" y="40" fill="#000000">' + model.title + '</text>')
        out.println("</svg>")
        out.close()
    }

    protected void drawText(Graphics2D g, Rectangle box, String text, Align align=Align.LEADING) {
        // first is for measuring (with points) while second is for drawing (with pixels)
        def font = new Font([
            (TextAttribute.FAMILY): FONT_NAME,
            (TextAttribute.SIZE)  : box.height * 0.75,
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
        ])
        def lm = font.getLineMetrics(text, g.fontRenderContext)
        def w = g.getFontMetrics(font).stringWidth(text)
        if (w > box.width) {
            throw new UnsupportedOperationException("scaling text down isn't supported yet")
        } else if (align == Align.CENTER && w < box.width) {
            // new bounding box
            box = new Rectangle(
                (int) box.x + (box.width - w) / 2,
                (int) box.y,
                w,
                (int) box.height
            )
            g.draw(box)
        }
        def str = new AttributedString(text, [
            (TextAttribute.FAMILY): FONT_NAME,
            (TextAttribute.SIZE)  : box.height,
            (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD
        ])
        g.drawString(str.iterator, (float) box.x, (float) box.y + (box.height + lm.ascent) / 2)
    }
}
