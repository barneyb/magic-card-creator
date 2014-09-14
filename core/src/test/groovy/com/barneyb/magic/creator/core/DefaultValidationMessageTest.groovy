package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.ValidationMessage
import org.junit.Test

/**
 *
 *
 * @author barneyb
 */
class DefaultValidationMessageTest {

    @Test
    void infoFactory() {
        def d = DefaultValidationMessage.info(5, 'oh, hai!')
        assert d.level == ValidationMessage.Level.INFO
        assert d.item == 5
        assert d.message == 'oh, hai!'
    }

    @Test
    void warningFactory() {
        def d = DefaultValidationMessage.warning(5, 'oh, hai!')
        assert d.level == ValidationMessage.Level.WARNING
        assert d.item == 5
        assert d.message == 'oh, hai!'
    }

    @Test
    void errorFactory() {
        def d = DefaultValidationMessage.error(5, 'oh, hai!')
        assert d.level == ValidationMessage.Level.ERROR
        assert d.item == 5
        assert d.message == 'oh, hai!'
    }

}
