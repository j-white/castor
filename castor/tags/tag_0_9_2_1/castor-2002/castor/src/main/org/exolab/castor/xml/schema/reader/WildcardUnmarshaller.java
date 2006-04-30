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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

 package org.exolab.castor.xml.schema.reader;

import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.xml.sax.*;

import java.util.StringTokenizer;
/**
 * A class for Unmarshalling WildCard
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
**/
public class WildcardUnmarshaller extends SaxUnmarshaller {


    /**
     * The value of the maximum occurance wild card
     */
    private static final String MAX_OCCURS_WILDCARD = "unbounded";


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The current SaxUnmarshaller
    **/
    private SaxUnmarshaller unmarshaller;

    /**
     * The current branch depth
    **/
    private int depth = 0;

    /**
     * The wildcard we are constructing
     */
     private Wildcard _wildcard = null;
    /**
     * The Schema being "unmarshalled"
    **/
    private Schema _schema = null;

    /**
     * The element name of the Group to be unmarshalled.
     */
    private String _element = SchemaNames.ANY;


      //----------------/
     //- Constructors -/
    //----------------/
    public WildcardUnmarshaller
        (ComplexType complexType, Schema schema, String element, AttributeList atts, Resolver resolver)
    {
        super();
        _wildcard = new Wildcard(complexType);
        init(schema, element, atts, resolver);
    }

    public WildcardUnmarshaller
        (Group group, Schema schema, String element, AttributeList atts, Resolver resolver)
    {
        super();
        _wildcard = new Wildcard(group);
        init(schema, element, atts, resolver);
    }

    /**
     * Creates a new WildcardUnmarshaller
     * @param schema the Schema to which the Wildcard belongs
     * @param element the name of the element
     * @param atts the AttributeList
     * @param resolver the resolver being used for reference resolving
    **/
    public void init
        (Schema schema, String element, AttributeList atts, Resolver resolver)
    {
        setResolver(resolver);
        this._schema = schema;
        this._element = element;

        //-- handle attributes
        String attValue = null;

        if (SchemaNames.ANY_ATTRIBUTE.equals(element))
           _wildcard.setAttributeWildcard();
        _element = element;

        //--namespace
        attValue = atts.getValue(SchemaNames.NAMESPACE);

        if (attValue != null) {
           // check if there is more than one namespace
           StringTokenizer tokenizer = new StringTokenizer(attValue);
           while (tokenizer.hasMoreTokens()) {
               //need to retrieve all the namespaces
               String temp = tokenizer.nextToken();
               //if there is more than one namespace ##any or ##other should not
               //appear
               /**@todo optimize the following?*/
               if (tokenizer.countTokens() >1 )
                   if ( (SchemaNames.NAMESPACE_ANY.equals(temp)) ||
                        (SchemaNames.NAMESPACE_OTHER.equals(temp)) )
                        throw new IllegalArgumentException(temp+" is not valid when multiple namespaces are listed.");

               /**
                *@todo validation on the value of the attribute
                * we need a way to check the validity of an URI.
                * A temporary solution if to assume that the URI are URL.
                * @see SchemaNames#isNamespaceName()
                */
                if (SchemaNames.isNamespaceName(temp))
                   _wildcard.addNamespace(temp);
                else {
                     String err = "Invalid 'namespace' value: "+temp;
                     throw new IllegalArgumentException(err);
                }
           }
         }//if
         else _wildcard.addNamespace(SchemaNames.ANY);

        /*
         * @maxOccurs
         * If maxOccurs is present, the value is either unbounded
         * or the int value of the attribute, otherwise maxOccurs
         * equals the minOccurs value.
         */
        attValue = atts.getValue(SchemaNames.MAX_OCCURS_ATTR);
        if (attValue != null) {
            if (MAX_OCCURS_WILDCARD.equals(attValue)) attValue = "-1";
            int maxOccurs = toInt(attValue);
            _wildcard.setMaxOccurs(maxOccurs);
        }
        //-- minOccurs
        attValue = atts.getValue("minOccurs");
        if (attValue != null)
            _wildcard.setMinOccurs(toInt(attValue));

        //-- processContents
        attValue = atts.getValue("processContents");

        if (attValue != null) {
           try {
               _wildcard.setProcessContents(attValue);
           } catch (SchemaException e) {
               throw new IllegalArgumentException(e.getMessage());
           }
        }


        //-- id
        _wildcard.setId(atts.getValue("id"));

    } //-- WildCardUnmarshaller

      //-----------/
     //- Methods -/
    //-----------/

     /**
     * Returns the name of the element that this SaxUnmarshaller
     * handles
     * @return the name of the element that this SaxUnmarshaller
     * handles
    **/
    public String elementName() {
        return _element;
    } //-- elementName


    /**
     * Returns the Wildcard unmarshalled by this Unmarshaller.
     * @return the unmarshalled Wildcard
     */
    public Wildcard getWildcard() {
        return _wildcard;
    }
    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return getWildcard();
    } //-- getObject

    /**
     * @param name
     * @param atts
     * @see org.xml.sax.DocumentHandler
    **/
    public void startElement(String name, AttributeList atts)
        throws org.xml.sax.SAXException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.startElement(name, atts);
            ++depth;
            return;
        }
         //-- <annotation>
        if (name == SchemaNames.ANNOTATION) {
            unmarshaller = new AnnotationUnmarshaller(atts);
        }

        else {
            StringBuffer err = new StringBuffer("illegal element <");
            err.append(name);
            err.append("> found in <");
            err.append(_element);
            err.append(">");
            throw new SAXException(err.toString());
        }

    } //-- startElement

    /**
     *
     * @param name
    **/
    public void endElement(String name)
        throws org.xml.sax.SAXException
    {

        //-- Do delagation if necessary
        if ((unmarshaller != null) && (depth > 0)) {
            unmarshaller.endElement(name);
            --depth;
            return;
        }

        //-- check for name mismatches
        if (unmarshaller != null) {
            if (!name.equals(unmarshaller.elementName())) {
                String err = "missing end element for ";
                err += unmarshaller.elementName();
                throw new SAXException(err);
            }
        }

        //-- have unmarshaller perform any necessary clean up
        unmarshaller.finish();

        //-- <annotation>
        if (name == SchemaNames.ANNOTATION) {
            _schema.addAnnotation((Annotation)unmarshaller.getObject());
        }
        unmarshaller = null;
    } //-- endElement

    public void characters(char[] ch, int start, int length)
        throws SAXException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.characters(ch, start, length);
        }
    } //-- characters

}