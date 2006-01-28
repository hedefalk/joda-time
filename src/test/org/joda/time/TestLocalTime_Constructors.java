/*
 *  Copyright 2001-2006 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

/**
 * This class is a Junit unit test for LocalTime.
 *
 * @author Stephen Colebourne
 */
public class TestLocalTime_Constructors extends TestCase {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final ISOChronology ISO_UTC = ISOChronology.getInstanceUTC();
    private static final JulianChronology JULIAN = JulianChronology.getInstance();
    private static final JulianChronology JULIAN_PARIS = JulianChronology.getInstance(PARIS);
    private static final JulianChronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final int OFFSET_LONDON = LONDON.getOffset(0L) / DateTimeConstants.MILLIS_PER_HOUR;
    private static final int OFFSET_PARIS = PARIS.getOffset(0L) / DateTimeConstants.MILLIS_PER_HOUR;

    private long TEST_TIME_NOW =
            10L * DateTimeConstants.MILLIS_PER_HOUR
            + 20L * DateTimeConstants.MILLIS_PER_MINUTE
            + 30L * DateTimeConstants.MILLIS_PER_SECOND
            + 40L;

    private long TEST_TIME1 =
        1L * DateTimeConstants.MILLIS_PER_HOUR
        + 2L * DateTimeConstants.MILLIS_PER_MINUTE
        + 3L * DateTimeConstants.MILLIS_PER_SECOND
        + 4L;

    private long TEST_TIME2 =
        1L * DateTimeConstants.MILLIS_PER_DAY
        + 5L * DateTimeConstants.MILLIS_PER_HOUR
        + 6L * DateTimeConstants.MILLIS_PER_MINUTE
        + 7L * DateTimeConstants.MILLIS_PER_SECOND
        + 8L;

    private DateTimeZone zone = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestLocalTime_Constructors.class);
    }

    public TestLocalTime_Constructors(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        zone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(LONDON);
        java.util.TimeZone.setDefault(LONDON.toTimeZone());
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(zone);
        java.util.TimeZone.setDefault(zone.toTimeZone());
        zone = null;
    }

    //-----------------------------------------------------------------------
    /**
     * Test constructor ()
     */
    public void testConstantMidnight() throws Throwable {
        LocalTime test = LocalTime.MIDNIGHT;
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(0, test.getHourOfDay());
        assertEquals(0, test.getMinuteOfHour());
        assertEquals(0, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testFactory_ForFields_Calendar() throws Exception {
        GregorianCalendar cal = new GregorianCalendar(1970, 1, 3, 4, 5, 6);
        cal.set(Calendar.MILLISECOND, 7);
        LocalTime expected = new LocalTime(4, 5, 6, 7);
        assertEquals(expected, LocalTime.forFields(cal));
        try {
            LocalTime.forFields((Calendar) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testFactory_ForFields_Date() throws Exception {
        GregorianCalendar cal = new GregorianCalendar(1970, 1, 3, 4, 5, 6);
        cal.set(Calendar.MILLISECOND, 7);
        LocalTime expected = new LocalTime(4, 5, 6, 7);
        assertEquals(expected, LocalTime.forFields(cal.getTime()));
        try {
            LocalTime.forFields((Date) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testFactoryMillisOfDay_long() throws Throwable {
        LocalTime test = LocalTime.fromMillisOfDay(TEST_TIME1);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testFactoryMillisOfDay_long_Chronology() throws Throwable {
        LocalTime test = LocalTime.fromMillisOfDay(TEST_TIME1, JULIAN);
        assertEquals(JULIAN_UTC, test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    public void testFactoryMillisOfDay_long_nullChronology() throws Throwable {
        LocalTime test = LocalTime.fromMillisOfDay(TEST_TIME1, null);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(1, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testFactory_nowDefaultZone() throws Throwable {
        LocalTime test = LocalTime.nowDefaultZone();
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(10 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testFactory_now_DateTimeZone() throws Throwable {
        DateTime dt = new DateTime(2005, 6, 8, 23, 59, 30, 40, LONDON);
        DateTimeUtils.setCurrentMillisFixed(dt.getMillis());
        // 23:59 in London is 00:59 the following day in Paris
        
        LocalTime test = LocalTime.now(LONDON);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(23, test.getHourOfDay());
        assertEquals(59, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
        
        test = LocalTime.now(PARIS);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(0, test.getHourOfDay());
        assertEquals(59, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    public void testFactory_now_nullDateTimeZone() throws Throwable {
        DateTime dt = new DateTime(2005, 6, 8, 23, 59, 30, 40, LONDON);
        DateTimeUtils.setCurrentMillisFixed(dt.getMillis());
        // 23:59 in London is 00:59 the following day in Paris
        
        LocalTime test = LocalTime.now((DateTimeZone) null);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(23, test.getHourOfDay());
        assertEquals(59, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testFactory_now_Chronology() throws Throwable {
        LocalTime test = LocalTime.now(JULIAN);
        assertEquals(JULIAN_UTC, test.getChronology());
        assertEquals(10 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    public void testFactory_now_nullChronology() throws Throwable {
        LocalTime test = LocalTime.now((Chronology) null);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(10 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testFactory_forInstantDefaultZone_long1() throws Throwable {
        LocalTime test = LocalTime.forInstantDefaultZone(TEST_TIME1);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(1 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    public void testFactory_forInstantDefaultZone_long2() throws Throwable {
        LocalTime test = LocalTime.forInstantDefaultZone(TEST_TIME2);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(5 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(6, test.getMinuteOfHour());
        assertEquals(7, test.getSecondOfMinute());
        assertEquals(8, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testFactory_forInstant_long_DateTimeZone() throws Throwable {
        LocalTime test = LocalTime.forInstant(TEST_TIME1, PARIS);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(1 + OFFSET_PARIS, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    public void testFactory_forInstant_long_nullDateTimeZone() throws Throwable {
        LocalTime test = LocalTime.forInstant(TEST_TIME1, (DateTimeZone) null);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(1 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testFactory_forInstant_long1_Chronology() throws Throwable {
        LocalTime test = LocalTime.forInstant(TEST_TIME1, JULIAN_PARIS);
        assertEquals(JULIAN_UTC, test.getChronology());
        assertEquals(1 + OFFSET_PARIS, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    public void testFactory_forInstant_long2_Chronology() throws Throwable {
        LocalTime test = LocalTime.forInstant(TEST_TIME2, JULIAN);
        assertEquals(JULIAN_UTC, test.getChronology());
        assertEquals(5 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(6, test.getMinuteOfHour());
        assertEquals(7, test.getSecondOfMinute());
        assertEquals(8, test.getMillisOfSecond());
    }

    public void testFactory_forInstant_long_nullChronology() throws Throwable {
        LocalTime test = LocalTime.forInstant(TEST_TIME1, (Chronology) null);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(1 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testFactory_forInstant_Object1() throws Throwable {
        Date date = new Date(TEST_TIME1);
        LocalTime test = LocalTime.forInstant(date);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(1 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    public void testFactory_forInstant_Object2() throws Throwable {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date(TEST_TIME1));
        LocalTime test = LocalTime.forInstant(cal);
        assertEquals(GJChronology.getInstanceUTC(), test.getChronology());
        assertEquals(1 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    public void testFactory_forInstant_nullObject() throws Throwable {
        LocalTime test = LocalTime.forInstant((Object) null);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(10 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

//    public void testFactory_forInstant_localTimeObject() throws Throwable {
//        LocalTime base = new LocalTime(10, 20, 30, 40, CopticChronology.getInstance(PARIS));
//        LocalTime test = LocalTime.forInstant(base);
//        assertEquals(CopticChronology.getInstanceUTC(), test.getChronology());
//        assertEquals(10, test.getHourOfDay());
//        assertEquals(20, test.getMinuteOfHour());
//        assertEquals(30, test.getSecondOfMinute());
//        assertEquals(40, test.getMillisOfSecond());
//    }

    //-----------------------------------------------------------------------
    public void testFactory_forInstant_Object1_Chronology() throws Throwable {
        Date date = new Date(TEST_TIME1);
        LocalTime test = LocalTime.forInstant(date, JULIAN);
        assertEquals(JULIAN_UTC, test.getChronology());
        assertEquals(1 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    public void testFactory_forInstant_Object2_Chronology() throws Throwable {
        LocalTime test = LocalTime.forInstant("T10:20");
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(0, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
        
        try {
            LocalTime.forInstant("T1020");
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testFactory_forInstant_nullObject_Chronology() throws Throwable {
        LocalTime test = LocalTime.forInstant((Object) null, JULIAN);
        assertEquals(JULIAN_UTC, test.getChronology());
        assertEquals(10 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    public void testFactory_forInstant_Object_nullChronology() throws Throwable {
        Date date = new Date(TEST_TIME1);
        LocalTime test = LocalTime.forInstant(date, (Chronology) null);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(1 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(2, test.getMinuteOfHour());
        assertEquals(3, test.getSecondOfMinute());
        assertEquals(4, test.getMillisOfSecond());
    }

    public void testFactory_forInstant_nullObject_nullChronology() throws Throwable {
        LocalTime test = LocalTime.forInstant((Object) null, (Chronology) null);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(10 + OFFSET_LONDON, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testConstructor_int_int() throws Throwable {
        LocalTime test = new LocalTime(10, 20);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(0, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
        try {
            new LocalTime(-1, 20);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(24, 20);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, -1);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 60);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testConstructor_int_int_int() throws Throwable {
        LocalTime test = new LocalTime(10, 20, 30);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
        try {
            new LocalTime(-1, 20, 30);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(24, 20, 30);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, -1, 30);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 60, 30);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 20, -1);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 20, 60);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testConstructor_int_int_int_int() throws Throwable {
        LocalTime test = new LocalTime(10, 20, 30, 40);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
        try {
            new LocalTime(-1, 20, 30, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(24, 20, 30, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, -1, 30, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 60, 30, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 20, -1, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 20, 60, 40);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 20, 30, -1);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 20, 30, 1000);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testConstructor_int_int_int_int_Chronology() throws Throwable {
        LocalTime test = new LocalTime(10, 20, 30, 40, JULIAN);
        assertEquals(JULIAN_UTC, test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
        try {
            new LocalTime(-1, 20, 30, 40, JULIAN);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(24, 20, 30, 40, JULIAN);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, -1, 30, 40, JULIAN);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 60, 30, 40, JULIAN);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 20, -1, 40, JULIAN);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 20, 60, 40, JULIAN);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 20, 30, -1, JULIAN);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new LocalTime(10, 20, 30, 1000, JULIAN);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testConstructor_int_int_int_int_nullChronology() throws Throwable {
        LocalTime test = new LocalTime(10, 20, 30, 40, null);
        assertEquals(ISO_UTC, test.getChronology());
        assertEquals(10, test.getHourOfDay());
        assertEquals(20, test.getMinuteOfHour());
        assertEquals(30, test.getSecondOfMinute());
        assertEquals(40, test.getMillisOfSecond());
    }

}
