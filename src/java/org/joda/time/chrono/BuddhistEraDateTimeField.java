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
package org.joda.time.chrono;

import java.util.Locale;

import org.joda.time.DateTimeField;
import org.joda.time.DurationField;
import org.joda.time.field.BaseDateTimeField;
import org.joda.time.field.UnsupportedDurationField;
import org.joda.time.field.FieldUtils;

/**
 * Provides time calculations for the buddhist era component of time.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
final class BuddhistEraDateTimeField extends BaseDateTimeField {
    
    /** Serialization version */
    private static final long serialVersionUID = -9175876774456816364L;

    /**
     * Singleton instance
     */
    static final DateTimeField INSTANCE = new BuddhistEraDateTimeField();

    /**
     * Restricted constructor
     */
    private BuddhistEraDateTimeField() {
        super("era");
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return INSTANCE;
    }

    public boolean isLenient() {
        return false;
    }

    /**
     * Get the Era component of the specified time instant.
     * 
     * @param millis  the time instant in millis to query.
     * @return the era extracted from the input.
     */
    public int get(long instant) {
        return BuddhistChronology.BE;
    }

    /**
     * Set the Era component of the specified time instant.
     * 
     * @param millis  the time instant in millis to update.
     * @param era  the era (BuddhistChronology.BE) to update the time to.
     * @return the updated time instant.
     * @throws IllegalArgumentException  if era is invalid.
     */
    public long set(long instant, int era) {
        FieldUtils.verifyValueBounds(this, era, getMinimumValue(), getMaximumValue());

        return instant;
    }

    /**
     * @see org.joda.time.DateTimeField#set(long, String, Locale)
     */
    public long set(long instant, String text, Locale locale) {
        if ("BE".equals(text) == false) {
            throw new IllegalArgumentException("Invalid era text: " + text);
        }
        return instant;
    }

    public long roundFloor(long instant) {
        return Long.MIN_VALUE;
    }

    public long roundCeiling(long instant) {
        return Long.MAX_VALUE;
    }

    public long roundHalfFloor(long instant) {
        return Long.MIN_VALUE;
    }

    public long roundHalfCeiling(long instant) {
        return Long.MIN_VALUE;
    }

    public long roundHalfEven(long instant) {
        return Long.MIN_VALUE;
    }

    public DurationField getDurationField() {
        return UnsupportedDurationField.getInstance("eras");
    }

    public DurationField getRangeDurationField() {
        return null;
    }

    public int getMinimumValue() {
        return BuddhistChronology.BE;
    }

    public int getMaximumValue() {
        return BuddhistChronology.BE;
    }

    protected String getAsText(int fieldValue, Locale locale) {
        return "BE";
    }

    public int getMaximumTextLength(Locale locale) {
        return 2;
    }

}
