package assets.print

interface Icon {
    String getId()
    String getFilename()
    String getLarge()
    String getSmall()
}

class SimpleIcon implements Icon {

    String id
    String color = '#d0cac3'
    String body

    String getFilename() {
        id + ".svg"
    }

    String getLarge() {
        """\
<svg xmlns="http://www.w3.org/2000/svg" width="45" height="47">
    <circle r="22" cx="22" cy="25" fill="#000" opacity="0.75" />
    <circle r="22" cx="23" cy="22" fill="$color" />
    <g transform="translate(-2 0)">
        $body
    </g>
</svg>
"""
    }

    String getSmall() {
        """\
<svg xmlns="http://www.w3.org/2000/svg" width="22" height="22">
    <circle r="11" cx="11" cy="11" fill="$color" />
    <g transform="translate(-1.5 0) scale(0.5)">
        $body
    </g>
</svg>
"""
    }
}

class HybridIcon implements Icon {

    SimpleIcon top
    SimpleIcon bottom
    String body

    def HybridIcon(SimpleIcon t, SimpleIcon b) {
        this.top = t
        this.bottom = b
        this.body = """\
            <g transform="translate(10 10)">
                ${top.body}
            </g>
            <g transform="translate(45 45)">
                ${bottom.body}
            </g>
        """
    }

    String getId() {
        top.id + '_' + bottom.id
    }

    String getFilename() {
        id + ".svg"
    }

    String getLarge() {
        """\
<svg xmlns="http://www.w3.org/2000/svg" width="45" height="47">
    <defs>
        <clipPath id="top-half">
            <rect width="47" height="22" />
        </clipPath>
    </defs>
    <circle r="22" cx="22" cy="25" fill="#000" opacity="0.75" />
    <g transform="rotate(-45 23 22)">
        <circle r="22" cx="23" cy="22" fill="$bottom.color" />
        <circle r="22" cx="23" cy="22" fill="$top.color" clip-path="url(#top-half)" />
    </g>
    <g transform="scale(.45)">
        $body
    </g>
</svg>
"""
    }

    String getSmall() {
        """\
<svg xmlns="http://www.w3.org/2000/svg" width="22" height="22">
    <defs>
        <clipPath id="top-half">
            <rect width="22" height="11" />
        </clipPath>
    </defs>
    <g transform="rotate(-45 11 11)">
        <circle r="11" cx="11" cy="11" fill="$bottom.color" />
        <circle r="11" cx="11" cy="11" fill="$top.color" clip-path="url(#top-half)" />
    </g>
    <g transform="scale(.22)">
        $body
    </g>
</svg>
"""
    }

}

def dir = new File('.')
def path = 'src/main/resources/assets/print'
if (! dir.canonicalPath.endsWith(path)) {
    dir = new File(dir, path)
}
if (! dir.exists()) {
    throw new IllegalArgumentException("I can't run from this location: ${new File('.').canonicalPath}")
}

def lg = new File(dir, 'large')
def sm = new File(dir, 'small')
Map<String, Icon> icons = [:]
def add = { Icon i ->
    icons[i.id] = i
}
// base icons
new File(dir, 'icons.txt').text.trim().readLines().findAll {
    ! it.trim().startsWith('#')
}.join('\n').split('\n\n')*.trim().each {
    def lines = it.readLines()
    def top = lines.first().trim().tokenize()
    def i = new SimpleIcon(id: top.first(), body: lines.tail().join('\n'))
    if (top.size() > 1) {
        i.color = top.get(1)
    }
    add i
}

// numeric icons...
def nadd = { Icon base, int it ->
    add new SimpleIcon(id: it, body: base.body.replace('${num}', it.toString()))
}
(0..9).each nadd.curry(icons.remove("0"))
(10..20).each nadd.curry(icons.remove("00"))

// dual-color hybrid icons
[
    ['w', 'u'],
    ['w', 'b'],
    ['u', 'b'],
    ['u', 'r'],
    ['b', 'r'],
    ['b', 'g'],
    ['r', 'g'],
    ['r', 'w'],
    ['g', 'w'],
    ['g', 'u'],
].each { ids ->
    add new HybridIcon(icons[ids.first()], icons[ids.last()])
}

// colorless hybrid icons
['w', 'u', 'b', 'r', 'g'].each {
    add new HybridIcon(icons['2'], icons[it])
}

def view = new File(dir, 'icons.html').newPrintWriter()
view.print("""\
<html>
<head>
<style>
html { text-align: center; }
body { max-width: 980px; margin: auto; }
div { float: left; margin-bottom: 20px; }
img { margin: 10px; }
</style>
</head>
<body>
""")
icons.values().each {
    print "generating '$it.id' icon..."
    new File(lg, it.filename).text = it.large
    new File(sm, it.filename).text = it.small
    view.print("""\
    <div>
        <img src="$sm.name/$it.filename" /><br />
        <img src="$lg.name/$it.filename" />
    </div>
""")
    println "done"
}

view.print("""\
</body>
</html>
""")
view.close()