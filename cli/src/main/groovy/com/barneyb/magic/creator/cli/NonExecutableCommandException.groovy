package com.barneyb.magic.creator.cli

/**
 *
 *
 * @author barneyb
 */
class NonExecutableCommandException extends RuntimeException {

    NonExecutableCommandException(Object cmd) {
        super("The ${cmd.getClass().simpleName} command is not Executable")
    }

}
