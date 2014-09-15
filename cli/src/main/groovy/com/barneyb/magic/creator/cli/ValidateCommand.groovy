package com.barneyb.magic.creator.cli

import com.barneyb.magic.creator.api.Card
import com.barneyb.magic.creator.api.ValidationMessage
import com.barneyb.magic.creator.core.CardSetValidator
import com.barneyb.magic.creator.core.CardValidator
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

/**
 *
 *
 * @author barneyb
 */
@Parameters(commandNames = "validate", commandDescription = "validate one or more descriptors")
class ValidateCommand extends BaseDescriptorCommand {

    @Parameter(names = ["-l", "--level"], description = "validation level: error, warning, info")
    ValidationMessage.Level level = ValidationMessage.Level.WARNING

    void execute() {
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
        cs.cards.each { Card card ->
            def cms = cv.validate(card).findAll(filter)
            if (cms.size() > 0) {
                println 2, "Card '$card.title'"
                cms.each {
                    println 6, "$it.propertyName: $it.message"
                }
            }
        }
    }

    void println(int indent, String s) {
        println ' ' * indent + s
    }
}
