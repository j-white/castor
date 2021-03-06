/*
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
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id: XSLong.java 6317 2006-10-17 14:24:18Z wguttmn $
 */
package org.exolab.castor.builder.types;

import java.math.BigInteger;
import java.util.Enumeration;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The XML Schema unsigned long type.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision: 6317 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class XSUnsignedLong extends XSPatternBase {

    /** Maximum Unsigned Long (inclusive). */
    private BigInteger _maxInclusive = null;
    /** Maximum Unsigned Long (exclusive). */
    private BigInteger _maxExclusive = null;
    /** Minimum Unsigned Long (inclusive). */
    private BigInteger _minInclusive = null;
    /** Minimum Unsigned Long (exclusive). */
    private BigInteger _minExclusive = null;
    /** Total number of digits. */
    private int  _totalDigits = -1;

    /** The JType represented by this XSType. */
    private static final JType JTYPE = new JClass("java.math.BigInteger");

    /**
     * No-arg constructor.
     */
    public XSUnsignedLong() {
        super(XSType.UNSIGNED_LONG_TYPE);
        setMinInclusive(BigInteger.valueOf(0));
        setMaxInclusive(new BigInteger("18446744073709551615"));
    } //-- XSLong

    /**
     * Returns the Java code necessary to create a new instance of the JType
     * associated with this XSType.
     *
     * @return the Java code necessary to create a new instance of the JType
     *         associated with this XSType.
     */
    public String newInstanceCode() {
        return "new " + getJType().getName() + "(\"0\");";
    }

    /**
     * Returns the JType that this XSType represents.
     * @return the JType that this XSType represents.
     */
    public JType getJType() {
        return JTYPE;
    }

    /**
     * Returns the maximum exclusive value that this XSLong can hold.
     *
     * @return the maximum exclusive value that this XSLong can hold. If no
     *         maximum exclusive value has been set, Null will be returned
     * @see #getMaxInclusive
     */
    public BigInteger getMaxExclusive() {
        return _maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSLong can hold.
     *
     * @return the maximum inclusive value that this XSLong can hold. If no
     *         maximum inclusive value has been set, Null will be returned
     * @see #getMaxExclusive
     */
    public BigInteger getMaxInclusive() {
        return _maxInclusive;
    } //-- getMaxInclusive

    /**
     * Returns the minimum exclusive value that this XSLong can hold.
     *
     * @return the minimum exclusive value that this XSLong can hold. If no
     *         minimum exclusive value has been set, Null will be returned
     * @see #getMinInclusive()
     * @see #setMaxInclusive(long)
     */
    public BigInteger getMinExclusive() {
        return _minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSLong can hold.
     *
     * @return the minimum inclusive value that this XSLong can hold. If no
     *         minimum inclusive value has been set, Null will be returned
     * @see #getMinExclusive
     */
    public BigInteger getMinInclusive() {
        return _minInclusive;
    } //-- getMinInclusive

    /**
     * Returns the totalDigits facet value of this XSInteger.
     * @return the totalDigits facet value of this XSInteger.
     */
    public int getTotalDigits() {
        return _totalDigits;
    }

    /**
     * Returns true if a maximum (inclusive or exclusive) has been set.
     * @return true if a maximum (inclusive or exclusive) has been set.
     */
    public boolean hasMaximum() {
        return _maxInclusive != null || _maxExclusive != null;
    } //-- hasMaximum

    /**
     * Returns true if a minimum (inclusive or exclusive) has been set.
     * @return true if a minimum (inclusive or exclusive) has been set.
     */
    public boolean hasMinimum() {
        return _minInclusive != null || _minExclusive != null;
    } //-- hasMinimum

    /**
     * Sets the maximum exclusive value that this XSLong can hold.
     *
     * @param max
     *            the maximum exclusive value this XSLong can be
     * @see #setMaxInclusive(Long)
     */
    public void setMaxExclusive(final BigInteger max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSLong can hold.
     *
     * @param max
     *            the maximum inclusive value this XSLong can be
     * @see #setMaxExclusive(Long)
     */
    public void setMaxInclusive(final BigInteger max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } //-- setMaxInclusive

    /**
     * Sets the minimum exclusive value that this XSLong can hold.
     *
     * @param min
     *            the minimum exclusive value this XSLong can be
     * @see #setMinInclusive(Long)
     */
    public void setMinExclusive(final BigInteger min) {
        _minExclusive = min;
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSLong can hold.
     *
     * @param min
     *            the minimum inclusive value this XSLong can be
     * @see #setMinExclusive(long)
     */
    public void setMinInclusive(final BigInteger min) {
        _minInclusive = min;
        _minExclusive = null;
    } //-- setMinInclusive

    /**
     * Sets the totalDigits facet for this XSInteger.
     * @param totalDig the value of totalDigits (must be > 0)
     */
     public void setTotalDigits(final int totalDig) {
          if (totalDig <= 0) {
              throw new IllegalArgumentException(this.getName()
                      + ": the totalDigits facet must be positive");
          }
          _totalDigits = totalDig;
     }

    /**
     * Transfer facets from the provided simpleType to <code>this</code>.
     *
     * @param simpleType
     *            The SimpleType containing our facets.
     * @see org.exolab.castor.builder.types.XSType#getFacets
     */
    public void setFacets(final SimpleType simpleType) {
        Enumeration enumeration = getFacets(simpleType);
        while (enumeration.hasMoreElements()) {
            Facet facet = (Facet) enumeration.nextElement();
            String name = facet.getName();

            if (Facet.MAX_EXCLUSIVE.equals(name)) {
                setMaxExclusive(new BigInteger(facet.getValue()));
            } else if (Facet.MAX_INCLUSIVE.equals(name)) {
                setMaxInclusive(new BigInteger(facet.getValue()));
            } else if (Facet.MIN_EXCLUSIVE.equals(name)) {
                BigInteger min = new BigInteger(facet.getValue());
                if (min.compareTo(new BigInteger("-1")) == -1) {
                    throw new IllegalArgumentException("minExclusive must be > -1 for "
                            + this.getName());
                }
                setMinExclusive(min);
            } else if (Facet.MIN_INCLUSIVE.equals(name)) {
                BigInteger min = new BigInteger(facet.getValue());
                if (min.compareTo(new BigInteger("0")) == -1) {
                    throw new IllegalArgumentException("minInclusive must be > 0 for "
                            + this.getName());
                }
                setMinInclusive(min);
            } else if (Facet.PATTERN.equals(name)) {
                addPattern(facet.getValue());
            } else if (Facet.TOTALDIGITS.equals(name)) {
                setTotalDigits(facet.toInt());
            } else if (Facet.FRACTIONDIGITS.equals(name)) {
                if (facet.toInt() != 0) {
                    throw new IllegalArgumentException("fractionDigits must be 0 for "
                                                       + this.getName());
                }
            } else if (Facet.WHITESPACE.equals(name)) {
                // If this facet is set correctly, we don't need to do anything
                if (!facet.getValue().equals(Facet.WHITESPACE_COLLAPSE)) {
                    throw new IllegalArgumentException("Warning: The facet 'whitespace'"
                            + " can only be set to '"
                            + Facet.WHITESPACE_COLLAPSE + "' for '"
                            + this.getName() + "'.");
                }
            }
        } //setFacets
    } //-- readLongFacets

    /**
     * Returns the String necessary to convert an Object to
     * an instance of this XSType. This method is really only useful
     * for primitive types
     * @param variableName the name of the Object
     * @return the String necessary to convert an Object to an
     * instance of this XSType
     */
    public String createFromJavaObjectCode(final String variableName) {
        return "((java.math.BigInteger) " + variableName + ")";
    } //-- fromJavaObject

    /**
     * Creates the validation code for an instance of this XSType. The validation
     * code should if necessary create a newly configured TypeValidator, that
     * should then be added to a FieldValidator instance whose name is provided.
     *
     * @param fixedValue a fixed value to use if any
     * @param jsc the JSourceCode to fill in.
     * @param fieldValidatorInstanceName the name of the FieldValidator
     * that the configured TypeValidator should be added to.
     */
    public void validationCode(final JSourceCode jsc, final String fixedValue,
                               final String fieldValidatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.BigIntegerValidator typeValidator"
                + " = new org.exolab.castor.xml.validators.BigIntegerValidator();");

        if (_minExclusive != null) {
            jsc.add("java.math.BigInteger min = new java.math.BigInteger(\"");
            jsc.append(_minExclusive + "\");");
            jsc.add("typeValidator.setMinExclusive(min);");
        } else if (_minInclusive != null) {
            jsc.add("java.math.BigInteger min = new java.math.BigInteger(\"");
            jsc.append(_minInclusive + "\");");
            jsc.add("typeValidator.setMinInclusive(min);");
        }

        if (_maxExclusive != null) {
            jsc.add("java.math.BigInteger max = new java.math.BigInteger(\"");
            jsc.append(_maxExclusive + "\");");
            jsc.add("typeValidator.setMaxExclusive(max);");
        } else if (_maxInclusive != null) {
            jsc.add("java.math.BigInteger max = new java.math.BigInteger(\"");
            jsc.append(_maxInclusive + "\");");
            jsc.add("typeValidator.setMaxInclusive(max);");
        }

        //-- fixed values
        if (fixedValue != null) {
            //-- just make sure we have a valid value...
            new BigInteger(fixedValue);

            jsc.add("typeValidator.setFixed(new BigInteger(\"");
            jsc.append(fixedValue);
            jsc.append("\");");
        }

        // pattern facet
        codePatternFacet(jsc, "typeValidator");

        // -- totalDigits
        int totalDigits = getTotalDigits();
        if (totalDigits != -1) {
            jsc.add("typeValidator.setTotalDigits(");
            jsc.append(Integer.toString(totalDigits));
            jsc.append(");");
        }

        jsc.add(fieldValidatorInstanceName + ".setValidator(typeValidator);");
    }

} //-- XSLong
