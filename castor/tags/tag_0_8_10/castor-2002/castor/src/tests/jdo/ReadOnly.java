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


package jdo;


import java.io.IOException;
import java.util.Enumeration;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 * Tests that read only objects are not stored in the database.
 */
public class ReadOnly
    extends CWTestCase
{


    private JDOCategory    _category;


    static final String    NewValue = "new value";


    public ReadOnly( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC04", "Read only tests" );
        _category = (JDOCategory) category;
    }


    public void preExecute()
    {
        super.preExecute();
    }


    public void postExecute()
    {
        super.postExecute();
    }


    public boolean run( CWVerboseStream stream )
    {
        boolean result = true;
        Database db;

        try {
            OQLQuery      oql;
            TestObject    object;
            Enumeration   enum;

            // Open transaction in order to perform JDO operations
            db = _category.getDatabase( stream.verbose() );
            db.begin();
        
            // Determine if test object exists, if not create it.
            // If it exists, set the name to some predefined value
            // that this test will later override.
            oql = db.getOQLQuery( "SELECT object FROM jdo.TestObject object WHERE id = $1" );
            oql.bind( TestObject.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                object = (TestObject) enum.nextElement();
                object.setValue1( TestObject.DefaultValue1 );
                object.setValue2( TestObject.DefaultValue2 );
            } else {
                object = new TestObject();
                stream.writeVerbose( "Creating new object: " + object );
                db.create( object );
            } 
            db.commit();

            db.begin();
            oql.bind( TestObject.DefaultId );
            enum = oql.execute( Database.ReadOnly );
            object = (TestObject) enum.nextElement();
            stream.writeVerbose( "Retrieved object: " + object );
            object.setValue1( NewValue );
            stream.writeVerbose( "Modified object: " + object );
            db.commit();
            
            db.begin();
            oql.bind( TestObject.DefaultId );
            enum = oql.execute( Database.ReadOnly );
            object = (TestObject) enum.nextElement();
            stream.writeVerbose( "Retrieved object: " + object );
            if ( object.getValue1().equals( NewValue ) ) {
                result = false;
                stream.writeVerbose( "Error: modified object was stored" );
            } else
                stream.writeVerbose( "OK: object is read-only" );
            db.commit();

            db.close();
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }


}

