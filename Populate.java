package assign3;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import org.json.*;

import assign3.PopulateReview.FieldType;

//import assign3.PopulateReview.FieldType;

public class Populate {

	// Database parameters
	private Connection con = null;
	private Statement st = null;
	
	// Reading for populating Review Table
		private static final String jsonReviewFile = "C:/Users/vidu/Downloads/YelpDataset/YelpDataset-CptS451/yelp_review.json";
		private static final String[] allowedReviewFields = { "votes", "user_id", "review_id", "stars", "date", "text",
				"business_id" };
		private static final String[] allowedReviewVoteFields = { "funny", "useful", "cool" };

		enum FieldType {
			OBJECT, DATE, STR, INTEGER, ARRAY, FLOAT
		}

		private static final FieldType[] ReviewFieldTypes = { FieldType.OBJECT, FieldType.STR, FieldType.STR,
				FieldType.INTEGER, FieldType.DATE, FieldType.STR, FieldType.STR };
		private static final FieldType[] ReviewVotesFieldTypes = { FieldType.INTEGER, FieldType.INTEGER,
				FieldType.INTEGER };
		
		// Reading for populating checkedIn Table
		private static final String jsonCheckInFile = "C:/Users/vidu/Downloads/YelpDataset/YelpDataset-CptS451/yelp_checkin.json";
		private static final String[] allowedCheckInFields = { "checkin_info", "business_id" };
		private static final String[] allowedCheckInDayTimeFields = 
			{ "0-0", "1-0", "2-0", "3-0", "4-0", "5-0", "6-0", "7-0", "8-0", "9-0", "10-0","11-0", "12-0","13-0","14-0", "15-0", "16-0", "17-0", "18-0", "19-0", "20-0", "21-0", "22-0", "23-0",
			  "0-1", "1-1", "2-1", "3-1", "4-1", "5-1", "6-1", "7-1", "8-1", "9-1", "10-1","11-1", "12-1","13-1","14-1", "15-1", "16-1", "17-1", "18-1", "19-1", "20-1", "21-1", "22-1", "23-1",
			  "0-2", "1-2", "2-2", "3-2", "4-2", "5-2", "6-2", "7-2", "8-2", "9-2", "10-2","11-2", "12-2","13-2","14-2", "15-2", "16-2", "17-2", "18-2", "19-2", "20-2", "21-2", "22-2", "23-2",
			  "0-3", "1-3", "2-3", "3-3", "4-3", "5-3", "6-3", "7-3", "8-3", "9-3", "10-3","11-3", "12-3","13-3","14-3", "15-3", "16-3", "17-3", "18-3", "19-3", "20-3", "21-3", "22-3", "23-3",
			  "0-4", "1-4", "2-4", "3-4", "4-4", "5-4", "6-4", "7-4", "8-4", "9-4", "10-4","11-4", "12-4","13-4","14-4", "15-4", "16-4", "17-4", "18-4", "19-4", "20-4", "21-4", "22-4", "23-4",
			  "0-5", "1-5", "2-5", "3-5", "4-5", "5-5", "6-5", "7-5", "8-5", "9-5", "10-5","11-5", "12-5","13-5","14-5", "15-5", "16-5", "17-5", "18-5", "19-5", "20-5", "21-5", "22-5", "23-5",
			  "0-6", "1-6", "2-6", "3-6", "4-6", "5-6", "6-6", "7-6", "8-6", "9-6", "10-6","11-6", "12-6","13-6","14-6", "15-6", "16-6", "17-6", "18-6", "19-6", "20-6", "21-6", "22-6", "23-6",
			};

		

		private static final FieldType[] CheckInFieldTypes = { FieldType.OBJECT, FieldType.STR };
		
		
		// Reading for populating Business Table
		private static final String jsonBusinessFile = "C:/Users/vidu/Downloads/YelpDataset/YelpDataset-CptS451/yelp_business.json";
		private static final String[] allowedBusinessCategoryFields = { "business_id", "categories" };
		

		

		private static final FieldType[] businessCategoryFieldTypes = { FieldType.STR, FieldType.ARRAY };
		
		
		// Reading for populating Business Table
		
		private static final String[] allowedBusinessFields = { "business_id", "full_address", "city", "state" , "review_count" , "name",
				"stars" };
		

		private static final FieldType[] businessFieldTypes = { FieldType.STR, FieldType.STR, FieldType.STR, FieldType.STR,
				FieldType.INTEGER, FieldType.STR, FieldType.FLOAT };
		
		private static final Set<String> catSet = new HashSet<>();
		
		
		// Creating set of given 28 Main Categories
		public void fillCatSet(){
			catSet.add("Active Life");catSet.add("Arts & Entertainment"); catSet.add("Automotive"); 
			catSet.add("Car Rental");catSet.add("Cafes"); catSet.add("Beauty & Spas"); catSet.add("Convenience Stores"); 
			catSet.add("Dentists");catSet.add("Doctors"); catSet.add("Drugstores"); catSet.add("Department Stores"); 
			catSet.add("Education"); catSet.add("Event Planning & Services"); catSet.add("Flowers & Gifts"); 
			catSet.add("Food"); catSet.add("Health & Medical");catSet.add("Home Services"); catSet.add("Home & Garden"); 
			catSet.add("Hospitals"); catSet.add("Hotels & Travel");catSet.add("Hardware Stores"); catSet.add("Grocery"); 
			catSet.add("Medical Centers"); catSet.add("Nurseries & Gardening");catSet.add("Nightlife"); 
			catSet.add("Restaurants"); catSet.add("Shopping"); catSet.add("Transportation");
			
	 }
		
		

	// Reading for populating Yelp_User Table
	private static final String jsonUserFile = "C:/Users/vidu/Downloads/YelpDataset/YelpDataset-CptS451/yelp_user.json";
	private static final String[] allowedUserFields = { "yelping_since", "review_count", "name", "user_id",
			"average_stars" };
	private static final String[] allowedUserFriendFields = { "user_id", "friends" };

	

	private static final FieldType[] userFieldTypes = { FieldType.DATE, FieldType.INTEGER, FieldType.STR, FieldType.STR,
			FieldType.FLOAT };
	private static final FieldType[] userFriendFieldTypes = { FieldType.STR, FieldType.ARRAY };

	// Connect to Oracle database, and delete all the old data from the table
	public void connectDB() {
		try {

			System.out.println("Connecting to Database.....");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "SCOTT", "tiger");
			st = con.createStatement();
			System.out.println("Connected to Database successfully.");
			
			System.out.println("Deleting Old tuples from all tables...");
			
			st.executeUpdate("Truncate table Review");
			System.out.println("Review tuples deleted!");
			
			st.executeUpdate("Truncate table CheckedIn");
			System.out.println("CheckedIn tuples deleted!");
			
			
			st.executeUpdate("Truncate table BSubCategory");
			System.out.println("BSubCategory tuples deleted!");
			
			st.executeUpdate("Truncate table BCategory");
			System.out.println("BCategory tuples deleted!");
			
			st.executeUpdate("Delete from Business");
			System.out.println("Business tuples deleted!");
			
			
			st.executeUpdate("Delete from User_friends");
			System.out.println("User_Friends tuples deleted!");
			
			st.executeUpdate("Delete from Yelp_User");
			System.out.println("Yelp_User tuples deleted!");
			
			System.out.println("All tuples deleted!");
			// con.close();

		} catch (SQLException e) {
			System.err.println("Error while connecting and deleting tables.");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	// Populating Business Table in DB
		public void printBusinessFields(File file) throws JSONException, IOException, SQLException {
			BufferedReader fileReader = new BufferedReader(new FileReader(file));
			try {
				String line = null;
				int count = 0;
				while ((line = fileReader.readLine()) != null) {
					//if (++count > 5)
						//break;
					JSONObject jsonObject = new JSONObject(line);
					Map<String, String> keyVals = new HashMap<String, String>();
					for (int i = 0; i < allowedBusinessFields.length; i++) {
						String key = allowedBusinessFields[i];
						FieldType fieldType = businessFieldTypes[i];
						if (jsonObject.has(key)) {
							String val;
							if (fieldType != FieldType.ARRAY) {
								val = jsonObject.get(key).toString();
								if (fieldType == FieldType.STR)
									val = quoteStr(val);
								//if (fieldType == FieldType.DATE)
									//val = "TO_DATE(" + "\'" + val + "\'" + ",\'YYYY-MM\')";
								keyVals.put(key, val);

							}
						}
					}
					createAndInsertBusinessStatement("Business", keyVals);

				}
			} finally {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// con.close();
		}

		public void createAndInsertBusinessStatement(String table, Map<String, String> keyVals) throws SQLException {
			StringBuilder insertStmt = new StringBuilder();
			StringBuilder columnBuffer = new StringBuilder();
			StringBuilder valueBuffer = new StringBuilder();

			insertStmt.append("Insert into " + table + "(");
			valueBuffer.append("values (");

			boolean first = true;
			for (String k : keyVals.keySet()) {
				if (!first) {
					columnBuffer.append(" , ");
					valueBuffer.append(" , ");

				} else {
					first = false;
				}
				columnBuffer.append(k);
				valueBuffer.append(keyVals.get(k));
			}
			columnBuffer.append(')');
			valueBuffer.append(')');
			insertStmt.append(columnBuffer);
			insertStmt.append(" ");
			insertStmt.append(valueBuffer);
			String insertStr = insertStmt.toString();
			try {
				st.executeUpdate(insertStr);
			} catch (Exception e) {
				System.err.println("Failed: " + insertStr);
				System.err.println(e);
			}

		}
	
	
	// For populating BCategory Table in DB	
		public void printBusinessCategoryFields(File file) throws JSONException, IOException, SQLException {
			BufferedReader fileReader = new BufferedReader(new FileReader(file));
			try {
				String line = null;
				String bid = null;
				int count = 0;
				while ((line = fileReader.readLine()) != null) {
					//if (++count > 3)
						//break;
					
					JSONObject jsonObject = new JSONObject(line);
					// Map<String, String> keyVals = new HashMap<String, String>();
					for (int i = 0; i < allowedBusinessCategoryFields.length; i++) {
						String key = allowedBusinessCategoryFields[i];
						FieldType fieldType = businessCategoryFieldTypes[i];
						if (jsonObject.has(key) && fieldType == FieldType.STR) {
							bid = jsonObject.get("business_id").toString();
						}
						// System.out.println(fieldType + key +
						// jsonObject.has(key));
						if (jsonObject.has(key) && fieldType == FieldType.ARRAY) {
							JSONArray array = jsonObject.getJSONArray(key);
							createBusinessCategoryInsertStmt(bid, array);
						}
					}
				}
			} finally {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// con.close();
		}

		// For populating BCategory Table in DB; Preparing insert statement.
		public void createBusinessCategoryInsertStmt(String business_id, JSONArray array) {
			    Set<String> mainCategorySet  = new HashSet<>();
			    Set<String> subCategorySet  = new HashSet<>();
				for (int i = 0; i < array.length(); i++) {
					String val = array.get(i).toString();
					if(catSet.contains(val)) {
						mainCategorySet.add(val);
					} else {
						subCategorySet.add(val);
					}
				}
				insertCategories(business_id, mainCategorySet, subCategorySet);
		}

	   private void insertCategories(String businessId, Set<String> mainCategorySet, Set<String> subCategorySet) {
		   for(String mainCategory: mainCategorySet) {
			  insertValues(st, "BCategory", businessId, mainCategory);
			  for(String subCategory: subCategorySet) {
				  insertValues(st,"BSubCategory" , businessId, mainCategory, subCategory);
			  }
		   }
	   }

	  //copies from SqlUtil.java
	   
	   public static void insertValues(Statement statement, String tableName, String ...values) {
			String insertStmt = createInsertStatementWithValues(tableName, values);
			try {
				statement.executeUpdate(insertStmt);
			} catch (Exception e) {
				System.err.println("Failed: " + insertStmt);
				System.err.println(e);
			}
		}
		
		public static String createInsertStatementWithValues(String tableName, String ... values) {
			StringBuilder insertStmtBuilder = new StringBuilder();
			insertStmtBuilder.append("Insert into ");
			insertStmtBuilder.append(tableName);
			insertStmtBuilder.append(" values(");
			
	       boolean first = true;
			for(String val: values) {
				if(first) {
					first = false;
				} else {
					insertStmtBuilder.append(", ");
				}
				insertStmtBuilder.append(quoteStr(val));
			}
			insertStmtBuilder.append(")");
			return insertStmtBuilder.toString();
		}
		
		public static String quoteStr(String strValue) {
			strValue = strValue.replaceAll("´", "'");
			return "'" + strValue.replaceAll("'", "''") + "'";
		}
	
	private static class ReviewVoteInfo {
		public final int hour, day;
		public final int checkin;
		public ReviewVoteInfo(int hour, int day, int checkin) {
			this.hour = hour;
			this.day = day;
			this.checkin = checkin;
		}
		
	}

	// Populating checkedIn Table in DB
	public void printCheckInFields(File file) throws JSONException, IOException, SQLException {
		BufferedReader fileReader = new BufferedReader(new FileReader(file));
		try {
			String line = null;
			int count = 0;
			while ((line = fileReader.readLine()) != null) {
				//if (++count > 3)
				//	break;
				JSONObject jsonObject = new JSONObject(line);
				
				String bId = jsonObject.get("business_id").toString();
				JSONObject dayTimeJsonObject = new JSONObject(jsonObject);
				dayTimeJsonObject = (JSONObject) jsonObject.get("checkin_info");
				for (int j = 0; j < allowedCheckInDayTimeFields.length; j++) {
					String voteKey = allowedCheckInDayTimeFields[j];

					if (dayTimeJsonObject.has(voteKey)) {
						String voteVal;
						String [] valArray = voteKey.split("-");
						int hour = Integer.parseInt(valArray[0]);
						int day = Integer.parseInt(valArray[1]);
						voteVal = dayTimeJsonObject.get(voteKey).toString();
						int checkin = Integer.parseInt(voteVal);
						System.out.println("key: " + voteKey );
						System.out.println("val: "+ voteVal);
						System.out.println("hour,day: "+ hour + ", " + day);
						
						String insertStmt = "Insert into CheckedIn values (" + "'" + bId +"'," + day +", " + hour + ", " + checkin + " )";
						st.addBatch(insertStmt);
						//System.out.println(insertStmt);
						try {
							//st.executeUpdate(insertStmt);
						} catch (Exception e) {
							System.err.println("Failed: " + insertStmt);
							System.err.println(e);
						}
						
					}
					st.executeBatch();
				}
				
				

			}
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// con.close();
	}
	
	/*
	// Populating Review Table in DB
		public void printReviewFields(File file) throws JSONException, IOException, SQLException {
			BufferedReader fileReader = new BufferedReader(new FileReader(file));
			try {
				String line = null;
				int count = 0;
				while ((line = fileReader.readLine()) != null) {
					//if (++count > 6)
						//break;
					JSONObject jsonObject = new JSONObject(line);
					Map<String, String> keyVals = new HashMap<String, String>();
					Map<String, String> voteVals = new HashMap<String, String>();
					for (int i = 0; i < allowedReviewFields.length; i++) {
						String key = allowedReviewFields[i];
						FieldType fieldType = ReviewFieldTypes[i];
						if (jsonObject.has(key)) {
							String val;
							if (fieldType == FieldType.OBJECT) {

								JSONObject voteJsonObject = new JSONObject(jsonObject);
								voteJsonObject = (JSONObject) jsonObject.get(key);

								for (int j = 0; j < allowedReviewVoteFields.length; j++) {
									String voteKey = allowedReviewVoteFields[j];

									if (voteJsonObject.has(voteKey)) {
										String voteVal;
										voteVal = voteJsonObject.get(voteKey).toString();
										voteVals.put(voteKey, voteVal);
									}
								}
							}
							if (fieldType != FieldType.ARRAY && fieldType != FieldType.OBJECT) {
								val = jsonObject.get(key).toString();
								if (fieldType == FieldType.STR)
									val = quoteStr(val);
								if (fieldType == FieldType.DATE)
									val = "TO_DATE(" + "\'" + val + "\'" + ",\'YYYY-MM-DD\')";
								if (key == "date")
									key = "rdate";

								keyVals.put(key, val);

							}
						}
					}
					createAndInsertReviewStatement("Review", keyVals, voteVals);

				}
			} finally {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// con.close();
		}

		public void createAndInsertReviewStatement(String table, Map<String, String> keyVals, Map<String, String> voteVals)
				throws SQLException {
			StringBuilder insertStmt = new StringBuilder();
			StringBuilder columnBuffer = new StringBuilder();
			StringBuilder valueBuffer = new StringBuilder();

			insertStmt.append("Insert into " + table + "(");
			valueBuffer.append("values (");
			// For map voteVals Map
			boolean first = true;
			for (String v : voteVals.keySet()) {
				if (!first) {
					columnBuffer.append(" , ");
					valueBuffer.append(" , ");

				} else {
					first = false;
				}
				columnBuffer.append(v);
				valueBuffer.append(voteVals.get(v));
			}

			// For keyVals Map

			for (String k : keyVals.keySet()) {

				columnBuffer.append(" , ");
				valueBuffer.append(" , ");

				columnBuffer.append(k);
				valueBuffer.append(keyVals.get(k));
			}
			columnBuffer.append(')');
			valueBuffer.append(')');
			insertStmt.append(columnBuffer);
			insertStmt.append(" ");
			insertStmt.append(valueBuffer);
			String insertStr = insertStmt.toString();
			try {
				st.executeUpdate(insertStr);
			} catch (Exception e) {
				System.err.println("Failed: " + insertStr);
				System.err.println(e);
			}

		}*/
	
	// Populating Review Table in DB
		public void printReviewFields(File file) throws JSONException, IOException, SQLException {
			BufferedReader fileReader = new BufferedReader(new FileReader(file));
			try {
				String line = null;
				int count = 0;
				while ((line = fileReader.readLine()) != null) {
					/*++count;
					// if (++count < 399900) continue;
					if(count%10000 == 0) {
						System.err.println(count);
					}*/
					JSONObject jsonObject = new JSONObject(line);
					Map<String, String> keyVals = new HashMap<String, String>();
					Map<String, String> voteVals = new HashMap<String, String>();
					for (int i = 0; i < allowedReviewFields.length; i++) {
						String key = allowedReviewFields[i];
						FieldType fieldType = ReviewFieldTypes[i];
						if (jsonObject.has(key)) {
							String val;
							if (fieldType == FieldType.OBJECT) {

								JSONObject voteJsonObject = new JSONObject(jsonObject);
								voteJsonObject = (JSONObject) jsonObject.get(key);

								for (int j = 0; j < allowedReviewVoteFields.length; j++) {
									String voteKey = allowedReviewVoteFields[j];

									if (voteJsonObject.has(voteKey)) {
										String voteVal;
										voteVal = voteJsonObject.get(voteKey).toString();
										voteVals.put(voteKey, voteVal);
									}
								}
							}
							if (fieldType != FieldType.ARRAY && fieldType != FieldType.OBJECT) {

								val = jsonObject.get(key).toString();
								if (fieldType == FieldType.STR) {
									if (key == "text") {
										val = val.substring(0, Math.min(val.length(), 20));
									}
									val = quoteStr(val);
								}

								if (fieldType == FieldType.DATE)
									val = "TO_DATE(" + "\'" + val + "\'" + ",\'YYYY-MM-DD\')";
								if (key == "date")
									key = "rdate";

								keyVals.put(key, val);

							}
						}
					}
					createAndInsertReviewStatement("Review", keyVals, voteVals);
					/*if(count%200000 == 0) {
						//st.executeBatch();
					}*/
				}
				st.executeBatch();

			} finally {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// con.close();
		}

		public void createAndInsertReviewStatement(String table, Map<String, String> keyVals, Map<String, String> voteVals)
				throws SQLException {
			StringBuilder insertStmt = new StringBuilder();
			StringBuilder columnBuffer = new StringBuilder();
			StringBuilder valueBuffer = new StringBuilder();

			insertStmt.append("Insert into " + table + "(");
			valueBuffer.append("values (");
			// For map voteVals Map
			boolean first = true;
			for (String v : voteVals.keySet()) {
				if (!first) {
					columnBuffer.append(" , ");
					valueBuffer.append(" , ");

				} else {
					first = false;
				}
				columnBuffer.append(v);
				valueBuffer.append(voteVals.get(v));
			}

			// For keyVals Map

			for (String k : keyVals.keySet()) {

				columnBuffer.append(" , ");
				valueBuffer.append(" , ");

				columnBuffer.append(k);
				valueBuffer.append(keyVals.get(k));
			}
			columnBuffer.append(')');
			valueBuffer.append(')');
			insertStmt.append(columnBuffer);
			insertStmt.append(" ");
			insertStmt.append(valueBuffer);
			String insertStr = insertStmt.toString();
			try {
				// st.executeUpdate(insertStr);
				st.addBatch(insertStr);
			} catch (Exception e) {
				System.err.println("Failed: " + insertStr);
				int index  = insertStr.indexOf("60");
				if(index > 0) {
					String s = insertStr.substring(index, index + 5);
					int ch = s.charAt(2);
					System.err.println(s + " " + ch);
				}
				System.err.println(e);
			}
			// st.executeBatch();

		}

	// For populating User_Friends Table in DB
	public void printUserFriendFields(File file) throws JSONException, IOException, SQLException {
		BufferedReader fileReader = new BufferedReader(new FileReader(file));
		try {
			String line = null;
			String uid = null;
			int count = 0;
			while ((line = fileReader.readLine()) != null) {
				//if (++count > 2)
					//break;
				
				JSONObject jsonObject = new JSONObject(line);
				
				for (int i = 0; i < allowedUserFriendFields.length; i++) {
					String key = allowedUserFriendFields[i];
					FieldType fieldType = userFriendFieldTypes[i];
					if (jsonObject.has(key) && fieldType == FieldType.STR) {
						uid = jsonObject.get("user_id").toString();
					}
					
					if (jsonObject.has(key) && fieldType == FieldType.ARRAY) {
						JSONArray array = jsonObject.getJSONArray(key);
						createUserFriendInsertStmt(uid, array);
					}
				}
			}
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// con.close();
	}

	// For populating User_Friends Table in DB; Preparing insert statement.
	public void createUserFriendInsertStmt(String user_id, JSONArray array) {

		String insertStmt = null;
		if (array.length() == 0) {
			insertStmt = "Insert into User_Friends values(" + "\'" + user_id + "\',null )";

			try {
				st.executeUpdate(insertStmt);
			} catch (Exception e) {
				System.err.println("Failed: " + insertStmt);
				System.err.println(e);
			}
		}

		else {
			for (int i = 0; i < array.length(); i++) {
				insertStmt = "Insert into User_Friends values(" + "\'" + user_id + "\'," + "\'" + array.get(i) + "\'"
						+ ")";

				try {
					st.addBatch(insertStmt);
					//st.executeUpdate(insertStmt);
					
				} catch (Exception e) {
					System.err.println("Failed: " + insertStmt);
					System.err.println(e);
				}

			}
			//
			try {
				st.executeBatch();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //
			
		}

	}

	public void printUserFields(File file) throws JSONException, IOException, SQLException {
		BufferedReader fileReader = new BufferedReader(new FileReader(file));
		try {
			String line = null;
			int count = 0;
			while ((line = fileReader.readLine()) != null) {
				//if (++count > 5)
					//break;
				JSONObject jsonObject = new JSONObject(line);
				Map<String, String> keyVals = new HashMap<String, String>();
				for (int i = 0; i < allowedUserFields.length; i++) {
					String key = allowedUserFields[i];
					FieldType fieldType = userFieldTypes[i];
					if (jsonObject.has(key)) {
						String val;
						if (fieldType != FieldType.ARRAY) {
							val = jsonObject.get(key).toString();
							if (fieldType == FieldType.STR)
								val = quoteStr(val);
							if (fieldType == FieldType.DATE)
								val = "TO_DATE(" + "\'" + val + "\'" + ",\'YYYY-MM\')";
							keyVals.put(key, val);

						}
					}
				}
				createAndInsertYelpUserStatement("Yelp_User", keyVals);

			}
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// con.close();
	}

	public void createAndInsertYelpUserStatement(String table, Map<String, String> keyVals) throws SQLException {
		StringBuilder insertStmt = new StringBuilder();
		StringBuilder columnBuffer = new StringBuilder();
		StringBuilder valueBuffer = new StringBuilder();

		insertStmt.append("Insert into " + table + "(");
		valueBuffer.append("values (");

		boolean first = true;
		for (String k : keyVals.keySet()) {
			if (!first) {
				columnBuffer.append(" , ");
				valueBuffer.append(" , ");

			} else {
				first = false;
			}
			columnBuffer.append(k);
			valueBuffer.append(keyVals.get(k));
		}
		columnBuffer.append(')');
		valueBuffer.append(')');
		insertStmt.append(columnBuffer);
		insertStmt.append(" ");
		insertStmt.append(valueBuffer);
		String insertStr = insertStmt.toString();
		try {
			//st.addBatch(insertStr);
			st.executeUpdate(insertStr);
			//st.executeBatch();
		} catch (Exception e) {
			System.err.println("Failed: " + insertStr);
			System.err.println(e);
		}
		//st.executeBatch();

	}

	
	public static void main(String[] args) {
		Populate obj = new Populate();
		obj.connectDB();
		obj.fillCatSet();

		try {
			System.out.println("Populating Yelp_User Table...");
			obj.printUserFields(new File(jsonUserFile));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		try {
			System.out.println("Populating User_Friends Table...");
			obj.printUserFriendFields(new File(jsonUserFile));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		
		try {
			System.out.println("Populating Business Table...");
			obj.printBusinessFields(new File(jsonBusinessFile));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		try {
			System.out.println("Populating BCategory Table...");
			obj.printBusinessCategoryFields(new File(jsonBusinessFile));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		try {
			System.out.println("Populating Review Table...");
			obj.printReviewFields(new File(jsonReviewFile));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		try {
			System.out.println("Populating CheckedIn Table...");
			obj.printCheckInFields(new File(jsonCheckInFile));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		System.out.println("Database populated!");

	}

}

