package assets.print

class Icon {

    String id
    String color = '#c5bbb8'
    String body

    String getFilename() {
        id + ".svg"
    }

    String getLarge() {
        """\
<svg xmlns="http://www.w3.org/2000/svg" width="47" height="47">
    <circle r="22" cx="22" cy="25" fill="#000" opacity="0.75" />
    <circle r="22" cx="25" cy="22" fill="$color" />
    $body
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
def icons = new File(dir, 'icons.txt').text.trim().readLines().findAll {
    ! it.startsWith('#')
}.join('\n').split('\n\n')*.trim().collect {
    def lines = it.readLines()
    def top = lines.first().trim().tokenize()
    def i = new Icon(id: top.first(), body: lines.tail().join('\n'))
    if (top.size() > 1) {
        i.color = top.get(1)
    }
    i
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

def spew = { Icon it ->
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
def nspew = { Icon base, int it ->
    spew new Icon(id: it, body: base.body.replace('${num}', it.toString()))
}

icons.each {
    if (it.id == '0') {
        (0..9).each nspew.curry(it)
    } else if (it.id == '00') {
        (10..20).each nspew.curry(it)
    } else {
        spew(it)
    }
}

view.print("""\
</body>
</html>
""")
view.close()