package com.barneyb.magic.creator.textlayout;

import java.text.CharacterIterator;

/**
 * @author bboisvert
 */
public class ICUBreakIteratorAdapter extends java.text.BreakIterator {

    protected final com.ibm.icu.text.BreakIterator base;

    public ICUBreakIteratorAdapter(com.ibm.icu.text.BreakIterator base) {
        this.base = base;
    }

    @Override
    public int first() {
        return base.first();
    }

    @Override
    public int last() {
        return base.last();
    }

    @Override
    public int next(int i) {
        return base.next(i);
    }

    @Override
    public int next() {
        return base.next();
    }

    @Override
    public int previous() {
        return base.previous();
    }

    @Override
    public int following(int i) {
        return base.following(i);
    }

    @Override
    public int preceding(int i) {
        return base.preceding(i);
    }

    @Override
    public int current() {
        return base.current();
    }

    @Override
    public CharacterIterator getText() {
        return base.getText();
    }

    @Override
    public void setText(CharacterIterator characterIterator) {
        base.setText(characterIterator);
    }

    @Override
    public void setText(String s) {
        base.setText(s);
    }

    @Override
    public boolean isBoundary(int i) {
        return base.isBoundary(i);
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Object clone() {
        return new ICUBreakIteratorAdapter((com.ibm.icu.text.BreakIterator) base.clone());
    }

}
