package org.castor.spring.orm.tests;

import org.exolab.castor.service.ProductService;

/**
 * JUnit test case for Castor's Spring integration.
 * @author Werner Guttmann  
 */
public class TestProductServiceUsingDaoSupportWithDeclarativeTransactionDemarcationShortened 
    extends BaseSpringTestCaseWithTransactionDemarcation {

    protected void setUp() throws Exception {
        super.setUp();
        this.productService = (ProductService) this.context.getBean ("myProductServiceUsingDaoSupportDeclarativeShortened");
        assertNotNull (this.productService);
    }
    
}
