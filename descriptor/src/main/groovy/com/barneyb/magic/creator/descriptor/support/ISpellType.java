package com.barneyb.magic.creator.descriptor.support;

/**
 * @author barneyb
 */
public interface ISpellType extends ICoreType {
    String getCastingCost();

    void setCastingCost(String v);
}
