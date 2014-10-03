package com.barneyb.magic.creator.cli

import org.junit.Test

/**
 *
 *
 * @author barneyb
 */
class CardCreatorTest {

    String xml = getClass().classLoader.getResource("test-set.xml").toString()
    String md = getClass().classLoader.getResource("test-set.md").toString()

    @Test
    void help() {
        new CardCreator().run("help")
    }

    @Test
    void validate() {
        new CardCreator().run("validate", "-d", xml)
    }

    @Test
    void compose() {
        new CardCreator().run("compose", "-d", xml, "-o", "target", "--cards", "The Green Woods")
    }

    @Test
    void stats() {
        new CardCreator().run("stats", "-d", xml)
    }

    @Test
    void md2xml() {
        def f = File.createTempFile("mgc-md2xml-", ".xml")
        f.deleteOnExit()
        new CardCreator().run("md2xml", "-m", md, "-x", f.canonicalPath)
        f.eachLine { l, n ->
            if (n < 5) {
                println l
            }
        }
    }

    @Test
    void pngHelp() {
        // can't actaully execute this one...
        new CardCreator().run("help", "png")
    }

    @Test
    void pdfHelp() {
        // can't actaully execute this one...
        new CardCreator().run("help", "pdf")
    }

}
