import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Query {
    
    /******************		Member Variables	********************************/
    private ResultSet result ;
    private static String queryStmts[] = new String[] {
        /*
         * Fill in query statements             \
         */
    };
	
    public final static int NUM_QUERIES = queryStmts.length;
    /***************************************************************************/
	
    public Query() {
        result = null;
    }
	@SuppressWarnings("unused")
	private ResultSet execQuery(Statement stmt, int qNum) {
            try {
                result = stmt.executeQuery(queryStmts[qNum]);
            } catch (Exception e) {
                System.out.println("Error: failed to execute query.\\n" + e.getMessage());
            }
            
            return result;
	}
}
