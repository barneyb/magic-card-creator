package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.ValidationMessage
import com.barneyb.magic.creator.core.CardSetValidator
import com.barneyb.magic.creator.core.CardValidator
import com.barneyb.magic.creator.descriptor.CardSetImporter
import com.beust.jcommander.JCommander
import com.beust.jcommander.MissingCommandException

/**
 *
 *
 * @author barneyb
 */
class CardCreator {

    static void main(String[] args) {
        def main = new MainCommand()
        def jc = new JCommander(main)
        jc.programName = CardCreator.simpleName

        def help = new HelpCommand()
        jc.addCommand("help", help)

        def validate = new ValidateCommand()
        jc.addCommand("validate", validate)

        def compose = new ComposeCommand()
        jc.addCommand("compose", compose)

        try {
            jc.parse(args)
        } catch (MissingCommandException mce) {
            println mce.message
        }
        switch (jc.parsedCommand) {
            case "validate":
                def cs = new CardSetImporter().fromFile(validate.descriptor)
                def filter = { ValidationMessage it ->
                    it.level >= validate.level
                }
                def ms = new CardSetValidator().validate(cs).findAll(filter)
                if (ms.size() > 0) {
                    println "Set '$cs.title':"
                    ms.each {
                        println "    $it.propertyName: $it.message"
                    }
                }
                def cv = new CardValidator()
                cs.cards.each { Card card ->
                    def cms = cv.validate(card).findAll(filter)
                    if (cms.size() > 0) {
                        println "  Card '$card.title'"
                        cms.each {
                            println "      $it.propertyName: $it.message"
                        }
                    }
                }
                break
            case "compose":
                // todo: implement composition
                break
            case null:
            case "help":
                jc.usage()
                break
        }
    }

}
