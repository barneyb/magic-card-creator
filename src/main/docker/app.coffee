express = require("express")
exec = require('child_process').exec
PORT = 8080

app = express()
app.use express.bodyParser()

app.get "/", (req, res) ->
    res.send "try a POST to /convert/{format} (where format is 'pdf' or 'png') with an 'svg' file upload\n"

app.post "/convert/png", (req, res) ->
    fn = req.files.svg.path
    out = "#{fn}.png"
    cmd = "/usr/bin/rsvg-convert -f png --width #{req.query.w ? 400} -o #{out} #{fn}"
    exec cmd, (e, so, se) ->
        res.sendfile out

app.post "/convert/pdf", (req, res) ->
    fn = req.files.svg.path
    out = "#{fn}.pdf"
    cmd = "/usr/bin/rsvg-convert -f pdf -o #{out} #{fn}"
    exec cmd, (e, so, se) ->
        res.sendfile out

app.listen PORT
console.log "Running on http://localhost:" + PORT