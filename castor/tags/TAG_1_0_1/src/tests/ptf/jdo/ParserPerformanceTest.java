/*
 * Copyright 2005 Stein M. Hugubakken
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
package ptf.jdo;

import org.exolab.castor.jdo.oql.Lexer;
import org.exolab.castor.jdo.oql.Parser;

/**
 * Performance Test
 * 
 * @author <a href="mailto:dulci@start.no">Stein M. Hugubakken </a>
 * @version $Revision$ $Date: 2006-01-03 17:47:48 -0700 (Tue, 03 Jan 2006) $
 */
public final class ParserPerformanceTest {
    private static final String[] QUERIES = new String[] {
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestObject object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceCount object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceTime object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceNone object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT object FROM jdo.TestRaceUnlimited object WHERE id = $1",
        "SELECT t FROM jdo.Master t",
        "SELECT types FROM jdo.TestTypes types WHERE id = $(integer)1",
        "SELECT object FROM jdo.TestManyGroup object WHERE object.id < $1",
        "SELECT object FROM jdo.TestManyPerson object WHERE object.id < $1",
        "SELECT object FROM jdo.TestManyGroup object WHERE id = $1",
        "SELECT object FROM jdo.TestManyPerson object WHERE id = $1",
        "SELECT master FROM jdo.TestMaster master",
        "SELECT group FROM jdo.TestGroup group",
        "SELECT master FROM jdo.TestMaster master WHERE master.details.value1=$1",
        "SELECT m FROM jdo.TestMaster m WHERE m.details.details2.value1=$1",
        "SELECT master FROM jdo.TestMaster master WHERE master.group=$1",
        "SELECT master FROM jdo.TestMaster master",
        "SELECT group FROM jdo.TestGroup group",
        "SELECT master FROM jdo.TestMasterKeyGen master",
        "SELECT group FROM jdo.TestGroup group",
        "SELECT master FROM jdo.TestMasterKeyGen master WHERE master.details.value1=$1",
        "SELECT m FROM jdo.TestMasterKeyGen m WHERE m.details.details2.value1=$1",
        "SELECT master FROM jdo.TestMasterKeyGen master WHERE master.group=$1",
        "SELECT master FROM jdo.TestMasterKeyGen master",
        "SELECT group FROM jdo.TestGroup group",
        "SELECT t FROM jdo.TestNestedFields t WHERE id = $1",
        "SELECT t FROM jdo.TestNestedFields t WHERE nested2.nested3.value2 = $1",
        "SELECT t FROM jdo.TestOqlExtends t",
        "SELECT t FROM jdo.TestGroup t",
        "SELECT t FROM jdo.TestOqlExtends2 t",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT t FROM jdo.TestPersistent t",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT t FROM jdo.TestOqlExtends t WHERE t.group.id=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT t FROM jdo.TestPersistent t WHERE t.creationTime<=$1",
        "SELECT t FROM jdo.TestOqlExtends t WHERE t.list.id=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT object FROM jdo.TestMaxObject object WHERE id = $1",
        "SELECT ext FROM jdo.TestMaxExtends ext WHERE id = $1",
        "SELECT object FROM jdo.TestHighLowObject object WHERE id = $1",
        "SELECT ext FROM jdo.TestHighLowExtends ext WHERE id = $1",
        "SELECT object FROM jdo.TestUuidObject object WHERE id = $1",
        "SELECT ext FROM jdo.TestUuidExtends ext WHERE id = $1",
        "SELECT object FROM jdo.TestIdentityObject object WHERE id = $1",
        "SELECT ext FROM jdo.TestIdentityExtends ext WHERE id = $1",
        "SELECT p FROM jdo.TestPersistent p WHERE id=$1",
        "SELECT g FROM jdo.TestGroup g",
        "SELECT r FROM jdo.TestPersistRelated r",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1",
        "SELECT p FROM jdo.TestObjectTimeStampable p WHERE id=$1",
        "SELECT p FROM jdo.TestObjectTimeStampable p WHERE id=$1",
        "select obj from jdo.TestObject2 obj",
        "select obj from jdo.TestObject2 obj",
        "SELECT object FROM jdo.TestManyGroup object WHERE object.id < $1",
        "SELECT object FROM jdo.TestManyPerson object WHERE object.id < $1",
        "SELECT object FROM jdo.TestManyGroup object WHERE object.id < $1",
        "SELECT object FROM jdo.TestManyPerson object WHERE object.id < $1"
    };
    
    public static void main(final String[] args) {
        try {
            long start = System.currentTimeMillis();
            
            for (int i = 0; i < QUERIES.length; i++) {
                Lexer lexer = new Lexer(QUERIES[i]);
                Parser parser = new Parser(lexer);
                parser.getParseTree();
            }
            
            long stop = System.currentTimeMillis();
            
            System.out.println("Parsed " + QUERIES.length + " queries in "
                    + (stop - start) + " milliseconds");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

    }

    private ParserPerformanceTest() { }
}