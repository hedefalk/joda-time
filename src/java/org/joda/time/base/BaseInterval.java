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
package org.joda.time.base;

import java.io.Serializable;

import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.MutableInterval;
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePeriod;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.IntervalConverter;
import org.joda.time.field.FieldUtils;

/**
 * BaseInterval is an abstract implementation of ReadableInterval that stores
 * data in two <code>long</code> millisecond fields.
 * <p>
 * This class should generally not be used directly by API users.
 * The {@link ReadableInterval} interface should be used when different 
 * kinds of interval objects are to be referenced.
 * <p>
 * BaseInterval subclasses may be mutable and not thread-safe.
 *
 * @author Brian S O'Neill
 * @author Sean Geoghegan
 * @author Stephen Colebourne
 * @since 1.0
 */
public abstract class BaseInterval
        extends AbstractInterval
        implements ReadableInterval, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 576586928732749278L;

    /** The chronology of the interval */
    private Chronology iChronology;
    /** The start of the interval */
    private long iStartMillis;
    /** The end of the interval */
    private long iEndMillis;

    /**
     * Constructs an interval from a start and end instant.
     * 
     * @param startInstant  start of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     * @param endInstant  end of this interval, as milliseconds from 1970-01-01T00:00:00Z.
     * @param chrono  the chronology to use, null is ISO default
     * @throws IllegalArgumentException if the end is before the start
     */
    protected BaseInterval(long startInstant, long endInstant, Chronology chrono) {
        super();
        iChronology = DateTimeUtils.getChronology(chrono);
        checkInterval(startInstant, endInstant);
        iStartMillis = startInstant;
        iEndMillis = endInstant;
    }

    /**
     * Constructs an interval from a start and end instant.
     * 
     * @param start  start of this interval, null means now
     * @param end  end of this interval, null means now
     * @throws IllegalArgumentException if the end is before the start
     */
    protected BaseInterval(ReadableInstant start, ReadableInstant end) {
        super();
        if (start == null && end == null) {
            iStartMillis = iEndMillis = DateTimeUtils.currentTimeMillis();
            iChronology = Chronology.getISO();
        } else {
            iChronology = DateTimeUtils.getInstantChronology(start);
            iStartMillis = DateTimeUtils.getInstantMillis(start);
            iEndMillis = DateTimeUtils.getInstantMillis(end);
            checkInterval(iStartMillis, iEndMillis);
        }
    }

    /**
     * Constructs an interval from a start instant and a duration.
     * 
     * @param start  start of this interval, null means now
     * @param duration  the duration of this interval, null means zero length
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    protected BaseInterval(ReadableInstant start, ReadableDuration duration) {
        super();
        iChronology = DateTimeUtils.getInstantChronology(start);
        iStartMillis = DateTimeUtils.getInstantMillis(start);
        long durationMillis = DateTimeUtils.getDurationMillis(duration);
        iEndMillis = FieldUtils.safeAdd(iStartMillis, durationMillis);
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a millisecond duration and an end instant.
     * 
     * @param duration  the duration of this interval, null means zero length
     * @param end  end of this interval, null means now
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    protected BaseInterval(ReadableDuration duration, ReadableInstant end) {
        super();
        iChronology = DateTimeUtils.getInstantChronology(end);
        iEndMillis = DateTimeUtils.getInstantMillis(end);
        long durationMillis = DateTimeUtils.getDurationMillis(duration);
        iStartMillis = FieldUtils.safeAdd(iEndMillis, -durationMillis);
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a start instant and a time period.
     * <p>
     * When forming the interval, the chronology from the instant is used
     * if present, otherwise the chronology of the period is used.
     * 
     * @param start  start of this interval, null means now
     * @param period  the period of this interval, null means zero length
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the end instant exceeds the capacity of a long
     */
    protected BaseInterval(ReadableInstant start, ReadablePeriod period) {
        super();
        Chronology chrono = DateTimeUtils.getInstantChronology(start);
        iChronology = chrono;
        iStartMillis = DateTimeUtils.getInstantMillis(start);
        if (period == null) {
            iEndMillis = iStartMillis;
        } else {
            iEndMillis = chrono.add(period, iStartMillis, 1);
        }
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs an interval from a time period and an end instant.
     * <p>
     * When forming the interval, the chronology from the instant is used
     * if present, otherwise the chronology of the period is used.
     * 
     * @param period  the period of this interval, null means zero length
     * @param end  end of this interval, null means now
     * @throws IllegalArgumentException if the end is before the start
     * @throws ArithmeticException if the start instant exceeds the capacity of a long
     */
    protected BaseInterval(ReadablePeriod period, ReadableInstant end) {
        super();
        Chronology chrono = DateTimeUtils.getInstantChronology(end);
        iChronology = chrono;
        iEndMillis = DateTimeUtils.getInstantMillis(end);
        if (period == null) {
            iStartMillis = iEndMillis;
        } else {
            iStartMillis = chrono.add(period, iEndMillis, -1);
        }
        checkInterval(iStartMillis, iEndMillis);
    }

    /**
     * Constructs a time interval converting or copying from another object
     * that describes an interval.
     * 
     * @param interval  the time interval to copy
     * @param chrono  the chronology to use, null means let converter decide
     * @throws IllegalArgumentException if the interval is invalid
     */
    protected BaseInterval(Object interval, Chronology chrono) {
        super();
        IntervalConverter converter = ConverterManager.getInstance().getIntervalConverter(interval);
        if (converter.isReadableInterval(interval, chrono)) {
            ReadableInterval input = (ReadableInterval) interval;
            iChronology = (chrono != null ? chrono : input.getChronology());
            iStartMillis = input.getStartMillis();
            iEndMillis = input.getEndMillis();
        } else if (this instanceof ReadWritableInterval) {
            converter.setInto((ReadWritableInterval) this, interval, chrono);
        } else {
            MutableInterval mi = new MutableInterval();
            converter.setInto(mi, interval, chrono);
            iChronology = mi.getChronology();
            iStartMillis = mi.getStartMillis();
            iEndMillis = mi.getEndMillis();
        }
        checkInterval(iStartMillis, iEndMillis);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the chronology of this interval.
     *
     * @return the chronology
     */
    public Chronology getChronology() {
        return iChronology;
    }

    /**
     * Gets the start of this time interval which is inclusive.
     *
     * @return the start of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    public long getStartMillis() {
        return iStartMillis;
    }

    /**
     * Gets the end of this time interval which is exclusive.
     *
     * @return the end of the time interval,
     *  millisecond instant from 1970-01-01T00:00:00Z
     */
    public long getEndMillis() {
        return iEndMillis;
    }

    //-----------------------------------------------------------------------
    /**
     * Sets this interval from two millisecond instants and a chronology.
     *
     * @param startInstant  the start of the time interval
     * @param endInstant  the start of the time interval
     * @param chrono  the chronology, not null
     * @throws IllegalArgumentException if the end is before the start
     */
    protected void setInterval(long startInstant, long endInstant, Chronology chrono) {
        checkInterval(startInstant, endInstant);
        iStartMillis = startInstant;
        iEndMillis = endInstant;
        iChronology = DateTimeUtils.getChronology(chrono);
    }

}
