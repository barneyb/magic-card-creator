package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.descriptor.schema.ArtworkType
import com.barneyb.magic.creator.descriptor.schema.NonNormativeTextType
import com.barneyb.magic.creator.descriptor.schema.RulesTextType

/**
 *
 *
 * @author barneyb
 */
interface ICoreType {

    String getTitle()
    void setTitle(String v)

    String getColorIndicator()
    void setColorIndicator(String v)

    ArtworkType getArtwork()
    void setArtwork(ArtworkType v)

    ArtworkType getOverArtwork()
    void setOverArtwork(ArtworkType v)

    RulesTextType getRulesText()
    void setRulesText(RulesTextType v)

    NonNormativeTextType getFlavorText()
    void setFlavorText(NonNormativeTextType v)

    String getWatermark()
    void setWatermark(String v)

}
