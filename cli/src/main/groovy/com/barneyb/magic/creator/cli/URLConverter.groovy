package com.barneyb.magic.creator.cli

import com.beust.jcommander.IStringConverter

/**
 *
 *
 * @author barneyb
 */
class URLConverter implements IStringConverter<URL> {
    @Override
    URL convert(String value) {
        value.contains("://") ? new URL(value) : new File(value).toURI().toURL()
    }
}
