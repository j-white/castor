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


import java.io.PrintWriter;
import java.util.Vector;
import java.util.Enumeration;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.TypeConvertor;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.loader.MappingLoader;
import org.exolab.castor.mapping.loader.Types;
import org.exolab.castor.mapping.loader.FieldDescriptorImpl;
import org.exolab.castor.mapping.loader.TypeInfo;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.mapping.xml.Xml;

import org.exolab.castor.xml.util.XMLClassDescriptorImpl;
import org.exolab.castor.xml.util.XMLClassDescriptorAdapter;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;


/**
 * An XML implementation of mapping helper. Creates XML class
 * descriptors from the mapping file.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class XMLMappingLoader
    extends MappingLoader
{


    public XMLMappingLoader( ClassLoader loader, PrintWriter logWriter )
        throws MappingException
    {
        super( loader, logWriter );
    }


    protected void resolveRelations( ClassDescriptor clsDesc )
        throws MappingException
    {
        FieldDescriptor[] fields;

        fields = clsDesc.getFields();
        for ( int i = 0 ; i < fields.length ; ++i ) {
            ClassDescriptor   relDesc;

            relDesc = getDescriptor( fields[ i ].getFieldType() );
            if ( relDesc == NoDescriptor ) {
                // XXX Error message should come here
            } else if ( relDesc != null && relDesc instanceof XMLClassDescriptor &&
                        fields[ i ] instanceof XMLFieldDescriptorImpl ) {
		( (XMLFieldDescriptorImpl) fields[ i ] ).setClassDescriptor( (XMLClassDescriptor) relDesc );
                ( (XMLFieldDescriptorImpl) fields[ i ] ).setNodeType( NodeType.Element );
            }
        }
        if ( clsDesc instanceof XMLClassDescriptorImpl )
            ( (XMLClassDescriptorImpl) clsDesc ).sortDescriptors();
    }


    protected ClassDescriptor createDescriptor( ClassMapping clsMap )
        throws MappingException
    {
        ClassDescriptor clsDesc;
        String          xmlName;
        
        // See if we have a compiled descriptor.
        clsDesc = loadClassDescriptor( clsMap.getName() );
        if ( clsDesc != null && clsDesc instanceof XMLClassDescriptor )
            return clsDesc;

        // Use super class to create class descriptor. Field descriptors will be
        // generated only for supported fields, see createFieldDesc later on.
        clsDesc = super.createDescriptor( clsMap );
        if ( clsMap.getMapTo() == null || clsMap.getMapTo().getXml() == null )
            xmlName = MarshalHelper.toXMLName( clsDesc.getJavaClass().getName() );
        else
            xmlName = clsMap.getMapTo().getXml();
            
        return new XMLClassDescriptorAdapter( clsDesc, xmlName );
    }


    protected FieldDescriptor createFieldDesc( Class javaClass, FieldMapping fieldMap )
        throws MappingException
    {
        FieldDescriptor        fieldDesc;
        String                 xmlName  = null;
        NodeType               nodeType = null;
        String                 match    = null;
        XMLFieldDescriptorImpl xmlDesc;
        
        // Create an XML field descriptor
        
        fieldDesc = super.createFieldDesc( javaClass, fieldMap );
        Xml xml = fieldMap.getXml();
        
        if (xml != null) {
            //-- xml name
            xmlName = xml.getName();
          
            //-- node type
            if ( xml.getNode() != null )
                nodeType = NodeType.getNodeType( xml.getNode() );
            
            //-- matches
            match = xml.getMatch();
        }
        
        xmlDesc = new XMLFieldDescriptorImpl( fieldDesc, xmlName, nodeType );
        
        //-- matches
        if (match != null) xmlDesc.setMatches(match);
            
        return xmlDesc; 
    }


    protected TypeInfo getTypeInfo( Class fieldType, CollectionHandler colHandler, FieldMapping fieldMap )
        throws MappingException
    {
        fieldType = Types.typeFromPrimitive( fieldType );
        return new TypeInfo( fieldType, null, null,
                             fieldMap.getRequired(), null, colHandler );
    }


}



