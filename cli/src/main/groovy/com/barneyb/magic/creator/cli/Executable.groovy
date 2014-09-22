package com.barneyb.magic.creator.cli

import com.beust.jcommander.JCommander

/**
 *
 *
 * @author barneyb
 */
interface Executable {

    void execute(MainCommand main, JCommander jc)

}
