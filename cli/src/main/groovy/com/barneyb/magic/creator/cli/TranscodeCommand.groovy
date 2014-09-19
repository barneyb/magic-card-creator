package com.barneyb.magic.creator.cli
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import com.beust.jcommander.Parameters
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

    static final String IMAGE_NAME = 'magic-card-creator'

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
        def started = false
        // check if container is running
        def p = docker("ps")
        def contId = p.inputStream.readLines().find {
            it.tokenize()[1].startsWith(IMAGE_NAME + ":")
        }?.tokenize()?.first()
        // start up new container?
        if (contId == null) {
            println "no docker container found, creating a temporary one"
            contId = 'mtgc-' + UUID.randomUUID()
            docker("run", "--publish-all", "--detach", "--name", contId, IMAGE_NAME)
            Thread.sleep(1000) // OMG! // KLUDGE! // BE ASHAMED!
            started = true
        }
        // get network endpoint
        def ip = docker("port", contId, "8080").inputStream.readLines().first()
        def baseUrl = "http://$ip/convert/png"
        // transcode the file(s)
        eachFile { File f ->
            println "processing $f"
            def tgt = new File(outputDir, f.name + '.png')
            def httpclient = HttpClients.createDefault()
            try {
                def req = new HttpPost(baseUrl)
                req.entity = MultipartEntityBuilder.create()
                    .addPart("svg", new FileBody(f))
                    .build()
                def resp = httpclient.execute(req)
                if (resp.entity != null) {
                    def out = tgt.newOutputStream()
                    resp.entity.writeTo(out)
                    out.close()
                }
                println "saved $tgt"
            } finally {
                httpclient.close()
            }
        }
        // kill off container
        if (started) {
            println "cleaning up temporary docker container"
            docker("rm", "--force", contId).waitFor()
        }
    }

    protected docker(String... args) {
        new ProcessBuilder((List) [dockerCommand] + args.toList())
            .start()
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
