package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.descriptor.XmlCardSetWriter
import com.barneyb.magic.creator.descriptor.markdown.MarkdownCardSetReader
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

/**
 *
 *
 * @author barneyb
 */
@Parameters(commandDescription = "convert a Markdown descriptor to an equivalent XML descriptor", separators = "=")
class MarkdownToXmlCommand implements Executable {

    @Parameter(names = ["-m", "--markdown", "--input"], description = "Markdown descriptor to convert", required = true)
    URL source

    @Parameter(names = ["-x", "--xml", "--output"], description = "file to save the XML descriptor in", required = true)
    File destination

    @Override
    void execute(MainCommand main, JCommander jc) {
        def md = new MarkdownCardSetReader(source)
        def cs = md.read()
        md.close()
        def xml = new XmlCardSetWriter(destination)
        xml.write(cs)
        xml.close()
    }
}
