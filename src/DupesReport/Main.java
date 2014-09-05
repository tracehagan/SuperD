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



        try{
            db = Database.getInstance();
            try {
                db.openConnection();
                log.debug("Opened connection");
            } catch (ClassNotFoundException | SQLException e) {
                log.fatal("Failed to open database connection! Check config file!", e);
            }
            String sqlHashes = "SELECT file_hash FROM signatures;";
            String sqlDuplicates = "SELECT file_path FROM nonUnique WHERE file_hash = ?";
            String sqlHashCount = "SELECT count(*) FROM signatures;";
            String sqlFileCount = "SELECT count(*) FROM files;";
            String sqlNonUniqueCount = "SELECT count(*) FROM nonUnique;";
            String sqlSignaturesCount = "SELECT count(*) FROM signatures;";

            PreparedStatement psHashCount = null;
            PreparedStatement psHashes = null;
            PreparedStatement psDuplicates = null;
            PreparedStatement psFileCount = null;
            PreparedStatement psNonUniqueCount = null;
            PreparedStatement psSignaturesCount = null;

            ResultSet rsHashes = null;
            ResultSet rsHashCount = null;
            ResultSet rsDuplicates = null;
            ResultSet rsFileCount = null;
            ResultSet rsNonUniqueCount = null;
            ResultSet rsSignaturesCount = null;

            try {
                psHashes = db.getConnection().prepareStatement(sqlHashes);
                psDuplicates = db.getConnection().prepareStatement(sqlDuplicates);
                psHashCount = db.getConnection().prepareStatement(sqlHashCount);
                psFileCount = db.getConnection().prepareStatement(sqlFileCount);
                psNonUniqueCount = db.getConnection().prepareStatement(sqlNonUniqueCount);
                psSignaturesCount = db.getConnection().prepareStatement(sqlSignaturesCount);

            } catch (Exception e){
                e.printStackTrace();
            }
            int numSig=0;
            try {
                rsHashCount = psHashCount.executeQuery();
                rsHashCount.next();
                numSig = rsHashCount.getInt(1);

                rsFileCount = psFileCount.executeQuery();
                rsFileCount.next();
                log.debug("Num rows in files table: " + rsFileCount.getInt(1));

                rsNonUniqueCount = psNonUniqueCount.executeQuery();
                rsNonUniqueCount.next();
                log.debug("Num rows in nonUnique table: " + rsNonUniqueCount.getInt(1));

                rsSignaturesCount = psSignaturesCount.executeQuery();
                rsSignaturesCount.next();
                log.debug("Num rows in Signatures table: " + rsSignaturesCount.getInt(1));

                rsHashes = psHashes.executeQuery();
                log.debug("executed query");

                PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("report.txt"), "utf-8")));
                //TODO MAKE REPORT

                while (rsHashes != null && rsHashes.next()){
                    //log.debug(rsReport.getString("file_hash"));
                    psDuplicates.setString(1, rsHashes.getString(1));
                    rsDuplicates = psDuplicates.executeQuery();
                    rsDuplicates.next();
                    writer.write(rsDuplicates.getString("file_path"));
                    while (rsDuplicates != null && rsDuplicates.next()){
                        writer.write("|~|~|" + rsDuplicates.getString("file_path"));
                    }
                    writer.write("\n");
                    psDuplicates.clearParameters();

                    rsDuplicates.close();
                    rsDuplicates = null;

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

