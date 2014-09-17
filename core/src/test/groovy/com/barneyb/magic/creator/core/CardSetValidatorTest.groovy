package com.barneyb.magic.creator.core

import com.barneyb.magic.creator.api.ValidationMessage
import org.junit.Before
import org.junit.Test

import static com.barneyb.magic.creator.api.ValidationMessage.Level.*
/**
 *
 *
 * @author barneyb
 */
class CardSetValidatorTest {

    CardSetValidator validator

    @Before
    void _makeValidator() {
        validator = new CardSetValidator()
    }

    protected check(String pn, HashMap args, Object... tests) {
        if (tests.size() == 1 && tests.first() instanceof List) {
            tests = tests.first()
        }
        def ms = validator.validate(new DefaultCardSet(args)).findAll {
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

    @Test
    void allInOne() {
        def ms = check null, [:], WARNING, ERROR, WARNING, WARNING, ERROR
        assert ms*.propertyName == ['title', 'key', 'copyright', 'icon', 'cards']
    }

    @Test
    void title2() {
        def check = { v, Object... tests ->
            check('title', [title: v], tests)
        }
        check null, WARNING, "null"
        check '', WARNING, "empty"
        check '   \t\t \n ', WARNING, "empty"
        check 'fred'
    }

    @Test
    void icon() {
        def check = this.&check.curry('icon')
        check([:], WARNING)
        check([iconField: new DefaultIcon('b', '')], WARNING, "No set", ERROR, "icon field")
        check([iconSymbol: new DefaultIcon('b', '')], INFO, "just a symbol")
    }

}
