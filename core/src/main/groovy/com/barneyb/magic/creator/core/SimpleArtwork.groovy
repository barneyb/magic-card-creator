package com.barneyb.magic.creator.core
import com.barneyb.magic.creator.api.Artwork
import com.barneyb.magic.creator.api.RasterImage
/**
 *
 *
 * @author barneyb
 */
class SimpleArtwork implements Artwork {

    final String artist

    @Delegate
    protected RasterImage _image

    def SimpleArtwork(URL url, String artist) {
        this._image = new UrlRasterImage(url)
        this.artist = artist
    }

}
