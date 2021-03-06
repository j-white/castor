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
 * Copyright 1999-2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder;

import org.exolab.castor.builder.types.*;
import org.exolab.castor.builder.util.*;
import org.exolab.castor.mapping.*;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.util.*;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.xml.schema.SimpleTypesFactory;
import org.exolab.javasource.*;

import java.util.Enumeration;
import java.util.Vector;


/**
 * This class creates the Java Source classes for Schema
 * components
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SourceFactory  {

    private static final short BASE_TYPE_ENUMERATION   = 0;
    private static final short OBJECT_TYPE_ENUMERATION = 1;

    /**
     * The type factory.
    **/
    private FieldInfoFactory infoFactory = null;


    /**
     * The member factory.
    **/
    private MemberFactory memberFactory = null;


    private short enumerationType = OBJECT_TYPE_ENUMERATION;

	/**
	 * A flag indicating whether or not to generate XML marshalling
	 * framework specific methods.
	 */
	private boolean _createMarshalMethods = true;

    /**
	 * A flag indicating whether or not to implement CastorTestable
     * (used by the Castor Testing Framework)
	 */
	private boolean _testable = false;

    /**
     * Creates a new SourceFactory using the default FieldInfo factory.
    **/
    public SourceFactory() {
        this(null);
    } //-- SourceFactory


    /**
     * Creates a new SourceFactory with the given FieldInfoFactory
     * @param infoFactory the FieldInfoFactory to use
    */
    public SourceFactory(FieldInfoFactory infoFactory) {
        super();
        if (infoFactory == null)
            this.infoFactory = new FieldInfoFactory();
        else
            this.infoFactory = infoFactory;

        this.memberFactory = new MemberFactory(infoFactory);
    } //-- SourceFactory

   /**
     * Sets whether or not to create the XML marshalling framework specific
     * methods (marshall, unmarshall, validate) in the generated classes.
	 * By default, these methods are generated.
	 *
     * @param createMarshalMethods a boolean, when true indicates
     * to generated the marshalling framework methods
     *
    **/
    public void setCreateMarshalMethods(boolean createMarshalMethods) {
        _createMarshalMethods = createMarshalMethods;
    } //-- setCreateMarshalMethpds

    /**
     * Sets whether or not to create the XML marshalling framework specific
     * methods (marshall, unmarshall, validate) in the generated classes.
	 * By default, these methods are generated.
	 *
     * @param createMarshall a boolean, when true indicates
     * to generated the marshalling framework methods
     *
     */
    public void setTestable(boolean testable) {
           _testable = testable;
    }

    //------------------/
    //- Public Methods -/
    //------------------/

    /**
     * Creates a new ClassInfo for the given XML Schema element declaration
     * @param element the XML Schema element declaration to create the
     * ClassInfo for
     * @param resolver the ClassInfoResolver for resolving "derived" types.
     * @param packageName the package to use when generating source
     * from this ClassInfo
	 * @param createMarshall whether or not to create marshalling framework methods
    **/
    public JClass createSourceCode
        (ElementDecl element, ClassInfoResolver resolver, String packageName)
    {
        FactoryState state = null;

		String elementName = element.getName();
		String className = JavaNaming.toJavaClassName(elementName);

        className = resolveClassName(className, packageName);

        state = new FactoryState(className, resolver, packageName);
		//-- mark this element as being processed in this current
        //-- state to prevent the possibility of endless recursion
        ElementDecl tmpDecl = element;
        while (tmpDecl.isReference()) tmpDecl = tmpDecl.getReference();
        state.markAsProcessed(tmpDecl);

        ClassInfo classInfo = state.classInfo;
        JClass    jClass    = state.jClass;

        initialize(jClass);

        //-- set super class if necessary
        String base = SourceGenerator.getProperty(SourceGenerator.Property.SUPER_CLASS, null);
        if (base != null)
            jClass.setSuperClass(base);

        //-- name information
        classInfo.setNodeName(element.getName());

        //-- namespace information
        Schema  schema = element.getSchema();
        classInfo.setNamespaceURI(schema.getTargetNamespace());

        //-- process annotation
        String comment  = processAnnotations(element);
        if (comment != null)
            jClass.getJDocComment().setComment(comment);

        XMLType type = element.getType();

        boolean derived = false;

        // No Type?
        if (type == null) {
            // ???
            classInfo.setSchemaType(new XSClass(state.jClass));
        }
        // ComplexType
        else if (type.isComplexType()) {
            ComplexType complexType = (ComplexType)type;
            
            //-- If anonymous complex type, we need to process
            //-- the complex type
			if ( ! complexType.isTopLevel() ) {
				//process the complexType only if it has not been proceed
				 if (!state.processed(complexType))
					processComplexType( complexType, state);
				 derived = (state.jClass.getSuperClass() != null);
            }
            //-- top-level complex type...just extend it
            else {
                String typeName = complexType.getName();
                String superClass = JavaNaming.toJavaClassName(typeName);
                superClass = resolveClassName(superClass, packageName);
                jClass.setSuperClass(superClass);
                derived = true;
            }
        }
        // SimpleType
        else {
            SimpleType simpleType = (SimpleType)type;
            classInfo.setSchemaType(TypeConversion.convertType(simpleType));
            //-- handle our special case for enumerated types
            if (simpleType.hasFacet(Facet.ENUMERATION)) {
                createSourceCode(simpleType, state, state.packageName);
            }
        }

        //-- add imports required by the marshal methods
        jClass.addImport("java.io.Writer");
        jClass.addImport("java.io.Reader");

		if (_createMarshalMethods) {
           //-- #validate()
           createValidateMethods(jClass);
           //-- #marshal()
           createMarshalMethods(jClass);
           //-- #unmarshal()
           createUnmarshalMethods(jClass);
		}
        //create equals() method?
        if (SourceGenerator.equalsMethod())
            createEqualsMethod(jClass);
        //implements CastorTestable?
        if (_testable)
               SourceFactory.createTestableMethods(jClass);

		//-- This boolean is set to create bound properties
        //-- even if the user has set the SUPER CLASS property

        boolean realDerived = derived;
        if (base != null)
            realDerived = false;
		//-- create Bound Properties code
		if (state.hasBoundProperties() && (!realDerived))
		    createPropertyChangeMethods(jClass);

        if (resolver != null) {
            resolver.bindReference(jClass, classInfo);
            resolver.bindReference(element, classInfo);
        }

        return jClass;
    } //-- createSourceCode

    /**
     * Creates a new ClassInfo for the given XML Schema type declaration.
     * The type declaration must be a top-level declaration.
     * @param type the XML Schema type declaration to create the
     * ClassInfo for
     * @param resolver the ClassInfoResolver for resolving "derived" types.
     * @param packageName the package to which generated classes should belong
	 * @param createMarshall whether or not to create marshalling framework methods
    **/
    public JClass createSourceCode
        (ComplexType type, ClassInfoResolver resolver, String packageName)
    {
        if (type == null)
            throw new IllegalArgumentException("null ComplexType");

        if (!type.isTopLevel())
            throw new IllegalArgumentException("ComplexType is not top-level.");

        String className = JavaNaming.toJavaClassName(type.getName());
        className = resolveClassName(className, packageName);

        FactoryState state
            = new FactoryState(className, resolver, packageName);
		ClassInfo classInfo = state.classInfo;
        JClass    jClass    = state.jClass;

		state.markAsProcessed(type);

		initialize(jClass);
        //-- set super class if necessary
        String base = SourceGenerator.getProperty(SourceGenerator.Property.SUPER_CLASS, null);
        if (base != null)
            jClass.setSuperClass(base);

        //-- make class abstract?
		//-- when mapping elements to Java classes this class forms the
		//-- base for elements that reference this type.
		if (SourceGenerator.mappingSchemaElement2Java())
			jClass.getModifiers().setAbstract(true);

		//-- name information
        classInfo.setNodeName(type.getName());

        //-- namespace information
        Schema  schema = type.getSchema();
        classInfo.setNamespaceURI(schema.getTargetNamespace());

        //-- process annotation
        String comment  = processAnnotations(type);
        if (comment != null)
            jClass.getJDocComment().setComment(comment);


        processComplexType(type, state);

        //-- add imports required by the marshal methods
        jClass.addImport("java.io.Writer");
        jClass.addImport("java.io.Reader");

		if (_createMarshalMethods) {
            boolean isAbstract = jClass.getModifiers().isAbstract();
            //-- #validate()
            createValidateMethods(jClass);           
            
			//-- #marshal()
			createMarshalMethods(jClass, isAbstract);
			
			//-- #unmarshal()
			if (!isAbstract) {
			    createUnmarshalMethods(jClass);
			}
		}

		//-- create Bound Properties code
		if (state.hasBoundProperties())
		    createPropertyChangeMethods(jClass);

		if (resolver != null) {
            resolver.bindReference(jClass, classInfo);
            resolver.bindReference(type, classInfo);
        }

        return jClass;

    } //-- createSourceCode(ComplexType)

    /**
     * Creates the Java source code to support the given Simpletype
     * @param simpletype the Simpletype to create the Java source for
     * @return the JClass representation of the given Simpletype
    **/
    public JClass createSourceCode
        (SimpleType simpleType, ClassInfoResolver resolver, String packageName)
    {

        if ( SimpleTypesFactory.isBuiltInType( simpleType.getTypeCode() ) ) {
            String err = "You cannot construct a ClassInfo for a " +
                "built-in SimpleType.";
            throw new IllegalArgumentException(err);
        }

        boolean enumeration = false;

        //-- class name information
        String typeName = simpleType.getName();
        if (typeName == null) {
            Structure struct = simpleType.getParent();
            switch (struct.getStructureType()) {
                case Structure.ATTRIBUTE:
                    typeName = ((AttributeDecl)struct).getName();
                    break;
                case Structure.ELEMENT:
                    typeName = ((ElementDecl)struct).getName();
                    break;
            }
            typeName += "Type";
        }

        String className = JavaNaming.toJavaClassName(typeName);

        if (simpleType.hasFacet(Facet.ENUMERATION)) {
            enumeration = true;
            //-- XXXX Fix packageName...this is a hack I know,
            //-- XXXX we should change this
            if ((packageName != null) && (packageName.length() > 0))
                packageName = packageName + ".types";
            else
                packageName = "types";
        }

        className = resolveClassName(className, packageName);

        FactoryState state
            = new FactoryState(className, resolver, packageName);

        ClassInfo classInfo = state.classInfo;
        JClass    jClass    = state.jClass;

        initialize(jClass);

        //-- XML information
        Schema  schema = simpleType.getSchema();
        classInfo.setNamespaceURI(schema.getTargetNamespace());
        classInfo.setNodeName(typeName);

        //-- process annotation
        String comment  = processAnnotations(simpleType);
        if (comment != null)
            jClass.getJDocComment().setComment(comment);

        XSClass xsClass = new XSClass(jClass, typeName);

        classInfo.setSchemaType(xsClass);

        //-- handle enumerated types
        if (enumeration) {
            xsClass.setAsEnumertated(true);
            processEnumeration(simpleType, state);
        }

		//-- create Bound Properties code
		if (state.hasBoundProperties())
		    createPropertyChangeMethods(jClass);

        if (resolver != null) {
            resolver.bindReference(jClass, classInfo);
            resolver.bindReference(simpleType, classInfo);
        }

        return jClass;

    } //-- createSourceCode(SimpleType);

    /**
     * Creates a new ClassInfo for the given XML Schema type declaration.
     * The type declaration must be a top-level declaration.
     * @param type the XML Schema type declaration to create the
     * ClassInfo for
     * @param resolver the ClassInfoResolver for resolving "derived" types.
     * @param packageName the package to which generated classes should belong
	 * @param createMarshall whether or not to create marshalling framework methods
    **/
    public JClass createSourceCode
        (Group group, ClassInfoResolver resolver, String packageName)
    {

        String groupName = group.getName();
        if (groupName == null) {
            throw new IllegalArgumentException("Currently unnamed groups are not supported.");
            //groupName = "Group" + sgInfo.getNextGroupNumber();
        }

        String className = JavaNaming.toJavaClassName(groupName);
        className = resolveClassName(className, packageName);

        FactoryState state
            = new FactoryState(className, resolver, packageName);
		ClassInfo classInfo = state.classInfo;
        JClass    jClass    = state.jClass;

		state.markAsProcessed(group);

		initialize(jClass);
        //-- set super class if necessary
        String base = SourceGenerator.getProperty(SourceGenerator.Property.SUPER_CLASS, null);
        if (base != null)
            jClass.setSuperClass(base);

        //-- namespace information
        //Schema  schema = group.getSchema();
        //classInfo.setNamespaceURI(schema.getTargetNamespace());

        //-- process annotation
        String comment  = processAnnotations(group);
        if (comment != null)
            jClass.getJDocComment().setComment(comment);


        processContentModel(group, state);


		if (_createMarshalMethods) {
            //-- add imports required by the marshal methods
            jClass.addImport("java.io.Writer");
            jClass.addImport("java.io.Reader");
            
            boolean isAbstract = jClass.getModifiers().isAbstract();
            //-- #validate()
            createValidateMethods(jClass);
			//-- #marshal()
			createMarshalMethods(jClass, isAbstract);
			//-- #unmarshal()
			if (!isAbstract) {
			    createUnmarshalMethods(jClass);
			}
		}

		//-- create Bound Properties code
		if (state.hasBoundProperties())
		    createPropertyChangeMethods(jClass);

        resolver.bindReference(jClass, classInfo);
        resolver.bindReference(group, classInfo);

        return jClass;

    } //-- createSourceCode(Group)


    /**
     * Creates Source Code from the given ClassDescriptor
     * NOT YET IMPLEMENTED
    **
    public JClass createSourceCode(ClassDescriptor descriptor)
    {

        //-- handle null arguments
        if (descriptor == null) {
            String err = "ClassDescriptor passed as an argument to "+
                " SourceFactory#createSourceCode cannot be null.";
            throw new IllegalArgumentException(err);
        }

        ClassDescriptor classDesc = descriptor;
        /*
        if (descriptor instanceof XMLClassDescriptor)
            classDesc = (XMLClassDescriptor) descriptor;
        else {
            try {
                classDesc = new XMLClassDescriptorAdapter(descriptor, null);
            }
            catch(org.exolab.castor.mapping.MappingException mx) {
                throw new IllegalStateException(mx.toString());
            }
        }
        *

        Class type = classDesc.getJavaClass();
        JClass jClass = new JClass(type.getName());

        //-- Loop through fields and add members
        JMember field = null;
        FieldDescriptor[] fields = classDesc.getFields();
        for (int i = 0; i < fields.size(); i++) {
        }

        return jClass;

    } //-- createSourceCode

    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     * Initializes the given JClass
    **/
    private void initialize(JClass jClass) {


        jClass.addInterface("java.io.Serializable");

        //-- add default constructor
        JConstructor con = jClass.createConstructor();
        jClass.addConstructor(con);
        con.getSourceCode().add("super();");

        //-- add default import list
        if (_createMarshalMethods)
           jClass.addImport("org.exolab.castor.xml.*");
        jClass.addImport("java.io.Serializable");

    } //-- initialize

    /**
     * Creates the #marshal methods for the given JClass
     * @param parent the JClass to create the #marshal methods for
    **/
    private void createPropertyChangeMethods(JClass parent) {

		parent.addImport("java.beans.PropertyChangeEvent");
		parent.addImport("java.beans.PropertyChangeListener");

        //-- add vector to hold listeners
        String vName = "propertyChangeListeners";
        JField field = new JField(SGTypes.Vector, vName);
        field.getModifiers().makePrivate();
        parent.addField(field);


        JSourceCode jsc = parent.getConstructor(0).getSourceCode();
        jsc.add("propertyChangeListeners = new Vector();");

        //---------------------------------/
        //- notifyPropertyChangeListeners -/
        //---------------------------------/

        JMethod jMethod = new JMethod(null,"notifyPropertyChangeListeners");
        jMethod.getModifiers().makeProtected();

        String desc = "Notifies all registered "+
            "PropertyChangeListeners when a bound property's value "+
            "changes.";

        JDocComment jdc = jMethod.getJDocComment();
        JDocDescriptor jdDesc = null;

        jdc.appendComment(desc);

        jMethod.addParameter(new JParameter(SGTypes.String, "fieldName"));
        jdDesc = jdc.getParamDescriptor("fieldName");
        jdDesc.setDescription("the name of the property that has changed.");

        jMethod.addParameter(new JParameter(SGTypes.Object, "oldValue"));
        jdDesc = jdc.getParamDescriptor("oldValue");
        jdDesc.setDescription("the old value of the property.");

        jMethod.addParameter(new JParameter(SGTypes.Object, "newValue"));
        jdDesc = jdc.getParamDescriptor("newValue");
        jdDesc.setDescription("the new value of the property.");

        parent.addMethod(jMethod);
        jsc = jMethod.getSourceCode();
        jsc.add("java.beans.PropertyChangeEvent event = new ");
        jsc.append("java.beans.PropertyChangeEvent");
        jsc.append("(this, fieldName, oldValue, newValue);");
        jsc.add("");
        jsc.add("for (int i = 0; i < ");
        jsc.append(vName);
        jsc.append(".size(); i++) {");
        jsc.indent();
        jsc.add("((java.beans.PropertyChangeListener) ");
        jsc.append(vName);
        jsc.append(".elementAt(i)).");
        jsc.append("propertyChange(event);");
        jsc.unindent();
        jsc.add("}");

        //-----------------------------/
        //- addPropertyChangeListener -/
        //-----------------------------/

        JType jType = new JClass("java.beans.PropertyChangeListener");
        jMethod = new JMethod(null,"addPropertyChangeListener");

        desc = "Registers a PropertyChangeListener with this class.";

        jdc = jMethod.getJDocComment();
        jdc.appendComment(desc);

        jMethod.addParameter(new JParameter(jType, "pcl"));
        desc = "The PropertyChangeListener to register.";
        jdDesc = jdc.getParamDescriptor("pcl");
        jdDesc.setDescription(desc);

        parent.addMethod(jMethod);

        jsc = jMethod.getSourceCode();
        jsc.add(vName);
        jsc.append(".addElement(pcl);");

        //--------------------------------/
        //- removePropertyChangeListener -/
        //--------------------------------/

        jMethod = new JMethod(JType.Boolean,"removePropertyChangeListener");

        desc = "Removes the given PropertyChangeListener "+
            "from this classes list of ProperyChangeListeners.";

        jdc = jMethod.getJDocComment();
        jdc.appendComment(desc);

        jMethod.addParameter(new JParameter(jType, "pcl"));
        desc = "The PropertyChangeListener to remove.";
        jdDesc = jdc.getParamDescriptor("pcl");
        jdDesc.setDescription(desc);

        desc = "true if the given PropertyChangeListener was removed.";
        jdc.addDescriptor(JDocDescriptor.createReturnDesc(desc));

        parent.addMethod(jMethod);

        jsc = jMethod.getSourceCode();
        jsc.add("return ");
        jsc.append(vName);
        jsc.append(".removeElement(pcl);");

    } //-- createPropertyChangeMethods

    /**
     * Creates the #marshal methods for the given JClass
     * @param parent the JClass to create the #marshal methods for
    **/
    private void createMarshalMethods(JClass parent) {
        createMarshalMethods(parent, false);
    } //-- createMarshalMethods

    /**
     * Creates the #marshal methods for the given JClass
     * @param parent the JClass to create the #marshal methods for
    **/
    private void createMarshalMethods(JClass parent, boolean isAbstract) {

        //-- create main marshal method
        JMethod jMethod = new JMethod(null,"marshal");
        jMethod.addException(SGTypes.MarshalException);
        jMethod.addException(SGTypes.ValidationException);
        jMethod.addParameter(new JParameter(SGTypes.Writer, "out"));
        parent.addMethod(jMethod);
        
        if (isAbstract) {
            jMethod.getModifiers().setAbstract(true);
        }
        else {
            JSourceCode jsc = jMethod.getSourceCode();
            jsc.add("");
            jsc.add("Marshaller.marshal(this, out);");
        }


        //-- create helper marshal method
        //-- start helper marshal method, this method will
        //-- be built up as we process the given ElementDecl
        jMethod = new JMethod(null, "marshal");
        JClass jc = new JClass("org.xml.sax.DocumentHandler");
        jMethod.addException(SGTypes.MarshalException);
        jMethod.addException(SGTypes.ValidationException);
        jMethod.addParameter(new JParameter(jc, "handler"));
        parent.addMethod(jMethod);
        
        if (isAbstract) {
            jMethod.getModifiers().setAbstract(true);
        }
        else {
            JSourceCode jsc = jMethod.getSourceCode();
            jsc = jMethod.getSourceCode();
            jsc.add("");
            jsc.add("Marshaller.marshal(this, handler);");
        }

    } //-- createMarshalMethods

    private void createUnmarshalMethods(JClass parent) {

        //-- mangle method name to avoid compiler errors when this class is extended
		String methodName = "unmarshal";
		if (SourceGenerator.mappingSchemaType2Java())
			methodName+= parent.getName(true);

		//-- create main marshal method
        JMethod jMethod = new JMethod(parent,methodName);
        jMethod.getModifiers().setStatic(true);
        jMethod.addException(SGTypes.MarshalException);
        jMethod.addException(SGTypes.ValidationException);
        jMethod.addParameter(new JParameter(SGTypes.Reader, "reader"));
        parent.addMethod(jMethod);
        
        JSourceCode jsc = jMethod.getSourceCode();
        jsc.add("return (");
        jsc.append(parent.getName());
        jsc.append(") Unmarshaller.unmarshal(");
        jsc.append(parent.getName());
        jsc.append(".class, reader);");

    } //-- createUnmarshalMethods

    /**
     * Create an 'equals' method on the given
     * JClass
     * @param jclass the Jclass in which we create the equals method
     */
     public static void createEqualsMethod(JClass jclass) {
         if (jclass == null)
            throw new IllegalArgumentException("JClass must not be null");

        JField[] fields = jclass.getFields();
        JMethod jMethod = new JMethod(JType.Boolean, "equals");
        jMethod.setComment("Override the java.lang.Object.equals method");
        jMethod.setComment("Note: hashCode() has not been overriden");
        jMethod.addParameter(new JParameter(SGTypes.Object, "obj"));
        jclass.addMethod(jMethod);
        JSourceCode jsc = jMethod.getSourceCode();
        jsc.add("if ( this == obj )");
        jsc.indent();
        jsc.add("return true;");
        jsc.unindent();
        jsc.add("");
        jsc.add("if (obj instanceof "+jclass.getName(true)+") {");
        jsc.add("");
        jsc.indent();
        jsc.add(jclass.getName(true)+" temp = ("+jclass.getName(true)+")obj;");
        for (int i = 0; i <fields.length; i++) {
            JField temp = fields[i];
            //Be careful to arrayList....

            String name = temp.getName();
            if (temp.getType().isPrimitive())
               jsc.add("if (this."+ name +" != temp."+name+")");
            else jsc.add("if (!(this."+ name +".equals(temp."+name+"))) ");
            jsc.indent();
            jsc.add("return false;");
            jsc.unindent();
        }
        jsc.add("return true;");
        jsc.unindent();
        jsc.add("}");
        jsc.add("return false;");

     }//CreateEqualsMethod


    /**
     * Implement org.exolab.castor.tests.framework.CastorTestable im the
     * given JClass
     * @param jclass the JCLass which will implement the CastorTestable Interface
     * @see org.exolab.castor.tests.framework.CastorTestable
     */
     public static void createTestableMethods(JClass jclass) {
         if (jclass == null)
            throw new IllegalArgumentException("JClass must not be null");

        jclass.addInterface("org.exolab.castor.tests.framework.CastorTestable");
        jclass.addImport("org.exolab.castor.tests.framework.CastorTestable");
        jclass.addImport("org.exolab.castor.tests.framework.RandomHelper");

        //implementation of randomizeFields
        JMethod jMethod = new JMethod(null, "randomizeFields");
        jMethod.addException(new JClass("InstantiationException"));
        jMethod.addException(new JClass("IllegalAccessException"));
        jMethod.setComment("implementation of org.exolab.castor.tests.framework.CastorTestable");
        jclass.addMethod(jMethod);
        JSourceCode jsc = jMethod.getSourceCode();
        JField[] fields = jclass.getFields();
        for (int i = 0; i <fields.length; i++) {

            JField temp = fields[i];
            JType type = temp.getType();
            JType component = null;
            String name = temp.getName();
            String setName = "set" + JavaNaming.toJavaClassName(name);
            String componentName = null;

            if (name.indexOf("_has") == -1) {
               //Collection needs a specific handling
               if ( (type.getName().equals("java.util.Vector")) ||
                    (type.getName().equals("java.util.ArrayList")) ) {
                     //if we are dealing with a Vector or an ArrayList
                    //we retrieve the type included in this Collection
                    int listLocat = name.lastIndexOf("List");
                    String tempName = name;
                    if (listLocat != -1)
                       tempName = tempName.substring(0,listLocat);
                    String methodName = JavaNaming.toJavaClassName(tempName);
                    methodName = "get"+methodName;
                    JMethod method = jclass.getMethod(methodName,0);
                    componentName = method.getReturnType().getName();
                    method = null;
                    methodName = null;
                    tempName = null;
                    jsc.add(name+" = RandomHelper.getRandom("+name+", "+componentName+".class);");
               }
               else if (type.isPrimitive())
                  jsc.add(setName+"(RandomHelper.getRandom("+name+"));");
               else
                 jsc.add(setName+"(("+type.getName()+")RandomHelper.getRandom("+name+", "+type.getName()+".class));");
                 //jsc.add(setName+"(("+type.getName()+")RandomHelper.getRandom("+name+", "+((componentName != null)?componentName:type.getName())+".class));");
                jsc.add("");
            }
        }

        //implementation of dumpFields
        jMethod = new JMethod(SGTypes.String, "dumpFields");
        jMethod.setComment("implementation of org.exolab.castor.tests.framework.CastorTestable");
        jclass.addMethod(jMethod);
        jsc = jMethod.getSourceCode();
        jsc.add("String result = \"DumpFields() for element: "+jclass.getName()+"\\n\";");
        for (int i = 0; i <fields.length; i++) {

            JField temp = fields[i];
            String name = temp.getName();
            if ( (temp.getType().isPrimitive()) ||
                 (temp.getType().getName().equals("java.lang.String")))
                  jsc.add("result += \"Field "+name+":\" +"+name+"+\"\\n\";");
            else {
                jsc.add("if ( ("+name+" != null) && ("+name+".getClass().isAssignableFrom(CastorTestable.class)))");
                jsc.indent();
                jsc.add("result += ((CastorTestable)"+name+").dumpFields();");
                jsc.unindent();
                jsc.add("else result += \"Field "+name+":\" +"+name+"+\"\\n\";");
            }
            jsc.add("");
        }
        jsc.add("");
        jsc.add("return result;");
     }//CreateTestableMethods


    /**
     * Creates the Validate methods for the given JClass
     * @param jClass the JClass to create the Validate methods for
    **/
    private void createValidateMethods(JClass jClass) {

        JMethod     jMethod = null;
        JSourceCode jsc     = null;

        //-- #validate
        jMethod = new JMethod(null, "validate");
        jMethod.addException(SGTypes.ValidationException);

        jClass.addMethod(jMethod);
        jsc = jMethod.getSourceCode();
        jsc.add("org.exolab.castor.xml.Validator validator = new ");
        jsc.append("org.exolab.castor.xml.Validator();");
        jsc.add("validator.validate(this);");

        //-- #isValid
        jMethod  = new JMethod(JType.Boolean, "isValid");
        jsc = jMethod.getSourceCode();
        jsc.add("try {");
        jsc.indent();
        jsc.add("validate();");
        jsc.unindent();
        jsc.add("}");
        jsc.add("catch (org.exolab.castor.xml.ValidationException vex) {");
        jsc.indent();
        jsc.add("return false;");
        jsc.unindent();
        jsc.add("}");
        jsc.add("return true;");
        jClass.addMethod(jMethod);

    } //-- createValidateMethods

    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     * Resolves the className out of the given name and the packageName
    **/
    private String resolveClassName(String name, String packageName) {
        if ((packageName != null) && (packageName.length() > 0)) {
            return packageName+"."+name;
        }
        return name;
    } //-- resolveClassName

    /**
     * @param complexType the ComplexType for this ClassInfo
     * @param resolver the ClassInfoResolver for resolving "derived" types.
    **/
    private void processComplexType
        (ComplexType complexType, FactoryState state)
    {
        String typeName = complexType.getName();
		ClassInfo classInfo = state.classInfo;
        classInfo.setSchemaType(new XSClass(state.jClass, typeName));

        Schema schema = complexType.getSchema();
        classInfo.setNamespaceURI(schema.getTargetNamespace());



        //- Handle derived types
        XMLType base = complexType.getBaseType();

        //-- if the base is a complexType, we need to process it
		if ((base != null) && (base.isComplexType())) {

            String className = null;

			//-- Is this base type from the schema we are currently generating source for?
			if (base.getSchema()==schema)
			{
				ClassInfo cInfo = state.resolve(base);
				if (cInfo == null) {

					String packageName = state.jClass.getPackageName();
					JClass jClass = createSourceCode((ComplexType)base,
					                                    state,
					                                    packageName);
					cInfo = state.resolve(base);
					className = jClass.getName();
				}
				else className = cInfo.getJClass().getName();
			}
			else
			{
				//-- Create package qualified class name to a base type class from another package
				className =
					SourceGenerator.getQualifiedClassName(
							base.getSchema().getTargetNamespace(),
							JavaNaming.toJavaClassName(base.getName()));
			}

			//-- Set super class
            state.jClass.setSuperClass(className);
        }
		//--if the base a simpleType,we create a field info to handle the content
		if ( (base != null) && (base.isSimpleType()) ) {
           FieldInfo fieldInfo = memberFactory.createFieldInfoForContent(
                               TypeConversion.convertType((SimpleType)base));
		   handleField(fieldInfo,state);
        }


     	//---------------------/
        //- handle attributes -/
        //---------------------/
        //-- loop throug each attribute
        Enumeration enum = complexType.getAttributeDecls();
        while (enum.hasMoreElements()) {
            AttributeDecl attr = (AttributeDecl)enum.nextElement();

            //-- if we have a new SimpleType...generate ClassInfo
            SimpleType sType = attr.getSimpleType();
            if (sType != null) {
                if ( ! (SimpleTypesFactory.isBuiltInType(sType.getTypeCode())) )
                createSourceCode(sType, state, state.packageName);
            }

            FieldInfo fieldInfo = memberFactory.createFieldInfo(attr, state);
            handleField(fieldInfo, state);
        }

        //------------------------/
        //- handle content model -/
        //------------------------/
        //-- check contentType
        ContentType contentType = complexType.getContentType();

        //-- create text member
        if ((contentType == ContentType.mixed) ||
            (contentType == ContentType.any))
        {

            FieldInfo fieldInfo = memberFactory.createFieldInfoForContent(new XSString());
            handleField(fieldInfo, state);

            if (contentType == ContentType.any) {
                fieldInfo = memberFactory.createFieldInfoForAny();
                handleField(fieldInfo, state);
            }

        }
        processContentModel(complexType, state);
    } //-- processComplextype


    private void handleField(FieldInfo fieldInfo, FactoryState state) {

        if (fieldInfo == null) return;

        JSourceCode scInitializer
            = state.jClass.getConstructor(0).getSourceCode();


        state.classInfo.addFieldInfo(fieldInfo);

        //-- Have FieldInfo create the proper field
        fieldInfo.createJavaField(state.jClass);

        //-- do not create access methods for transient fields
        if (!fieldInfo.isTransient()) {
            fieldInfo.createAccessMethods(state.jClass);
            if (fieldInfo.isBound())
                state.setBoundProperties(true);
        }

        //-- Add initialization code
        fieldInfo.generateInitializerCode(scInitializer);

    } //-- handleField

    /**
     * Processes the given ContentModelGroup
     * @param contentModel the ContentModelGroup to process
    **/
    private void processContentModel
        (ContentModelGroup contentModel, FactoryState state)
    {

        //------------------------------/
        //- handle elements and groups -/
        //------------------------------/

        Enumeration enum = contentModel.enumerate();

        FieldInfo fieldInfo = null;
        while (enum.hasMoreElements()) {

            Structure struct = (Structure)enum.nextElement();
            switch(struct.getStructureType()) {

                //-- handle element declarations
                case Structure.ELEMENT:
                {
                    ElementDecl eDecl = (ElementDecl)struct;

					//-- Output source for element definition?
					boolean elementSource = false;
					if (SourceGenerator.mappingSchemaElement2Java())
						//-- If mapping elements to Java classes
						elementSource = true;
					else if (SourceGenerator.mappingSchemaType2Java()
                             && ( (eDecl.getType().getName() == null)
                                  || (eDecl.getType().isSimpleType())) )
                    {
                        //-- If mapping schema types to Java classes
						//-- only when anonymous complexType used by element
                        //-- or if the element is a simpleType
                        elementSource = true;
					}

					//-- Output Java class for element declaration?
					if (elementSource)
					{
						//-- make sure we haven't processed this element yet
						//-- to prevent endless recursion.
						ElementDecl tmpDecl = eDecl;
						while (tmpDecl.isReference()) {
						    tmpDecl = tmpDecl.getReference();
                            if (tmpDecl == null) {
                                String err = "Unable to find element referenced :\" ";
                                err += eDecl.getName();
                                err +="\"";
                                throw new IllegalStateException(err);
                            }
		                }
                        boolean processed = state.processed(tmpDecl);

						//-- make sure we process the element first
						//-- so that it's available to the MemberFactory
						if ((state.resolve(struct) == null) && (!processed))
						    createSourceCode((ElementDecl)struct,
						                      state,
						                      state.packageName);
					}

                    fieldInfo
                        = memberFactory.createFieldInfo((ElementDecl)struct,
                                                         state);
                    handleField(fieldInfo, state);
                    break;
                }
                //-- handle groups
                case Structure.GROUP:
                    Group group = (Group) struct;
                    //-- if not nested, set compositor
                    if (contentModel instanceof ComplexType) {
                        Order order = group.getOrder();
                        if (order == Order.choice)
                            state.classInfo.setAsChoice();
                        else if (order == Order.seq)
                            state.classInfo.setAsSequence();
                        else
                            state.classInfo.setAsAll();
                    } 
                    //-- handle named groups
                    else if (group.getName() != null) {
                        JClass groupClass 
                            = createSourceCode(group, state, state.packageName);
                        fieldInfo = memberFactory.createFieldInfo(group, state);
                        handleField(fieldInfo, state);
                        break;    
                    }
                    
                    processContentModel(group, state);
                    break;
                //--In case of a ModelGroup:
                //-- 1- it contains another group (all, choice, sequence)
                //-- 2- it is a reference to another group
                //-- No validation on cross referencing since this validation
                //-- will be done in the upcoming SOM
                case Structure.MODELGROUP:
                     ModelGroup modelgroup = (ModelGroup) struct;
                     //--if it is a reference we resolve it
                     if (modelgroup.hasReference()) {
                        ModelGroup tmp = modelgroup.getReference();
                        if (tmp == null) {
                             String err = "Unable to find group referenced :\" ";
                             err += modelgroup.getName();
                             err +="\"";
                            throw new IllegalStateException(err);
                        }
                        //get the contentModel and proccess it
                        if (tmp.getContentModelGroup() != null)
                           processContentModel(tmp.getContentModelGroup(), state);
                        else {
                             String err = "<group> should contains :\" ";
                             err += " 'all' or 'sequence' or 'choice'";
                             err +="\"";
                             throw new IllegalStateException(err);
                        }
                     }

                     //--if not we proceed the nested (all, choice, sequence)
                     break;

                default:
                    break;
            }
        }

    } //-- process(ContentModelGroup)

    /**
     * Creates Comments from Schema annotations
     * @param annotated the Annotated structure to process
     * @return the generated comment
    **/
    private String processAnnotations(Annotated annotated) {
        //-- process annotations
        Enumeration enum = annotated.getAnnotations();
        if (enum.hasMoreElements()) {
            StringBuffer comment = new StringBuffer();
            while (enum.hasMoreElements()) {
                Annotation ann = (Annotation) enum.nextElement();
                Enumeration documentations = ann.getDocumentation();
                while (documentations.hasMoreElements()) {
                    Documentation documentation =
                        (Documentation) documentations.nextElement();
                    String content = documentation.getContent();
                    if ( content != null) comment.append(content);
                }
            }
            return comment.toString();
        }
        return null;
    } //-- processAnnotations

    /**
     * Creates all the necessary enumeration code from the given
     * SimpleType. Enumerations are handled a couple ways.
     * @see processEnumerationAsBaseType
    **/
    private void processEnumeration
        (SimpleType simpleType, FactoryState state)
    {


        switch (enumerationType) {
            case BASE_TYPE_ENUMERATION:
                processEnumerationAsBaseType(simpleType, state);
                break;
            default:
                processEnumerationAsNewObject(simpleType, state);
                break;
        }

    } //-- processEnumeration

    /**
     * Creates all the necessary enumeration code from the given
     * SimpleType. Enumerations are handled a couple ways.
     * @see processEnumerationAsBaseType
    **/
    private void processEnumerationAsNewObject
        (SimpleType simpleType, FactoryState state)
    {

        Enumeration enum = simpleType.getFacets("enumeration");


        //-- select naming for types and instances
        boolean useValuesAsName = true;
        while (enum.hasMoreElements()) {
            Facet facet = (Facet)enum.nextElement();
            String value = facet.getValue().toUpperCase();
            if (!JavaNaming.isValidJavaIdentifier(value)) {
                useValuesAsName = false;
                break;
            }
        }

        enum = simpleType.getFacets("enumeration");

        JClass jClass = state.jClass;
        String className = jClass.getLocalName();

        JField  field  = null;
        JField  fHash  = new JField(SGTypes.Hashtable, "_memberTable");
        fHash.setInitString("init()");
        fHash.getModifiers().setStatic(true);

        JDocComment jdc = null;
        JSourceCode jsc = null;

        //-- modify constructor
        JConstructor constructor = jClass.getConstructor(0);
        constructor.getModifiers().makePrivate();
        constructor.addParameter(new JParameter(JType.Int, "type"));
        constructor.addParameter(new JParameter(SGTypes.String, "value"));
        jsc = constructor.getSourceCode();
        jsc.add("this.type = type;");
        jsc.add("this.stringValue = value;");



        //-- #valueOf method
        JMethod mValueOf = new JMethod(jClass, "valueOf");
        mValueOf.addParameter(new JParameter(SGTypes.String, "string"));
        mValueOf.getModifiers().setStatic(true);
        jClass.addMethod(mValueOf);
        jdc = mValueOf.getJDocComment();
        jdc.appendComment("Returns a new " + className);
        jdc.appendComment(" based on the given String value.");

        jsc = mValueOf.getSourceCode();
        jsc.add("Object obj = null;");
        jsc.add("if (string != null) ");
        jsc.append("obj = _memberTable.get(string);");
        jsc.add("if (obj == null) {");
        jsc.indent();
        jsc.add("String err = \"'\" + string + \"' is not a valid ");
        jsc.append(className);
        jsc.append("\";");
        jsc.add("throw new IllegalArgumentException(err);");
        jsc.unindent();
        jsc.add("}");
        jsc.add("return (");
        jsc.append(className);
        jsc.append(") obj;");

        //-- #enumerate method
        JMethod mEnumerate = new JMethod(SGTypes.Enumeration, "enumerate");
        mEnumerate.getModifiers().setStatic(true);
        jClass.addMethod(mEnumerate);
        jdc = mEnumerate.getJDocComment();
        jdc.appendComment("Returns an enumeration of all possible instances of ");
        jdc.appendComment(className);
        mEnumerate.getSourceCode().add("return _memberTable.elements();");

        //-- #toString method
        JMethod mToString = new JMethod(SGTypes.String, "toString");
        jClass.addMethod(mToString);
        jdc = mToString.getJDocComment();
        jdc.appendComment("Returns the String representation of this ");
        jdc.appendComment(className);
        mToString.getSourceCode().add("return this.stringValue;");

        //-- #init method
        JMethod mInit = new JMethod(SGTypes.Hashtable, "init");
        jClass.addMethod(mInit);
        mInit.getModifiers().makePrivate();
        mInit.getModifiers().setStatic(true);
        mInit.getSourceCode().add("Hashtable members = new Hashtable();");


        //-- Loop through "enumeration" facets
        int count = 0;

        while (enum.hasMoreElements()) {

            Facet facet = (Facet) enum.nextElement();

            String value = facet.getValue();

            String typeName = null;
            String objName = null;

            if (useValuesAsName) objName = value.toUpperCase();
            else objName = "VALUE_" + count;
                
            //-- create typeName
            //-- Note: this could cause name conflicts
            typeName = objName + "_TYPE";


            //-- Inheritence/Duplicate name cleanup
            boolean addInitializerCode = true;
            if (jClass.getField(objName) != null) {
                //-- either inheritence, duplicate name, or error.
                //-- if inheritence or duplicate name, always take
                //-- the later definition. Do same if error, for now.
                jClass.removeField(objName);
                jClass.removeField(typeName);
                addInitializerCode = false;
            }

            //-- handle int type
            field = new JField(JType.Int, typeName);
            field.setComment("The " + value + " type");
            JModifiers modifiers = field.getModifiers();
            modifiers.setFinal(true);
            modifiers.setStatic(true);
            modifiers.makePublic();
            field.setInitString(Integer.toString(count));
            jClass.addField(field);

            //-- handle Class type
            field = new JField(jClass, objName);
            field.setComment("The instance of the " + value + " type");

            modifiers = field.getModifiers();

            modifiers.setFinal(true);
            modifiers.setStatic(true);
            modifiers.makePublic();

            StringBuffer init = new StringBuffer();
            init.append("new ");
            init.append(className);
            init.append("(");
            init.append(typeName);
            init.append(", \"");
            init.append(value);
            init.append("\")");

            field.setInitString(init.toString());
            jClass.addField(field);


            //-- initializer method
            
            if (addInitializerCode) {
                jsc = mInit.getSourceCode();
                jsc.add("members.put(\"");
                jsc.append(escapeValue(value));
                jsc.append("\", ");
                jsc.append(objName);
                jsc.append(");");
            }

            ++count;
        }

        //-- finish init method
        mInit.getSourceCode().add("return members;");

        //-- add memberTable to the class, we can only
        //-- add this after all the types, or we'll
        //-- create source code that will generate
        //-- null pointer exceptions, because calling
        //-- init() will try to add null values to
        //-- the hashtable.
        jClass.addField(fHash);

        //-- add internal type
        field = new JField(JType.Int, "type");
        field.setInitString("-1");
        jClass.addField(field);

        //-- add internal stringValue
        field = new JField(SGTypes.String, "stringValue");
        field.setInitString("null");
        jClass.addField(field);

        //-- add #getType method

        JMethod mGetType = new JMethod(JType.Int, "getType");
        mGetType.getSourceCode().add("return this.type;");
        jdc = mGetType.getJDocComment();
        jdc.appendComment("Returns the type of this " + className);
        jClass.addMethod(mGetType);



    } //-- processEnumeration

    /**
     * Creates all the necessary enumeration code from the given
     * SimpleType.
     * Enumerations are handled by creating an Object like the
     * following:
     * <BR />
     * <CODE>
     *    public class {name} {
     *        // list of values
     *        {type}[] values = {
     *            ...
     *        };
     *
     *        // Returns true if the given value is part
     *        // of this enumeration
     *        public boolean contains({type} value);
     *
     *        // Returns the {type} value whose String value
     *        // is equal to the given String
     *        public {type} valueOf(String strValue);
     *    }
     *
     * </CODE>
     * /// NOT YET FINISHED, so it's not enabled
    **/
    private void processEnumerationAsBaseType
        (SimpleType simpleType, FactoryState state)
    {


        SimpleType base = (SimpleType)simpleType.getBaseType();
        XSType baseType = null;

        if (base == null)
            baseType = new XSString();
        else
            baseType = TypeConversion.convertType(base);


        Enumeration enum = simpleType.getFacets("enumeration");

        JClass jClass = state.jClass;
        String className = jClass.getLocalName();


        JField      fValues = null;
        JDocComment jdc     = null;
        JSourceCode jsc     = null;

        //-- modify constructor
        JConstructor constructor = jClass.getConstructor(0);
        constructor.getModifiers().makePrivate();

        fValues = new JField(baseType.getJType().createArray(), "values");

        //-- Loop through "enumeration" facets
        //-- and create the default values for the type.
        int count = 0;

        StringBuffer values = new StringBuffer("{\n");

        while (enum.hasMoreElements()) {

            Facet facet = (Facet) enum.nextElement();

            String value = facet.getValue();

            //-- Should we make sure the value is valid
            //-- before proceeding??


            //-- we need to move this code to XSType
            //-- so that we don't have to do special
            //-- code here for each type

            if (count > 0) values.append(",\n");

            //-- indent for fun
            values.append("    ");

            if (baseType.getType() == XSType.STRING) {
                values.append('\"');
                //-- escape value
                values.append(escapeValue(value));
                values.append('\"');

            }
            else values.append(value);

            ++count;
        }

        values.append("\n}");

        fValues.setInitString(values.toString());
        jClass.addField(fValues);

        //-- #valueOf method
        JMethod method = new JMethod(jClass, "valueOf");
        method.addParameter(new JParameter(SGTypes.String, "string"));
        method.getModifiers().setStatic(true);
        jClass.addMethod(method);
        jdc = method.getJDocComment();
        jdc.appendComment("Returns the " + baseType.getJType());
        jdc.appendComment(" based on the given String value.");
        jsc = method.getSourceCode();

        jsc.add("for (int i = 0; i < values.length; i++) {");
        jsc.add("}");
        jsc.add("throw new IllegalArgumentException(\"");
        jsc.append("Invalid value for ");
        jsc.append(className);
        jsc.append(": \" + string + \".\");");

    } //-- processEnumerationAsBaseType

    /**
     * Escapes special characters in the given String so that it can
     * be printed correctly.
     *
     * @param str the String to escape
     * @return the escaped String, or null if the given String was null.
    **/
    private static String escapeValue(String str) {
        if (str == null) return str;

        StringBuffer sb = new StringBuffer();
        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            switch (ch) {
                case '\\':
                case '\"':
                case '\'':
                    sb.append('\\');
                    break;
                default:
                    break;
            }
            sb.append(ch);
        }
        return sb.toString();

    } //-- escapeValue



} //-- SourceFactory


/**
 * A class used to save State information for the SourceFactory
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
**/
class FactoryState implements ClassInfoResolver {

    //--------------------/
    //- Member Variables -/
    //--------------------/

    JClass    jClass           = null;
    ClassInfo classInfo        = null;

    String packageName         = null;

    private ClassInfoResolver _resolver = null;
    private Vector            _processed = null;

    /**
     * Keeps track of whether or not the BoundProperties
     * methods have been created
    **/
    private boolean           _bound = false;

    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates a new FactoryState
    **/
    protected FactoryState(String className, ClassInfoResolver resolver) {
        this(className, resolver, null);
    } //-- FactoryState

    /**
     * Creates a new FactoryState
    **/
    protected FactoryState
        (String className, ClassInfoResolver resolver, String packageName)
    {
        _processed   = new Vector();
		//keep the elements and complexType already processed
		if (resolver instanceof FactoryState)
		   _processed = ((FactoryState)resolver)._processed;

        jClass       = new JClass(className);
        classInfo    = new ClassInfo(jClass);

        if (resolver == null)
            _resolver = new ClassInfoResolverImpl();
        else
            _resolver = resolver;

        this.packageName = packageName;

        //-- boundProperties
        _bound = SourceGenerator.boundPropertiesEnabled();

    } //-- FactoryState

    //-----------/
    //- Methods -/
    //-----------/

    /**
     * Adds the given Reference to this ClassInfo resolver
     * @param key the key to bind a reference to
     * @param classInfo the ClassInfo which is being referenced
    **/
    public void bindReference(Object key, ClassInfo classInfo) {
        _resolver.bindReference(key, classInfo);
    } //-- bindReference

    /**
     * Marks the given element as having been processed.
     * @param complexType the Element to mark as having
     * been processed.
    **/
    void markAsProcessed(ElementDecl element) {
        _processed.addElement(element);
    } //-- markAsProcessed

    /**
     * Returns true if the given Element has been marked as processed
     * @param complexType the Element to check for being marked as processed
    **/
    boolean processed(ElementDecl element) {
        return _processed.contains(element);
    } //-- processed

    /**
     * Marks the given complexType as having been processed.
     * @param complexType the ComplexType to mark as having
     * been processed.
    **/
    void markAsProcessed(ComplexType complex) {
        _processed.addElement(complex);
    } //-- markAsProcessed

    /**
     * Returns true if the given ComplexType has been marked as processed
     * @param complexType the ComplexType to check for being marked as processed
    **/
    boolean processed(ComplexType complex) {
       return _processed.contains(complex);
    } //-- processed

    /**
     * Marks the given Group as having been processed.
     * @param group the Group to mark as having
     * been processed.
    **/
    void markAsProcessed(Group group) {
        _processed.addElement(group);
    } //-- markAsProcessed

    /**
     * Returns true if the given Group has been marked as processed
     * @param group the Group to check for being marked as processed
    **/
    boolean processed(Group group) {
       return _processed.contains(group);
    } //-- processed

	/**
     * Returns true if any bound properties have been found
     *
     * @return true if any bound properties have been found
    **/
    boolean hasBoundProperties() {
        return _bound;
    } //-- hasBoundProperties

    /**
     * Allows setting the bound properties flag
     *
     * @param bound the new value of the bound properties flag
     * @see #hasBoundProperties
    **/
    void setBoundProperties(boolean bound) {
        _bound = bound;
    } //-- setBoundProperties

    /**
     * Returns the ClassInfo which has been bound to the given key
     * @param key the object to which the ClassInfo has been bound
     * @return the ClassInfo which has been bound to the given key
    **/
    public ClassInfo resolve(Object key) {
        return _resolver.resolve(key);
    } //-- resolve

} //-- FactoryState
