package com.barneyb.magic.creator.cli
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import com.beust.jcommander.Parameters
/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "transcode", commandDescription = "transcode composed cards (via docker)")
class TranscodeCommand implements Executable {

    @Parameter(names = "--docker", description = "The name of the 'docker' binary to use")
    String dockerCommand = "docker"

    @Parameter(description = "The list of transcoding inputs, supporting normal ? and * filename globs.", required = true)
    List<String> inputs

    @Parameter(names = ["-o", "--output-dir"], description = "The directory to transcode into", required = true)
    File outputDir

    @Override
    void execute(MainCommand main) {
        if (inputs == null || inputs.empty) {
            throw new ParameterException("No inputs were specified.")
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
        inputs.each FileUtils.&eachFile.curry(work)
    }

}
