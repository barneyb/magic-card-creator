package com.barneyb.magic.creator.descriptor

/**
 *
 * @author bboisvert
 */
enum CostType implements AssetKeyed {

    COLORLESS_X('x', ManaColor.COLORLESS),
    COLORLESS_0('0', ManaColor.COLORLESS),
    COLORLESS_1('1', ManaColor.COLORLESS),
    COLORLESS_2('2', ManaColor.COLORLESS),
    COLORLESS_3('3', ManaColor.COLORLESS),
    COLORLESS_4('4', ManaColor.COLORLESS),
    COLORLESS_5('5', ManaColor.COLORLESS),
    COLORLESS_6('6', ManaColor.COLORLESS),
    COLORLESS_7('7', ManaColor.COLORLESS),
    COLORLESS_8('8', ManaColor.COLORLESS),
    COLORLESS_9('9', ManaColor.COLORLESS),
    WHITE('w', ManaColor.WHITE),
    BLUE('u', ManaColor.BLUE),
    BLACK('b', ManaColor.BLACK),
    RED('r', ManaColor.RED),
    GREEN('g', ManaColor.GREEN),
    TAP('t', []),

    final String symbol
    final Collection<ManaColor> colors
    final String assetKey

    def CostType(String symbol, ManaColor color, String assetKey=symbol) {
        this(symbol, [color], assetKey)
    }

    def CostType(String symbol, Collection<ManaColor> colors, String assetKey=symbol) {
        this.symbol = symbol
        this.colors = colors
        this.assetKey = assetKey
    }

    static CostType fromSymbol(String key) {
        CostType.enumConstants.find {
            it.symbol == key
        }
    }

}