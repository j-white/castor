/*
 * Copyright 2005 Bruce Snyder, Werner Guttmann, Ralf Joachim
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
 */
package org.exolab.castor.jdo.transactionmanager.spi;

/**
 * An IBM Websphere 5.1 specific factory for acquiring transactions from 
 * this J2EE container.
 *
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner.guttmann@gmx.net">Werner Guttmann</a>
 * @author <a href=" mailto:ralf.joachim@syscon-world.de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class WebSphere51TransactionManagerFactory
extends AbstractTransactionManagerFactory {
    //--------------------------------------------------------------------------

    /** Name of the IBM Websphere specific transaction manager factory class. */
    private static final String FACTORY_CLASS_NAME =
        "com.ibm.ws.Transaction.TransactionManagerFactory";

    /** Name of the method that is used upon the factory to have a TransactionManager
     *  instance created. */
    private static final String FACTORY_METHOD_NAME = "getTransactionManager";
    
    /** The name of the factory. */
    private static final String NAME = "websphere51";

    //--------------------------------------------------------------------------

    /**
     * @see org.exolab.castor.jdo.transactionmanager.spi.AbstractTransactionManagerFactory
     *      #getFactoryClassName()
     */
    public String getFactoryClassName() { return FACTORY_CLASS_NAME; }
    
    /**
     * @see org.exolab.castor.jdo.transactionmanager.spi.AbstractTransactionManagerFactory
     *      #getFactoryMethodName()
     */
    public String getFactoryMethodName() { return FACTORY_METHOD_NAME; }
    
    /**
     * @see org.exolab.castor.jdo.transactionmanager.TransactionManagerFactory#getName()
     */
    public String getName() { return NAME; }

    //--------------------------------------------------------------------------
}
