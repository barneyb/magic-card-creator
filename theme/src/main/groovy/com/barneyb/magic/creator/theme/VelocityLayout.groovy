package com.barneyb.magic.creator.theme
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.Theme
import com.barneyb.magic.creator.util.XmlUtils
import groovy.transform.TupleConstructor
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import org.apache.velocity.runtime.resource.loader.URLResourceLoader
import org.w3c.dom.svg.SVGDocument
/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
abstract class VelocityLayout {

    Theme theme

    String template

    SVGDocument layout(Card card) {
        def ve = new VelocityEngine()
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath, url")
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.getName())
        ve.setProperty("url.resource.loader.class", URLResourceLoader.getName())
        ve.setProperty("url.resource.loader.root", "") // without this, even though unused, the loader won't be active.
        ve.setProperty("runtime.references.strict", true)
        ve.init()
        def t = ve.getTemplate(template)
        def ctx = new VelocityContext([
            tool: new FrameTool(theme, card)
        ])
        def writer = new StringWriter()
        t.merge(ctx, writer)
        def doc = XmlUtils.read(writer.toString())
        layoutInternal(doc, card)
        doc
    }

    abstract void layoutInternal(SVGDocument doc, Card card)

}
