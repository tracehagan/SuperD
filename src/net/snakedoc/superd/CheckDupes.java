/*******************************************************************************
 *  Copyright 2013 Jason Sipula, Trace Hagan                                   *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *******************************************************************************/

package net.snakedoc.superd;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.database.H2;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.io.File;


public class CheckDupes {
    private static final Logger log = Logger.getLogger(CheckDupes.class);
    
	public static void main(String[] args) {
		CheckDupes cd = new CheckDupes();
		cd.checkDupes();
	}
	public void checkDupes() {
	    Config config = new Config("props/superD.properties");
	    config.loadConfig("props/log4j.properties");

	    // SQL statements
        String sqlSelectDuplicateHashes = "INSERT INTO nonUnique (file_path, file_hash, file_size) SELECT  file_path, file_hash, file_size FROM files WHERE file_hash IN (SELECT file_hash FROM files GROUP BY file_hash HAVING COUNT(*) > 1)";
        String sqlSelectUnique = "INSERT INTO signatures (file_hash, file_size) SELECT DISTINCT file_hash, file_size FROM nonUnique";
		
		// Prepared Statements (NULL)
        PreparedStatement psSelectDuplicateHashes = null;
        PreparedStatement psSelectUnique = null;


		// setup database object
		H2 db = null;
        try {
            db = Database.getInstance();
        } catch (ConfigException e2) {
            log.error("Failed to read config file!", e2);
        }
        try {
            db.openConnection();
        } catch (ClassNotFoundException e1) {
            // means driver for database is not found
            log.fatal("Failed to read the database!", e1);
        } catch (SQLException e1) {
            log.fatal("Failed to open database!", e1);
        }
		
		// let's get to business...
		
        // initialize our prepared statements
		try {
            psSelectDuplicateHashes = db.getConnection().prepareStatement(sqlSelectDuplicateHashes);
            psSelectUnique = db.getConnection().prepareStatement(sqlSelectUnique);
		} catch (SQLException e) {
			log.error("Error setting database statements!", e);
		}
        try
        {   psSelectDuplicateHashes.execute();
            psSelectDuplicateHashes.close();

        } catch (SQLException e) {
            log.error("unable to insert duplicate hash rows!", e);
        }

        try
        {
            psSelectUnique.execute();
            psSelectUnique.close();
        } catch (SQLException e){
            log.error("unable to get unique hashes!", e);
        }
        /*
        //ADDING SOMETHING TO TEST DELETER.java
        String sqlSelectAll = "SELECT * FROM nonUnique ORDER BY file_hash ASC;";
        String sqlCount = "SELECT COUNT(*) FROM nonUnique;";
        PreparedStatement psSelectAll = null;
        PreparedStatement psCount = null;
        ResultSet rsCount = null;
        ResultSet rsSelectAll = null;
        int count = 0;

        try{
            psSelectAll = db.getConnection().prepareStatement(sqlSelectAll);
            psCount = db.getConnection().prepareStatement(sqlCount);
        } catch (SQLException e){
            log.fatal("unable to prepare statement");
        }

        try{
            rsSelectAll = psSelectAll.executeQuery();
            rsCount = psCount.executeQuery();
            rsCount.next();
            count = rsCount.getInt(1);
        } catch (SQLException e){
            log.fatal("unable to execute query");
        }
        File[] dupes = new File[count];
        try{
            int i = 0;
            while(rsSelectAll.next() && rsSelectAll != null && i < count){
                dupes[i] = new File(rsSelectAll.getString("file_path"));
                i++;
            }
        } catch (SQLException e){
            log.fatal("Unable to build duplicates list");
        }

        Deleter.buildGUI(dupes); */
		
		try {
            db.closeConnection();
        } catch (SQLException e) {
            log.warn("Failed to close resource!", e);
        }
	}
}
