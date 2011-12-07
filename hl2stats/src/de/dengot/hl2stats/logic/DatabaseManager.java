package de.dengot.hl2stats.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseManager
{
	private static DatabaseManager instance;

	private Connection con;
	
	private DatabaseManager()
	{
	}

	public static synchronized DatabaseManager getInstance()
	{
		if (instance == null)
		{
			instance = new DatabaseManager();
			instance.init();
		}
		return instance;
	}

	private synchronized void init()
	{
		try
		{
			DriverManager.registerDriver(new org.hsqldb.jdbcDriver());
			// HSQLDB In-Memory
			this.con = DriverManager.getConnection("jdbc:hsqldb:mem:hl2stats", "sa", "");
//			this.con = DriverManager.getConnection("jdbc:hsqldb:hl2stats", "sa", "");
			this.createDataModel();
		}
		catch (SQLException e)
		{
			System.out.println(this.getClass().getName() + ".init(): " + e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println(this.getClass().getName() + ".init(): " + e.getMessage());
		}
	}

	private void createDataModel() throws SQLException, IOException
	{
		BufferedReader r = new BufferedReader(new InputStreamReader(
				getResourceStream("de.dengot.hl2stats.resources",
						"create_datamodel.sql")));
		Statement stmt = this.getConnection().createStatement();
		ArrayList<String> sqlStrings = new ArrayList<String>();
		while (r.ready())
		{
			String line = r.readLine();
			if (line != null && line.length() > 0 && (! line.startsWith("--")))
			{
				sqlStrings.add(line);
				// stmt.addBatch(line);
				int retCode = stmt.executeUpdate(line);
				if (retCode != 0 && retCode != 1)
				{
					System.out.println("RC: " + retCode + " FOR " + line);
				}

			}
		}
		// int retCodes[] = stmt.executeBatch();
		// for (int i = 0; i < retCodes.length; i++)
		// {
		// if (retCodes[i] != 0)
		// {
		// System.out.println("RC: " + retCodes[i] + " FOR "
		// + sqlStrings.get(i));
		// }
		// }
	}

	private InputStream getResourceStream(String pkgname, String fname)
	{
		String resname = "/" + pkgname.replace('.', '/') + "/" + fname;
		Class clazz = getClass();
		InputStream is = clazz.getResourceAsStream(resname);
		return is;
	}

	public Connection getConnection()
	{
		if (this.con == null)
		{
			this.init();
		}
		return con;
	}

}
