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

package org.exolab.castor.xml;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A specialized FieldHandler for the XML Schema 
 * Date/Time related types
 * @author <a href="kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class DateFieldHandler implements FieldHandler {
        
    
    public static final String DATE_FORMAT =
        "yyyy-MM-dd'T'hh:mm:ss.SSS";
        
    private FieldHandler handler = null;
    
    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates a new DateFieldHandler using the given
     * FieldHandler for delegation.
     * @param
    **/
    public DateFieldHandler(FieldHandler fieldHandler) {
        
        if (fieldHandler == null) {
            String err = "The FieldHandler argument passed to " +
                "the constructor of DateFieldHandler must not be null.";
            throw new IllegalArgumentException(err);
        }
        this.handler = fieldHandler;
    } //-- DateFieldHandler
    
    
    //------------------/
    //- Public Methods -/
    //------------------/
    
    
    /**
     * Returns the value of the field associated with this
     * descriptor from the given target object.
     * @param target the object to get the value from
     * @return the value of the field associated with this
     * descriptor from the given target object.
    **/
    public Object getValue(Object target) 
        throws java.lang.IllegalStateException
    {
        
        Object val = handler.getValue(target);
        
        if (val == null) return val;
        
        Object formatted = null;
        
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        if (val.getClass().isArray()) {
            
            int size = Array.getLength(val);
            String[] values = new String[ size ];
            
            for (int i = 0; i < size; i++) {
                Object obj = Array.get(val, i);
                if (obj instanceof java.util.Date)
                    values[i] = df.format( (Date) obj );
                else
                    values[i] = obj.toString();
            }
            
            formatted = values;
        }
        else {
            if (val instanceof java.util.Date)
                formatted = df.format( (Date) val);
            else 
                formatted = val.toString();
        }
        return formatted;
    } //-- getValue

    /**
     * Sets the value of the field associated with this descriptor.
     * @param target the object in which to set the value
     * @param value the value of the field 
    **/
    public void setValue(Object target, Object value)
        throws java.lang.IllegalStateException
    {
        Date date = null;
        
        if (! (value instanceof Date) ) {
            
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            
            try {
                date = df.parse(value.toString());
            }
            catch (java.text.ParseException ex) {
                //-- ignore for now
                date = new Date();
            }
        }
        else date = (Date)value;
        
        handler.setValue(target, date);
        
    } //-- setValue
    
    /**
     * Checks the field validity. Returns successfully if the field
     * can be stored, is valid, etc, throws an exception otherwise.
     *
     * @param object The object
     * @throws ValidityException The field is invalid, is required and
     *  null, or any other validity violation
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler
     *  is not compatiable with the Java object
     */
    public void checkValidity( Object object )
        throws ValidityException, IllegalStateException 
    {
        //-- do nothing for now
    } //-- checkValidity


    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and
     *  cannot be instantiated
     */
    public Object newInstance( Object parent )
        throws IllegalStateException
    {
        return new Date();
    } //-- newInstance
    
    
} //-- DateFieldHandler



