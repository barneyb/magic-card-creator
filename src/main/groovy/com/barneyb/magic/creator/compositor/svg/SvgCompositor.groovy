package com.barneyb.magic.creator.compositor.svg

import com.barneyb.magic.creator.asset.RenderSet
import com.barneyb.magic.creator.compositor.Compositor
import com.barneyb.magic.creator.compositor.RenderModel

/**
 *
 * @author bboisvert
 */
class SvgCompositor implements Compositor {

    @Override
    void compose(RenderModel model, RenderSet rs, OutputStream dest) {
        def out = new PrintWriter(dest)
        out.println('<svg xmlns="http://www.w3.org/2000/svg" width="' + rs.frames.size.width + '" height="' + rs.frames.size.height + '">')
        out.println('<text x="10" y="40" fill="#000000">' + model.title + '</text>')
        out.println("</svg>")
        out.close()
    }

}
