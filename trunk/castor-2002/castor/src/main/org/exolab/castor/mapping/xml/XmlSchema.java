/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8 (20000324)</a>,
 * using an XML Schema.
 * $Id
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class XmlSchema implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _prefix;

    private java.lang.String _systemId;

    private java.lang.String _publicId;

    private java.lang.String _name;

    private java.lang.String _namespace;


      //----------------/
     //- Constructors -/
    //----------------/

    public XmlSchema() {
        super();
    } //-- org.exolab.castor.mapping.xml.XmlSchema()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public java.lang.String getName() {
        return this._name;
    } //-- java.lang.String getName() 

    /**
    **/
    public java.lang.String getNamespace() {
        return this._namespace;
    } //-- java.lang.String getNamespace() 

    /**
    **/
    public java.lang.String getPrefix() {
        return this._prefix;
    } //-- java.lang.String getPrefix() 

    /**
    **/
    public java.lang.String getPublicId() {
        return this._publicId;
    } //-- java.lang.String getPublicId() 

    /**
    **/
    public java.lang.String getSystemId() {
        return this._systemId;
    } //-- java.lang.String getSystemId() 

    /**
    **/
    public boolean isValid() {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
     * 
     * @param _name
    **/
    public void setName(java.lang.String _name) {
        this._name = _name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param _namespace
    **/
    public void setNamespace(java.lang.String _namespace) {
        this._namespace = _namespace;
    } //-- void setNamespace(java.lang.String) 

    /**
     * 
     * @param _prefix
    **/
    public void setPrefix(java.lang.String _prefix) {
        this._prefix = _prefix;
    } //-- void setPrefix(java.lang.String) 

    /**
     * 
     * @param _publicId
    **/
    public void setPublicId(java.lang.String _publicId) {
        this._publicId = _publicId;
    } //-- void setPublicId(java.lang.String) 

    /**
     * 
     * @param _systemId
    **/
    public void setSystemId(java.lang.String _systemId) {
        this._systemId = _systemId;
    } //-- void setSystemId(java.lang.String) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.XmlSchema unmarshal(java.io.Reader reader) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.XmlSchema) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.XmlSchema.class, reader);
    } //-- org.exolab.castor.mapping.xml.XmlSchema unmarshal(java.io.Reader) 

    /**
    **/
    public void validate() 
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator.validate(this, null);
    } //-- void validate() 

}
