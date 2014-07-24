package com.barneyb.magic.creator.compositor.svg

import com.barneyb.magic.creator.asset.ImageAsset
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.Align
import com.barneyb.magic.creator.compositor.Compositor
import com.barneyb.magic.creator.compositor.LayoutUtils
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
import java.awt.font.TextAttribute
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.awt.image.AffineTransformOp

/**
 *
 * @author bboisvert
 */
class SvgCompositor implements Compositor {

    @Override
    void compose(RenderModel model, RenderSet rs, OutputStream dest) {
        def impl = SVGDOMImplementation.getDOMImplementation();
        def svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        def doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

        composeInto(doc, rs, model)

        def tf = TransformerFactory.newInstance()
        Transformer t = tf.newTransformer()
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
        t.setOutputProperty(OutputKeys.INDENT, "yes")
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
        t.transform(new DOMSource(doc), new StreamResult(dest))
        dest.close()
    }

    protected void composeInto(SVGDocument doc, RenderSet rs, RenderModel model) {
        doc.rootElement.setAttributeNS(null, "width", rs.frames.size.width.toString())
        doc.rootElement.setAttributeNS(null, "height", rs.frames.size.height.toString())

        def defs = el(doc.rootElement, 'defs')

        def mask = el(defs, 'mask', [
            id            : 'artwork-hole',
            fill          : '#000',
            'stroke-width': 0
        ])
        xmlBox(mask, new Rectangle(new Point(0, 0), rs.frames.size)).setAttributeNS(null, 'fill', 'white')
        xmlBox(mask, rs.artwork).setAttributeNS(null, 'fill', 'black')


        def withGraphics = { work ->
            SVGGraphics2D g = new SVGGraphics2D(doc)
            work(g)
            (Element) doc.documentElement.appendChild(g.topLevelGroup)
        }

        // this will inline the rasters as Base64-encoded data URLs.
        withGraphics { SVGGraphics2D it ->
            xmlImage(it, rs.artwork, model.artwork)
        }
        withGraphics { SVGGraphics2D it ->
            xmlImage(it, new Rectangle(new Point(0, 0), rs.frames.size), model.frame)
        }.setAttributeNS(null, 'mask', 'url(#artwork-hole)')

        float iconWidth = rs.titlebar.height / rs.large.size.height * rs.large.size.width

        def titleBox = new Rectangle(
            (int) rs.titlebar.x,
            (int) rs.titlebar.y,
            (int) rs.titlebar.width - (model.cost.size() + 0.5) * iconWidth,
            (int) rs.titlebar.height
        )

//        def gbx = el(doc.rootElement, 'g', [
//            fill          : "none",
//            'stroke-width': 1,
//            stroke        : 'red' // "#ebebeb"
//        ])
//
//        // draw all the bounding boxes (for debugging)
//        xmlBox(gbx, rs.titlebar) // title and cost
//        xmlBox(gbx, titleBox) // just title, after constrained by cost
//        xmlBox(gbx, rs.artwork)
//        xmlBox(gbx, rs.typebar)
//        xmlBox(gbx, rs.textbox)
//        if (model.isPowerToughnessVisible()) {
//            xmlBox(gbx, rs.powertoughness)
//        }
//        xmlBox(gbx, rs.artist)
//        xmlBox(gbx, rs.footer)

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
            transform: "translate(${(float) rs.titlebar.x + rs.titlebar.width - model.cost.size() * iconWidth} ${(float) rs.titlebar.y})"
        ])
        gc = el(gc, 'g', [
            transform: "scale(${(float) rs.titlebar.height / rs.large.size.height})"
        ])

        def iconDef = { idPrefix, it ->
            String parser = XMLResourceDescriptor.getXMLParserClassName()
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser)
            def icon = (f.createDocument('', it.inputStream) as SVGDocument).rootElement

            doc.adoptNode(icon)
            el(defs, 'g', [
                id: "$idPrefix-$it.id"
            ]).appendChild(icon)
        }
        model.cost.unique(false).each iconDef.curry('lg')
        model.cost.eachWithIndex { it, i ->
            el(gc, 'use', [
                'xlink:href': "#lg-$it.id",
                transform: "translate(${i * rs.large.size.width} 0)"
            ])
        }

        model.bodyIcons.each iconDef.curry('sm')
        withGraphics { Graphics2D it ->
            new LayoutUtils().block(it, rs.textbox, model.body, BODY_FONT, FLAVOR_FONT, { Graphics2D g, Rectangle2D box, ImageAsset asset ->
                el(doc.rootElement, 'use', [
                    'xlink:href': "#sm-$asset.id",
                    transform: "translate($box.x $box.y)" + (box.size == asset.size ? '' : " scale(${(float) box.width / asset.size.width} ${(float) box.height / asset.size.height})")
                ])
            })
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
        def ll = new LayoutUtils().line(box, text, attrs, align)
        def container = this.el(parent, "g", [
            transform: "translate($ll.x $ll.y)",
            'font-size': ll.fontSize + "px"
        ])
        def el = this.el(container, "text", [
            'font-family': attrs[TextAttribute.FAMILY],
            'font-weight': attrs[TextAttribute.WEIGHT] == TextAttribute.WEIGHT_BOLD ? "bold" : "normal",
        ])
        el.appendChild(parent.ownerDocument.createTextNode(text))
        if (ll.scaled) {
            // wrap it with a transform
            def g = this.el(parent, "g", [
                transform: "scale($ll.scale 1)"
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
