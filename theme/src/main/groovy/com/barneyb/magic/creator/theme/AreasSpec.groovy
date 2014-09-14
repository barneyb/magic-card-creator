package com.barneyb.magic.creator.theme

import java.awt.Rectangle
import java.awt.geom.Rectangle2D

/**
 *
 *
 * @author barneyb
 */
class AreasSpec {

    TextAreaSpec title = new TextAreaSpec(70, 70, 738, 45)
    Rectangle2D artwork = new Rectangle(68, 139, 740, 541)
    TextAreaSpec type = new TextAreaSpec(72, 706, 745, 40)
    TextAreaSpec textbox = new TextAreaSpec(76, 780, 720, 305)
    TextAreaSpec powerToughness = new TextAreaSpec(677, 1112, 125, 46)
    TextAreaSpec artist = new TextAreaSpec(135, 1132, 503, 28)
    TextAreaSpec footer = new TextAreaSpec(55, 1161, 583, 24)

}
