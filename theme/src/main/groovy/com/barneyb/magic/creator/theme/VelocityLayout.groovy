package com.barneyb.magic.creator.theme
import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.Theme
import com.barneyb.magic.creator.util.XmlUtils
import groovy.transform.TupleConstructor
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import org.w3c.dom.svg.SVGDocument
/**
 *
 *
 * @author barneyb
 */
@TupleConstructor
class VelocityLayout {

    final Theme theme

    final String template

    SVGDocument layout(Card card) {
        def ve = new VelocityEngine()
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.setProperty("runtime.references.strict", true)
        ve.init()
        def t = ve.getTemplate(template)
        def ctx = new VelocityContext([
            tool: new FrameTool(theme, card)
        ])
        def writer = new StringWriter()
        t.merge(ctx, writer)
        def doc = XmlUtils.read(writer.toString())
        // todo: do the card layout
        doc
    }

}
