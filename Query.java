import java.util.ArrayList;
import java.sql.*;

public class Query {
    
    final int NUM_TYPES = 5;
    int qType;
    String question;
    ArrayList<String> args;
    String[] options;
    boolean valid;
    
    static String[] answerChars = {"A","B","C","D"};
    
    public Query(int type) {
        qType = type % NUM_TYPES;
        args = args;
        options = new String[5];
        createQuestion();
    }
    
    
    public void createQuestion() {
        int rand = 0;
        String object;
        switch(qType) {
                
        case 0:
            // Instance name of thing that has object orbitting it (e.g. 'Sun')
            String orbited;
            // Class of object above
            String oType;
                
            rand = (int)(Math.random() * 2);
            if(rand == 0) {
                oType = "star";
                orbited = randObject(oType);
            } else {
                oType = "planet";
                orbited = randObject(oType);
            }
                
            // E.g. comet, asteroid, planet, star
            String oClass = randOrbiter(oType, orbited);
                
            question = "Name a " + oClass + " that orbits " + orbited + ".";
            String query = "SELECT * FROM " + oClass + " WHERE orbits LIKE '" + orbited + "';";

            ResultSet rs = execQuery(query);
                
            genAnswers("*", oClass, " WHERE orbits NOT LIKE '" + orbited + "'");
            try {
                if(rs.next()) {
                    options[3] = rs.getString("Name");
                    valid = true;
                
                } else {
                    valid = false;
                
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                
            rand = (int)(Math.random() * 3);
            final String temp = options[rand];
            options[rand] = options[3];
            options[3] = temp;
            options[4] = answerChars[rand];
            break;
                
        case 1:
            ArrayList<String> tableNames = getTableNames();
            String table = "";
            do {
                    
                rand = (int)(Math.random() * tableNames.size());
                table = tableNames.get(rand);
            } while(table.toLowerCase().equals("galaxy") ||
                    table.toLowerCase().equals("constellation") ||
                    table.toLowerCase().equals("made_of"));

            question = "Which " + table + " is the most massive?";
            query = "SELECT name, mass FROM " + table + ";";
           
            rs = execQuery(query);
            
            double[] masses = new double[4];
            double maxMass = 0;
            int index = 0;
                
            try {
                rs.next();
                for(int i = 0; (i < 4); i++, rs.next()) {
                    options[i] = rs.getString("name");
                    masses[i] = rs.getDouble("mass");
                        
                    if(masses[i] > maxMass) {
                        maxMass = masses[i];
                        index = i;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                
            if(options[index] != null) {
                valid = true;
            }
                
            options[4] = answerChars[index];
                
            break;
                
        default:
            break;
        }
    }
    
    /**
       Generate a random block of answers
    */
    public void genAnswers(String c_name, String oClass, String where_clause) {
        String query = "SELECT " + c_name +
            " FROM " + oClass + where_clause + ";"; // Moved the " WHERE " up when called.
       
        ResultSet rs = execQuery(query);
        try {
            for(int i = 0; i < 4; i++) {
                if(rs.next()) {
                    options[i] = rs.getString("name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
    
    /**
       Get a random object from table
    */
    public String randObject(String table) {
        
        // Find the number of rows in the upcoming result set.
        String countQuery = "SELECT COUNT(*) FROM " + table;
        int rows = -1;
        
        try {
            ResultSet rs = execQuery(countQuery);
            rows = Integer.parseInt(rs.getString("COUNT(*)"));
            rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        // Prepare and execute query
        String query = ("SELECT * FROM " + table);
    	ResultSet rs = execQuery(query);
    	
    	String randName = "";
    	
        // Get object name from random row in result set
    	int rand = (int)(Math.random() * rows);
        
    	try {
            for(int i = 0; (i < rand && rs.next()); i++) {
                randName = rs.getString("Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    	return randName;
    }
    
    /**
       Get a random object that orbits a specific type of planetary body
    */
    public String randOrbiter(String object, String name) {
        
        if(object.toLowerCase().equals("planet")) {
            
            return "moon";
            
        } else {
            int rand = 0;
            if(name.toLowerCase().equals("sun")) {
                rand = (int)(Math.random() * 3);
            } else {
                rand = (int)(Math.random() * 2);
            }
            switch(rand) {
            case 0:
                return "asteroid";
            case 1:
                return "comet";
            case 2:
                return "planet";
            default:
                return "";
            }
        }
    }
    
    /**
       @return Computes the result set for a query
    */
    public ResultSet execQuery(String query) {
        
        ResultSet result = null;
        try {
            Statement stmt = AstroQuiz.connection.createStatement();
            stmt.setQueryTimeout(10);
            
            try {
                result = stmt.executeQuery(query);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;    
    }
    
    /**
       Get list of names for all tables in database
    */
    public ArrayList<String> getTableNames() {
        ArrayList<String> tables = new ArrayList<String>();
        ResultSet rs = null;
        try {
            try {
                DatabaseMetaData md = AstroQuiz.connection.getMetaData();
                String[] types = {"TABLE"};
                rs = md.getTables(null, null, "%", types);
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
            }
            finally {
                if(rs != null) {
                    rs.close();
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tables;   
    }    
}
