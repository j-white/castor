/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */


package org.exolab.adaptx.xpath.functions;


import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.StringResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.engine.Names;
import org.exolab.adaptx.xpath.engine.XMLUtils;


/**
 * An implementation of the XPath "normalize" function call
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class Normalize
    extends FunctionCallImpl {
    
    /**
     * Creates a new Normalize function call
    **/
    public Normalize()
    {
        super( Names.NORMALIZE_FN );
    } //-- Normalize
    
    /**
     * Evaluates the expression and returns the XPath result.
     *
     * @param context The XPathContext to use during evaluation.
     * @return The XPathResult (not null).
     * @exception XPathException if an error occured while 
     * evaluating this expression.
    **/
    public XPathResult evaluate( XPathContext context )
        throws XPathException
    {
        
        int paramCount = getParameterCount();
        if ( paramCount > 1 )
            throw new XPathException( INVALID_NUMBER_PARAMS + this );
            
        String value = null;
        if ( paramCount == 1 ) {
            value = getParameter( 0 ).evaluate( context ).stringValue();
        } 
        else {
            XPathNode node = context.getNode();
            value = node.getStringValue();
        }
        value = XMLUtils.stripSpace( value, true, true );
        return new StringResult( value );
    } //-- evaluate
    

} //-- Normalize

