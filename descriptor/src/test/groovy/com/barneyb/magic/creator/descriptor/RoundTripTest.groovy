package com.barneyb.magic.creator.descriptor
import com.barneyb.magic.creator.util.XmlTestUtils
import org.junit.Ignore
import org.junit.Test
/**
 *
 *
 * @author barneyb
 */
class RoundTripTest {

    // These tests are based on the correct functioning of the reader.

    @Test
    void testSet() {
        roundTrip("test-set.xml")
    }

    // todo: symbols can't be w/in reminders
    @Ignore("symbols can't be w/in reminders")
    @Test
    void allInOne() {
        roundTrip("all-in-one.xml")
    }

    protected void roundTrip(String path) {
        roundTrip(getClass().classLoader.getResource(path))
    }

    protected void roundTrip(URL d) {
        def sw = new StringWriter()
        def w = new XmlCardSetWriter(d, sw)
        w.formatOutput = false
        def r = new XmlCardSetReader(d)
        def cs = r.read()
        r.close()
        w.write(cs)
        w.close()
        XmlTestUtils.assertXml(d.text, sw.toString())

        // now self-trip it too
        def sw2 = new StringWriter()
        w = new XmlCardSetWriter(d, sw2)
        w.formatOutput = false
        r = new XmlCardSetReader(d, new StringReader(sw.toString()))
        cs = r.read()
        r.close()
        w.write(cs)
        w.close()
        XmlTestUtils.assertXml(sw.toString(), sw2.toString())
    }

}
