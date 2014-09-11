package com.barneyb.magic.creator.icon
import com.barneyb.magic.creator.api.Rarity
import com.barneyb.magic.creator.util.XmlUtils
import org.junit.Before
import org.junit.Test
/**
 *
 *
 * @author barneyb
 */
class DefaultCardSetIconFactoryTest {

    static final String PROOFSHEET_FILENAME = "proof-set-icons.html"

    DefaultCardSetIconFactory factory

    @Before
    void _makeFactory() {
        factory = new DefaultCardSetIconFactory()
    }

    @Test
    void proofsheet() {
        def tgt = new File(PROOFSHEET_FILENAME)
        if (tgt.exists()) {
            tgt.delete()
        }
        def out = tgt.newPrintWriter()
        out.print """\
<html>
<head>
<style>
html { text-align: center; }
body { max-width: 980px; margin: auto; background-color: #eed; }
div { float: left; margin-bottom: 20px; }
div > svg { margin: 10px; }
</style>
</head>
<body>
"""
        [
            'BaB', 'AiO', 'Barney', 'BB'
        ].each {
            println "  $it"
            out.println "<div>"
            Rarity.enumConstants.each { r ->
                XmlUtils.write factory.getIcon(it, r).document, out
                out.println "<br />"
            }
            out.println "</div>"
        }
        out.write """\
<p>Generated at: ${new Date()}
</body>
</html>
""".toString()
        out.close()
    }

}
