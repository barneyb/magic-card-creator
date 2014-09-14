package com.barneyb.magic.creator.core

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

    protected ctx(HashMap args) {
        new BaseValidator.Ctx(new DefaultCardSet(args))
    }

    @Test
    void allInOne() {
        def ms = validator.validate(new DefaultCardSet()).sort {
            it.propertyName
        }
        assert ms.size() == 4
        assert ms*.level == [ERROR, WARNING, ERROR, WARNING]
        assert ms*.propertyName == ['cards', 'copyright', 'key', 'title']
    }

    @Test
    void nullTitle() {
        def ctx = ctx(
            title: null
        )
        validator.validateTitle(ctx)
        def ms = ctx.messages
        assert ms.size() == 1
        def m = ms.first()
        assert m.level == WARNING
        assert m.propertyName == 'title'
        assert m.message.contains('null')
    }

    @Test
    void emptyTitle() {
        def ctx = ctx(
            title: '   \t\t \n '
        )
        validator.validateTitle(ctx)
        def ms = ctx.messages
        assert ms.size() == 1
        def m = ms.first()
        assert m.level == WARNING
        assert m.propertyName == 'title'
        assert m.message.contains('empty')
    }

}
