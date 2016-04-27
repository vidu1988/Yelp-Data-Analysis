package assign3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import java.util.TimeZone;

import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePickerImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import assign3.PopulateBusinessCategory.FieldType;

public class Hw3 {
	private Connection con = null;
	private Statement st = null;
	private static final String CHECK_IN = "CheckIn";
	private static final String SUBCATEGORY = "Subcategory";
	private static final String MAIN_CATEGORIES = "Main Categories";
	private JFrame frame;
	private final List<String> mainCategoriesList;
	private final Map<String, Set<String>> categorySubcategoryMap;
	private final Map<String, JPanel> uiSectionToPanelMap = new HashMap<>();
	private final Map<String, JTable> uiSectionToTableMap = new HashMap<>();
	private final Map<String, JScrollPane> uiSectionToScrollPaneMap = new HashMap<>();
	private final Map<String, Component> componentMap = new HashMap<>();
	private final Map<String, Integer> daysMap = new HashMap<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Hw3 window = new Hw3();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Hw3() {
		mainCategoriesList = Collections.unmodifiableList(createCategoryList());
		categorySubcategoryMap = Collections.unmodifiableMap(createCategoryToSubCategoriesMap(mainCategoriesList));
		createDaysMap(daysMap);

		
		initialize();
		String panelTitle[] = new String[] { "Main Categories", "Subcategory", CHECK_IN, "Review", "Users", "Query",
				"Results" };
		String s = "Results";
		// setJTableEnabled(uiSectionToTableMap.get(s), false);
		for (String str : panelTitle) {
			setPanelEnabled(uiSectionToPanelMap.get(str), false);
		}
		for (String str : panelTitle) {
			createComponentMap(uiSectionToPanelMap.get(str));

		}
	}

	public void createDaysMap(Map<String, Integer> daysMap) {
		daysMap.put("Sun", 0);
		daysMap.put("Mon", 1);
		daysMap.put("Tue", 2);
		daysMap.put("Wed", 3);
		daysMap.put("Thurs", 4);
		daysMap.put("Fri", 5);
		daysMap.put("Sat", 6);

	}

	public JTable createResultTable(String sqlQuery) {
		JTable table = new JTable();
		try {

			// System.out.println("Connecting to Database.....");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "SCOTT", "tiger");
			st = con.createStatement();
			ResultSet rs = st.executeQuery(sqlQuery);
			// It creates and displays the table
			table = new JTable(buildTableModel(rs));

		} catch (SQLException e) {
			System.out.println("Cannot connect to Database");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}

	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

		ResultSetMetaData metaData = rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}

		// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}

		return new DefaultTableModel(data, columnNames);

	}

	// This method creates a list which has all the main categories
	private static ArrayList<String> createCategoryList() {
		ArrayList<String> mainCategoriesList = new ArrayList<>();
		mainCategoriesList.add("Active Life");
		mainCategoriesList.add("Arts & Entertainment");
		mainCategoriesList.add("Automotive");
		mainCategoriesList.add("Car Rental");
		mainCategoriesList.add("Cafes");
		mainCategoriesList.add("Beauty & Spas");
		mainCategoriesList.add("Convenience Stores");
		mainCategoriesList.add("Dentists");
		mainCategoriesList.add("Doctors");
		mainCategoriesList.add("Drugstores");
		mainCategoriesList.add("Department Stores");
		mainCategoriesList.add("Education");
		mainCategoriesList.add("Event Planning & Services");
		mainCategoriesList.add("Flowers & Gifts");
		mainCategoriesList.add("Food");
		mainCategoriesList.add("Health & Medical");
		mainCategoriesList.add("Home Services");
		mainCategoriesList.add("Home & Garden");
		mainCategoriesList.add("Hospitals");
		mainCategoriesList.add("Hotels & Travel");
		mainCategoriesList.add("Hardware Stores");
		mainCategoriesList.add("Grocery");
		mainCategoriesList.add("Medical Centers");
		mainCategoriesList.add("Nurseries & Gardening");
		mainCategoriesList.add("Nightlife");
		mainCategoriesList.add("Restaurants");
		mainCategoriesList.add("Shopping");
		mainCategoriesList.add("Transportation");
		return mainCategoriesList;

	}

	/*
	 * This method creates a map, which maps a main category with its all sub
	 * categories.
	 */
	private static Map<String, Set<String>> createCategoryToSubCategoriesMap(List<String> mainCategoryList) {
		Map<String, Set<String>> myMap = new HashMap<>();
		String jsonBusinessFile = "C:/Users/vidu/Downloads/YelpDataset/YelpDataset-CptS451/yelp_business.json";
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(jsonBusinessFile));

			String line = null;
			int count = 0;
			while ((line = fileReader.readLine()) != null) {
				// if (++count > 3)
				// break;

				JSONObject jsonObject = new JSONObject(line);
				if (jsonObject.has("categories")) {
					JSONArray array = jsonObject.getJSONArray("categories");
					updateMap(array, myMap, mainCategoryList);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return myMap;

	}

	private static void updateMap(JSONArray array, Map<String, Set<String>> catToSubCatMap,
			List<String> mainCategoriesList) {
		Set<String> subCategorySet = new HashSet<>();
		Set<String> categorySet = new HashSet<>();
		for (int i = 0; i < array.length(); i++) {
			String val = array.get(i).toString();
			if (mainCategoriesList.contains(val)) {
				categorySet.add(val);
			} else {
				subCategorySet.add(val);
			}
		}

		for (String s : categorySet) {
			if (!catToSubCatMap.containsKey(s)) {
				catToSubCatMap.put(s, new HashSet<>());
			}

			catToSubCatMap.get(s).addAll(subCategorySet);
		}
		// return catToSubCatMap;

	}

	/*
	 * This function creates a map which maps each JComponent (in each panel)
	 * with its name
	 */
	private void createComponentMap(JPanel panel) {
		Component[] components = panel.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof JLabel)
				continue;
			else {
				componentMap.put(components[i].getName(), components[i]);
			}
		}
	}

	public Component getComponentByName(String name) {
		if (componentMap.containsKey(name)) {
			return (Component) componentMap.get(name);
		} else
			return null;
	}

	private void createCheckBoxMainCategories(JPanel panel) {
		panel.setLayout(new GridLayout(0, 1));
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				onCategorySelected();
			}
		};

		for (String category : mainCategoriesList) {
			JCheckBox checkbox = new JCheckBox();
			checkbox.setText(category);
			checkbox.addActionListener(actionListener);
			panel.add(checkbox);
		}
	}

	private Set<String> findSelectedCheckBoxes(JPanel panel) {
		Set<String> selectedCheckBoxes = new HashSet<>();
		for (int j = 0; j < panel.getComponentCount(); j++) {
			Component component = panel.getComponent(j);
			if (component instanceof JCheckBox) {
				JCheckBox checkBox = (JCheckBox) component;
				if (checkBox.isSelected()) {
					selectedCheckBoxes.add(checkBox.getText());
				}
			}
		}
		return selectedCheckBoxes;
	}

	private void onCategorySelected() {
		TreeSet<String> sortedSubCategorySet = new TreeSet<String>();
		JPanel categoryPanel = uiSectionToPanelMap.get(MAIN_CATEGORIES);
		Set<String> selectedCategories = findSelectedCheckBoxes(categoryPanel);
		for (String category : selectedCategories) {
			Set<String> subCategories = categorySubcategoryMap.get(category);
			if (subCategories != null) {
				sortedSubCategorySet.addAll(subCategories);
			}
		}

		JPanel subCategoryPanel = uiSectionToPanelMap.get(SUBCATEGORY);
		// Make sure to clear the panel so there is no left over.
		subCategoryPanel.removeAll();
		subCategoryPanel.setLayout(new GridLayout(0, 1));

		for (String subCategory : sortedSubCategorySet) {
			JCheckBox checkbox = new JCheckBox();
			checkbox.setText(subCategory);
			subCategoryPanel.add(checkbox);
		}
		subCategoryPanel.repaint();
		subCategoryPanel.revalidate();
	}

	private void addToPanel(JPanel panel, GridBagConstraints gc, int x, int y, JComponent component) {
		gc.gridx = x;
		gc.gridy = y;
		panel.add(component, gc);
	}

	private void createCheckInPanel(JPanel panel) {

		panel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0.5;
		gc.insets = new Insets(10, 10, 10, 0);

		// gc.weighty = 1.0 / 7;
		// gc.fill = GridBagConstraints.BOTH;
		gc.fill = GridBagConstraints.HORIZONTAL;

		String operators[] = new String[] { "=, >, <", "=", ">", "<" };
		String days[] = new String[] { "Day", "Sun", "Mon", "Tue", "Wed", "Thurs", "Fri", "Sat" };
		addToPanel(panel, gc, 0, 0, new JLabel("From"));
		addToPanel(panel, gc, 1, 0, new JLabel("Hour"));

		JComboBox comboFromDays = new JComboBox(days);
		comboFromDays.setName("FromDays");
		comboFromDays.setEditable(false);
		addToPanel(panel, gc, 0, 1, comboFromDays);
		JTextField txtFromHour = new JTextField(5);
		txtFromHour.setName("FromHour");
		addToPanel(panel, gc, 1, 1, txtFromHour);

		addToPanel(panel, gc, 0, 2, new JLabel("To"));
		addToPanel(panel, gc, 1, 2, new JLabel("Hour"));

		JComboBox comboToDays = new JComboBox(days);
		comboToDays.setEditable(false);
		comboToDays.setName("ToDays");
		addToPanel(panel, gc, 0, 3, comboToDays);

		JTextField txtToHour = new JTextField(5);
		txtToHour.setName("ToHour");
		addToPanel(panel, gc, 1, 3, txtToHour);

		// gc.weightx = 1;
		gc.weightx = 0.5;
		addToPanel(panel, gc, 0, 4, new JLabel("No. Of CheckIn:"));

		JComboBox comboCheckInOperators = new JComboBox(operators);
		comboCheckInOperators.setEditable(false);
		comboCheckInOperators.setName("checkInOperator");
		addToPanel(panel, gc, 1, 4, comboCheckInOperators);

		gc.weightx = 0.5;
		addToPanel(panel, gc, 0, 5, new JLabel("Value"));

		JTextField txtCheckInVal = new JTextField(5);
		txtCheckInVal.setName("checkInValue");
		addToPanel(panel, gc, 1, 5, txtCheckInVal);
	}

	private void createReviewPanel(JPanel panel) {
		String operators[] = new String[] { "=, >, <", "=", ">", "<" };
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0.5;
		gc.weighty = 1.0 / 6;
		// gc.fill = GridBagConstraints.BOTH;
		// gc.fill = GridBagConstraints.HORIZONTAL;
		// gc.gridheight=1;
		JLabel lblFrom = new JLabel("From");
		panel.add(lblFrom, gc);
		gc.gridx = 1;
		JDatePicker jDatePickerFromDate = new JDateComponentFactory().createJDatePicker();
		panel.add((JComponent) jDatePickerFromDate, gc);
		((JComponent) jDatePickerFromDate).setName("FromDate");
		gc.gridx = 0;
		gc.gridy = 1;
		JLabel lblTo = new JLabel("To");
		panel.add(lblTo, gc);
		gc.gridx = 1;
		JDatePicker jDatePickerToDate = new JDateComponentFactory().createJDatePicker();
		panel.add((JComponent) jDatePickerToDate, gc);
		((JComponent) jDatePickerToDate).setName("ToDate");
		gc.gridx = 0;
		gc.gridy = 2;
		JLabel lblStars = new JLabel("Stars ★★★✰✰");
		panel.add(lblStars, gc);
		gc.gridx = 1;
		JComboBox comboStarsOperators = new JComboBox(operators);
		panel.add(comboStarsOperators, gc);
		comboStarsOperators.setName("StarOperator");
		gc.gridx = 0;
		gc.gridy = 3;
		JLabel lblStarValue = new JLabel("Value");
		panel.add(lblStarValue, gc);
		gc.gridx = 1;
		JTextField txtStarValue = new JTextField(5);
		txtStarValue.setName("StarValue");
		panel.add(txtStarValue, gc);
		gc.gridx = 0;
		gc.gridy = 4;
		JLabel lblVotes = new JLabel("Votes ♥♥♥♥♥");
		panel.add(lblVotes, gc);
		gc.gridx = 1;
		JComboBox comboVotesOperators = new JComboBox(operators);
		comboVotesOperators.setName("VotesOperator");
		panel.add(comboVotesOperators, gc);
		gc.gridx = 0;
		gc.gridy = 5;
		JLabel lblVotesValue = new JLabel("Value");
		panel.add(lblVotesValue, gc);
		gc.gridx = 1;
		JTextField txtVoteValue = new JTextField(5);
		txtVoteValue.setName("VoteValue");
		panel.add(txtVoteValue, gc);

	}

	private void createUsersPanel(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		String operators[] = new String[] { "=, >, <", "=", ">", "<" };
		String logicalOperators[] = new String[] { "AND, OR, Between attributes ", "AND", "OR" };
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.BOTH;
		JLabel lblMemberSince = new JLabel("Member Since:");
		gc.weightx = 1.0 / 2;
		// gc.weighty = 1.0;
		panel.add(lblMemberSince, gc);
		JDatePicker jDatePickerMemberSinceDate = new JDateComponentFactory().createJDatePicker();
		((JComponent) jDatePickerMemberSinceDate).setName("MemberSinceDate");
		gc.gridx = 1;
		panel.add((JComponent) jDatePickerMemberSinceDate, gc);
		gc.weightx = 1.0 / 4;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.anchor = GridBagConstraints.LINE_END;
		JLabel lblReviewCount = new JLabel("Review Count:");
		panel.add(lblReviewCount, gc);
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 1;
		JComboBox comboReviewCountOperators = new JComboBox(operators);
		comboReviewCountOperators.setName("ReviewCountOperator");
		comboReviewCountOperators.setEditable(false);
		panel.add(comboReviewCountOperators, gc);
		gc.anchor = GridBagConstraints.LINE_END;
		gc.gridx = 2;
		JLabel lblReviewValue = new JLabel("Value:");
		panel.add(lblReviewValue, gc);
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 3;
		JTextField txtReviewValue = new JTextField(5);
		txtReviewValue.setName("ReviewValue");
		panel.add(txtReviewValue, gc);

		gc.weightx = 1.0 / 4;
		gc.gridx = 0;
		gc.gridy = 2;
		gc.anchor = GridBagConstraints.LINE_END;
		JLabel lblNoOfFriends = new JLabel("No.Of Friends:");
		panel.add(lblNoOfFriends, gc);
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 1;
		JComboBox comboNoOfFriendsOperators = new JComboBox(operators);
		comboNoOfFriendsOperators.setName("FriendOperator");
		comboNoOfFriendsOperators.setEditable(false);
		panel.add(comboNoOfFriendsOperators, gc);
		gc.anchor = GridBagConstraints.LINE_END;
		gc.gridx = 2;
		JLabel lblFriendsValue = new JLabel("Value:");
		panel.add(lblFriendsValue, gc);
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 3;
		JTextField txtFriendValue = new JTextField(5);
		txtFriendValue.setName("FriendValue");
		panel.add(txtFriendValue, gc);

		gc.weightx = 1.0 / 4;
		gc.gridx = 0;
		gc.gridy = 3;
		gc.anchor = GridBagConstraints.LINE_END;
		JLabel lblAvgStars = new JLabel("Avg. Stars:");
		panel.add(lblAvgStars, gc);
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 1;
		JComboBox comboAvgStarsOperators = new JComboBox(operators);
		comboAvgStarsOperators.setName("AverageStarsOperator");
		comboAvgStarsOperators.setEditable(false);
		panel.add(comboAvgStarsOperators, gc);
		gc.anchor = GridBagConstraints.LINE_END;
		gc.gridx = 2;
		JLabel lblAvgStarsValue = new JLabel("Value:");
		panel.add(lblAvgStarsValue, gc);
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 3;
		JTextField txtAvgStarsValue = new JTextField(5);
		txtAvgStarsValue.setName("AverageStarsValue");
		panel.add(txtAvgStarsValue, gc);

		gc.weightx = 1.0 / 2;
		gc.gridx = 0;
		gc.gridy = 4;
		gc.anchor = GridBagConstraints.LINE_END;
		JLabel lblSelect = new JLabel("Select:");
		panel.add(lblSelect, gc);
		gc.gridx = 1;
		JComboBox comboLogicalOperators = new JComboBox(logicalOperators);
		comboLogicalOperators.setName("LogicalOPerators");
		comboLogicalOperators.setEditable(false);
		panel.add(comboLogicalOperators, gc);

	}

	private void createQueryPanel(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.weightx = 1.0;
		gc.gridx = 0;
		gc.gridy = 0;
		JTextArea txtAreaQuery = new JTextArea("<Your Query Here>", 10, 30);
		txtAreaQuery.setLineWrap(true);
		txtAreaQuery.setName("QueryTextBox");
		txtAreaQuery.setEditable(false);
		txtAreaQuery.setBackground(Color.BLACK);
		txtAreaQuery.setForeground(Color.WHITE);
		panel.add(txtAreaQuery, gc);
		gc.gridy = 2;
		JButton btnExecuteQuery = new JButton("Execute Query");
		btnExecuteQuery.setName("QueryButton");
		panel.add(btnExecuteQuery, gc);

		JTable table = new JTable();
		// Query Execution
		btnExecuteQuery.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// User Query Search
				if (uiSectionToPanelMap.get("Users").isEnabled()) {
					Component c = componentMap.get("ReviewValue");
					JTextField text = (JTextField) c;
					String reviewVal = text.getText().toString();
					c = componentMap.get("FriendValue");
					text = (JTextField) c;
					String friendVal = text.getText().toString();
					c = componentMap.get("AverageStarsValue");
					text = (JTextField) c;
					String starValue = text.getText().toString();
					c = componentMap.get("ReviewCountOperator");
					JComboBox combo = (JComboBox) c;
					String reviewOperator = combo.getSelectedItem().toString();
					c = componentMap.get("FriendOperator");
					combo = (JComboBox) c;
					String friendOperator = combo.getSelectedItem().toString();
					c = componentMap.get("AverageStarsOperator");
					combo = (JComboBox) c;
					String starsOperator = combo.getSelectedItem().toString();
					c = componentMap.get("LogicalOPerators");
					combo = (JComboBox) c;
					String logicalOperator = combo.getSelectedItem().toString();
					c = componentMap.get("MemberSinceDate");
					JDatePicker datePicker = (JDatePicker) c;
					Calendar selectedValue = (Calendar) datePicker.getModel().getValue();
					Date selectedDate = selectedValue.getTime();
					int month = selectedValue.get(Calendar.MONTH) + 1;
					String mm = Integer.toString(month);
					if (month < 9)
						mm = "0" + month;
					int year = selectedValue.get(Calendar.YEAR);

					String sqlQuery = "select * from yelp_user where review_count " + reviewOperator + " " + reviewVal
							+ " " + logicalOperator + " " + "average_stars " + starsOperator + " " + starValue + " "
							+ logicalOperator + " " + "to_char(yelping_since,'mm-YYYY') <=" + " " + "'" + mm + "-" + year
							+ "' " + logicalOperator
							+ " user_id in (select user_id from user_friends group by user_id having count(friends)"
							+ " " + friendOperator + " " + friendVal + ")";

					txtAreaQuery.setText(sqlQuery);
					JPanel panel = uiSectionToPanelMap.get("Results");
					panel.setLayout(new GridLayout(0, 1));
					JTable table = createResultTable(sqlQuery);
					JScrollPane tableContainer = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					tableContainer.setViewportBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "",
							TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
					panel.add(tableContainer);
					panel.repaint();
					panel.revalidate();

					table.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount() == 1) {
								JTable target = (JTable) e.getSource();
								int selectedRow = target.getSelectedRow();
								int selectedColumn = target.getSelectedColumn();
								Object selectedCellValue = target.getValueAt(selectedRow, selectedColumn);
								String usersReviewQuery = "Select * from Review where user_id = " + "'"
										+ selectedCellValue + "'";
								createNewTable(selectedCellValue, usersReviewQuery);
							}
						}
					});
				}
				// Business Query Search
				else {

					Set<String> selectedCategoriesSet = findSelectedCheckBoxes(
							uiSectionToPanelMap.get(MAIN_CATEGORIES));
					String selectedCategories = setToStrings(selectedCategoriesSet).toString();
					Set<String> selectedSubCategoriesSet = findSelectedCheckBoxes(uiSectionToPanelMap.get(SUBCATEGORY));
					String selectedSubCategories = setToStrings(selectedSubCategoriesSet).toString();
					Component c = componentMap.get("StarOperator");
					JComboBox combo = (JComboBox) c;
					String starOperator = combo.getSelectedItem().toString();
					c = componentMap.get("StarValue");
					JTextField text = (JTextField) c;
					String starVal = text.getText().toString();
					c = componentMap.get("VotesOperator");
					combo = (JComboBox) c;
					String votesOperator = combo.getSelectedItem().toString();
					c = componentMap.get("VoteValue");
					text = (JTextField) c;
					String voteVal = text.getText().toString();

					c = componentMap.get("FromDate");
					JDatePicker datePicker = (JDatePicker) c;
					Calendar selectedFromValue = (Calendar) datePicker.getModel().getValue();
					Date selectedFromDate = selectedFromValue.getTime();
					int fMonth = selectedFromValue.get(Calendar.MONTH) + 1;
					String fromMonth = Integer.toString(fMonth);
					if (fMonth < 9)
						fromMonth = "0" + fromMonth;
					int fromYear = selectedFromValue.get(Calendar.YEAR);
					int fDate = selectedFromValue.get(Calendar.DAY_OF_MONTH);
					String fromDate = Integer.toString(fDate);
					if (fDate < 9)
						fromDate = "0" + fromDate;
					//System.out.println("from date:" + fromDate);

					c = componentMap.get("ToDate");
					datePicker = (JDatePicker) c;
					Calendar selectedToValue = (Calendar) datePicker.getModel().getValue();
					Date selectedToDate = selectedToValue.getTime();
					int tMonth = selectedToValue.get(Calendar.MONTH) + 1;
					String toMonth = Integer.toString(tMonth);
					if (tMonth < 9)
						toMonth = "0" + toMonth;
					int toYear = selectedToValue.get(Calendar.YEAR);
					int tDate = selectedToValue.get(Calendar.DAY_OF_MONTH);
					String toDate = Integer.toString(tDate);
					if (tDate < 9)
						toDate = "0" + toDate;
					//System.out.println("to date:" + toDate);

					c = componentMap.get("FromDays");
					combo = (JComboBox) c;
					String fDays = combo.getSelectedItem().toString();
					int fromDay = daysMap.get(fDays);
					c = componentMap.get("ToDays");
					combo = (JComboBox) c;
					String tDays = combo.getSelectedItem().toString();
					int toDay = daysMap.get(tDays);
					c = componentMap.get("checkInOperator");
					combo = (JComboBox) c;
					String checkInOperator = combo.getSelectedItem().toString();

					c = componentMap.get("FromHour");
					text = (JTextField) c;
					String fromHourVal = text.getText().toString();
					c = componentMap.get("ToHour");
					text = (JTextField) c;
					String toHourVal = text.getText().toString();
					c = componentMap.get("checkInValue");
					text = (JTextField) c;
					String checkInVal = text.getText().toString();

					String sqlQuery = "select B.business_id , B.full_address , B.city, B.state , B.review_count , B.name, B.stars from Business B, BCategory BC, BSubCategory BSC where B.business_Id = BC.business_Id and BC.categories IN  "
							+ selectedCategories
							+ " and BC.business_Id = BSC.business_Id and BC.categories = BSC.categories and BSC.subcategories IN "
							+ selectedSubCategories
							+ " and B.Business_Id IN (select business_Id from Review group by business_Id having avg(stars)"
							+ starOperator + " " + starVal
							+ " intersect SELECT business_id FROM Review WHERE rdate BETWEEN TO_DATE('" + fromYear + "-"
							+ fromMonth + "-" + fromDate + "','YYYY-MM-DD') AND TO_DATE('" + toYear + "-" + toMonth
							+ "-" + toDate + "', 'YYYY-MM-DD')"
							+ "intersect select business_id from review group by business_id having sum(useful+funny+cool) "
							+ " " + votesOperator + " " + voteVal + " "
							+ "intersect select Business_Id from CheckedIn where Hour between " + fromHourVal + " and "
							+ toHourVal + " and Day between " + fromDay + " and " + toDay
							+ " group by Business_Id having sum(checkInNum ) " + checkInOperator + " " + checkInVal + " )";
					//System.out.println(sqlQuery);

					txtAreaQuery.setText(sqlQuery);
					JPanel panel = uiSectionToPanelMap.get("Results");
					panel.setLayout(new GridLayout(0, 1));
					JTable table = createResultTable(sqlQuery);
					JScrollPane tableContainer = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
							JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					tableContainer.setViewportBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "",
							TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
					panel.add(tableContainer);
					panel.repaint();
					panel.revalidate();

					table.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount() == 1) {
								JTable target = (JTable) e.getSource();
								int selectedRow = target.getSelectedRow();
								int selectedColumn = target.getSelectedColumn();
								Object selectedCellValue = target.getValueAt(selectedRow, selectedColumn);
								//System.out.println(selectedCellValue);
								//String businessReviewQuery = "Select * from Review where business_id = " + "'"
									//	+ selectedCellValue + "'";
								
								
								String businessReviewQuery = "Select R.useful, R.funny, R.cool, R.user_id, R.review_id, R.stars, R.rdate, R.text, R.business_id, Y.name from Review R, Yelp_User Y where R.business_id = " + "'" + selectedCellValue + "' and R.user_id = Y.user_id " ;
								createNewTable(selectedCellValue, businessReviewQuery);
							}
						}
					});

				}

			}

		});

	}

	public StringBuilder setToStrings(Set<String> mySet) {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		result.append("(");
		for (String str : mySet) {
			if (!first) {
				result.append(" , ");

			} else {
				first = false;
			}

			result.append("'" + str + "'");

		}
		result.append(")");
		return result;
	}

	public void createNewTable(Object selectedCellValue, String usersReviewQuery) {
		try {

			ResultSet rs = st.executeQuery(usersReviewQuery);
			// It creates and displays the table
			JTable table = new JTable(buildTableModel(rs));
			JOptionPane.showMessageDialog(null, new JScrollPane(table));
		} catch (SQLException e) {
			System.out.println("Cannot connect to Database");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private JScrollPane createScrollPaneWithPanel(String title) {
		return createScrollPaneWithPanel(title, new JPanel());
	}

	private JScrollPane createScrollPaneWithPanel(String title, JPanel panel) {
		uiSectionToPanelMap.put(title, panel);
		if (title.equals(MAIN_CATEGORIES)) {
			createCheckBoxMainCategories(panel);
		}
		if (title.equals(SUBCATEGORY)) {
			// createCheckBoxSubCategories(panel);
		}
		if (title.equals(CHECK_IN)) {
			createCheckInPanel(panel);
		}
		if (title.equals("Review")) {
			createReviewPanel(panel);
		}

		if (title.equals("Users")) {
			createUsersPanel(panel);
		}

		if (title.equals("Query")) {
			createQueryPanel(panel);
		}

		JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), title,
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		return scrollPane;
	}

	private void createModePanel(JPanel panel) {
		panel.setLayout(new GridLayout(0, 1));
		JRadioButton rdbtnUser = new JRadioButton("User");
		JRadioButton rdbtnBusiness = new JRadioButton("Business");
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(rdbtnUser);
		btnGroup.add(rdbtnBusiness);
		panel.add(rdbtnBusiness);
		panel.add(rdbtnUser);

		String userRelatedTitle[] = new String[] { "Users" };
		String businessRelatedTitle[] = new String[] { "Main Categories", "Subcategory", CHECK_IN, "Review" };
		rdbtnUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPanelEnabled(uiSectionToPanelMap.get("Query"), true);
				setPanelEnabled(uiSectionToPanelMap.get("Users"), true);
				setPanelEnabled(uiSectionToPanelMap.get("Results"), true);
				for (String str : businessRelatedTitle) {
					setPanelEnabled(uiSectionToPanelMap.get(str), false);
				}

			}
		});

		rdbtnBusiness.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (String str : businessRelatedTitle) {
					setPanelEnabled(uiSectionToPanelMap.get(str), true);
				}
				setPanelEnabled(uiSectionToPanelMap.get("Results"), true);
				setPanelEnabled(uiSectionToPanelMap.get("Query"), true);
				for (String str : userRelatedTitle) {
					setPanelEnabled(uiSectionToPanelMap.get(str), false);
				}

			}
		});

	}

	private JPanel createPanel(String panelName) {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(panelName));
		panel.setName(panelName);
		if (panelName.equals("Business/User Mode"))
			createModePanel(panel);

		return panel;
	}

	void setPanelEnabled(JPanel panel, Boolean isEnabled) {
		panel.setEnabled(isEnabled);
		Component[] components = panel.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i].getClass().getName() == "javax.swing.JPanel") {
				setPanelEnabled((JPanel) components[i], isEnabled);
			}

			components[i].setEnabled(isEnabled);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Yelp Data Set Challenge");
		frame.setBounds(100, 100, 1218, 738);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0.7 / 4;
		gc.gridwidth = 5;
		gc.gridheight = 1;

		gc.fill = GridBagConstraints.BOTH;
		frame.getContentPane().add(createPanel("Business/User Mode"), gc);
		gc.gridy = 1;
		gc.gridwidth = 1;
		gc.weightx = 0.7 / 4;
		gc.weighty = 0.6;

		frame.getContentPane().add(createScrollPaneWithPanel(MAIN_CATEGORIES), gc);
		gc.gridx = 1;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		frame.getContentPane().add(createScrollPaneWithPanel(SUBCATEGORY), gc);
		gc.gridx = 2;
		frame.getContentPane().add(createScrollPaneWithPanel(CHECK_IN), gc);
		gc.gridx = 3;
		frame.getContentPane().add(createScrollPaneWithPanel("Review"), gc);

		gc.gridx = 4;
		gc.weightx = 0.3;
		gc.weighty = 1.0;
		gc.gridheight = 2;
		frame.getContentPane().add(createScrollPaneWithPanel("Results"), gc);

		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridheight = 1;
		gc.gridwidth = 2;

		frame.getContentPane().add(createScrollPaneWithPanel("Users"), gc);
		gc.gridx = 2;
		gc.gridwidth = 2;
		frame.getContentPane().add(createScrollPaneWithPanel("Query"), gc);

	}

}
