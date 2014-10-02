package com.barneyb.magic.creator.cli

import org.junit.Test

/**
 *
 *
 * @author barneyb
 */
class CardCreatorTest {

    String descriptor = getClass().classLoader.getResource("test-set.xml").toString()

    @Test
    void help() {
        new CardCreator().run("help")
    }

    @Test
    void validate() {
        new CardCreator().run("validate", "-d", descriptor)
    }

    @Test
    void compose() {
        new CardCreator().run("compose", "-d", descriptor, "-o", "target", "--cards", "The Green Woods")
    }

    @Test
    void stats() {
        new CardCreator().run("stats", "-d", descriptor)
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
