package DupesReport;

/**
 * Created by tracehagan on 12/27/13.
 */

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;
import java.io.*;
import org.apache.log4j.Logger;

import net.snakedoc.jutils.Config;
import net.snakedoc.jutils.ConfigException;
import net.snakedoc.jutils.timer.MilliTimer;
import net.snakedoc.jutils.database.H2;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);

    public static void main( String args[]){
        Config config = new Config("props/superD.properties");
        config.loadConfig("props/log4j.properties");
        log.info("\n\n");
        log.info("Starting program!");
        H2 db = null;

        /*try{
            db = Database.getInstance();
            try {
                db.openConnection();
                log.debug("Opened connection");
            } catch (ClassNotFoundException | SQLException e) {
                log.fatal("Failed to open database connection! Check config file!", e);
            }
            String sqlReport = "SELECT file_path, file_size FROM files INNER JOIN duplicates ON files.record_id = duplicates.dupe1_id ORDER BY file_size DESC";
            PreparedStatement psReport = null;
            ResultSet rsReport = null;
            try {
                psReport = db.getConnection().prepareStatement(sqlReport);
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                rsReport = psReport.executeQuery();
                log.debug("executed query");
                PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("report.txt"), "utf-8")));
                //TODO MAKE REPORT

                while (rsReport != null && rsReport.next()){
                    log.debug(rsReport.getString("file_size"));
                    writer.write(rsReport.getString("file_path") + " | " + rsReport.getString("file_size") + "\n");

                }
                writer.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }*/

        try{
            db = Database.getInstance();
            try {
                db.openConnection();
                log.debug("Opened connection");
            } catch (ClassNotFoundException | SQLException e) {
                log.fatal("Failed to open database connection! Check config file!", e);
            }
            String sqlReport = "SELECT file_hash FROM signatures;";
            String sqlCount = "SELECT count(*) FROM files;";
            String sqlNonUnique = "SELECT COUNT(DISTINCT file_hash) FROM files;";
            String sqlHashCount = "SELECT count(*) FROM signatures;";
            String sqlNumRows = "SELECT count(*) from nonUnique;";
            PreparedStatement psCount = null;
            PreparedStatement psHashCount = null;
            PreparedStatement psReport = null;
            PreparedStatement psNonUnique = null;
            PreparedStatement psNumRows = null;
            ResultSet rsNumRows = null;
            ResultSet rsReport = null;
            ResultSet rsCount = null;
            ResultSet rsHashCount = null;
            ResultSet rsNonUnique = null;
            try {
                psReport = db.getConnection().prepareStatement(sqlReport);
                psCount = db.getConnection().prepareStatement(sqlCount);
                psHashCount = db.getConnection().prepareStatement(sqlHashCount);
                psNonUnique = db.getConnection().prepareStatement(sqlNonUnique);
                psNumRows = db.getConnection().prepareStatement(sqlNumRows);
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                rsCount = psCount.executeQuery();
                rsCount.next();
                log.debug(rsCount.getInt(1));
                rsHashCount = psHashCount.executeQuery();
                rsHashCount.next();
                log.debug(rsHashCount.getInt(1));
                log.debug("num unique");
                rsNonUnique = psNonUnique.executeQuery();
                rsNonUnique.next();
                log.debug(rsNonUnique.getInt(1));
                log.debug("num rows");
                rsNumRows = psNumRows.executeQuery();
                rsNumRows.next();
                log.debug(rsNumRows.getInt(1));
                rsReport = psReport.executeQuery();
                log.debug("executed query");
                PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("report.txt"), "utf-8")));
                //TODO MAKE REPORT

                while (rsReport != null && rsReport.next()){
                    //log.debug(rsReport.getString("file_hash"));
                    writer.write(rsReport.getString("file_hash") + "\n");

                }
                writer.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        }
}

