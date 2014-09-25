package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.api.Theme
import com.barneyb.magic.creator.api.ThemeLoader
import com.barneyb.magic.creator.core.ServiceUtils
import com.barneyb.magic.creator.util.XmlUtils
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import groovy.util.logging.Log

/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "compose", commandDescription = "compose cards from a descriptor", separators = "=")
@Log
class ComposeCommand extends BaseDescriptorCommand implements Executable {

    @Parameter(names = ["-o", "--output-dir"], description = "The directory to compose into", required = true)
    File outputDir

    @Parameter(names = "--theme-key", description = "The key of the theme to use")
    String themeKey = "dynamic"

    @Parameter(names = "--theme-descriptor", description = "A theme descriptor file, if needed")
    File themeDescriptor

    @Parameter(names = "--print", description = "If true, composed cards will be bled for printing")
    boolean print = false

    void execute(MainCommand main, JCommander jc) {
        if (! outputDir.exists()) {
            outputDir.mkdirs()
        } else if (outputDir.isFile()) {
            throw new IllegalStateException("You cannot output into a file")
        }
        def cs = loadDescriptor()
        println("composing set '$cs.title' ($cs.key)")
        def theme = createTheme()
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
                def doc = theme.layout(it)
                if (print) {
                    def xbleed = 17.5
                    def ybleed = 17.5
                    def rotate = 0 // todo: setting this to anything but 0 mod 360 screws up the filters and patterns.  no idea why.
                    def size = SvgUtils.size(doc)
                    def base = doc
                    doc = XmlUtils.create()

                    def w = size.width
                    def h = size.height
                    def flip = rotate % 180 != 0
                    def newWidth = (flip ? h : w) + xbleed * 2
                    def newHeight = (flip ? w : h) + ybleed * 2

                    XmlUtils.elattr(doc.rootElement, [
                        width: newWidth,
                        height: newHeight
                    ])
                    XmlUtils.el(doc.rootElement, 'rect', [
                        width: "100%",
                        height: "100%",
                        fill: "#000"
                    ])
                    def g = XmlUtils.el(doc.rootElement, 'g', [
                        transform: "translate($xbleed $ybleed)"
                    ])
                    if (rotate % 360 != 0) {
                        g = XmlUtils.el(g, 'g', [
                            transform: (flip ? "translate(${(h - w) / 2} ${(w - h) / 2}) " : "") + "rotate($rotate ${(float) w / 2} ${(float) h / 2})"
                        ])
                    }
                    g.appendChild(doc.adoptNode(base.rootElement))
                }
                def out = file.newWriter()
                XmlUtils.write(doc, out)
                out.close()
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

    protected Theme createTheme() {
        ServiceUtils.load(ThemeLoader, themeKey).load(themeDescriptor?.toURI()?.toURL())
    }

}
