package com.barneyb.magic.creator.cli
import com.barneyb.magic.creator.api.ValidationMessage
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

}
