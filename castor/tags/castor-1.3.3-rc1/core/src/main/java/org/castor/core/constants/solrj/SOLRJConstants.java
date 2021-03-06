/*
 * Copyright 2011 Werner Guttmann
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
 */
package org.castor.core.constants.solrj;

/**
 * Defines SOLRJ specific constants.
 * 
 * @author Werner Guttmann
 * @since 1.3.3
 * 
 */
public interface SOLRJConstants {

//    /**
//     * File name suffix used for JDO-specific descriptor classes.
//     */
//    String JDO_DESCRIPTOR_SUFFIX = "SORLJDescriptor";

//    /**
//     * Package name of the sub-package where descriptors can be found.
//     */
//    String JDO_DESCRIPTOR_PACKAGE = "solrj_descriptors";

    /**
     * SOLRJ namespace (as used by the extensions for the XML code generator).
     */
    String NAMESPACE = "http://www.castor.org/binding/solrj";

    /**
     * Name of the field annotation element.
     */
    String ANNOTATIONS_FIELD_NAME = "field";

    /**
     * Name of the id annotation element.
     */
    String ANNOTATIONS_ID_NAME = "id";

    /**
     * Package where to find generated JDO classes to unmarshal annotations.
     */
    String GENERATED_ANNOTATION_CLASSES_PACKAGE = "org.exolab.castor.xml.schema.annotations.solrj";
    
//    /**
//     * Name of the JDP-specific CDR file.
//     */
//    String PKG_CDR_LIST_FILE = ".castor.solrj.cdr";
    
}
