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

    static final String OUTPUT_DIRECTORY = "proof-icons"
    static final String FACTORY_FILENAME = "factory.js"
    static final String PROOFSHEET_FILENAME = "index.html"

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

    private String toFilename(s) {
        s.replaceAll(/[^a-zA-Z0-9]+/, '_')
    }

    private String toPad(s) {
        ' ' * (3 - s.length())
    }

    @Test
    void proofsheet() {
        def proofFile = new File(OUTPUT_DIRECTORY, PROOFSHEET_FILENAME)
        if (proofFile.exists()) {
            proofFile.delete()
        } else {
            proofFile.parentFile.mkdirs()
        }
        def factoryFile = new File(OUTPUT_DIRECTORY, FACTORY_FILENAME)
        if (factoryFile.exists()) {
            factoryFile.delete()
        }
        def symbolExpr = { s, pad ->
            def fn = toFilename(s)
            "shadow ? ${pad}_${fn}_shadow : disc ? ${pad}_$fn : ${pad}_${fn}_nodisc"
        }
        factoryFile.text = """\
${SYMBOL_GROUPS.collect { g, ss ->
    ss.collect { s ->
        def fn = toFilename(s)
        def pad = toPad(s)
        [
            "import ${pad}_${fn}        from './${fn}.svg';",
            "import ${pad}_${fn}_shadow from './${fn}_shadow.svg';",
            "import ${pad}_${fn}_nodisc from './${fn}_nodisc.svg';",
        ]
    }
}.flatten().join('\n')}

// style must be one of 'shadow'/'cost', 'disc'/'inline', 'nodisc'/'bare'
const factory = (symbol, shadow=false, disc=true) => {
    switch (symbol) {${SYMBOL_GROUPS.collect { g, ss ->
    ss.collect { s ->
        def pad = toPad(s)
        """
        case ${pad}'${s.toUpperCase()}': return ${symbolExpr(s, pad)};"""
    }
}.flatten().join('')}
        // sorta silly; don't have "unknown"
        default   : return ${symbolExpr('0', '  ')};
    }
}

export default factory;
"""
        def out = proofFile.newPrintWriter()
        out.print """\
<html>
<head>
<style>
html { text-align: center; }
body { max-width: 980px; margin: auto; background-color: #eed; }
div { float: left; margin-bottom: 20px; }
div > svg,
div > img { margin: 10px; }
h1,
p { text-align: left; clear: left; }
textarea { width: 100%; height: 140px; }
</style>
</head>
<body>
<h1>As Inline SVG</h1>
"""
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
                new File(OUTPUT_DIRECTORY, "${fn}_nodisc.svg").text = svg
                out.println "<br>"
                svg = XmlUtils.write factory.getIcon(it).document
                out.println svg
                new File(OUTPUT_DIRECTORY, "${fn}.svg").text = svg
                out.println "<br>"
                svg = XmlUtils.write factory.getShadowedIcon(it).document
                out.println svg
                new File(OUTPUT_DIRECTORY, "${fn}_shadow.svg").text = svg
                out.println "</div>"
            }
        }
        out.write """\
<h1>As ES6 Factory (in <a href="factory.js"><code>factory.js</code></a>)</h1>
<textarea>${factoryFile.text}</textarea>
<h1>As External SVG (via <code>&lt;img src&gt;</code>)</h1>
${SYMBOL_GROUPS.collect { g, ss ->
    ss.collect { s ->
        def fn = toFilename(s)
        """
        <div>
            <img src='${fn}_nodisc.svg' /><br />
            <img src='${fn}.svg' /><br />
            <img src='${fn}_shadow.svg' />
        </div>
        """
    }
}.flatten().join('\n')}
<p>Generated at: ${new Date()}
</body>
</html>
""".toString()
        out.close()
    }

}
