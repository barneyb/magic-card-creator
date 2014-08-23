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
class DefaultIconFactoryTest {

    static final String  PROOFSHEET_FILENAME = "proof-icons.html"

    DefaultSymbolFactory sf
    DefaultIconFactory factory

    @Before
    void _makeFactory() {
        sf = new DefaultSymbolFactory()
        factory = new DefaultIconFactory()
    }

    @Test
    void proofsheet() {
        def tgt = new File(PROOFSHEET_FILENAME)
        if (tgt.exists()) {
            tgt.delete()
        }
        def tmp = File.createTempFile(PROOFSHEET_FILENAME, "html")
        tmp.deleteOnExit()
        def out = tmp.newPrintWriter()
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
//            "phyrexian": ['w/p', 'u/p', 'b/p', 'r/p', 'g/p'],
            "colorless": ['x'] + (0..20)*.toString(),
        ].each { n, ss ->
            println n
            ss.collect {
                sf.getSymbol(it)
            }.each {
                println "  $it"
                out.println "<div>"
                XmlUtils.write factory.getBareIcon(it), out
                out.println "<br>"
                XmlUtils.write factory.getIcon(it), out
                out.println "<br>"
                XmlUtils.write factory.getShadowedIcon(it), out
                out.println "</div>"
            }
        }
        out.write """\
</body>
</html>
"""
        out.flush()
        out.close()
        tmp.renameTo(tgt)
    }

}
