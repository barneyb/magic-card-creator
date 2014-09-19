package com.barneyb.magic.creator.cli

import java.nio.file.Files

/**
 *
 *
 * @author barneyb
 */
class FileUtils {

    /**
     * I return a closure that accepts an arbitrary File object and returns a
     * File w/in the passed outputDir that has not yet been returned by the
     * same closure.
     * @param outputDir the directory all returned files should be within
     * @param srcSuffix the suffix on source files to stripped, if any
     * @param destSuffix the suffix all returned files should have
     * @throws NullPointerException if outputDir or destSuffix is null
     */
    static Closure<File> uniqueFileBuilder(File outputDir, String srcSuffix=null, String destSuffix) {
        final Set alreadyTargeted = []
        return { File f ->
            def root = f.name
            if (srcSuffix && root.endsWith(srcSuffix)) {
                root = root.substring(0, root.length() - 4) // strip the extension
            }
            def t = new File(outputDir, root + destSuffix)
            for (int i = 0; ! alreadyTargeted.add(t.canonicalFile); i++) {
                t = new File(outputDir, root + '_' + i + destSuffix)
            }
            t
        }
    }

    /**
     * I accept a glob pattern and invoke the passed work for each File it
     * matches based on the working directory (or the root, if the glob starts
     * with a leading slash).
     * @param work The work to do per File, which will be passed to it
     * @param glob The glob to identify files of interest
     */
    static void eachFile(Closure work, String glob) {
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
