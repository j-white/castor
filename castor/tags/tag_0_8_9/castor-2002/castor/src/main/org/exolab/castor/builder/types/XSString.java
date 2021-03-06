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
 * The XML Schema String type
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class XSString extends XSPatternBase {

    /**
     * The JType represented by this XSType
    **/
    private static final JType jType
        = new JClass("java.lang.String");

    private String value = null;

    /**
     * The max length facet
    **/
    private int maxLength = -1;

    /**
     * The min length facet
    **/
    private int minLength =  0;

    /**
     * Creates a new XSString
    **/
    public XSString() {
        super(XSType.STRING);
    } //-- XSString

    /**
     * Returns the String necessary to convert an Object to
     * an instance of this XSType. This method is really only useful
     * for primitive types
     * @param variableName the name of the Object
     * @return the String necessary to convert an Object to an
     * instance of this XSType
    **/
    public String createFromJavaObjectCode(String variableName) {
        return "(String)"+variableName;
    } //-- fromJavaObject

    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public JType getJType() {
        return this.jType;
    } //-- getJType

    /**
     * Returns the maximum length occurances of this type can be.
     * A negative value denotes no maximum length
     * @return the maximum length facet
    **/
    public int getMaxLength() {
        return maxLength;
    } //-- getMaxLength

    /**
     * Returns the minimum length occurances of this type can be.
     * @return the minimum length facet
    **/
    public int getMinLength() {
        return minLength;
    } //-- getMinLength

    /**
     * Returns true if a maximum length has been set
     * @return true if a maximum length has been set
    **/
    public boolean hasMaxLength() {
        return (maxLength >= 0);
    } //-- hasMaxLength

    /**
     * Returns true if a minimum length has been set
     * @return true if a minimum length has been set
    **/
    public boolean hasMinLength() {
        return (minLength > 0);
    } //-- hasMinLength

    /**
     * Sets the maximum length of this XSString. To remove the max length
     * facet, use a negative value.
     * @param maxLength the maximum length for occurances of this type
    **/
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    } //-- setMaxLength

    /**
     * Sets the minimum length of this XSString.
     * @param minLength the minimum length for occurances of this type
    **/
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    } //-- setMinLength

    public void setFacets(SimpleType simpleType) {}
    /**
     * Returns the String value of this XSString
     * @return thr String value of this XSString
    **/
    public String toString() {
        return value;
    }

} //-- XSString
