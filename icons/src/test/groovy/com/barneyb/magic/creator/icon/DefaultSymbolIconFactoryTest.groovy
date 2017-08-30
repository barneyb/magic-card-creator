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

    static final String PROOFSHEET_FILENAME = "proof-icons/index.html"

    static final Map SYMBOL_GROUPS = [
        "colored"    : ['w', 'u', 'b', 'r', 'g'],
        "tap"        : ['t', 'q'],
        "hybrid"     : ['w/u', 'w/b', 'u/b', 'u/r', 'b/r', 'b/g', 'r/g', 'r/w', 'g/w', 'g/u'],
        "mono-hybrid": ['2/w', '2/u', '2/b', '2/r', '2/g'],
        "phyrexian"  : ['w/p', 'u/p', 'b/p', 'r/p', 'g/p'],
        "colorless"  : ['x'] + (0..20)*.toString(),
    ]

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
        } else {
            tgt.parentFile.mkdirs()
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
pre { text-align: left; clear: left; }
</style>
</head>
<body>
"""
        def toFilename = { s ->
            s.replaceAll(/[^a-zA-Z0-9]+/, '_')
        }
        SYMBOL_GROUPS.each { n, ss ->
            println n
            ss.collect {
                [it, sf.getSymbol(it)]
            }.each { p ->
                def fn = toFilename(p.first())
                def it = p.last()
                println "  $it"
                out.println "<div>"
                def svg = XmlUtils.write factory.getBareIcon(it).document
                out.println svg
                new File("proof-icons/${fn}_nodisc.svg").text = svg
                out.println "<br>"
                svg = XmlUtils.write factory.getIcon(it).document
                out.println svg
                new File("proof-icons/${fn}.svg").text = svg
                out.println "<br>"
                svg = XmlUtils.write factory.getShadowedIcon(it).document
                out.println svg
                new File("proof-icons/${fn}_shadow.svg").text = svg
                out.println "</div>"
            }
        }
        out.write """\
<p>Generated at: ${new Date()}
<pre>
${SYMBOL_GROUPS.collect { g, ss ->
    ss.collect { s ->
        def fn = toFilename(s)
        [
            "import _$fn from '../assets/symbols/${fn}.svg';",
            "import _${fn}_shadow from '../assets/symbols/${fn}_shadow.svg';",
            "import _${fn}_nodisc from '../assets/symbols/${fn}_nodisc.svg';",
        ]
    }
}.flatten().join('\n')}
</pre>
<pre>
${SYMBOL_GROUPS.collect { g, ss ->
    ss.collect { s ->
        def fn = toFilename(s)
        "case '${s.toUpperCase()}': return shadow ? _${fn}_shadow : disc ? _$fn : _${fn}_nodisc;"
    }.join('\n')
}.join('\n')}
</pre>
</body>
</html>
""".toString()
        out.close()
    }

}
