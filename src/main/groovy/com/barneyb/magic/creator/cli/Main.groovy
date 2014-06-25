package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.descriptor.MarkdownDescriptor
/**
 *
 * @author bboisvert
 */
class Main {

    public static void main(String[] args) {
        if (args.length == 1 && args[0].toLowerCase() == "help") {
            Commands.HELP.action()
        }
        if (args.length < 2) {
            Commands.HELP.action "You must supply a command and a descriptor file as the first two arguments."
        }
        def command = args[0].toUpperCase()
        if (! Commands.enumConstants*.name().contains(command)) {
            Commands.HELP.action "The '$command' command is not recognized (${Commands.enumConstants*.name()*.toLowerCase()})."
        }
        def cmd = Commands.valueOf(command)
        def desc = new File(args[1])
        if (! desc.name.endsWith(".md")) {
            Commands.HELP.action "Descriptors must be Markdown files with an 'md' extension."
        }
        if (! desc.exists()) {
            Commands.HELP.action "The specified descriptor file doesn't exit."
        }
        cmd.execute(new MarkdownDescriptor(desc.toURI().toURL()).cardSet, args[2..<args.length])
    }

}
