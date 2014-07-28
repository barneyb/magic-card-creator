package com.barneyb.magic.creator.compositor.svg

import com.barneyb.magic.creator.asset.ImageAsset
import com.barneyb.magic.creator.asset.RemoteImage
import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.*
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
@SuppressWarnings("GrMethodMayBeStatic")
class SvgCompositor implements Compositor {

    PrintMorph printMorph

    boolean getForPrint() {
        printMorph != null
    }
    void setForPrint(boolean val) {
        printMorph = val ? new PrintMorph(17.5f, 17.5f, 90) : null
    }

    @Override
    void compose(RenderModel model, RenderSet rs, OutputStream dest) {
        SVGDocument doc = newDoc()

        composeInto(doc, rs, model)

        if (forPrint) {
            doc = wrapForPrint(doc, rs)
        }

        def tf = TransformerFactory.newInstance()
        Transformer t = tf.newTransformer()
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
        t.setOutputProperty(OutputKeys.INDENT, "yes")
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
        t.transform(new DOMSource(doc), new StreamResult(dest))
        dest.close()
    }

    protected SVGDocument newDoc() {
        (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(
            SVGDOMImplementation.SVG_NAMESPACE_URI,
            "svg",
            null
        )
    }

    protected SVGDocument wrapForPrint(SVGDocument base, RenderSet rs) {
        if (! forPrint) {
            throw new IllegalStateException("You cannot wrap for print w/out setting a PrintMorph.")
        }
        def xpad = printMorph.xBleed
        def ypad = printMorph.yBleed
        def rotate = printMorph.rotate

        if (rotate % 90 != 0) {
            throw new IllegalArgumentException("Only rotations of multiples of 90 degrees are supported.")
        }

        def doc = newDoc()

        def w = rs.frames.size.width
        def h = rs.frames.size.height
        def flip = rotate % 180 != 0
        def newWidth = (flip ? h : w) + xpad * 2
        def newHeight = (flip ? w : h) + ypad * 2
        elattr(doc.rootElement, [
            width: newWidth,
            height: newHeight,
        ])
        el(doc.rootElement, 'rect', [
            x: 0,
            y: 0,
            width: newWidth,
            height: newHeight,
            fill: 'black',
            'stroke-width': 0
        ])
        def g = el(doc.rootElement, 'g', [
            transform: "translate($xpad $ypad)"
        ])
        if (rotate % 360 != 0) {
            g = el(g, 'g', [
                transform: (flip ? "translate(${(h - w) / 2} ${(w - h) / 2}) " : "") + "rotate($rotate ${(float) w / 2} ${(float) h / 2})"
            ])
        }
        g.appendChild(doc.adoptNode(base.rootElement))
        doc
    }

    protected void composeInto(SVGDocument doc, RenderSet rs, RenderModel model) {
        elattr(doc.rootElement, [
            width: rs.frames.size.width,
            height: rs.frames.size.height
        ])

        def defs = el(doc.rootElement, 'defs')

        def frameBox = new Rectangle(new Point(0, 0), rs.frames.size)
        if (rs.frames.type != 'svg') {
            // assume fully-opaque raster
            def mask = el(defs, 'mask', [
                id            : 'artwork-hole',
                fill          : '#000',
                'stroke-width': 0
            ])
            xmlBox(mask, frameBox).setAttributeNS(null, 'fill', 'white')
            xmlBox(mask, rs.artwork).setAttributeNS(null, 'fill', 'black')
        }

        def withGraphics = { work ->
            SVGGraphics2D g = new SVGGraphics2D(doc)
            work(g)
            (Element) doc.documentElement.appendChild(g.topLevelGroup)
        }

        // if the art and frame are RemoteImage, reference them, otherwise inline as Base64
        def drawRaster = { Rectangle box, ImageAsset asset ->
            if (asset.exists) {
                if (xlinkInsteadOfInline(asset)) {
                    xmlImage(doc.rootElement, box, (RemoteImage) asset)
                } else {
                    withGraphics { SVGGraphics2D it ->
                        xmlImage(it, box, asset)
                    }
                }
            }
        }
        drawRaster(rs.artwork, model.artwork)
        if (rs.frames.type == 'svg') {
            throw new UnsupportedOperationException("SVG frames are not yet supported.")
        } else {
            drawRaster(frameBox, model.frame)?.setAttributeNS(null, 'mask', 'url(#artwork-hole)')
        }

        float iconWidth = rs.titlebar.height / rs.large.size.height * rs.large.size.width

        def titleBox = new Rectangle(
            (int) rs.titlebar.x,
            (int) rs.titlebar.y,
            (int) rs.titlebar.width - (model.cost.size() + 0.5) * iconWidth,
            (int) rs.titlebar.height
        )

//        // draw all the bounding boxes (for debugging)
//        def gbx = el(doc.rootElement, 'g', [
//            fill          : "none",
//            'stroke-width': 1,
//            stroke        : 'red' // "#ebebeb"
//        ])
//        xmlBox(gbx, new Rectangle(1, 1, (int) rs.frames.size.width - 2, (int) rs.frames.size.height - 2))
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
        xmlText(gtx, titleBox, model.title, rs.titlebar.textAttributes)
        xmlText(gtx, rs.typebar, model.type, rs.typebar.textAttributes)
        if (model.isPowerToughnessVisible()) {
            xmlText(gtx, rs.powertoughness, model.powerToughness, rs.powertoughness.textAttributes, Align.CENTER)
        }
        xmlText(gtx, rs.artist, model.artist, rs.artist.textAttributes)
        xmlText(gtx, rs.footer, model.footer, rs.footer.textAttributes)

        // draw the casting cost
        def gc = el(doc.rootElement, 'g', [
            transform: "translate(${(float) rs.titlebar.x + rs.titlebar.width - model.cost.size() * iconWidth} ${(float) rs.titlebar.y})"
        ])
        gc = el(gc, 'g', [
            transform: "scale(${(float) rs.titlebar.height / rs.large.size.height})"
        ])

        def iconDef = { idPrefix, ImageAsset it ->
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
            new LayoutUtils().block(it, rs.textbox, model.body, rs.textbox.font, rs.textbox.italicFont, { Graphics2D g, Rectangle2D box, ImageAsset asset ->
                el(doc.rootElement, 'use', [
                    'xlink:href': "#sm-$asset.id",
                    transform: "translate($box.x $box.y)" + (box.size == asset.size ? '' : " scale(${(float) box.width / asset.size.width} ${(float) box.height / asset.size.height})")
                ])
            })
        }
    }

    protected boolean xlinkInsteadOfInline(ImageAsset asset) {
        if (asset instanceof RemoteImage) {
            return asset.url.protocol == 'file' || asset.url.protocol == 'http'
        }
        false
    }

    protected Element xmlImage(Element parent, Rectangle box, RemoteImage asset) {
        def size = asset.size
        el(
            el(parent, 'g', [transform: "matrix(${(float) box.width / size.width} 0 0 ${(float) box.height / size.height} $box.x $box.y)"]),
            'image',
            [
                width       : size.width,
                height      : size.height,
                'xlink:href': asset.url
            ]
        )
    }

    protected void xmlImage(SVGGraphics2D svgg, Rectangle box, ImageAsset asset) {
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
        parent.appendChild(elattr(el, attrs))
    }

    protected Element elattr(Element el, Map<String, ?> attrs) {
        attrs.each { n, v ->
            el.setAttributeNS(null, n, v.toString())
        }
        el
    }

}
