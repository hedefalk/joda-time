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
package org.joda.time.format;

import java.io.IOException;
import java.io.Writer;

import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadablePeriod;

/**
 * Abstract base class for implementing {@link PeriodPrinter}s,
 * {@link PeriodParser}s, and {@link PeriodFormatter}s. This class
 * intentionally does not implement any of those interfaces. You can subclass
 * and implement only the interfaces that you need to.
 * <p>
 * The print methods assume that your subclass has implemented PeriodPrinter or
 * PeriodFormatter. If not, a ClassCastException is thrown when calling those
 * methods.
 * <p>
 * Likewise, the parse methods assume that your subclass has implemented
 * PeriodParser or PeriodFormatter. If not, a ClassCastException is thrown
 * when calling the parse methods.
 * 
 * @author Brian S O'Neill
 * @since 1.0
 */
public abstract class BasePeriodFormatter {
    
    /**
     * Returns the exact number of characters produced for the given period.
     * 
     * @param period  the period to use
     * @return the estimated length
     */
    protected int calculatePrintedLength(ReadablePeriod period) {
        throw new UnsupportedOperationException("Printing not supported");
    }

    /**
     * Returns the amount of fields from the given period that this printer
     * will print.
     * 
     * @param period  the period to use
     * @return amount of fields printed
     */
    protected int countFieldsToPrint(ReadablePeriod period) {
        return countFieldsToPrint(period, Integer.MAX_VALUE);
    }

    /**
     * Returns the amount of fields from the given period that this printer
     * will print.
     * 
     * @param period  the period to use
     * @param stopAt stop counting at this value
     * @return amount of fields printed
     */
    protected int countFieldsToPrint(ReadablePeriod period, int stopAt) {
        throw new UnsupportedOperationException("Printing not supported");
    }

    //-----------------------------------------------------------------------
    public void printTo(StringBuffer buf, ReadablePeriod period) {
        throw new UnsupportedOperationException("Printing not supported");
    }

    public void printTo(Writer out, ReadablePeriod period) throws IOException {
        throw new UnsupportedOperationException("Printing not supported");
    }

    public String print(ReadablePeriod period) {
        StringBuffer buf = new StringBuffer(calculatePrintedLength(period));
        printTo(buf, period);
        return buf.toString();
    }

    //-----------------------------------------------------------------------
    public int parseInto(ReadWritablePeriod period, String periodStr, int position) {
        throw new UnsupportedOperationException("Parsing not supported");
    }

    public Period parsePeriod(PeriodType type, String text) {
        return parseMutablePeriod(type, text).toPeriod();
    }

    public MutablePeriod parseMutablePeriod(PeriodType type, String text) {
        PeriodParser p = (PeriodParser) this;
        MutablePeriod period = new MutablePeriod(0, type);

        int newPos = p.parseInto(period, text, 0);
        if (newPos >= 0) {
            if (newPos >= text.length()) {
                return period;
            }
        } else {
            newPos = ~newPos;
        }

        throw new IllegalArgumentException(FormatUtils.createErrorMessage(text, newPos));
    }

}
