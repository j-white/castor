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

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.*;
import org.exolab.castor.util.List;

import java.util.Enumeration;

/**
 * A list for maintaining facets
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
class FacetList {

    /**
     * The list of facets
    **/
    private List facets    = null;
    
    /**
     * Creates a new FacetList
    **/
    FacetList() {
        facets = new List();
    } //-- FacetList

    /**
     * Adds the given Facet to this list
     * @param facet the facet to add
    **/
    void add(Facet facet) {
        if (facet != null) facets.add(facet);
    } //-- add
    
    /**
     * Returns the facet at the given index
     * @param index the index of the Facet to return
    **/
    Facet get(int index) {
        return (Facet)facets.get(index);
    } //-- get
    
    /**
     * Removes the given Facet from this list
     * @param facet the Facet to remove
    **/
    void remove(Facet facet) {
        facets.remove(facet);
    } //-- remove
    
    /**
     * Returns the number of Facets in this list
     * @return the number of Facets in this list
    **/
    int size() {
        return facets.size();
    } //-- size
    
    /**
     * Returns an Enumeration of the Facets in this list
     * @return an Enumeration of the Facets in this list
    **/
    Enumeration enumerate() {
        return new FacetListEnumerator(this);
    } //-- enumerate
    
} //-- FacetList

class FacetListEnumerator implements Enumeration {
    
    private int                 currentIdx    = 0;
    private FacetList           facets        = null;
    private FacetListEnumerator inherited     = null;
    private String              mask          = null;
    
    /**
     * Creates a new FacetList for the given FacetList
    **/
    FacetListEnumerator(FacetList facets) {
        this.facets = facets;
    } //-- FacetListEnumerator
    
    /**
     * Creates a new FacetList enumerator
    **/
    FacetListEnumerator
        (FacetList facets, FacetListEnumerator inheritedFacets) 
    {
        this.facets = facets;
        inherited = inheritedFacets;
        
    } //-- FacetListEnumerator
    
    /**
     * Sets the mask for this enumerator. The mask is the name of the facets
     * to enumerate.
     * @param name the name of the facets to enumerate
    **/
    void setMask(String name) {
        this.mask = name;
        if (inherited != null) inherited.setMask(name);
    } //-- setMask
    
    //-------------------------------------------/
    //- implementation of java.util.Enumeration -/
    //-------------------------------------------/
    
    public boolean hasMoreElements() {
        
        if (inherited != null) {
            if (inherited.hasMoreElements()) return true;
        }
        
        if (facets == null) return false;
        
        if (mask == null) return (currentIdx < facets.size());
        else {
            for (; currentIdx < facets.size(); currentIdx++) {
                Facet facet = (Facet)facets.get(currentIdx);
                if (mask.equals(facet.getName())) return true;
            }
        }
        return false;
    } //-- hasMoreElements
    
    public Object nextElement() {
        if (inherited != null) {
            if (inherited.hasMoreElements()) 
                return inherited.nextElement();
        }
        if (facets == null) return null;
        
        if (mask == null) {
            if (currentIdx < facets.size())
                return facets.get(currentIdx++);
        }
        else {
            for (; currentIdx < facets.size(); currentIdx++) {
                Facet facet = (Facet)facets.get(currentIdx);
                if (mask.equals(facet.getName())) {
                    ++currentIdx;
                    return facet;
                }
            }
        }
        return null;
    } //-- nextElement
    
} //-- FacetListEnumerator
