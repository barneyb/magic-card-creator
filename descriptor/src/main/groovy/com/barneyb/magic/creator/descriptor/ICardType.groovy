package com.barneyb.magic.creator.descriptor

import com.barneyb.magic.creator.descriptor.schema.RarityEnum

/**
 *
 *
 * @author barneyb
 */
interface ICardType {

    RarityEnum getRarity()
    void setRarity(RarityEnum v)

}
