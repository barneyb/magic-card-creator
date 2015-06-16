magic-card-creator
==================

A composition toolkit for creating custom Magic: The Gathering cards.

Travis does our CI: [![Build Status](https://travis-ci.org/barneyb/magic-card-creator.svg?branch=master)](https://travis-ci.org/barneyb/magic-card-creator)

The impetus was issues with the card generator at [http://www.mtgcardmaker.com/](http://www.mtgcardmaker.com/)

After a bit of hashing out a model on a [Google Docs Spreadsheet](https://docs.google.com/spreadsheets/d/17iSbeWZgER-P-_swBY-jfAdZfm9QvhYrX_uEDTCnfyg/edit?usp=sharing)
and finding [an online print service](http://gotprint.net/g/uploadCollectorsCard.do),
we went to see if we could build a) something better, and b) a reasonable
workflow for creating custom playable physical cards.

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
