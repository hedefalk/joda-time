/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;

/**
 * This class is a JUnit test for MutableDateTime.
 *
 * @author Stephen Colebourne
 */
public class TestMutableDateTime_Sets extends TestCase {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)

    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.getInstance("Europe/London");
    
    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    long y2003days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365 + 365;
    
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;
            
    // 2002-04-05
    private long TEST_TIME1 =
            (y2002days + 31L + 28L + 31L + 5L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 12L * DateTimeConstants.MILLIS_PER_HOUR
            + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    // 2003-05-06
    private long TEST_TIME2 =
            (y2003days + 31L + 28L + 31L + 30L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 14L * DateTimeConstants.MILLIS_PER_HOUR
            + 28L * DateTimeConstants.MILLIS_PER_MINUTE;
    
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestMutableDateTime_Sets.class);
    }

    public TestMutableDateTime_Sets(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testTest() {
        assertEquals("2002-06-09T00:00:00.000Z", new Instant(TEST_TIME_NOW).toString());
        assertEquals("2002-04-05T12:24:00.000Z", new Instant(TEST_TIME1).toString());
        assertEquals("2003-05-06T14:28:00.000Z", new Instant(TEST_TIME2).toString());
    }

    //-----------------------------------------------------------------------
    public void testSetMillis_long1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.setMillis(TEST_TIME2);
        assertEquals(TEST_TIME2, test.getMillis());
        assertEquals(ISOChronology.getInstance(), test.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testSetChronology_Chronology1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.setChronology(GregorianChronology.getInstance(PARIS));
        assertEquals(TEST_TIME1, test.getMillis());
        assertEquals(GregorianChronology.getInstance(PARIS), test.getChronology());
    }        

    public void testSetChronology_Chronology2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.setChronology(null);
        assertEquals(TEST_TIME1, test.getMillis());
        assertEquals(ISOChronology.getInstance(), test.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testSetZone_DateTimeZone1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.setZone(PARIS);
        assertEquals(TEST_TIME1, test.getMillis());
        assertEquals(ISOChronology.getInstance(PARIS), test.getChronology());
    }        

    public void testSetZone_DateTimeZone2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.setZone(null);
        assertEquals(TEST_TIME1, test.getMillis());
        assertEquals(ISOChronology.getInstance(), test.getChronology());
    }        

    //-----------------------------------------------------------------------
    public void testSetZoneRetainFields_DateTimeZone1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.setZoneRetainFields(PARIS);
        assertEquals(TEST_TIME1 - DateTimeConstants.MILLIS_PER_HOUR, test.getMillis());
        assertEquals(ISOChronology.getInstance(PARIS), test.getChronology());
    }        

    public void testSetZoneRetainFields_DateTimeZone2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.setZoneRetainFields(null);
        assertEquals(TEST_TIME1, test.getMillis());
        assertEquals(ISOChronology.getInstance(), test.getChronology());
    }        

    public void testSetZoneRetainFields_DateTimeZone3() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1, GregorianChronology.getInstance(PARIS));
        test.setZoneRetainFields(null);
        assertEquals(TEST_TIME1 + DateTimeConstants.MILLIS_PER_HOUR, test.getMillis());
        assertEquals(GregorianChronology.getInstance(), test.getChronology());
    }        

    public void testSetZoneRetainFields_DateTimeZone4() {
        Chronology chrono = new MockNullZoneChronology();
        MutableDateTime test = new MutableDateTime(TEST_TIME1, chrono);
        test.setZoneRetainFields(PARIS);
        assertEquals(TEST_TIME1 - DateTimeConstants.MILLIS_PER_HOUR, test.getMillis());
        assertSame(chrono, test.getChronology());
    }        

    //-----------------------------------------------------------------------
    public void testSetMillis_RI1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1, BuddhistChronology.getInstance());
        test.setMillis(new Instant(TEST_TIME2));
        assertEquals(TEST_TIME2, test.getMillis());
        assertEquals(BuddhistChronology.getInstance(), test.getChronology());
    }

    public void testSetMillis_RI2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1, BuddhistChronology.getInstance());
        test.setMillis(null);
        assertEquals(TEST_TIME_NOW, test.getMillis());
        assertEquals(BuddhistChronology.getInstance(), test.getChronology());
    }

    //-----------------------------------------------------------------------
    public void testSet_DateTimeField_int1() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.set(DateTimeFieldType.year(), 2010);
        assertEquals(2010, test.getYear());
    }

    public void testSet_DateTimeField_int2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        test.set(null, 2010); // has no effect
        assertEquals(TEST_TIME1, test.getMillis());
    }

    public void testSet_DateTimeField_int3() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        try {
            test.set(DateTimeFieldType.monthOfYear(), 13);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(TEST_TIME1, test.getMillis());
    }

    //-----------------------------------------------------------------------
    public void testSetDate_int_int_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 12, 24, 48, 501);
        test.setDate(2010, 12, 3);
        assertEquals(2010, test.getYear());
        assertEquals(12, test.getMonthOfYear());
        assertEquals(3, test.getDayOfMonth());
        assertEquals(12, test.getHourOfDay());
        assertEquals(24, test.getMinuteOfHour());
        assertEquals(48, test.getSecondOfMinute());
        assertEquals(501, test.getMillisOfSecond());
    }

    public void testSetDate_int_int_int2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        try {
            test.setDate(2010, 13, 3);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(TEST_TIME1, test.getMillis());
    }

    //-----------------------------------------------------------------------
    public void testSetDate_long1() {
        long setter = new DateTime(2010, 12, 3, 5, 7, 9, 501).getMillis();
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 12, 24, 48, 501);
        test.setDate(setter);
        assertEquals(2010, test.getYear());
        assertEquals(12, test.getMonthOfYear());
        assertEquals(3, test.getDayOfMonth());
        assertEquals(12, test.getHourOfDay());
        assertEquals(24, test.getMinuteOfHour());
        assertEquals(48, test.getSecondOfMinute());
        assertEquals(501, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testSetDate_RI1() {
        DateTime setter = new DateTime(2010, 12, 3, 5, 7, 9, 501);
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 12, 24, 48, 501);
        test.setDate(setter);
        assertEquals(2010, test.getYear());
        assertEquals(12, test.getMonthOfYear());
        assertEquals(3, test.getDayOfMonth());
        assertEquals(12, test.getHourOfDay());
        assertEquals(24, test.getMinuteOfHour());
        assertEquals(48, test.getSecondOfMinute());
        assertEquals(501, test.getMillisOfSecond());
    }

    public void testSetDate_RI2() {
        MutableDateTime test = new MutableDateTime(2010, 7, 8, 12, 24, 48, 501);
        test.setDate(null);  // sets to TEST_TIME_NOW
        assertEquals(2002, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
        assertEquals(12, test.getHourOfDay());
        assertEquals(24, test.getMinuteOfHour());
        assertEquals(48, test.getSecondOfMinute());
        assertEquals(501, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testSetTime_int_int_int_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 12, 24, 48, 501);
        test.setTime(5, 6, 7, 8);
        assertEquals(2002, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
        assertEquals(5, test.getHourOfDay());
        assertEquals(6, test.getMinuteOfHour());
        assertEquals(7, test.getSecondOfMinute());
        assertEquals(8, test.getMillisOfSecond());
    }

    public void testSetTime_int_int_int2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        try {
            test.setTime(60, 6, 7, 8);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals(TEST_TIME1, test.getMillis());
    }

    //-----------------------------------------------------------------------
    public void testSetTime_long1() {
        long setter = new DateTime(2010, 12, 3, 5, 7, 9, 11).getMillis();
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 12, 24, 48, 501);
        test.setTime(setter);
        assertEquals(2002, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
        assertEquals(5, test.getHourOfDay());
        assertEquals(7, test.getMinuteOfHour());
        assertEquals(9, test.getSecondOfMinute());
        assertEquals(11, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testSetTime_RI1() {
        DateTime setter = new DateTime(2010, 12, 3, 5, 7, 9, 11);
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 12, 24, 48, 501);
        test.setTime(setter);
        assertEquals(2002, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
        assertEquals(5, test.getHourOfDay());
        assertEquals(7, test.getMinuteOfHour());
        assertEquals(9, test.getSecondOfMinute());
        assertEquals(11, test.getMillisOfSecond());
    }

    public void testSetTime_RI2() {
        MutableDateTime test = new MutableDateTime(2010, 7, 8, 12, 24, 48, 501);
        test.setTime(null);  // sets to TEST_TIME_NOW, which has no time part
        assertEquals(2010, test.getYear());
        assertEquals(7, test.getMonthOfYear());
        assertEquals(8, test.getDayOfMonth());
        assertEquals(new DateTime(TEST_TIME_NOW).getHourOfDay(), test.getHourOfDay());
        assertEquals(new DateTime(TEST_TIME_NOW).getMinuteOfHour(), test.getMinuteOfHour());
        assertEquals(new DateTime(TEST_TIME_NOW).getSecondOfMinute(), test.getSecondOfMinute());
        assertEquals(new DateTime(TEST_TIME_NOW).getMillisOfSecond(), test.getMillisOfSecond());
    }

    public void testSetTime_Object3() {
        DateTime temp = new DateTime(2010, 12, 3, 5, 7, 9, 11);
        DateTime setter = new DateTime(temp.getMillis(), new MockNullZoneChronology());
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 12, 24, 48, 501);
        test.setTime(setter);
        assertEquals(2002, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
        assertEquals(5, test.getHourOfDay());
        assertEquals(7, test.getMinuteOfHour());
        assertEquals(9, test.getSecondOfMinute());
        assertEquals(11, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testSetDateTime_int_int_int_int_int_int_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 12, 24, 48, 501);
        test.setDateTime(2010, 12, 3, 5, 6, 7, 8);
        assertEquals(2010, test.getYear());
        assertEquals(12, test.getMonthOfYear());
        assertEquals(3, test.getDayOfMonth());
        assertEquals(5, test.getHourOfDay());
        assertEquals(6, test.getMinuteOfHour());
        assertEquals(7, test.getSecondOfMinute());
        assertEquals(8, test.getMillisOfSecond());
    }
    
    public void testSetDateTime_int_int_int_int_int_int_int2() {
        MutableDateTime test = new MutableDateTime(TEST_TIME1);
        try {
            test.setDateTime(2010, 13, 3, 5, 6, 7, 8);
            fail();
        } catch (IllegalArgumentException ex) {
        }
        assertEquals(TEST_TIME1, test.getMillis());
    }

    //-----------------------------------------------------------------------
    public void testSetYear_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setYear(2010);
        assertEquals("2010-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetMonthOfYear_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setMonthOfYear(12);
        assertEquals("2002-12-09T05:06:07.008Z", test.toString());
    }

    public void testSetMonthOfYear_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setMonthOfYear(13);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetDayOfMonth_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setDayOfMonth(17);
        assertEquals("2002-06-17T05:06:07.008+01:00", test.toString());
    }

    public void testSetDayOfMonth_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setDayOfMonth(31);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetDayOfYear_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setDayOfYear(3);
        assertEquals("2002-01-03T05:06:07.008Z", test.toString());
    }

    public void testSetDayOfYear_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setDayOfYear(366);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetWeekyear_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setWeekyear(2001);
        assertEquals("2001-06-10T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetWeekOfWeekyear_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setWeekOfWeekyear(2);
        assertEquals("2002-01-13T05:06:07.008Z", test.toString());
    }

    public void testSetWeekOfWeekyear_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setWeekOfWeekyear(53);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetDayOfWeek_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setDayOfWeek(5);
        assertEquals("2002-06-07T05:06:07.008+01:00", test.toString());
    }

    public void testSetDayOfWeek_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setDayOfWeek(8);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetHourOfDay_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setHourOfDay(13);
        assertEquals("2002-06-09T13:06:07.008+01:00", test.toString());
    }

    public void testSetHourOfDay_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setHourOfDay(24);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetMinuteOfHour_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setMinuteOfHour(13);
        assertEquals("2002-06-09T05:13:07.008+01:00", test.toString());
    }

    public void testSetMinuteOfHour_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setMinuteOfHour(60);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetMinuteOfDay_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setMinuteOfDay(13);
        assertEquals("2002-06-09T00:13:07.008+01:00", test.toString());
    }

    public void testSetMinuteOfDay_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setMinuteOfDay(24 * 60);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetSecondOfMinute_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setSecondOfMinute(13);
        assertEquals("2002-06-09T05:06:13.008+01:00", test.toString());
    }

    public void testSetSecondOfMinute_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setSecondOfMinute(60);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetSecondOfDay_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setSecondOfDay(13);
        assertEquals("2002-06-09T00:00:13.008+01:00", test.toString());
    }

    public void testSetSecondOfDay_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setSecondOfDay(24 * 60 * 60);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetMilliOfSecond_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setMillisOfSecond(13);
        assertEquals("2002-06-09T05:06:07.013+01:00", test.toString());
    }

    public void testSetMilliOfSecond_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setMillisOfSecond(1000);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

    //-----------------------------------------------------------------------
    public void testSetMilliOfDay_int1() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        test.setMillisOfDay(13);
        assertEquals("2002-06-09T00:00:00.013+01:00", test.toString());
    }

    public void testSetMilliOfDay_int2() {
        MutableDateTime test = new MutableDateTime(2002, 6, 9, 5, 6, 7, 8);
        try {
            test.setMillisOfDay(24 * 60 * 60 * 1000);
            fail();
        } catch (IllegalArgumentException ex) {}
        assertEquals("2002-06-09T05:06:07.008+01:00", test.toString());
    }

}
