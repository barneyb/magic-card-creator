package com.barneyb.magic.creator.cli

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

/**
 *
 *
 * @author barneyb
 */
@Parameters(commandDescription = "transcode composed cards to PNG (via docker)", separators = "=")
class PngCommand extends BaseTranscodeCommand {

    @Parameter(names = "--width", description = "The width the output images should be")
    int width = 400

    @Override
    protected String getUrlPath() {
        "/convert/png?w=$width"
    }

    @Override
    protected String getFileSuffix() {
        ".png"
    }

}
