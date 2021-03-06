/*
 * Copyright 2005 Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */

package org.castor.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A debugging cache proxy.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class DebuggingCacheProxy implements Cache {
    //--------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(DebuggingCacheProxy.class);

    /** The wrapped cache. */
    private Cache _cache;
    
    //--------------------------------------------------------------------------

    /**
     * Construct a DebugCacheProxy for given cache.
     * 
     * @param cache The wrapped cache.
     */
    public DebuggingCacheProxy(final Cache cache) { _cache = cache; }

    //--------------------------------------------------------------------------
    // operations for life-cycle management of cache
    
    /**
     * @see org.castor.cache.Cache#initialize(java.util.Map)
     */
    public void initialize(final Map params) throws CacheAcquireException {
        _cache.initialize(params);
        LOG.debug(getType() + ".initialize() [" + getName() + "]");
    }
    
    /**
     * @see org.castor.cache.Cache#close()
     */
    public void close() {
        LOG.debug(getType() + ".close() [" + getName() + "]");
        _cache.close();
    }
    
    //--------------------------------------------------------------------------
    // getters/setters for cache configuration

    /**
     * @see org.castor.cache.Cache#getType()
     */
    public String getType() { return _cache.getType(); }

    /**
     * @see org.castor.cache.Cache#getName()
     */
    public String getName() { return _cache.getName(); }
    
    //--------------------------------------------------------------------------
    // additional operations of cache interface
    
    /**
     * @see org.castor.cache.Cache#expire(java.lang.Object)
     */
    public void expire(final Object key) {
        LOG.debug(getType() + ".expire(" + key + ") [" + getName() + "]");
        _cache.expire(key);
    }
    
    /**
     * @see org.castor.cache.Cache#expireAll()
     */
    public void expireAll() {
        LOG.debug(getType() + ".expireAll() [" + getName() + "]");
        _cache.expireAll();
    }

    //--------------------------------------------------------------------------
    // query operations of map interface

    /**
     * @see java.util.Map#size()
     */
    public int size() {
        LOG.debug(getType() + ".size() [" + getName() + "]");
        return _cache.size();
    }

    /**
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        LOG.debug(getType() + ".isEmpty() [" + getName() + "]");
        return _cache.isEmpty();
    }

    /**
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(final Object key) {
        LOG.debug(getType() + ".containsKey(" + key + ") [" + getName() + "]");
        return _cache.containsKey(key);
    }

    /**
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(final Object value) {
        LOG.debug(getType() + ".containsValue(" + value + ") [" + getName() + "]");
        return _cache.containsValue(value);
    }

    /**
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(final Object key) {
        LOG.debug(getType() + ".get(" + key + ") [" + getName() + "]");
        return _cache.get(key);
    }
    
    //--------------------------------------------------------------------------
    // modification operations of map interface
    
    /**
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(final Object key, final Object value) {
        LOG.debug(getType() + ".put(" + key + ", " + value + ") [" + getName() + "]");
        return _cache.put(key, value);
    }

    /**
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(final Object key) {
        LOG.debug(getType() + ".remove(" + key + ") [" + getName() + "]");
        return _cache.remove(key);
    }

    //--------------------------------------------------------------------------
    // bulk operations of map interface
    
    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(final Map map) {
        LOG.debug(getType() + ".putAll(" + map + ") [" + getName() + "]");
        _cache.putAll(map);
    }

    /**
     * @see java.util.Map#clear()
     */
    public void clear() {
        LOG.debug(getType() + ".clear() [" + getName() + "]");
        _cache.clear();
    }

    //--------------------------------------------------------------------------
    // view operations of map interface
    
    /**
     * @see java.util.Map#keySet()
     */
    public Set keySet() {
        LOG.debug(getType() + ".keySet() [" + getName() + "]");
        return _cache.keySet();
    }

    /**
     * @see java.util.Map#values()
     */
    public Collection values() {
        LOG.debug(getType() + ".values() [" + getName() + "]");
        return _cache.values();
    }

    /**
     * @see java.util.Map#entrySet()
     */
    public Set entrySet() {
        LOG.debug(getType() + ".entrySet() [" + getName() + "]");
        return _cache.entrySet();
    }

    //--------------------------------------------------------------------------
}
