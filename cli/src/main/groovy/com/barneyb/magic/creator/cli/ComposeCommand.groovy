package com.barneyb.magic.creator.cli
import com.barneyb.magic.creator.theme.ThemeLoader
import com.barneyb.magic.creator.util.XmlUtils
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import groovy.util.logging.Log
/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "compose", commandDescription = "compose cards from a descriptor")
@Log
class ComposeCommand extends BaseDescriptorCommand implements Executable {

    public static final URL DEFAULT_THEME_DESCRIPTOR = ComposeCommand.classLoader.getResource("theme/default/descriptor.json")

    @Parameter(names = ["-o", "--output-dir"], description = "The directory to compose into", required = true)
    File outputDir

    @Parameter(names = ["-t", "--theme"], description = "A theme descriptor JSON file")
    File themeDescriptor

    void execute(MainCommand main) {
        if (! outputDir.exists()) {
            outputDir.mkdirs()
        } else if (outputDir.isFile()) {
            throw new IllegalStateException("You cannot output into a file")
        }
        def cs = loadDescriptor()
        println("composing set '$cs.title' ($cs.key)")
        def theme = new ThemeLoader().load(themeDescriptor ? themeDescriptor.toURI().toURL() : DEFAULT_THEME_DESCRIPTOR)
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
        filterCards(cs).each {
            println("(${it.cardNumber.toString().padLeft(nLength)}/$it.setCardCount) $it.title" + '.' * (maxTitleLength - it.title.length() + 2))
            def start = System.currentTimeMillis()
            def file = new File(outputDir, it.cardNumber + ".svg")
            try {
                XmlUtils.write(theme.layout(it), file.newWriter())
            } catch (Exception e) {
                log.severe("Failed to lay out card: $e")
                if (main.debug) {
                    e.printStackTrace()
                }
                return // bail
            }
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
