package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.icon.DefaultSymbolIconFactory
import com.barneyb.magic.creator.symbol.DefaultSymbolFactory
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
/**
 *
 *
 * @author barneyb
 */
@Parameters(commandDescription = "I generate symbol icons", separators = "=")
class SymbolIconCommand implements Executable {

    @Parameter(names = ['--size'], description = "The size of icon to generate")
    Integer size = 16

    @Parameter(names = ['--symbols'], description = "a comma-delimited list of symbols to generate")
    String symbols = 'W,U,B,R,G,X,T,Q,0,1,2,3,4,5,6,7,8,9'

    @Parameter(names = ["-o", "--output-dir"], description = "The directory to transcode into", required = true)
    File outputDir

    void execute(MainCommand main, JCommander jc) {
        def sFact = new DefaultSymbolFactory()
        def iFact = new DefaultSymbolIconFactory()
        def sheet = new ProofSheet()
        def trans = new PNGTranscoder()
        trans.addTranscodingHint(trans.KEY_HEIGHT, size as float)
        outputDir.mkdirs()
        symbols.tokenize(',').collect {
            sFact.getSymbol(it)
        }.each {
            println "generating $it.symbol..."

            def icon = iFact.getIcon(it)
            def fn = "${it.symbol}.png"
            sheet.addImage(fn)
            trans.transcode(
                new TranscoderInput(icon.document),
                new TranscoderOutput(new File(outputDir, fn).newOutputStream()))

            icon = iFact.getShadowedIcon(it)
            fn = "${it.symbol}_s.png"
            sheet.addImage(fn)
            trans.transcode(
                new TranscoderInput(icon.document),
                new TranscoderOutput(new File(outputDir, fn).newOutputStream()))

            icon = iFact.getBareIcon(it)
            fn = "${it.symbol}_b.png"
            sheet.addImage(fn)
            trans.transcode(
                new TranscoderInput(icon.document),
                new TranscoderOutput(new File(outputDir, fn).newOutputStream()))
        }
        sheet.render(new File(outputDir, "proof.html").newOutputStream())
    }

}
