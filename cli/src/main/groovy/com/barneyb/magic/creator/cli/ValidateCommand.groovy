package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.api.ValidationMessage
import com.barneyb.magic.creator.validate.CardSetValidator
import com.barneyb.magic.creator.validate.CardValidator
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import groovy.util.logging.Log

/**
 *
 *
 * @author barneyb
 */
@Parameters(commandDescription = "validate a cardset", separators = "=")
@Log
class ValidateCommand extends BaseDescriptorCommand implements Executable {

    @Parameter(names = ["-l", "--level"], description = "validation level: ERROR, WARNING, INFO")
    ValidationMessage.Level level = ValidationMessage.Level.WARNING

    void execute(MainCommand main, JCommander jc) {
        def cs = loadDescriptor()
        println("validating set '$cs.title' ($cs.key)")
        def filter = { ValidationMessage it ->
            it.level >= level
        }
        def ms = new CardSetValidator().validate(cs).findAll(filter)
        if (ms.size() > 0) {
            println "Set '$cs.title':"
            ms.each {
                println 4, "$it.propertyName: $it.message"
            }
        }
        def cv = new CardValidator()
        filterCards(cs).each { card ->
            try {
                def cms = cv.validate(card).findAll(filter)
                if (cms.size() > 0) {
                    println 2, "Card '$card.title'"
                    cms.each {
                        println 6, "$it.propertyName: $it.message"
                    }
                }
            } catch (Exception e) {
                if (main.debug) {
                    e.printStackTrace()
                }
                log.severe("Failed to validate card: $e")
            }
        }
    }

    void println(int indent, String s) {
        println ' ' * indent + s
    }
}
