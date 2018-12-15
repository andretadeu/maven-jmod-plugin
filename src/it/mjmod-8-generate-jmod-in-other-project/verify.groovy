/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.jar.JarEntry
import java.util.jar.JarFile

boolean result = true

try {
    println("Checking if ${basedir}/about-cli-app/target exists.")
    File target = new File( basedir, "/about-cli-app/target" )
    if ( !target.exists() || !target.isDirectory() ) {
        System.err.println( "${target.getAbsolutePath()} folder is missing or not a directory." )
        return false
    }

    File artifact = new File( basedir, "/about-cli-distribution-jmod/target/jmods/about-cli-distribution-jmod.jmod" )
    if ( !artifact.exists() || artifact.isDirectory() ) {
        System.err.println( "${artifact.getAbsolutePath()} file is missing or a directory." )
        return false
    }

    String[] artifactNames = [
            "classes/module-info.class",
            "classes/META-INF/MANIFEST.MF",
            "classes/myproject/about/cli/Main.class",
            "classes/META-INF/maven/org.apache.maven.plugins/about-cli-app/pom.xml",
            "classes/META-INF/maven/org.apache.maven.plugins/about-cli-app/pom.properties",
            "conf/about.yaml",
            "bin/about"
    ]

    Set contents = new HashSet()

    JarFile jar = new JarFile( artifact )
    Enumeration jarEntries = jar.entries()
    while ( jarEntries.hasMoreElements() ) {
        JarEntry entry = (JarEntry) jarEntries.nextElement()
        println("Current entry: ${entry}")
        if ( !entry.isDirectory() ) {
            // Only compare files
            contents.add( entry.getName() )
        }
    }

    if  ( artifactNames.length != contents.size() ) {
        System.err.println( "jar content size is different from the expected content size" )
        return false
    }
    artifactNames.each{ artifactName ->
        if ( !contents.contains( artifactName ) ) {
            System.err.println( "Artifact[" + artifactName + "] not found in jar archive" )
            return false
        }
    }
}
catch( Throwable e ) {
    e.printStackTrace()
    result = false
}

return result
