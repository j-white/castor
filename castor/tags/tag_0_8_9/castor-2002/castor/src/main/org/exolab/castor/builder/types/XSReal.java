/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder.types;

import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.*;

/**
 * The XML Schema Real type
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class XSReal extends XSType {

    //- Constraints for integer type
    Double maxInclusive = null;
    Double maxExclusive = null;
    Double minInclusive = null;
    Double minExclusive = null;

    /**
     * The JType represented by this XSType
    **/
    private static final JType jType = JType.Double;


    public XSReal() {
        super(XSType.DOUBLE);
    } //-- XSReal


    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public JType getJType() {
        return this.jType;
    }

    /**
     * Returns the maximum exclusive value that this XSReal can hold.
     * @return the maximum exclusive value that this XSReal can hold. If
     * no maximum exclusive value has been set, Null will be returned
     * @see getMaxInclusive
    **/
    public Double getMaxExclusive() {
        return maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSReal can hold.
     * @return the maximum inclusive value that this XSReal can hold. If
     * no maximum inclusive value has been set, Null will be returned
     * @see getMaxExclusive
    **/
    public Double getMaxInclusive() {
        return maxInclusive;
    } //-- getMaxInclusive


    /**
     * Returns the minimum exclusive value that this XSReal can hold.
     * @return the minimum exclusive value that this XSReal can hold. If
     * no minimum exclusive value has been set, Null will be returned
     * @see getMinInclusive
     * @see setMaxInclusive
    **/
    public Double getMinExclusive() {
        return minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSReal can hold.
     * @return the minimum inclusive value that this XSReal can hold. If
     * no minimum inclusive value has been set, Null will be returned
     * @see getMinExclusive
    **/
    public Double getMinInclusive() {
        return minInclusive;
    } //-- getMinInclusive

    public boolean hasMaximum() {
        return ((maxInclusive != null) || (maxExclusive != null));
    } //-- hasMaximum

    public boolean hasMinimum() {
        return ((minInclusive != null) || (minExclusive != null));
    } //-- hasMinimum


    //public String toString() {
    //    return value.toString();
    //}


    /**
     * Sets the maximum exclusive value that this XSReal can hold.
     * @param max the maximum exclusive value this XSReal can be
     * @see setMaxInclusive
    **/
    public void setMaxExclusive(double max) {
        maxExclusive = new Double(max);
    } //-- setMaxExclusive

    /**
     * Sets the maximum exclusive value that this XSReal can hold.
     * @param max the maximum exclusive value this XSReal can be
     * @see setMaxInclusive
    **/
    public void setMaxExclusive(Double max) {
        maxExclusive = max;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSReal can hold.
     * @param max the maximum inclusive value this XSReal can be
     * @see setMaxExclusive
    **/
    public void setMaxInclusive(double max) {
        maxInclusive = new Double(max);
    } //-- setMaxInclusive

    /**
     * Sets the maximum inclusive value that this XSReal can hold.
     * @param max the maximum inclusive value this XSReal can be
     * @see setMaxExclusive
    **/
    public void setMaxInclusive(Double max) {
        maxInclusive = max;
    } //-- setMaxInclusive


    /**
     * Sets the minimum exclusive value that this XSReal can hold.
     * @param max the minimum exclusive value this XSReal can be
     * @see setMinInclusive
    **/
    public void setMinExclusive(double min) {
        minExclusive = new Double(min);
    } //-- setMinExclusive

    /**
     * Sets the minimum exclusive value that this XSReal can hold.
     * @param max the minimum exclusive value this XSReal can be
     * @see setMinInclusive
    **/
    public void setMinExclusive(Double min) {
        minExclusive = min;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSReal can hold.
     * @param max the minimum inclusive value this XSReal can be
     * @see setMinExclusive
    **/
    public void setMinInclusive(double min) {
        minInclusive = new Double(min);
    } //-- setMinInclusive

    /**
     * Sets the minimum inclusive value that this XSReal can hold.
     * @param max the minimum inclusive value this XSReal can be
     * @see setMinExclusive
    **/
    public void setMinInclusive(Double min) {
        minInclusive = min;
    } //-- setMinInclusive
    public void setFacets(SimpleType simpleType) {}
    /**
     * Returns the String necessary to convert an instance of this XSType
     * to an Object. This method is really only useful for primitive types
     * @param variableName the name of the instance variable
     * @return the String necessary to convert an instance of this XSType
     * to an Object
    **/
    public String createToJavaObjectCode(String variableName) {
        StringBuffer sb = new StringBuffer("new Double(");
        sb.append(variableName);
        sb.append(")");
        return sb.toString();
    } //-- toJavaObject

    /**
     * Returns the String necessary to convert an Object to
     * an instance of this XSType. This method is really only useful
     * for primitive types
     * @param variableName the name of the Object
     * @return the String necessary to convert an Object to an
     * instance of this XSType
    **/
    public String createFromJavaObjectCode(String variableName) {
        StringBuffer sb = new StringBuffer("((Double)");
        sb.append(variableName);
        sb.append(").doubleValue()");
        return sb.toString();
    } //-- fromJavaObject

} //-- XSReal
