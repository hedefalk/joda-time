/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
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
package org.joda.test.time.gj;

import java.util.Calendar;

import junit.framework.TestSuite;

import org.joda.test.time.AbstractTestDateTimeField;
import org.joda.time.DateTimeField;
import org.joda.time.chrono.gj.GJChronology;
/**
 * This class is a Junit unit test for the date time field.
 *
 * @author Stephen Colebourne
 */
public class TestGJYearOfEraDateTimeField extends AbstractTestDateTimeField {


    public TestGJYearOfEraDateTimeField(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    public static TestSuite suite() {
        return new TestSuite(TestGJYearOfEraDateTimeField.class);
    }
    protected void setUp() throws Exception {
        super.setUp();
    }
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    protected String getFieldName() {
        return "yearOfEra";
    }
    protected DateTimeField getField() {
        return GJChronology.getInstance(getZone()).yearOfEra();
    }
    protected int getMinimumValue() {
        return 1;
    }
    protected int getMaximumValue() {
        return 292272992;
    }
    protected int getCalendarValue(long millis) {
        iDate.setTime(millis);
        iCalendar.setTime(iDate);
        int val = iCalendar.get(Calendar.YEAR);
        return val;
    }
    protected long getAddedResult(long millis, int add) {
        iDate.setTime(millis);
        iCalendar.setTime(iDate);
        iCalendar.add(Calendar.YEAR, add);
        return iCalendar.getTime().getTime();
    }
    protected long getAddWrappedResult(long millis, int add) {
        iDate.setTime(millis);
        iCalendar.setTime(iDate);
        iCalendar.add(Calendar.YEAR, add);
        return iCalendar.getTime().getTime();
    }
    
    protected long getUnitSize() {
        return 365 * 24 * 60 * 60 * 1000; // 365 day
    }
    protected long getIncrementSize() {
        return 8 * 60 * 60 * 1000;  // 8 hours
    }
    protected long getTestRange() {
        return 32L * 24 * 60 * 60 * 1000;  // 32 days
    }
    
}
