package com.barneyb.magic.creator.core

import org.junit.Before
import org.junit.Test

import static com.barneyb.magic.creator.api.ValidationMessage.Level.*
/**
 *
 *
 * @author barneyb
 */
class CardSetValidatorTest extends BaseValidatorTest {

    CardSetValidator validator

    @Before
    void _makeValidator() {
        validator = new CardSetValidator()
    }

    @Test
    void allInOne() {
        def ms = check validator, null, new DefaultCardSet(), WARNING, ERROR, WARNING, WARNING, ERROR
        assert ms*.propertyName == ['title', 'key', 'copyright', 'icon', 'cards']
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Test
    void title() {
        def check = this.&check.curry(validator, 'title')
        check new DefaultCardSet(), WARNING, "null"
        check new DefaultCardSet(title: ''), WARNING, "empty"
        check new DefaultCardSet(title: '   \t\t \n '), WARNING, "empty"
        check new DefaultCardSet(title: 'fred')
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Test
    void icon() {
        def check = this.&check.curry(validator, 'icon')
        check new DefaultCardSet([:]), WARNING
        check new DefaultCardSet([iconField: new DefaultIcon('b', '')]), WARNING, "No set", ERROR, "icon field"
        check new DefaultCardSet([iconSymbol: new DefaultIcon('b', '')]), INFO, "just a symbol"
    }

}
