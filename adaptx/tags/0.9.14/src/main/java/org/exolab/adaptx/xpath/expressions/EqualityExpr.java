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


package org.exolab.adaptx.xpath.expressions;


/**
 * Represents an Equality expression
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$
 */
public interface EqualityExpr extends BinaryExpr {


    public static final short EQUAL            = 0;
    public static final short LESS_THAN        = 1;
    public static final short GREATER_THAN     = 2;
    public static final short LT_OR_EQUAL      = 3;
    public static final short GT_OR_EQUAL      = 4;
    public static final short NOT_EQUAL        = 5;
    
    
    
    /**
     * Returns the equality comparison type. 
     * 
     * @return the equality comparison type
     */
    public int getComparisonType();
    
} //-- EqualityExpr
