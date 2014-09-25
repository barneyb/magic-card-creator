package com.barneyb.magic.creator.cli

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 *
 *
 * @author barneyb
 */
class DockerUtils {

    static final String IMAGE_NAME = 'magic-card-creator'
    static final String IMAGE_TAG = '0.6.1'

    static <T> T withDocker(String dockerCommand, Closure<T> work) {
        def docker = { String... args ->
            def cmd = (List) [dockerCommand] + args.toList()
            new ProcessBuilder(cmd).inheritIO()
        }
        def started = false
        // check if container is running
        def p = docker("ps")
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .start()
        def contId = p.inputStream.readLines().find {
            it.tokenize()[1] == IMAGE_NAME + ":" + IMAGE_TAG
        }?.tokenize()?.first()
        // start up new container?
        if (contId == null) {
            println "no docker container found, creating a temporary one"
            p = docker("images")
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .start()
            def imgId = p.inputStream.readLines().find {
                def parts = it.tokenize()
                parts[0] == IMAGE_NAME && parts[1] == IMAGE_TAG
            }?.tokenize()?.get(2)
            if (imgId == null) {
                def dir = unzip("docker-src.zip")
                println "building image"
                def exit = docker("build", "-t", IMAGE_NAME + ":" + IMAGE_TAG, dir.absoluteFile.canonicalPath)
                    .start()
                    .waitFor()
                dir.deleteDir()
                if (exit != 0) {
                    throw new IOException("Failed to build docker image (exit $exit)")
                }
            }
            contId = 'mtgc-' + UUID.randomUUID()
            docker("run", "--publish-all", "--detach", "--name", contId, IMAGE_NAME + ":" + IMAGE_TAG)
                .start()
                .waitFor()
            started = true
        }
        try {
            if (started) {
                Thread.sleep(1000) // OMG! // KLUDGE! // BE ASHAMED!
            }
            // get network endpoint
            def hostAndPort = docker("port", contId, "8080")
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .start()
                .inputStream.readLines().first()
            work(hostAndPort)
        } finally {
            // kill off container
            if (started) {
                println "cleaning up temporary docker container"
                docker("rm", "--force", contId)
                    .start()
                    .waitFor()
            }
        }
    }

    protected static File unzip(String zipResource){
        // this method largely based on http://www.mkyong.com/java/how-to-decompress-files-from-a-zip-file/
        byte[] buffer = new byte[1024];
        File folder = File.createTempDir().absoluteFile
        println "unzip to $folder"
        ZipInputStream zis = new ZipInputStream(DockerUtils.classLoader.getResource(zipResource).newInputStream());
        ZipEntry ze = zis.getNextEntry();
        while (ze!=null) {
            def fileName = ze.getName();
            println("  extract: " + fileName);
            def newFile = new File(folder, fileName);
            if (ze.directory) {
                newFile.mkdirs()
            } else {
                new File(newFile.getParent()).mkdirs();
                def fos = newFile.newOutputStream();
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            ze = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        folder
    }

}
