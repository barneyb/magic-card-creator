magic-card-creator
==================

A composition toolkit for creating custom Magic: The Gathering cards.

Travis does our CI: [![Build Status](https://travis-ci.org/barneyb/magic-card-creator.svg?branch=master)](https://travis-ci.org/barneyb/magic-card-creator)

The impetus was issues with the card generator at [http://www.mtgcardmaker.com/](http://www.mtgcardmaker.com/)

After a bit of hashing out a model on a [Google Docs Spreadsheet](https://docs.google.com/spreadsheets/d/17iSbeWZgER-P-_swBY-jfAdZfm9QvhYrX_uEDTCnfyg/edit?usp=sharing)
and finding [an online print service](http://gotprint.net/g/uploadCollectorsCard.do),
we went to see if we could build a) something better, and b) a reasonable
workflow for creating custom playable physical cards.

Usage
-----

The general workflow is to run the `compose` command to create SVG documents (one per
card) described in a descriptor file, and then to transcode those SVG documents into
either PNGs (for the web) or PDFs (for printing).

    $ java -jar card-creator-all.jar compose \
      --descriptor descriptor.md \
      --output-dir svgs \
      --print
    $ java -jar card-creator-all.jar pdf \
      --output-dir pdfs \
      svgs/*.svg

Building
--------

To build, you'll need Java 8 and Maven 3 available on your system.

    $ git clone https://github.com/barneyb/magic-card-creator.git
    $ cd magic-card-creator
    $ mvn install

You will now have the full toolkit installed, with the executable JAR both
in your local `.m2/repository` and in the `./cli/target` directory.

Toolkit
-------

The toolkit is packaged as an executable JAR file.  It is the artifact of the 'cli' module
(if you're building from source), or the release artifact if you're getting binaries.  Note
that for standalone execution, you need the one with the `-all` suffix.  Run it for a list
of available commands:

    $ java -jar card-creator-all.jar 
    Usage: CardCreator [options] [command] [command options]
    
    A toolkit for creating custom Magic: The Gathering cards.
    
    Commands:
        help      display usage information and exit
        validate  validate a cardset
        compose   compose cards from a descriptor
        stats     view stats about a cardset
        png       transcode composed cards to PNG (via docker)
        pdf       transcode composed cards to PDF (via docker)

You can get help for a given command by using the `help` command:

    $ java -jar card-creator-all.jar help compose
    compose cards from a descriptor
    Usage: compose [options]
      Options:
            --cards
           Card names or numbers to process
           Default: []
      * -d, --descriptor
           Cardset descriptor
      * -o, --output-dir
           The directory to compose into
            --print
           If true, composed cards will be bled for printing
           Default: false
            --theme-descriptor
           A theme descriptor file, if needed
            --theme-key
           The key of the theme to use
           Default: dynamic

### From Maven

If you've built from source, you can also invoke the toolkit using the maven
`exec:java` goal in the `cli` module.  The class you want to run is
`com.barneyb.magic.creator.cli.CardCreator`:

    $ cd cli
    $ mvn exec:java -Dexec.mainClass=com.barneyb.magic.creator.cli.CardCreator
    [INFO] Scanning for projects...
    [INFO]                                                                         
    [INFO] ------------------------------------------------------------------------
    [INFO] Building card-creator-cli 0.6.1-SNAPSHOT
    [INFO] ------------------------------------------------------------------------
    [INFO] 
    [INFO] --- exec-maven-plugin:1.4.0:java (default-cli) @ card-creator-cli ---
    Usage: CardCreator [options] [command] [command options]
    
    A toolkit for creating custom Magic: The Gathering cards.
    
    Commands:
        help      display usage information and exit
        validate  validate a cardset
        compose   compose cards from a descriptor
        stats     view stats about a cardset
        png       transcode composed cards to PNG (via docker)
        pdf       transcode composed cards to PDF (via docker)
        md2xml    convert a Markdown descriptor to an equivalent XML descriptor
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 0.991s
    [INFO] Finished at: Tue Jun 16 08:08:36 PDT 2015
    [INFO] Final Memory: 15M/300M
    [INFO] ------------------------------------------------------------------------

Descriptors
-----------

Each set of cards is described by a descriptor.  Two formats are supported: Markdown and XML.
Markdown descriptors are easier to read and write, and more naturally support previewing the
cards, but do not have the richness needed to cover all cases.  XML descriptors are rather
move verbose, but allow for complete description of all card attributes.  For example, the
Markdown format is not rich enough to describe Planeswalkers or Fused cards.

As an example, the Blitz Hellion (from the Alara block) can be described in Markdown like this:

    Blitz Hellion 3RG
    -----------------
    
    ![Anthony S. Waters](artwork/hellion.jpg)
    
    Creature - Hellion
    
    Trample, haste
    
    At the beginning of the end step, Blitz Hellion's owner shuffles it into his or her library.
    
    *Alarans commemorated its appearances with new holidays bearing names like the Great Cataclysm and the Fall of Ilson Gate.*
    
    7/7

And in XML like this:

    <creature title="Blitz Hellion" casting-cost="3RG" subtype="Hellion" rarity="rare">
        <artwork>
            <src>artwork/hellion.jpg</src>
            <artist>Anthony S. Waters</artist>
        </artwork>
        <rules-text>Trample, haste</rules-text>
        <rules-text>At the beginning of the end step, Blitz Hellion's owner shuffles it into his or her library.</rules-text>
        <flavor-text>Alarans commemorated its appearances with new holidays bearing names like the Great Cataclysm and the Fall of Ilson Gate.</flavor-text>
        <power>7</power>
        <toughness>7</toughness>
    </creature>

In general, starting with Markdown is probably easier, though if you have a good XML editor,
the assistance provided by the XSD may be beneficial.  The toolkit provides a tool for
automatic conversion from Markdown to XML, so you can switch later if needed/desired.  There
is not a tool to convert from XML to Markdown, as not ever XML descriptor can be represented
as a Markdown descriptor.
