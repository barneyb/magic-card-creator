package com.barneyb.magic.creator.icon
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory
import com.barneyb.magic.creator.util.XmlUtils
import org.junit.Before
import org.junit.Test
/**
 *
 *
 * @author barneyb
 */
class DefaultSymbolIconFactoryTest {

    static final String PROOFSHEET_FILENAME = "proof-icons.html"

    DefaultSymbolFactory sf
    DefaultSymbolIconFactory factory

    @Before
    void _makeFactory() {
        sf = new DefaultSymbolFactory()
        factory = new DefaultSymbolIconFactory()
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
            "colored": ['w', 'u', 'b', 'r', 'g'],
            "tap": ['t', 'q'],
            "hybrid": ['w/u', 'w/b', 'u/b', 'u/r', 'b/r', 'b/g', 'r/g', 'r/w', 'g/w', 'g/u'],
            "mono-hybrid": ['2/w', '2/u', '2/b', '2/r', '2/g'],
            "phyrexian": ['w/p', 'u/p', 'b/p', 'r/p', 'g/p'],
            "colorless": ['x'] + (0..20)*.toString(),
        ].each { n, ss ->
            println n
            ss.collect {
                sf.getSymbol(it)
            }.each {
                println "  $it"
                out.println "<div>"
                XmlUtils.write factory.getBareIcon(it).document, out
                out.println "<br>"
                XmlUtils.write factory.getIcon(it).document, out
                out.println "<br>"
                XmlUtils.write factory.getShadowedIcon(it).document, out
                out.println "</div>"
            }
        }
        out.write """\
<p>Generated at: ${new Date()}
</body>
</html>
""".toString()
        out.close()
    }

}
