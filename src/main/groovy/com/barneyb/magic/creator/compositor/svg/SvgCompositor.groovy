package com.barneyb.magic.creator.compositor.svg

import com.barneyb.magic.creator.asset.ImageAsset
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.Align
import com.barneyb.magic.creator.compositor.Compositor
import com.barneyb.magic.creator.compositor.RenderModel
import org.apache.batik.dom.svg.SAXSVGDocumentFactory
import org.apache.batik.dom.svg.SVGDOMImplementation
import org.apache.batik.svggen.SVGGraphics2D
import org.apache.batik.util.XMLResourceDescriptor
import org.w3c.dom.Element
import org.w3c.dom.svg.SVGDocument

import javax.imageio.ImageIO
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import java.awt.*
import java.awt.font.FontRenderContext
import java.awt.font.TextAttribute
import java.awt.font.TextLayout
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.text.AttributedString

/**
 *
 * @author bboisvert
 */
class SvgCompositor implements Compositor {

    public static final Map<TextAttribute, ?> TITLE_FONT = [
        (TextAttribute.FAMILY): "Matrix",
        (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD,
    ]
    public static final Map<TextAttribute, ?> BODY_FONT = [
        (TextAttribute.FAMILY): "Garamond",
        (TextAttribute.WEIGHT): TextAttribute.WEIGHT_REGULAR,
    ]
    public static final Map<TextAttribute, ?> POWER_TOUGHNESS_FONT = [
        (TextAttribute.FAMILY): "Goudy Old Style",
        (TextAttribute.WEIGHT): TextAttribute.WEIGHT_BOLD,
    ]

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

        def defs = el(doc.rootElement, 'defs')

        // this will inline the rasters as Base64-encoded data URLs.
        SVGGraphics2D svgg = new SVGGraphics2D(doc)
        xmlImage(svgg, new Rectangle(new Point(0, 0), rs.frames.size), model.frame)
        xmlImage(svgg, rs.artwork, model.artwork)
        doc.documentElement.appendChild(svgg.root)
        svgg = null

        float iconWidth = rs.titlebar.height / rs.large.size.height * rs.large.size.width

        def titleBox = new Rectangle(
            (int) rs.titlebar.x,
            (int) rs.titlebar.y,
            (int) rs.titlebar.width - (model.cost.size() + 0.5) * iconWidth,
            (int) rs.titlebar.height
        )

        def gbx = el(doc.rootElement, 'g', [
            fill          : "none",
            'stroke-width': 1,
            stroke        : "#ebebeb"
        ])

        // draw all the bounding boxes (for debugging)
        xmlBox(gbx, rs.titlebar) // title and cost
        xmlBox(gbx, titleBox) // just title, after constrained by cost
        xmlBox(gbx, rs.artwork)
        xmlBox(gbx, rs.typebar)
        xmlBox(gbx, rs.textbox)
        if (model.isPowerToughnessVisible()) {
            xmlBox(gbx, rs.powertoughness)
        }
        xmlBox(gbx, rs.artist)
        xmlBox(gbx, rs.footer)

        def gtx = el(doc.rootElement, 'g', [
            fill          : "black",
            'stroke-width': 0
        ])

        // draw all the single-line text elements
        xmlText(gtx, titleBox, model.title, TITLE_FONT)
        xmlText(gtx, rs.typebar, model.type, TITLE_FONT)
        if (model.isPowerToughnessVisible()) {
            xmlText(gtx, rs.powertoughness, model.powerToughness, POWER_TOUGHNESS_FONT, Align.CENTER)
        }
        xmlText(gtx, rs.artist, model.artist, TITLE_FONT)
        xmlText(gtx, rs.footer, model.footer, BODY_FONT)

        // draw the casting cost
        def gc = el(doc.rootElement, 'g', [
            transform: "translate(${rs.titlebar.x + rs.titlebar.width - model.cost.size() * iconWidth} ${(float) rs.titlebar.y})"
        ])
        gc = el(gc, 'g', [
            transform: "scale(${(float) rs.titlebar.height / rs.large.size.height})"
        ])
        model.cost.unique(false).sort().each { it ->
            String parser = XMLResourceDescriptor.getXMLParserClassName()
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser)
            def icon = (f.createDocument('', it.inputStream) as SVGDocument).rootElement

            doc.adoptNode(icon)
            el(defs, 'g', [
                id: "lg-$it.id"
            ]).appendChild(icon)
        }
        model.cost.eachWithIndex { it, i ->
            el(gc, 'use', [
                'xlink:href': "#lg-$it.id",
                transform: "translate(${i * rs.large.size.width} 0)"
            ])
        }
    }

    protected void xmlImage(SVGGraphics2D svgg, Rectangle box, ImageAsset asset) {
        if (! asset.exists) {
            return
        }
        def img = ImageIO.read(asset.inputStream)
        def size = new Dimension(img.width, img.height) // this should match 'asset.size'
        svgg.drawImage(img, new AffineTransformOp(AffineTransform.getScaleInstance(box.width / size.width, box.height / size.height), AffineTransformOp.TYPE_BICUBIC), (int) box.x, (int) box.y)
    }

    protected Element xmlText(Element parent, Rectangle box, String text, Map<TextAttribute, ?> attrs, Align align=Align.LEADING) {
        float fontSize = box.height
        def frc = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_ON, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        def font = new Font(attrs + [
            (TextAttribute.SIZE): fontSize,
        ])
        def lm = font.getLineMetrics(text, frc)
        fontSize = fontSize / (lm.ascent + Math.abs(lm.descent)) * box.height
        font = new Font(attrs + [
            (TextAttribute.SIZE): fontSize,
        ])
        lm = font.getLineMetrics(text, frc)
        float y = box.y + lm.ascent + (box.height - lm.ascent - Math.abs(lm.descent)) / 2
        float x = box.x
        float w = new TextLayout(new AttributedString(text, font.attributes).iterator, frc).advance
        if (align == Align.CENTER && w < box.width) {
            x += (box.width - w) / 2
        }
        def container = this.el(parent, "g", [
            transform: "translate($x $y)",
            'font-size': fontSize + "px"
        ])
        def el = this.el(container, "text", [
            'font-family': attrs[TextAttribute.FAMILY],
            'font-weight': attrs[TextAttribute.WEIGHT] == TextAttribute.WEIGHT_BOLD ? "bold" : "normal",
        ])
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
