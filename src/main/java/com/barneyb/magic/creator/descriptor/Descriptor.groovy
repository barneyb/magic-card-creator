package com.barneyb.magic.creator.descriptor
import com.google.gson.Gson

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
/**
 *
 * @author bboisvert
 */
class Descriptor {

    static Descriptor fromText(String text) {
       fromReader(new StringReader(text))
    }

    static Descriptor fromStream(InputStream stream) {
        fromReader(new InputStreamReader(stream))
    }

    static Descriptor fromReader(Reader reader) {
        new Descriptor(new Gson().fromJson(reader, new ParameterizedType() {

            @Override
            Type[] getActualTypeArguments() {
                [String, RenderSet] as Type[]
            }

            @Override
            Type getRawType() {
                Map
            }

            @Override
            Type getOwnerType() {
                null
            }
        }))
    }

    final Map<String, RenderSet> renderSets

    def Descriptor(Map<String, RenderSet> renderSets) {
        this.renderSets = renderSets.asImmutable()
    }

}
