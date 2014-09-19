package com.barneyb.magic.creator.cli

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

}
