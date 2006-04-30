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
 * Copyright 1999 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.*;

import java.util.Vector;

/**
 * An XML Schema ElementDecl
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ElementDecl extends Particle {


    /**
     * Error message for a null argument
    **/
    private static String NULL_ARGUMENT
        = "A null argument was passed to the constructor of " +
           ElementDecl.class.getName();

    /**
     * Flag indicating whether or not this Element declaration is
     * abstract
    **/
    private boolean isAbstract = false;

    /**
     * The element name
    **/
    private String name = null;


    /**
     * A reference to a top-level element declaration
    **/
    String elementRef = null;


    /**
     * The Schema URI for this Element Declaration
    **/
    private String schemaName = null;

    /**
     * The Schema Abbreviation for the Schema name
    **/
    private String schemaAbbrev = null;

    /**
     * The XMLType for this element declaration
    **/
    private XMLType xmlType = null;

    /**
     * True if the xmlType is a type reference
     */
    private boolean hasTypeReference= false;

    /**
     * The parent schema that this element declaration belongs to
    **/
    private Schema schema = null;

    /**
     * Creates a new default element definition
     * @param schema, the XML Schema to which this element declartion
     * belongs
     * <BR />This element definition will not be valid until a name has
     * been set
    **/
    public ElementDecl(Schema schema) {
        this(schema, null);
    } //-- ElementDecl

    /**
     * Creates a new default element definition
     * @param schema, the XML Schema to which this Element Declartion
     * belongs
     * @param name the name of the Element being declared
    **/
    public ElementDecl(Schema schema, String name) {
        super(1,1);
        if (schema == null) {
            String err = NULL_ARGUMENT + "; 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }
        this.schema = schema;
        this.name = name;
    } //-- ElementDecl

    /**
     * Returns the name of this Element declaration. The name of the 
     * referenced element is returned if the 'ref' attribute was used.
     * @return the name of this element declaration
    **/
    public String getName() {
		return getName(true);
	}
    
    /**
     * Returns the name of this Element declaration
     * @param ingoreRef If True the name of the referenced 
     * element (if specified) is returned
     * @return the name of this element declaration
    **/
    public String getName(boolean ignoreRef) {
        if (isReference() && ignoreRef == false) {
            return elementRef;
        }
		else return name;
    } //-- getName

    /**
     * Returns the XMLType (ComplexType or SimpleType) of this ElementDecl.
     * @return the XMLType of this ElementDecl
    **/
    public XMLType getType() {

        if (isReference()) {
            ElementDecl element = getReference();
            if (element != null)
                return element.getType();
            return null;
        }

        if (xmlType == null) return null;
        else
            return xmlType.getType();
    } //-- getXMLType

    /**
     * Returns the ElementDecl that this element definition references.
     * This will return null if this element definition does not reference
     * a different element definition.
     * @return the ElementDecl that this element definition references
    **/
    public ElementDecl getReference() {
        if (elementRef != null)
            return schema.getElementDecl(elementRef);
        return null;
    } //-- getReference

    /**
     * Returns the XML Schema to which this element declaration belongs.
     * @return the XML Schema to which this element declaration belongs.
    **/
    public Schema getSchema() {
        return this.schema;
    } //-- getSchema

    /**
     * Returns the Schema Abbreviation for the Schema name
     * @return the Schema Abbreviation for the Schema name
    **/
    public String getSchemaAbbrev() {
        return this.schemaAbbrev;
    } //-- getSchemaAbbrev

    /**
     * Returns the Schema Name for this Element declaration.
     * @return the Schema Name for this Element declaration.
    **/
    public String getSchemaName() {
        return this.schemaName;
    } //-- getSchemaName

    /**
     * Returns true if this element definition is abstract
     * @return true if this element definition is abstract
    **/
    public boolean isAbstract() {
        return isAbstract;
    } //-- isAbstract

    /**
     * Returns true if this element definition simply references another
     * element Definition
     * @return true if this element definition is a reference
    **/
    public boolean isReference() {
        return (elementRef != null);
    } //-- isReference

    /**
     * Sets whether or not this element definition is abstract
     * @param isAbstract, a boolean when true indicates that this
     * element definition should be abstract
    **/
    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    } //-- isAbstract

    /**
     * Sets the name of the element that this Element definition defines
     * @param name the name of the defined element
    **/
    public void setName(String name) {
        this.name = name;
    } //-- setName

    /**
     * Sets the reference for this element definition
     * @param reference the Element definition that this definition references
    **/
    public void setReference(ElementDecl reference) {
        if (reference == null)
            this.elementRef = null;
        else
            this.elementRef = reference.getName();
    } //-- setReference

    /**
     * Sets the reference for this element definition
     * @param reference the name of the element definition that this
     * definition references
    **/
    public void setReference(String reference) {
        this.elementRef = reference;
    } //-- setReference

    /**
     * Sets the Schema Abbreviation for the Schema name
     * @param abbrev the Schema Abbreviation for the Schema name
     * @see #setSchemaName
    **/
    public void setSchemaAbbrev(String abbrev) {
        this.schemaAbbrev = abbrev;
    } //-- setSchemaAbbrev

    /**
     * Sets the Schema Name for this Element declaration.
     * @param uri, the Schema Name, which is a URI for this element
     * declaration
     * @see #setSchemaAbbrev
    **/
    public void setSchemaName(String uri) {
        this.schemaName = uri;
    } //-- setSchemaName


    /**
     * Sets the XMLType for this Element declaration.
     * @param type the XMLType for this element declaration.
     * <BR />
     * <B>Note:</B> This method is mutually exclusive with
     * #setTypeReference, if a reference has previously been
     * set it will be ignored.
    **/
    public void setType(XMLType type)
    {
        hasTypeReference= false;

        if ((type != null) && (type.isSimpleType())) {
            ((SimpleType)type).setParent(this);
        }
        this.xmlType = type;
    } //-- setType


    /**
     * Sets the type of this element to be a reference.
     */
    public void setTypeReference(String name)
    {
        hasTypeReference= true;
        TypeReference reference= new TypeReference();
        reference.setName(name);
        reference.setSchema(schema);
        setType(reference);
    }

    /**
     * Returns true if this element's type is a reference.
     */
    public boolean hasTypeReference()
    {
        return hasTypeReference;
    }

    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.ELEMENT;
    } //-- getStructureType

    /**
     * Checks the validity of this element definition
     * @exception ValidationException when this element definition
     * is invalid
    **/
    public void validate() throws ValidationException {

        //-- if this merely references another element definition
        //-- just check that we can resolve the reference
        if (elementRef != null) {
            if (schema.getElementDecl(elementRef) == null) {
                String err = "<element ref=\"" + elementRef + "\"> "+
                    "is not resolvable.";
                throw new ValidationException(err);
            }
        }
        else if (name == null)  {
            String err = "<element> is missing required 'name' or " +
                "'ref' attribute.";
            throw new ValidationException(err);
        }
    } //-- validate


} //-- Element