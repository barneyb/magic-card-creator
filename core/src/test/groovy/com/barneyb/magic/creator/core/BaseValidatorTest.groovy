package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.ValidationMessage
import com.barneyb.magic.creator.api.Validator

/**
 *
 *
 * @author barneyb
 */
class BaseValidatorTest {

    protected <T> List<ValidationMessage<T>> check(Validator<T> validator, String pn, T item, Object... expected) {
        if (expected.size() == 1 && expected.first() instanceof List) {
            expected = expected.first()
        }
        def ms = validator.validate(item).findAll {
            pn == null || it.propertyName == pn
        }
        assert ms*.level == expected.findAll {
            it instanceof ValidationMessage.Level
        }
        int i = -1
        expected.each {
            if (it instanceof ValidationMessage.Level) {
                i += 1
            } else {
                assert ms[i].message.contains(it.toString())
            }
        }
        ms
    }

}
