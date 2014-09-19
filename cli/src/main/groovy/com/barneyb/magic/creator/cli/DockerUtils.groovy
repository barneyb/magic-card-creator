package com.barneyb.magic.creator.cli

/**
 *
 *
 * @author barneyb
 */
class DockerUtils {

    static final String IMAGE_NAME = 'magic-card-creator'

    static <T> T withDocker(String dockerCommand, Closure<T> work) {
        def docker = { String... args ->
            new ProcessBuilder((List) [dockerCommand] + args.toList())
                .start()
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
            started = true
        }
        try {
            if (started) {
                Thread.sleep(1000) // OMG! // KLUDGE! // BE ASHAMED!
            }
            // get network endpoint
            def hostAndPort = docker("port", contId, "8080").inputStream.readLines().first()
            work(hostAndPort)
        } finally {
            // kill off container
            if (started) {
                println "cleaning up temporary docker container"
                docker("rm", "--force", contId).waitFor()
            }
        }
    }

}
