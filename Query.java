{\rtf1\ansi\ansicpg1252\cocoartf1265\cocoasubrtf190
{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural

\f0\fs24 \cf0 package astronomy;\
\
import java.sql.Connection;\
import java.sql.ResultSet;\
import java.sql.Statement;\
\
public class Query \{\
\
	/******************		Member Variables	********************************/\
	private ResultSet result ;\
	private static String queryStmts[] = new String[] \{\
			/**\
			 * Fill in query statements\
			 */\
	\};\
	\
	public final static int NUM_QUERIES = queryStmts.length;\
	/***************************************************************************/\
\
	\
	\
	public Query() \{\
		result = null;\
	\}\
	\
	@SuppressWarnings("unused")\
	private ResultSet execQuery(Statement stmt, int qNum) \{\
		try \{\
			result = stmt.executeQuery(queryStmts[qNum]);\
		\} catch (Exception e) \{\
			System.out.println("Error: failed to execute query.\\n" + e.getMessage());\
		\}\
		\
		return result;\
	\}\
\}\
}