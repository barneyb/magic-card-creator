package com.barneyb.magic.creator.cli

import com.beust.jcommander.IStringConverter
import com.beust.jcommander.IStringConverterFactory

/**
 *
 *
 * @author barneyb
 */
class Converters implements IStringConverterFactory {

    private static final Map CONVERTERS = [
        (URL): URLConverter
    ]

    @Override
    public <T> Class<? extends IStringConverter<T>> getConverter(Class<T> forType) {
        CONVERTERS.get(forType)
    }

}
