/*
 * Copyright 2005 Werner Guttmann
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
package org.exolab.castor.builder;

import java.util.Enumeration;

import org.exolab.castor.builder.types.XSString;
import org.exolab.castor.builder.types.XSType;
import org.exolab.castor.xml.JavaNaming;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JArrayType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JConstructor;
import org.exolab.javasource.JDocComment;
import org.exolab.javasource.JField;
import org.exolab.javasource.JMethod;
import org.exolab.javasource.JModifiers;
import org.exolab.javasource.JParameter;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * This class creates the Java sources for XML Schema components that define
 * an enumeration.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6287 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class EnumerationFactory extends BaseFactory {

    /**
     * The TypeConversion instance to use for mapping SimpleTypes into XSTypes
     */
    private TypeConversion _typeConversion;

    /**
     * A flag indicating that enumerated types should be constructed to perform
     * case insensitive lookups based on the values.
     */
    private boolean _caseInsensitive = false;

    /**
     * Creates a new EnumerationFactory for the builder configuration given.
     * @param config the current BuilderConfiguration instance.
     * @param groupNaming The group naming scheme to be used.
     */
    public EnumerationFactory(final BuilderConfiguration config, final GroupNaming groupNaming) {
        super(config, null, groupNaming);
        _typeConversion = new TypeConversion(_config);
    } //-- SourceFactory

    /**
     * Creates all the necessary enumeration code for a given SimpleType.
     *
     * @param simpleType the SimpleType we are processing an enumeration for
     * @param state our current state
     * @see #processEnumerationAsBaseType
     */
    void processEnumerationAsNewObject(final SimpleType simpleType, final FactoryState state) {
        Enumeration enumeration = simpleType.getFacets("enumeration");

        //-- select naming for types and instances
        boolean useValuesAsName = true;
        useValuesAsName = selectNamingScheme(enumeration, useValuesAsName);

        enumeration = simpleType.getFacets("enumeration");

        JClass jClass = state.jClass;
        String className = jClass.getLocalName();

        jClass.addImport("java.util.Hashtable");
        JField  field  = null;
        JField  fHash  = new JField(SGTypes.createHashtable(_config.useJava50()), "_memberTable");
        fHash.setInitString("init()");
        fHash.getModifiers().setStatic(true);

        JSourceCode jsc = null;

        //-- modify constructor
        JConstructor constructor = jClass.getConstructor(0);
        constructor.getModifiers().makePrivate();
        constructor.addParameter(new JParameter(JType.INT, "type"));
        constructor.addParameter(new JParameter(SGTypes.String, "value"));
        jsc = constructor.getSourceCode();
        jsc.add("this.type = type;");
        jsc.add("this.stringValue = value;");

        //-- #valueOf method
        createValueOfMethod(jClass, className);

        //-- #enumerate method
        createEnumerateMethod(jClass, className);

        //-- #toString method
        createToStringMethod(jClass, className);

        //-- #init method
        JMethod mInit = createInitMethod(jClass);

        //-- #readResolve method
        createReadResolveMethod(jClass);

        //-- Loop through "enumeration" facets
        int count = 0;

        while (enumeration.hasMoreElements()) {
            Facet facet = (Facet) enumeration.nextElement();

            String value = facet.getValue();

            String typeName = null;
            String objName  = null;

            if (useValuesAsName) {
                objName = translateEnumValueToIdentifier(value);
            } else {
                objName = "VALUE_" + count;
            }

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
            field = new JField(JType.INT, typeName);
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
            init.append(escapeValue(value));
            init.append("\")");

            field.setInitString(init.toString());
            jClass.addField(field);

            //-- initializer method

            if (addInitializerCode) {
                jsc = mInit.getSourceCode();
                jsc.add("members.put(\"");
                jsc.append(escapeValue(value));
                if (_caseInsensitive) {
                    jsc.append("\".toLowerCase(), ");
                } else {
                    jsc.append("\", ");
                }
                jsc.append(objName);
                jsc.append(");");
            }

            ++count;
        }

        //-- finish init method
        mInit.getSourceCode().add("return members;");

        //-- add memberTable to the class, we can only add this after all the types,
        //-- or we'll create source code that will generate null pointer exceptions,
        //-- because calling init() will try to add null values to the hashtable.
        jClass.addField(fHash);

        //-- add internal type
        field = new JField(JType.INT, "type");
        field.setInitString("-1");
        jClass.addField(field);

        //-- add internal stringValue
        field = new JField(SGTypes.String, "stringValue");
        field.setInitString("null");
        jClass.addField(field);

        createGetTypeMethod(jClass, className);
    } //-- processEnumerationAsNewObject

    private boolean selectNamingScheme(final Enumeration enumeration, final boolean useValuesAsName) {
        boolean duplicateTranslation = false;
        short numberOfTranslationToSpecialCharacter = 0;

        while (enumeration.hasMoreElements()) {
            Facet facet = (Facet) enumeration.nextElement();
            String possibleId = translateEnumValueToIdentifier(facet.getValue());
            if (possibleId.equals("_")) {
                numberOfTranslationToSpecialCharacter++;
                if (numberOfTranslationToSpecialCharacter > 1) {
                    duplicateTranslation = true;
                }
            }

            if (!JavaNaming.isValidJavaIdentifier(possibleId)) {
                return false;
            }
        }

        if (duplicateTranslation) {
            return false;
        }
        return useValuesAsName;
    }

    /**
     * Creates 'getType()' method for this enumeration class.
     * @param jClass The enumeration class to create this method for.
     * @param className The name of the class.
     */
    private void createGetTypeMethod(final JClass jClass, final String className) {
        JMethod mGetType = new JMethod("getType", JType.INT, "the type of this " + className);
        mGetType.getSourceCode().add("return this.type;");
        JDocComment jdc = mGetType.getJDocComment();
        jdc.appendComment("Returns the type of this " + className);
        jClass.addMethod(mGetType);
    }

    /**
     * Creates 'readResolve(Object)' method for this enumeration class.
     * @param jClass The enumeration class to create this method for.
     */
    private void createReadResolveMethod(final JClass jClass) {
        JDocComment jdc;
        JSourceCode jsc;
        JMethod mReadResolve = new JMethod("readResolve", SGTypes.Object,
                                           "this deserialized object");
        mReadResolve.getModifiers().makePrivate();
        jClass.addMethod(mReadResolve);
        jdc = mReadResolve.getJDocComment();
        jdc.appendComment(" will be called during deserialization to replace ");
        jdc.appendComment("the deserialized object with the correct constant ");
        jdc.appendComment("instance.");
        jsc = mReadResolve.getSourceCode();
        jsc.add("return valueOf(this.stringValue);");
    }

    /**
     * Creates 'init()' method for this enumeration class.
     * @param jClass The enumeration class to create this method for.
     * @return an 'init()' method for this enumeration class.
     */
    private JMethod createInitMethod(final JClass jClass) {
        JMethod mInit = new JMethod("init", SGTypes.createHashtable(_config.useJava50()),
                                    "the initialized Hashtable for the member table");
        jClass.addMethod(mInit);
        mInit.getModifiers().makePrivate();
        mInit.getModifiers().setStatic(true);
        if (_config.useJava50()) {
            mInit.getSourceCode().add("Hashtable<Object, Object> members = new Hashtable<Object, Object>();");
        } else {
            mInit.getSourceCode().add("Hashtable members = new Hashtable();");
        }
        return mInit;
    }

    /**
     * Creates 'toString()' method for this enumeration class.
     * @param jClass The enumeration class to create this method for.
     * @param className The name of the class.
     */
    private void createToStringMethod(final JClass jClass, final String className) {
        JMethod mToString = new JMethod("toString", SGTypes.String,
                                        "the String representation of this " + className);
        jClass.addMethod(mToString);
        JDocComment jdc = mToString.getJDocComment();
        jdc.appendComment("Returns the String representation of this ");
        jdc.appendComment(className);
        mToString.getSourceCode().add("return this.stringValue;");
    }

    /**
     * Creates 'enumerate()' method for this enumeration class.
     * @param jClass The enumeration class to create this method for.
     * @param className The name of the class.
     */
    private void createEnumerateMethod(final JClass jClass, final String className) {
        // TODO: for the time being return Enumeration<Object> for Java 5.0; change
        JMethod mEnumerate = new JMethod("enumerate", SGTypes.createEnumeration(SGTypes.Object, _config.useJava50()),
                                         "an Enumeration over all possible instances of " + className);
        mEnumerate.getModifiers().setStatic(true);
        jClass.addMethod(mEnumerate);
        JDocComment jdc = mEnumerate.getJDocComment();
        jdc.appendComment("Returns an enumeration of all possible instances of ");
        jdc.appendComment(className);
        mEnumerate.getSourceCode().add("return _memberTable.elements();");
    }

    /**
     * Creates 'valueOf(String)' method for this enumeration class.
     * @param jClass The enumeration class to create this method for.
     * @param className The name of the class.
     */
    private void createValueOfMethod(final JClass jClass, final String className) {
        JMethod mValueOf = new JMethod("valueOf", jClass,
                                       "the " + className + " value of parameter 'string'");
        mValueOf.addParameter(new JParameter(SGTypes.String, "string"));
        mValueOf.getModifiers().setStatic(true);
        jClass.addMethod(mValueOf);

        JDocComment jdc = mValueOf.getJDocComment();
        jdc.appendComment("Returns a new " + className);
        jdc.appendComment(" based on the given String value.");

        JSourceCode jsc = mValueOf.getSourceCode();
        jsc.add("java.lang.Object obj = null;");
        jsc.add("if (string != null) ");

        if (_caseInsensitive) {
            jsc.append("obj = _memberTable.get(string.toLowerCase());");
        } else {
            jsc.append("obj = _memberTable.get(string);");
        }

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
    }

    /**
     * Creates all the necessary enumeration code from the given SimpleType.
     * Enumerations are handled by creating an Object like the following:
     *
     * <pre>
     *     public class {name} {
     *         // list of values
     *         {type}[] values = {
     *             ...
     *         };
     *
     *         // Returns true if the given value is part
     *         // of this enumeration
     *         public boolean contains({type} value);
     *
     *         // Returns the {type} value whose String value
     *         // is equal to the given String
     *         public {type} valueOf(String strValue);
     *     }
     * </pre>
     *
     * @param simpleType the SimpleType we are processing an enumeration for
     * @param state our current state
     */
    void processEnumerationAsBaseType(final SimpleType simpleType, final FactoryState state) {
        SimpleType base = (SimpleType) simpleType.getBaseType();
        XSType baseType = null;

        if (base == null) {
            baseType = new XSString();
        } else {
            baseType = _typeConversion.convertType(base, _config.useJava50());
        }

        Enumeration enumeration = simpleType.getFacets("enumeration");

        JClass jClass    = state.jClass;
        String className = jClass.getLocalName();

        JField      fValues = null;
        JDocComment jdc     = null;
        JSourceCode jsc     = null;

        //-- modify constructor
        JConstructor constructor = jClass.getConstructor(0);
        constructor.getModifiers().makePrivate();

        fValues = new JField(new JArrayType(baseType.getJType(), _config.useJava50()), "values");

        //-- Loop through "enumeration" facets
        //-- and create the default values for the type.
        int count = 0;

        StringBuffer values = new StringBuffer("{\n");

        while (enumeration.hasMoreElements()) {
            Facet facet = (Facet) enumeration.nextElement();
            String value = facet.getValue();

            //-- Should we make sure the value is valid before proceeding??

            //-- we need to move this code to XSType so that we don't have to do
            //-- special code here for each type

            if (count > 0) {
                values.append(",\n");
            }

            //-- indent for fun
            values.append("    ");

            if (baseType.getType() == XSType.STRING_TYPE) {
                values.append('\"');
                //-- escape value
                values.append(escapeValue(value));
                values.append('\"');
            } else {
                values.append(value);
            }

            ++count;
        }

        values.append("\n}");

        fValues.setInitString(values.toString());
        jClass.addField(fValues);

        //-- #valueOf method
        JMethod method = new JMethod("valueOf", jClass,
                                     "the String value of the provided " + baseType.getJType());
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
     * Attempts to translate a simpleType enumeration value into a legal java
     * identifier. Translation is through a couple of simple rules:
     * <ul>
     *   <li>if the value parses as a non-negative int, the string 'VALUE_' is
     *       prepended to it</li>
     *   <li>if the value parses as a negative int, the string 'VALUE_NEG_' is
     *       prepended to it</li>
     *   <li>the value is uppercased</li>
     *   <li>the characters <code>[](){}<>'`"</code> are removed</li>
     *   <li>the characters <code>|\/?~!@#$%^&*-+=:;.,</code> and any
     *       whitespace are replaced with <code>_</code></li>
     * </ul>
     * @param enumValue the enum value to turn into a legal Java identifier
     * @return an identifier name for this enum value.
     * @author rhett-sutphin@uiowa.edu
     */
    private String translateEnumValueToIdentifier(final String enumValue) {
        try {
            int intVal = Integer.parseInt(enumValue);
            if (intVal >= 0) {
                return "VALUE_" + intVal;
            }

            return "VALUE_NEG_" + Math.abs(intVal);
        } catch (NumberFormatException e) {
            // just keep going
        }

        StringBuffer sb = new StringBuffer(enumValue.toUpperCase());
        char c;
        for (int i = 0; i < sb.length(); i++) {
            c = sb.charAt(i);
            if ("[](){}<>'`\"".indexOf(c) >= 0) {
                sb.deleteCharAt(i);
                i--;
            } else if (Character.isWhitespace(c) || "\\/?~!@#$%^&*-+=:;.,".indexOf(c) >= 0) {
                sb.setCharAt(i, '_');
            }
        }
        return sb.toString();
    } //-- translateEnumValueToIdentifier

    /**
     * Set to true if enumerated type lookups should be performed in a case
     * insensitive manner.
     *
     * @param caseInsensitive when true
     */
    public void setCaseInsensitive(final boolean caseInsensitive) {
        _caseInsensitive = caseInsensitive;
    }

    /**
     * Escapes special characters in the given String so that it can be printed
     * correctly.
     *
     * @param str the String to escape
     * @return the escaped String, or null if the given String was null.
     */
    private static String escapeValue(final String str) {
        if (str == null) {
            return str;
        }

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

}
