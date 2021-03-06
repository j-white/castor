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


package org.exolab.castor.jdo.drivers;


import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.persist.spi.QueryExpression;
import org.exolab.castor.persist.spi.PersistenceFactory;


/**
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date$
 */
public class JDBCQueryExpression
    implements QueryExpression
{


    protected Hashtable _tables = new Hashtable();


    protected Vector    _cols = new Vector();


    protected Vector    _conds = new Vector();


    protected Vector    _joins = new Vector();

    protected String    _select;

    protected String    _where;


    protected String    _order;


    protected String    _limit;


    protected boolean   _distinct = false;


    protected PersistenceFactory  _factory;


    public JDBCQueryExpression( PersistenceFactory factory )
    {
        _factory = factory;
    }


    public void setDistinct(boolean distinct)
    {
        _distinct = distinct;
    }


    public void addColumn( String tableName, String columnName )
    {
        _tables.put( tableName, tableName );
        _cols.addElement( _factory.quoteName( tableName + JDBCSyntax.TableColumnSeparator + columnName ) );
    }

    public void addTable( String tableName )
    {
        _tables.put( tableName, tableName );
    }


    public void addParameter( String tableName, String columnName, String condOp )
    {
        addCondition( tableName, columnName, condOp, JDBCSyntax.Parameter );
    }


    public void addCondition( String tableName, String columnName,
                              String condOp, String value )
    {
        _tables.put( tableName, tableName );
        _conds.addElement( _factory.quoteName( tableName + JDBCSyntax.TableColumnSeparator + columnName ) +
                           condOp + value );
    }

    public String encodeColumn( String tableName, String columnName )
    {
        return _factory.quoteName( tableName + 
                   JDBCSyntax.TableColumnSeparator +
                   columnName );
    }


    public void addInnerJoin( String leftTable, String leftColumn,
                              String rightTable, String rightColumn )
    {
        _tables.put( leftTable, leftTable );
        _tables.put( rightTable, rightTable );
        _joins.addElement( new Join( leftTable, leftColumn, rightTable, rightColumn, false ) );
    }


    public void addInnerJoin( String leftTable, String[] leftColumn,
                              String rightTable, String[] rightColumn )
    {
        _tables.put( leftTable, leftTable );
        _tables.put( rightTable, rightTable );
        _joins.addElement( new Join( leftTable, leftColumn, rightTable, rightColumn, false ) );
    }


    public void addOuterJoin( String leftTable, String leftColumn,
                              String rightTable, String rightColumn )
    {
        _tables.put( leftTable, leftTable );
        _tables.put( rightTable, rightTable );
        _joins.addElement( new Join( leftTable, leftColumn, rightTable, rightColumn, true ) );
    }

    public void addOuterJoin( String leftTable, String[] leftColumn,
                              String rightTable, String[] rightColumn )
    {
        _tables.put( leftTable, leftTable );
        _tables.put( rightTable, rightTable );
        _joins.addElement( new Join( leftTable, leftColumn, rightTable, rightColumn, true ) );
    }


    public void addSelect( String selectClause )
    {
        _select = selectClause;
    }


    public void addWhereClause( String where )
    {
        _where = where;
    }


    public void addOrderClause( String order ) {
        _order = order;
    }


    public void addLimitClause( String limit ) {
        _limit = limit;
    }


    protected String getColumnList()
    {
        StringBuffer sql;

        if ( _cols.size() == 0 )
            return "1";

        sql = new StringBuffer();
        int i = 0;
        for ( i = 0 ; i < _cols.size() ; ++i ) {
            if ( i > 0 )
                sql.append( JDBCSyntax.ColumnSeparator );
            sql.append( (String) _cols.elementAt( i ) );
        }

        if ( _select != null )
          if ( i > 0 )
            sql.append( JDBCSyntax.ColumnSeparator ).append( _select );
          else
            sql.append( _select );

        return sql.toString();
    }


    protected boolean addWhereClause( StringBuffer sql, boolean first )
    {
        if ( _conds.size() > 0 ) {
            if ( first ) {
                sql.append( JDBCSyntax.Where );
                first = false;
            } else
                sql.append( JDBCSyntax.And );
            for ( int i = 0 ; i < _conds.size() ; ++i ) {
                if ( i > 0 )
                    sql.append( JDBCSyntax.And );
                sql.append( (String) _conds.elementAt( i ) );
            }
        }
        if ( _where != null ) {
            if ( first ) {
                sql.append( JDBCSyntax.Where );
                first = false;
            } else
                sql.append( JDBCSyntax.And );
            sql.append( '(' );
            sql.append( _where );
            sql.append( ')' );
        }
        return first;
    }


    public String getStatement( boolean lock )
    {
        StringBuffer sql;
        Enumeration  enum;
        boolean      first;
        Hashtable    tables;

        sql = new StringBuffer();
        sql.append( JDBCSyntax.Select );
        if ( _distinct )
          sql.append( JDBCSyntax.Distinct );
        
        if ( _select == null )
          sql.append( getColumnList() );
        else
          sql.append( _select ).append(" ");
          
        sql.append( JDBCSyntax.From );

        tables = (Hashtable) _tables.clone();
        // Use outer join syntax for all outer joins. Inner joins come later.
        first = true;
        for ( int i = 0 ; i < _joins.size() ; ++i ) {
            Join join;

            join = (Join) _joins.elementAt( i );
            if ( join.outer ) {
                if ( first )
                    first = false;
                else
                    sql.append( JDBCSyntax.TableSeparator );
                sql.append( "{oj " );
                sql.append( _factory.quoteName( join.leftTable ) ).append( JDBCSyntax.LeftJoin );
                sql.append( _factory.quoteName( join.rightTable ) ).append( JDBCSyntax.On );
                for ( int j = 0 ; j < join.leftColumns.length ; ++j ) {
                    if ( j > 0 )
                        sql.append( JDBCSyntax.And );
                    sql.append( _factory.quoteName( join.leftTable + JDBCSyntax.TableColumnSeparator +
                                                    join.leftColumns[ j ] ) ).append( OpEquals );
                    sql.append( _factory.quoteName( join.rightTable + JDBCSyntax.TableColumnSeparator +
                                                    join.rightColumns[ j ] ) );
                }
                sql.append( "}" );
                tables.remove( join.leftTable );
                tables.remove( join.rightTable );
            }
        }
        enum = tables.elements();
        while ( enum.hasMoreElements() ) {
            if ( first )
                first = false;
            else
                sql.append( JDBCSyntax.TableSeparator );
            sql.append( _factory.quoteName( (String) enum.nextElement() ) );
        }

        // Use standard join syntax for all inner joins.
        first = true;
        for ( int i = 0 ; i < _joins.size() ; ++i ) {
            Join join;
            
            join = (Join) _joins.elementAt( i );
            if ( ! join.outer ) {
                if ( first ) {
                    sql.append( JDBCSyntax.Where );
                    first = false;
                } else
                    sql.append( JDBCSyntax.And );
                for ( int j = 0 ; j < join.leftColumns.length ; ++j ) {
                    if ( j > 0 )
                        sql.append( JDBCSyntax.And );
                    sql.append( _factory.quoteName( join.leftTable + JDBCSyntax.TableColumnSeparator +
                                                    join.leftColumns[ j ] ) ).append( OpEquals );
                    sql.append( _factory.quoteName( join.rightTable + JDBCSyntax.TableColumnSeparator +
                                                    join.rightColumns[ j ] ) );
                }
            }
        } 
        first = addWhereClause( sql, first );

        if ( _order != null )
          sql.append(JDBCSyntax.OrderBy).append(_order);
          
        // There is no standard way to lock selected tables.
        return sql.toString();
    }


    public String toString()
    {
        return "<" + getStatement( false ) + ">";
    }


    public Object clone()
    {
        JDBCQueryExpression clone;

        try {
            clone = (JDBCQueryExpression) getClass().
                getConstructor( new Class[] { PersistenceFactory.class } ).
                newInstance( new Object[] { _factory } );
        } catch ( Exception except ) {
            // This should never happen
            throw new RuntimeException( except.toString() );
        }
        clone._tables = (Hashtable) _tables.clone();
        clone._conds = (Vector) _conds.clone();
        clone._cols = (Vector) _cols.clone();
        clone._joins = (Vector) _joins.clone();
        return clone;
    }


    static class Join
    {

        final String   leftTable;

        final String[] leftColumns;

        final String   rightTable;

        final String[] rightColumns;

        final boolean  outer;

        Join( String leftTable, String leftColumn, String rightTable, String rightColumn,
              boolean outer )
        {
            this.leftTable = leftTable;
            this.leftColumns = new String[] { leftColumn };
            this.rightTable = rightTable;
            this.rightColumns = new String[] { rightColumn };
            this.outer = outer;
        }

        Join( String leftTable, String[] leftColumns, String rightTable, String[] rightColumns,
              boolean outer )
        {
            this.leftTable = leftTable;
            this.leftColumns = (String[]) leftColumns.clone();
            this.rightTable = rightTable;
            this.rightColumns =(String[]) rightColumns.clone();
            this.outer = outer;
        }

    }


}


