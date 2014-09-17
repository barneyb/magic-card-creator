package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.ValidationMessage
import com.barneyb.magic.creator.api.Validator

/**
 *
 *
 * @author barneyb
 */
class BaseValidatorTest {

    protected <T> List<ValidationMessage<T>> check(Validator<T> validator, String pn, T item, Object... tests) {
        if (tests.size() == 1 && tests.first() instanceof List) {
            tests = tests.first()
        }
        def ms = validator.validate(item).findAll {
            pn == null || it.propertyName == pn
        }
        assert ms*.level == tests.findAll {
            it instanceof ValidationMessage.Level
        }
        int i = -1
        tests.each {
            if (it instanceof ValidationMessage.Level) {
                i += 1
            } else {
                assert ms[i].message.contains(it.toString())
            }
        }
        ms
    }

}
