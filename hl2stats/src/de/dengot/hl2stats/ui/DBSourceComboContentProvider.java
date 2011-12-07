package de.dengot.hl2stats.ui;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.dengot.hl2stats.logic.DatabaseManager;
import de.dengot.hl2stats.model.DbStructure;
import de.dengot.hl2stats.model.KeyLabelPair;

public class DBSourceComboContentProvider implements IStructuredContentProvider
{
	private DbStructure dbstruct;

	private String keyColumn;

	private String labelColumn;

	private List<KeyLabelPair<String, String>> dataList = new ArrayList<KeyLabelPair<String, String>>();

	public DBSourceComboContentProvider(DbStructure dbstruct, String keyColumn,
			String labelColumn)
	{
		this.dbstruct = dbstruct;
		this.keyColumn = keyColumn;
		this.labelColumn = labelColumn;
	}

	public Object[] getElements(Object inputElement)
	{
		if (this.dataList.isEmpty())
		{
			fetchData();
		}
		return this.dataList.toArray();
	}

	private void fetchData()
	{
		try
		{
			Connection con = DatabaseManager.getInstance().getConnection();
			Statement stmt = con.createStatement();
			String sql = "SELECT " + keyColumn + "," + labelColumn + " FROM "
					+ dbstruct.getTechnicalName() + " ORDER BY " + labelColumn;

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next())
			{
				KeyLabelPair<String, String> pair = new KeyLabelPair<String, String>(
						rs.getString(keyColumn), rs.getString(labelColumn));
				dataList.add(pair);
			}
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}

	public void dispose()
	{
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		// TODO Auto-generated method stub

	}

}
