package com.barneyb.magic.creator.descriptor
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 *
 * @author bboisvert
 */
@EqualsAndHashCode
@ToString
class CompoundFrame implements FrameType {

    final FrameBaseType base

    final Collection<FrameModifier> modifiers

    def CompoundFrame(FrameBaseType base, Collection<FrameModifier> modifiers=[]) {
        if (base == null) {
            throw new IllegalArgumentException("CompoundFrame must have a non-null base.")
        }
        if (modifiers.size() != modifiers*.priority.unique().size()) {
            throw new IllegalArgumentException("CompoundFrame doesn't support multiple modifiers of the same type.")
        }
        this.base = base
        this.modifiers = modifiers.sort {
            it.priority
        }.asImmutable()
    }

    @Override
    String getAssetKey() {
        def parts = modifiers*.assetKey
        parts.add(0, base.assetKey)
        parts.join("_")
    }

    @Override
    FrameType plus(FrameModifier mod) {
        if (mod == null) {
            throw new IllegalArgumentException("You cannot add a null modifier to a frame")
        }
        if (modifiers.contains(mod)) {
            this
        } else {
            new CompoundFrame(base, modifiers - mod.getClass().enumConstants + mod)
        }
    }

    @Override
    FrameType minus(FrameModifier mod) {
        if (modifiers.contains(mod)) {
            if (modifiers.size() == 1) {
                base
            } else {
                new CompoundFrame(base, modifiers - mod)
            }
        } else {
            this
        }
    }
}
