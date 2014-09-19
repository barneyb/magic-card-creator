package com.barneyb.magic.creator.cli
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import com.beust.jcommander.Parameters

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
            HttpUtils.withConverter baseUrl, { postAndSave ->
                // transcode the file(s)
                eachFile { File src ->
                    println "processing $src"
                    def dest = targetFromSource(src)
                    postAndSave(src, dest)
                    println "saved $dest"
                }
            }
        }
    }

    private Closure<File> targeter
    protected File targetFromSource(File f) {
        if (targeter == null) {
            targeter = targeter = FileUtils.uniqueFileBuilder(outputDir, ".svg", ".png")
        }
        targeter(f)
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
