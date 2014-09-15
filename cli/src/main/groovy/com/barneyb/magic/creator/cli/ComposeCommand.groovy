package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.theme.ThemeLoader
import com.barneyb.magic.creator.util.XmlUtils
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "compose", commandDescription = "compose cards from a descriptor")
class ComposeCommand extends BaseDescriptorCommand {

    @Parameter(names = ["-o", "--output"], description = "The directory to compose into", required = true)
    File outputDir

    @Parameter(names = ["-t", "--theme"], description = "A theme descriptor JSON file")
    URL themeDescriptor = getClass().classLoader.getResource("theme/default/descriptor.json")

    void execute() {
        if (! outputDir.exists()) {
            outputDir.mkdirs()
        } else if (outputDir.isFile()) {
            throw new IllegalStateException("You cannot output into a file")
        }
        def cs = loadDescriptor()
        def theme = new ThemeLoader().load(themeDescriptor)
        def maxTitleLength = cs.cards*.title*.length().max()
        def nLength = cs.cards.size().toString().length()
        def proofs = new File(outputDir, "proof-${cs.key}.html").newPrintWriter()
        proofs.println """\
<html>
<head>
<meta http-equiv="content-type" content="application/xhtml+xml; charset=utf-8" />
</head>
<body>
"""
        cs.cards.each {
            println("(${it.cardNumber.toString().padLeft(nLength)}/$it.setCardCount) $it.title" + '.' * (maxTitleLength - it.title.length() + 2))
            def start = System.currentTimeMillis()
            def file = new File(outputDir, it.cardNumber + ".svg")
            XmlUtils.write(theme.layout(it), file.newWriter())
            def elapsed = System.currentTimeMillis() - start
            println(' ' * (nLength * 2 + 4) + "done (${(Math.round(elapsed / 100) / 10).toString().padLeft(4)} s)")
            proofs.println """<img src="$file.name" />"""
        }
        proofs.println """
<p>Generated at: ${new Date()}
</body>
</html>"""
        proofs.close()
    }
}
