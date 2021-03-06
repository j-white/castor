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


package harness;


import java.util.Vector;
import java.util.Enumeration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import org.exolab.jtf.CWTestCategory;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWBaseApplication;
import org.exolab.exceptions.CWClassConstructorException;


public class Category
{


    private String  _name;


    private String  _description;


    private String  _className;


    private Vector  _cases = new Vector();


    private Object  _object;


    public void setName( String name )
    {
        _name = name;
    }


    public String getName()
    {
        return _name;
    }


    public void setDescription( String description )
    {
        _description = description;
    }


    public String getDescription()
    {
        return _description;
    }


    public void setClassName( String className )
    {
        _className = className;
    }


    public String getClassName()
    {
        return _className;
    }


    public void addCase( Case tc )
    {
        _cases.addElement( tc );
    }


    public Enumeration getCase()
    {
        return _cases.elements();
    }


    public void setObject( Object object )
    {
        _object = object;
    }


    public Object getObject()
    {
        return _object;
    }


    public CWTestCategory createTestCategory()
        throws CWClassConstructorException
    {
        Class          catClass;
        Constructor    cnst;
        CWTestCategory category;
        CWTestCase     tc;

        try {
            catClass = getClass().getClassLoader().loadClass( _className );
            cnst = catClass.getConstructor( new Class[] { String.class, String.class, Object.class } );
            category = (CWTestCategory) cnst.newInstance( new Object[] { _name, _description, _object } );
            for ( int i = 0 ; i < _cases.size() ; ++i ) {
                tc = ( (Case) _cases.elementAt( i ) ).createTestCase( category );
                category.add( tc.name(), tc, true );
            }
            return category;
        } catch ( CWClassConstructorException except ) {
            throw except;
        } catch ( InvocationTargetException except ) {
            throw new CWClassConstructorException( (Exception) except.getTargetException() );
        } catch ( Exception except ) {
            throw new CWClassConstructorException( except );
        }
    }


}
