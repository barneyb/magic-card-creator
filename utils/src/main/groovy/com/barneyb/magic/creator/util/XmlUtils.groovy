package com.barneyb.magic.creator.util

import org.apache.batik.dom.svg.SAXSVGDocumentFactory
import org.apache.batik.dom.svg.SVGDOMImplementation
import org.apache.batik.util.XMLResourceDescriptor
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.svg.SVGDocument
import org.xml.sax.InputSource

import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMResult
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 *
 *
 * @author barneyb
 */
class XmlUtils {

    /**
     * I write the specified Node out to the specified Writer using 4-space
     * indentation.  The Writer is not closed by this operation, so you may
     * continue to write to it after this method returns.
     */
    static void write(Node doc, Writer dest) {
        if (doc == null) {
            throw new IllegalArgumentException("You cannot write out a null Node")
        }
        if (dest == null) {
            throw new IllegalArgumentException("You cannot write to a null Writer")
        }
        def tf = TransformerFactory.newInstance()
        Transformer t = tf.newTransformer()
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
        t.setOutputProperty(OutputKeys.INDENT, "yes")
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
        t.transform(new DOMSource(doc), new StreamResult(dest))
    }

    /**
     * I convert the specified Node to a 4-space indented string of XML. If you
     * are intending to save the string to a file or other character sink, the
     * two-arg version of write should be preferred.
     */
    static String write(Node doc) {
        def sw = new StringWriter()
        write(doc, sw)
        sw.close()
        sw.toString()
    }

    static SVGDocument read(String xml) {
        read(new StringReader(xml))
    }

    static SVGDocument read(Reader xml) {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        f.createDocument(
            SVGDOMImplementation.SVG_NAMESPACE_URI,
            "svg",
            null,
            xml
        )
    }

    static SVGDocument create() {
        (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(
            SVGDOMImplementation.SVG_NAMESPACE_URI,
            "svg",
            null
        )
    }

}
