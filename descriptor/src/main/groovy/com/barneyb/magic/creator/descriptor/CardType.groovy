package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.descriptor.schema.ArtworkType
import com.barneyb.magic.creator.descriptor.schema.RarityEnum
import com.barneyb.magic.creator.descriptor.schema.RulesTextType

/**
 *
 *
 * @author barneyb
 */
interface CardType {

    ArtworkType getArtwork()

    ArtworkType getOverArtwork()

    String getColorIndicator()

    String getType()

    String getTypeModifiers()

    String getSubtype()

    RarityEnum getRarity()

    RulesTextType getRulesText()

    String getFlavorText()

    String getWatermark()

    String getTitle()

}
