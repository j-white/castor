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
 * Copyright 1999-2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.xml.sax.*;

/**
 * A class for Unmarshalling element definitions
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ElementUnmarshaller extends SaxUnmarshaller {

    /**
     * The value of the maximum occurance wild card
    **/
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
     * The element reference for the element definition we are "unmarshalling".
    **/
    private ElementDecl _element = null;


    private CharacterUnmarshaller charUnmarshaller = null;

    private Schema _schema = null;

    private boolean foundAnnotation         = false;
    private boolean foundIdentityConstraint = false;
    private boolean foundSimpleType         = false;
    private boolean foundComplexType        = false;

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new ElementUnmarshaller
     * @param schema the Schema to which the Element belongs
     * @param atts the AttributeList
     * @param resolver the resolver being used for reference resolving
    **/
    public ElementUnmarshaller
        (Schema schema, AttributeList atts, Resolver resolver)
    {
        super();
        setResolver(resolver);

        this._schema = schema;

        _element = new ElementDecl(schema);

        String attValue = null;

        //-- @ref
        attValue = atts.getValue("ref");
        if (attValue != null) {
            _element.setReference(attValue);
        }


        //-- @name
        _element.setName(atts.getValue("name"));

        //-- @default
        String temp = atts.getValue(SchemaNames.DEFAULT_ATTR);
        if (temp != null) {
            if (_element.getFixedValue() != null)
                throw new IllegalArgumentException("'default' and 'fixed' must not both be present.");
            _element.setFixedValue(temp);
        }

        //@fixed
        temp = atts.getValue(SchemaNames.FIXED_ATTR);
        if (temp != null) {
            if (_element.getDefaultValue() != null)
                throw new IllegalArgumentException("'default' and 'fixed' must not both be present.");
            _element.setDefaultValue(temp);
        }

        temp = null;
        //-- @type
        attValue = atts.getValue("type");
        if (attValue != null) _element.setTypeReference(attValue);

        //-- @nullable
        attValue = atts.getValue(SchemaNames.NILLABLE_ATTR);
        if (attValue != null) {
            if (attValue.equals("true")) _element.setNillable(true);
            else if (!attValue.equals("false")) {
                String err = "Invalid value for the 'nillable' attribute of "+
                    "an element definition: " + attValue;
                throw new IllegalArgumentException(err);
            }
        }

        /*
         * @minOccurs
         * if minOccurs is present the value is the int value of
         * of the attribute, otherwise minOccurs is 1.
         */
        attValue = atts.getValue(SchemaNames.MIN_OCCURS_ATTR);
        int minOccurs = 1;
        if (attValue != null) {
            minOccurs = toInt(attValue);
            _element.setMinOccurs(minOccurs);
        }

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
            _element.setMaxOccurs(maxOccurs);
        }
        else if (minOccurs > 0)
            _element.setMaxOccurs(minOccurs);
        else
            _element.setMaxOccurs(1);

        //-- @block
        _element.setBlock(atts.getValue(SchemaNames.BLOCK_ATTR));

        charUnmarshaller = new CharacterUnmarshaller();
    } //-- ElementUnmarshaller

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
        return SchemaNames.ELEMENT;
    } //-- elementName

    /**
     *
    **/
    public ElementDecl getElement() {
        return _element;
    } //-- getElement

    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return _element;
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

        if (SchemaNames.ANNOTATION.equals(name)) {
            if (foundSimpleType || foundIdentityConstraint ||foundComplexType)
                error("An annotation may only appear as the first child "+
                    "of an element definition.");


            if (foundAnnotation)
                error("Only one (1) 'annotation' is allowed as a child of "+
                    "element definitions.");

            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(atts);
        }
        else if (SchemaNames.COMPLEX_TYPE.equals(name)) {

            if (foundComplexType)
                error("Only one (1) 'complexType' may appear in an "+
                    "element definition.");
            if (foundSimpleType)
                error("Both 'simpleType' and 'complexType' cannot appear "+
                    "in the same element definition.");

            if (foundIdentityConstraint)
                error("A 'complexType' must appear before 'key', "+
                    "'keyref' and 'unique' elements.");

            foundComplexType = true;
            unmarshaller
                = new ComplexTypeUnmarshaller(_schema, atts, getResolver());
        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {

            if (foundSimpleType)
                error("Only one (1) 'simpleType' may appear in an "+
                    "element definition.");
            if (foundComplexType)
                error("Both 'simpleType' and 'complexType' cannot appear "+
                    "in the same element definition.");

            if (foundIdentityConstraint)
                error("A 'simpleType' must appear before 'key', "+
                    "'keyref' and 'unique' elements.");

            foundSimpleType = true;
            unmarshaller = new SimpleTypeUnmarshaller(_schema, atts);
        }
        else if (SchemaNames.KEY.equals(name)) {
            foundIdentityConstraint = true;
            error("Identity constraints are not yet supported");
        }
        else if (SchemaNames.KEYREF.equals(name)) {
            foundIdentityConstraint = true;
            error("Identity constraints are not yet supported");
        }
        else if (SchemaNames.UNIQUE.equals(name)) {
            foundIdentityConstraint = true;
            error("Identity constraints are not yet supported");
        }
        else illegalElement(name);

        unmarshaller.setResolver(getResolver());
        unmarshaller.setDocumentLocator(getDocumentLocator());

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
        if ((unmarshaller != null) && (charUnmarshaller != unmarshaller)) {
            if (!name.equals(unmarshaller.elementName())) {
                String err = "missing end element for ";
                err += unmarshaller.elementName();
                throw new SAXException(err);
            }
        }

        //-- call finish for any necessary cleanup
        unmarshaller.finish();

        if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation ann = (Annotation)unmarshaller.getObject();
            _element.addAnnotation(ann);
        }
        else if (SchemaNames.COMPLEX_TYPE.equals(name)) {

            XMLType xmlType
                = ((ComplexTypeUnmarshaller)unmarshaller).getComplexType();

            _element.setType(xmlType);

        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            XMLType xmlType
                = ((SimpleTypeUnmarshaller)unmarshaller).getSimpleType();
            _element.setType(xmlType);
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

} //-- ElementUnmarshaller
