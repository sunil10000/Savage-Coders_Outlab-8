import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DatabaseMetaData;

public interface FetchAndProcess {
    static String DB_NAME = "pokemon.db";
    static String TABLE_NAME = "pokemon";

    /* The map populated by fetch */
    // public Map<String, String> data = new HashMap<String, String>();
    
    // no default implementation
    void fetch(List<String> paths);

    // no default implementation
    Map<String, String> exposeData();
    
    /* Provides a default implementation that does a lot of work:
     * 1. Create the `TABLE_NAME` table if it does not exist (along with a uniqueness constraint).
     * 2. Inserts data into the table, safely. ensuring no duplication.
     * 3. Returns the Connection (useful for the FetchAndProcessNetwork* classes)
     */
    default List<String> process() {
    String sDriverName = "org.sqlite.JDBC";
    Class.forName(sDriverName);

    String sDbUrl = "jdbc:sqlite:" + DB_NAME;

    String sMakeTable = "CREATE TABLE [IF NOT EXISTS] pokemon (UNIQUE(pokemon_name, source_path))";

    Connection conn = DriverManager.getConnection(sDbUrl);
    try{
        Statment stmt = conn.createStatement();
        try{
            stmt.executeUpdate(sMakeTable);
            String sInsert = "INSERT INTO pokemon (pokemon_name, source_path) SELECT "
        }

        finally{
            try {
                stmt.close();
            }
            catch (Exception ignore){

            }
        }
    }

    finally{
        try{
            conn.close();
        }
        catch (Exception ignore){

        }
    }
	// you can use exposeData() here.
	return null;
    }
}
