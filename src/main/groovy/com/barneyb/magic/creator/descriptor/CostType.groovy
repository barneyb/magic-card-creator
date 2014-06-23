package com.barneyb.magic.creator.descriptor

/**
 *
 * @author bboisvert
 */
enum CostType implements AssetKeyed {

    BLACK('b'),
    BLUE('u'),
    COLORLESS_0('0'),
    COLORLESS_1('1'),
    COLORLESS_2('2'),
    COLORLESS_3('3'),
    COLORLESS_4('4'),
    COLORLESS_5('5'),
    COLORLESS_6('6'),
    COLORLESS_7('7'),
    COLORLESS_8('8'),
    COLORLESS_9('9'),
    COLORLESS_X('x'),
    GREEN('g'),
    RED('r'),
    TAP('t'),
    WHITE('w'),

    final String assetKey

    def CostType(String assetKey) {
        this.assetKey = assetKey
    }

    static CostType fromKey(String key) {
        CostType.enumConstants.find {
            it.assetKey == key
        }
    }

}