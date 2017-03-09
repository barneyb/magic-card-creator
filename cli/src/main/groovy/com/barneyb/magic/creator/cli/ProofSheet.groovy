package com.barneyb.magic.creator.cli

/**
 *
 *
 * @author barneyb
 */
class ProofSheet {

    private final List<String> imagePaths = []

    void addImage(String path) {
        imagePaths.add(path)
    }

    void render(OutputStream outStr) {
        def out = new PrintStream(outStr)
        out.println """\
<html>
<head>
<meta http-equiv="content-type" content="application/xhtml+xml; charset=utf-8" />
</head>
<body>
"""
        imagePaths.each {
            out.println """<img src="$it" />"""
        }
        out.println """
<p>Generated at: ${new Date()}
</body>
</html>"""
        out.close()
    }

}
