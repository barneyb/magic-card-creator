package com.barneyb.magic.creator.asset
import com.google.gson.Gson

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
/**
 *
 * @author bboisvert
 */
class AssetDescriptor {

    static AssetDescriptor fromText(String text) {
       fromReader(new StringReader(text))
    }

    static AssetDescriptor fromStream(InputStream stream) {
        fromReader(new InputStreamReader(stream))
    }

    static AssetDescriptor fromReader(Reader reader) {
        def d = new AssetDescriptor(new Gson().fromJson(reader, new ParameterizedType() {

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
        d.renderSets.each { rsk, rs ->
            rs.key = rsk
            ['frames', 'large', 'small'].each {
                def s = rs[it]
                s.renderSet = rs
                s.key = it
            }
        }
        d
    }

    final Map<String, RenderSet> renderSets

    def AssetDescriptor(Map<String, RenderSet> renderSets) {
        this.renderSets = renderSets.asImmutable()
    }

    RenderSet getRenderSet(String name) {
        def rs = renderSets.get(name)
        if (rs == null) {
            throw new IllegalArgumentException("No renderset named '$name' is known (${renderSets.keySet().join(', ')}).")
        }
        rs
    }
}
