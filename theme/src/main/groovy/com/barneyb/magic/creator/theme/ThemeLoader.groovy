package com.barneyb.magic.creator.theme
import com.barneyb.magic.creator.api.Flood
import com.barneyb.magic.creator.api.LayoutType
import com.barneyb.magic.creator.api.Theme
import com.barneyb.magic.creator.api.ThemedColor
import com.barneyb.magic.creator.core.SimpleFlood
import com.barneyb.magic.creator.util.ColorUtils
import com.barneyb.magic.creator.util.DoubleRectangle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import groovy.transform.InheritConstructors
import groovy.transform.TupleConstructor

import java.awt.Color
import java.awt.geom.Rectangle2D
import java.lang.reflect.Type
/**
 * I load a theme from a descriptor, which is a JSON file describing all aspects
 * of a theme, as described by {@link ThemeSpec} and it's referenced classes,
 * and hand the loaded spec off to a Theme implementation that will use it as
 * the basis for it's functionality.
 *
 * @author barneyb
 */
class ThemeLoader {

    Theme load(URL descUrl) {
        def gsb = new GsonBuilder()
        gsb.registerTypeAdapter(Long, new JsonDeserializer<Long>() {
            @Override
            Long deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                def s = jsonElement.asString
                if (s.startsWith("0x")) {
                    Long.parseLong(s.substring(2), 16)
                } else {
                    throw new IllegalArgumentException("Cannot deserialize '$s' as hex-encoded Long.")
                }
            }
        })
        gsb.registerTypeAdapterFactory(new TypeAdapterFactory() {
            @SuppressWarnings("GroovyAssignabilityCheck")
            @Override
            def <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                final delegate = gson.getDelegateAdapter(this, type)
                switch (type.getRawType()) {
                    case TextureSpec:
                        return new TextureAdapter(delegate)
                    case TextAreaSpec:
                        return new TextAreaAdapter(delegate)
                    case Color:
                        return new ColorAdapter()
                    case Flood:
                        return new FloodAdapter(gson.getDelegateAdapter(this, TypeToken.get(SimpleFlood)))
                    case LayoutSpec:
                        return new LayoutAdapter(delegate, gson.getAdapter(URL))
                    case Rectangle2D:
                        return new RectangleAdapter()
                    case URL:
                        return new URLAdapter(descUrl)
                    case Class:
                        return new ClassAdapter()
                    case LayoutType:
                        return new EnumAdapter(LayoutType)
                    case ThemedColor:
                        return new EnumAdapter(ThemedColor)
                    default:
                        return null
                }
            }
        })
        def td = gsb.create().fromJson(descUrl.newReader(), ThemeSpec)
        new DynamicTheme(td)
    }

    @TupleConstructor
    static abstract class BaseAdapter<T> extends TypeAdapter<T> {

        @SuppressWarnings("GrFinalVariableAccess")
        final TypeAdapter delegate

        @Override
        void write(JsonWriter out, T value) throws IOException {
            throw new UnsupportedOperationException()
        }

    }

    @InheritConstructors
    static class TextureAdapter extends BaseAdapter<TextureSpec> {

        @Override
        TextureSpec read(JsonReader r) throws IOException {
            if (r.peek() == JsonToken.STRING) {
                new TextureSpec(base: r.nextString())
            } else {
                delegate.read(r)
            }
        }

    }

    @InheritConstructors
    static class TextAreaAdapter extends BaseAdapter<TextAreaSpec> {

        @Override
        TextAreaSpec read(JsonReader r) throws IOException {
            if (r.peek() == JsonToken.BEGIN_ARRAY) {
                r.beginArray()
                def ta = new TextAreaSpec(
                    r.nextInt(),
                    r.nextInt(),
                    r.nextInt(),
                    r.nextInt()
                )
                r.endArray()
                return ta
            } else {
                delegate.read(r)
            }
        }

    }

    @TupleConstructor(includeSuperProperties = true, callSuper = true)
    static class LayoutAdapter extends BaseAdapter<LayoutSpec> {

        TypeAdapter<URL> urlAdapter

        @Override
        LayoutSpec read(JsonReader r) throws IOException {
            if (r.peek() == JsonToken.STRING) {
                new LayoutSpec(template: urlAdapter.read(r))
            } else {
                delegate.read(r)
            }
        }

    }

    static class ColorAdapter extends BaseAdapter<Color> {

        @Override
        Color read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.STRING) {
                def s = jsonReader.nextString()
                if (s.startsWith("0x")) {
                    return ColorUtils.fromHex(s)
                }
            }
            throw new JsonSyntaxException("cannot parse color")
        }
    }

    static class ClassAdapter extends BaseAdapter<Class> {

        @Override
        Class read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.STRING) {
                return Class.forName(jsonReader.nextString())
            }
            throw new JsonSyntaxException("cannot parse class")
        }
    }

    static class EnumAdapter<T extends Enum> extends BaseAdapter<T> {

        Class<T> type

        def EnumAdapter(Class<T> t) {
            type = t
        }

        @Override
        T read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.STRING) {
                return Enum.valueOf(type, jsonReader.nextString().toUpperCase())
            }
            throw new JsonSyntaxException("cannot parse enum")
        }
    }

    @InheritConstructors
    static class FloodAdapter extends BaseAdapter<Flood> {

        @Override
        Flood read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.STRING) {
                def s = jsonReader.nextString()
                if (s.startsWith("0x")) {
                    s = s.substring(2)
                    if (s.length() == 6) {
                        // if no alpha, assume fully opaque
                        s = "ff" + s
                    }
                    def l = Long.parseLong(s, 16)
                    return new SimpleFlood(new Color((int) l), (float) ((l & 0xff000000) >> 24) / 255.0)
                }
            } else if (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
                return (Flood) delegate.read(jsonReader)
            }
            throw new JsonSyntaxException("cannot parse flood")
        }
    }

    static class RectangleAdapter extends BaseAdapter<Rectangle2D> {

        @Override
        Rectangle2D read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.BEGIN_ARRAY) {
                jsonReader.beginArray()
                def r = new DoubleRectangle(
                    jsonReader.nextDouble(),
                    jsonReader.nextDouble(),
                    jsonReader.nextDouble(),
                    jsonReader.nextDouble()
                )
                jsonReader.endArray()
                return r
            }
            throw new JsonSyntaxException("cannot parse rectangle")
        }
    }

    @TupleConstructor
    static class URLAdapter extends BaseAdapter<URL> {

        URL baseUrl

        @Override
        URL read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.STRING) {
                return new URL(baseUrl, jsonReader.nextString())
            }
            throw new JsonSyntaxException("cannot parse url")
        }
    }

}
