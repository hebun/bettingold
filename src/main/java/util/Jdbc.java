package util;

//STEP 1. Import required packages
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import model.Match;

import betting.ApiNGDemo;

public class Jdbc {
	// JDBC driver name and database URL
	public	static  String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static  String DB_URL = "jdbc:mysql://localhost:3306/betting?useUnicode=true&characterEncoding=utf8";
	public static Connection conn = null;
	public static Statement stmt = null;

	// Database credentials
	public	static  String USER = "root";
	public static  String PASS = "2882";
	public static boolean started = false;
	public  static Logger log = Logger.getLogger(ApiNGDemo.class
			.getName());

	public static void start(String caller) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.createStatement();

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		started = true;
	}

	public static <T> List<T> select(String sql, Class<T> type) {

		try {
			start("");
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int colCount = metaData.getColumnCount();

			String[] columns = new String[colCount];

			for (int i = 0; i < colCount; i++) {
				columns[i] = new String(metaData.getColumnLabel(i + 1));
			}
			List<T> list = new ArrayList<T>();

			while (rs.next()) {
				T obj = type.newInstance();
				for (Field f : type.getDeclaredFields()) {
					if (Modifier.isPrivate(f.getModifiers())) {
						String input = f.getName();
						input = input.substring(0, 1).toUpperCase()
								+ input.substring(1);
						try {
							Class<?> type2 = f.getType();
							// System.out.println(type2);
							if (type2 == Integer.class) {

							} else {
								type2 = String.class;
							}
							Method met = type.getMethod("set" + input, type2);

							if (input.equals("Tarih")) {
								met.invoke(obj, rs.getString(f.getName()));
							}
							met.invoke(obj, rs.getObject(f.getName()));
						} catch (NoSuchMethodException e) {

							e.printStackTrace();
						} catch (Exception e) {
						}
					}
				}
				list.add(obj);
			}
			return list;
		} catch (SQLException se) {
			System.out.println("se in select<T" + sql);
			se.printStackTrace();
			return new ArrayList<T>();
		} catch (Exception e) {
			System.out.println("e in select");
			e.printStackTrace();
			return new ArrayList<T>();
		} finally {
			close("");
		}

	}

	public static void close(String caller) {

		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		started = false;
	}

	private static int say = 0;

	public static void query(String sql) {
		if (!started)
			start("query not started");
		try {
			say++;
			if (say % 100 == 0)
				System.out.print(say + ".");

			int rs = stmt.executeUpdate(sql);

		} catch (SQLException se) {
			System.out.println(sql);
			se.printStackTrace();
		} catch (Exception e) {

			System.out.println("query ex");
		} finally {

		}// end try

	}

	public static List<Map<String, String>> select(String sql) {
		if (false == started)
			start("select not started");
		List<Map<String, String>> list = null;
		try {
			say++;
			if (say % 100 == 0)
				System.out.print(say + ".");

			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData data = rs.getMetaData();
			int colCount = data.getColumnCount();
			list = new ArrayList<Map<String, String>>();
			while (rs.next()) {

				Hashtable<String, String> hash = new Hashtable<String, String>();
				for (int i = 1; i <= colCount; i++) {

					String value = rs.getString(i) == null ? "NULL" : rs
							.getString(i);
					hash.put(data.getColumnLabel(i), value);
				}
				list.add(hash);

			}
		} catch (SQLException se) {

			se.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

		}// end try
		return list;
	}

	public static List<Match> selectMatch(String string) {
		if (false == started)
			start("selectMatch not started");
		List<Match> list = null;
		try {
			say++;
			if (say % 100 == 0)
				System.out.print(say + ".");

			ResultSet rs = stmt.executeQuery(string);
			ResultSetMetaData data = rs.getMetaData();
			int colCount = data.getColumnCount();
			list = new ArrayList<Match>();
			while (rs.next()) {

				Match match = new Match(rs.getInt("siteId"),
						rs.getString("externId"), rs.getString("homeTeam"),
						rs.getString("awayTeam"), rs.getInt("ht"),
						rs.getInt("at"), rs.getInt("draw"), rs.getInt("deht"),
						rs.getInt("deat"), rs.getString("tarih"));

				list.add(match);

			}
		} catch (SQLException se) {

			se.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

		}// end try
		return list;
	}
}// end FirstExample