package com.barneyb.magic.creator.cli
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import com.beust.jcommander.Parameters
import org.apache.http.HttpStatus
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.impl.client.HttpClients

import java.nio.file.Files
/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "transcode", commandDescription = "transcode composed cards (via docker)")
class TranscodeCommand implements Executable {

    @Parameter(names = "--docker", description = "The name of the 'docker' binary to use")
    String dockerCommand = "docker"

    @Parameter(description = "The list of transcoding targets, supporting normal ? and * filename globs.  If '-' is passed as the only target, STDIN will be read instead")
    List<String> targets

    @Parameter(names = ["-o", "--output-dir"], description = "The directory to transcode into", required = true)
    File outputDir

    @Override
    void execute(MainCommand main) {
        if (targets == null || targets.empty) {
            throw new ParameterException("No target(s) were specified.")
        }
        DockerUtils.withDocker dockerCommand, { String hostAndPort ->
            def baseUrl = "http://$hostAndPort/convert/png"
            withHttpClient { HttpClient httpclient ->
                // transcode the file(s)
                eachFile { File src ->
                    println "processing $src"
                    def dest = targetFromSource(src)
                    postAndSave(httpclient, baseUrl, src, dest)
                    println "saved $dest"
                }
            }
        }
    }

    protected void postAndSave(HttpClient httpclient, String endpoint, File src, File dest) {
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

    protected withHttpClient(Closure work) {
        def httpclient = HttpClients.createDefault()
        try {
            work(httpclient)
        } finally {
            httpclient.close()
        }
    }

    private Set alreadyTargeted = []
    protected File targetFromSource(File f) {
        def root = f.name
        if (root.endsWith(".svg")) {
            root = root.substring(0, root.length() - 4) // strip the extension
        }
        def t = new File(outputDir, root + '.png')
        for (int i = 0; ! alreadyTargeted.add(t.canonicalFile); i++) {
            t = new File(outputDir, root + '_' + i + '.png')
        }
        t
    }

    protected void eachFile(Closure work) {
        if (targets.size() == 1 && targets.first() == '-') {
            // stdin
            char c
            StringBuilder sb = new StringBuilder()
            while ((c = System.in.read()) >= 0) {
                if (Character.isWhitespace(c)) {
                    if (sb.length() > 0) {
                        eachFile work, sb.toString()
                        sb = new StringBuilder()
                    }
                } else {
                    sb.append(c)
                }
            }
        } else {
            // args
            targets.each this.&eachFile.curry(work)
        }
    }

    protected void eachFile(Closure work, String glob) {
        def dir = new File(glob.startsWith('/') ? "/" : ".")
        def parts = glob.tokenize('/')
        parts.eachWithIndex { p, i ->
            if (i == parts.size() - 1) {
                //noinspection GroovyAssignabilityCheck
                Files.newDirectoryStream(dir.toPath(), p)*.toFile().each work
            } else {
                dir = new File(dir, p)
            }
        }
    }

}
