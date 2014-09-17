package com.barneyb.magic.creator.api

/**
 *
 *
 * @author barneyb
 */
interface ThemeLoader extends Keyed {

    Theme load(URL descUrl)

}
