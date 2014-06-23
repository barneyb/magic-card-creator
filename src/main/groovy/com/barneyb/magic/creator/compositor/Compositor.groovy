package com.barneyb.magic.creator.compositor
import com.barneyb.magic.creator.asset.RenderSet
/**
 *
 * @author bboisvert
 */
interface Compositor {

    /**
     * I compose the passed RenderModel and RenderSet and send the result to the
     * supplied OutputStream.  The stream should be flushed and closed by the
     * compose method before returning.
     */
    void compose(RenderModel model, RenderSet rs, OutputStream dest)

}