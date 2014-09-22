package com.barneyb.magic.creator.cli

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException

/**
 *
 *
 * @author barneyb
 */
abstract class BaseTranscodeCommand implements Executable {

    @Parameter(names = "--docker", description = "The name of the 'docker' binary to use")
    String dockerCommand = "docker"

    @Parameter(description = "list of transcoding inputs (supporting ? and * filename globs)", required = true)
    List<String> inputs

    @Parameter(names = ["-o", "--output-dir"], description = "The directory to transcode into", required = true)
    File outputDir

    @Override
    void execute(MainCommand main, JCommander jc) {
        if (inputs == null || inputs.empty) {
            throw new ParameterException("No inputs were specified.")
        }
        DockerUtils.withDocker dockerCommand, { String hostAndPort ->
            HttpUtils.withConverter "http://$hostAndPort" + urlPath, { postAndSave ->
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

    /**
     * I return a domain-relative path (starting with a slash) for where to
     * send transcode requests.
     */
    abstract String getUrlPath()

    /**
     * I return the file suffix to use for transcoded files.
     */
    abstract String getFileSuffix()

    private Closure<File> targeter
    protected File targetFromSource(File f) {
        if (targeter == null) {
            targeter = targeter = FileUtils.uniqueFileBuilder(outputDir, ".svg", fileSuffix)
        }
        targeter(f)
    }

    protected void eachFile(Closure work) {
        inputs.each FileUtils.&eachFile.curry(work)
    }

}
