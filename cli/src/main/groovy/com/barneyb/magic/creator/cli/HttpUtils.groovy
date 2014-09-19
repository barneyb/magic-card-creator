package com.barneyb.magic.creator.cli

import org.apache.http.HttpStatus
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.impl.client.HttpClients

/**
 *
 *
 * @author barneyb
 */
class HttpUtils {

    /**
     * I manage an HttpClient instance and supply it as the only parameter to
     * the passed 'work' Closure.
     */
    static void withHttpClient(Closure work) {
        def httpclient = HttpClients.createDefault()
        try {
            work(httpclient)
        } finally {
            httpclient.close()
        }
    }

    /**
     * I manage a HTTP-based conversion process, accepting a work Closure that
     * needs such services, and supply as it's only argument a convert
     * function:
     *
     * <pre>
     *     [File, File] -> void
     *     [String, File, File] -> void
     * </pre>
     *
     * which will do the actual conversion.
     */
    static void withConverter(String defaultEndpoint, Closure work) {
        withHttpClient { HttpClient httpclient ->
            work { String endpoint=defaultEndpoint, File src, File dest ->
                def req = new HttpPost(endpoint)
                req.entity = MultipartEntityBuilder.create()
                    .addPart("svg", new FileBody(src))
                    .build()
                def resp = httpclient.execute(req)
                if (resp.statusLine.statusCode != HttpStatus.SC_OK) {
                    throw new IOException(resp.statusLine.reasonPhrase)
                }
                if (resp.entity != null) {
                    def out = dest.newOutputStream()
                    resp.entity.writeTo(out)
                    out.close()
                }
            }
        }
    }

}
