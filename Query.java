import java.util.ArrayList;
import java.sql.*;

public class Query {
    
    final int NUM_TYPES = 5;
    int qType;
    String question;
    ArrayList<String> args;
    String[] options;
    String answer;
    
    static String[] answerChars = {"A","B","C","D"};
    
    
    public Query(int type) {
        qType = type % NUM_TYPES;
        args = args;
        options = new String[5];
        createQuestion();
    }
    
    
    public void createQuestion() {
        String object;
        switch(qType) {
                
            case 0:
                // Instance name of thing that has object orbitting it (e.g. 'Sun')
                String orbited;
                // Class of object above
                String oType;
                
                int rand = (int)(Math.random() * 2);
                if(rand == 0) {
                    oType = "star";
                    orbited = randObject(oType);
                } else {
                    oType = "planet";
                    orbited = randObject(oType);
                }
                
                // E.g. comet, asteroid, planet, star
                String oClass = randOrbiter(oType, orbited);
                
                System.out.println("Object type: " + oType + ", Orbited by type: " + oClass + ", name: " + orbited);
                
                question = "Name a " + oClass + " that orbits " + orbited + ".";
                String query = "SELECT * FROM " + oClass + " WHERE orbits LIKE '" + orbited + "';";
                //System.out.println("Main Query: " + query);
                ResultSet rs = execQuery(query);
                
                genAnswers("*", oClass, " WHERE orbits <>'" + orbited + "'");
                try {
                    if(rs.next()) {
                        options[3] = rs.getString("Name");
                    }
                    answer = options[3];
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
//                for(String s : options) {
//                    System.out.print(s + ", ");
//                }
                
                int r = (int)(Math.random() * 3);
                final String temp = options[r];
                options[r] = options[3];
                options[3] = temp;
                options[4] = answerChars[r];
                break;
                
            case 1:
                System.out.println("\n\n\nCASE 1 Question!!!\n");
                ArrayList<String> tableNames = getTableNames();
                String table = "";
                do {
                    System.out.println("Invalid table: " + table);
                    rand = (int)(Math.random() * tableNames.size());
                    table = tableNames.get(rand);
                } while(table.toLowerCase().equals("galaxy") ||
                        table.toLowerCase().equals("constellation") ||
                        table.toLowerCase().equals("made_of"));
                
                
                System.out.println("Random table: " + table);
                question = "Which " + table + " is the most massive?";
                query = "SELECT name, mass FROM " + table + ";";
                System.out.println("Query: " + query);
                
                rs = execQuery(query);
            
                double[] masses = new double[4];
                double maxMass = 0;
                int index = 0;
                
                try {
                    rs.next();
                    for(int i = 0; (i < 4); i++, rs.next()) {
                        options[i] = rs.getString("name");
                        masses[i] = rs.getDouble("mass");
                        System.out.println("Name: " + options[i] + ", Mass: " + masses[i]);
                        if(masses[i] > maxMass) {
                            maxMass = masses[i];
                            index = i;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                answer = options[index];
                System.out.println("Answer set to: " + answer + "\n\n");
                options[4] = answerChars[index];
                
                break;
                //            case 2:
                //                object = randObject("Planet");
                //                question = "What is the mass of " + object + "?";
                //                break;
                //
                //            case 3:
                //                String star = randObject("Start");
                //                question = "Which " + star + " has the most planets orbiting it?";
                //                break;
                //            case 4:
                //                String obj1 = randObject("Star");
                //                String obj2 = randObject("Star");
                //                question = "True or false. " + obj1 + " is more massive than " + obj2 + "?";
                //                break;
                
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
        //System.out.println("Answer Query: " + query +"\n\n");
        ResultSet rs = execQuery(query);
        try {
            for(int i = 0; i < 4; i++) {
                if(rs.next()) {
                    options[i] = rs.getString("name");
                    //System.out.println("Options: " + options[i]);
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
        //System.out.println("Randname: " + randName);
    	return randName;
    }
    
    /**
     Get a random object that orbits a specific type of planetary body
     */
    public String randOrbiter(String object, String name) {
        //System.out.println("Orbiter = " + object);
        if(object.toLowerCase().equals("planet")) {
            
            return "moon";
            
        } else {
            int rand = 0;
            if(!name.toLowerCase().equals("sun")) {
                    rand = (int)(Math.random() * 3);
            } else {
                rand = (int)(Math.random() * 2);
            }
            if(rand != 1) {
                System.out.println("RAND TYPE = " + rand + "!!!!!");
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