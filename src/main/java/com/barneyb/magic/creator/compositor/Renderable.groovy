package com.barneyb.magic.creator.compositor

import java.awt.*
/**
 *
 * @author bboisvert
 */
interface Renderable {

    static class DrawCtx {
        final Graphics2D graphics
        Rectangle bounds
        double x
        double y
        float fontSize
        float wrapOffset

        def DrawCtx(Graphics2D g, Rectangle b, float fs) {
            graphics = g
            bounds = b
            x = bounds.x
            y = bounds.y
            fontSize = fs
            wrapOffset = fs
        }

        double getXOffset() {
            x - bounds.x
        }

        void setXOffset(double xOffset) {
            x = bounds.x + xOffset
        }

        Point getLocation() {
            new Point((int) x, (int) y)
        }
    }

}