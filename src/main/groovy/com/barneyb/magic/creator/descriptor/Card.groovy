package com.barneyb.magic.creator.descriptor

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

/**
 *
 * @author bboisvert
 */
@TupleConstructor(excludes = ["set"])
@EqualsAndHashCode(excludes = ["set"])
@ToString
class Card {

    protected CardSet set

    String title
    void setTitle(String s) {
        title = s?.trim()
    }

    String costString
    void setCostString(String s) {
        costString = s?.trim()
    }

    List<CostType> getCost() {
        CostParser.parse(costString)
    }

    Collection<ManaColor> getColors() {
        (cost*.colors.flatten().unique() - ManaColor.COLORLESS).sort()
    }

    String artwork
    void setArtwork(String s) {
        artwork = s?.trim()
    }

    String type
    void setType(String s) {
        type = s?.trim()
    }

    List<String> getTypeParts() {
        type.toLowerCase().tokenize()
    }

    boolean isCreature() {
        typeParts.contains("creature")
    }

    boolean isLand() {
        typeParts.contains("land")
    }

    boolean isArtifact() {
        typeParts.contains("artifact")
    }

    boolean isEnchantment() {
        typeParts.contains("enchantment")
    }

    String subtype
    void setSubtype(String s) {
        subtype = s?.trim()
    }

    List<String> getSubtypeParts() {
        subtype.toLowerCase().tokenize()
    }

    boolean isSubtyped() {
        subtype != null && subtype != ''
    }

    String body
    void setBody(String s) {
        body = s?.trim()
    }

    String power
    String getPower() {
        power ?: "?"
    }
    void setPower(String s) {
        power = s?.trim()
    }

    String toughness
    String getToughness() {
        toughness ?: "?"
    }
    void setToughness(String s) {
        toughness = s?.trim()
    }

    String artist
    void setArtist(String s) {
        artist = s?.trim()
    }

    String getFooter() {
        set?.footer
    }

    boolean hasSet() {
        set != null
    }

    Integer getCardOfSet() {
        set?.getCardOfSet(this)
    }

    Integer getCardsInSet() {
        set?.cardsInSet
    }

}
