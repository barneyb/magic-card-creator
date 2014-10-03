package com.barneyb.magic.creator.descriptor.support;

import com.barneyb.magic.creator.descriptor.schema.RarityEnum;

/**
 * @author barneyb
 */
public interface ICardType {
    RarityEnum getRarity();

    void setRarity(RarityEnum v);
}
