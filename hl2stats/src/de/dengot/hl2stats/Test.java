package de.dengot.hl2stats;

import java.io.FileReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.List;

import java_cup.runtime.Symbol;

import org.jfree.chart.demo.StackedBarChart3DDemo;
import org.jfree.chart.demo.StackedBarChartDemo2;
import org.jfree.chart.demo.StackedBarChartDemo3;

import de.dengot.hl2stats.logic.DatabaseHL2LogParser;
import de.dengot.hl2stats.logic.DatabaseManager;
import de.dengot.hl2stats.logic.HL2LogFilterReader;
import de.dengot.hl2stats.logic.HL2LogParser;
import de.dengot.hl2stats.logic.HL2LogScanner;
import de.dengot.hl2stats.logic.HL2LogSymbols;
import de.dengot.hl2stats.model.Weapon;
import de.dengot.hl2stats.ui.AttackVisualizer;

public class Test
{
	static final boolean do_debug_parse = false;

	static final String PATH = "U:\\Eigene Dateien\\Eclipse Workspace\\hl2stats\\test\\";

	private Connection con;

	public static void main(String[] args)
	{
		String filename = PATH + "l1007001.log";
		Test t = new Test();
		// t.testScanner(filename);
		// t.testParser(filename);
		t.testDatabase(filename);
		//StackedBarChart3DDemo.main(null);
		//StackedBarChartDemo2.main(null);
		
		
		
//		for (Weapon w : Weapon.values())
//		{
//			System.out
//					.println("INSERT INTO t_weapon(name, description, game) VALUES('"
//							+ w.name() + "','" + w.getLongName() + "','cs');");
//		}
	}

	public void testDatabase(String fileToScan)
	{
		try
		{
			HL2LogFilterReader filter = new HL2LogFilterReader(new FileReader(
					fileToScan));

			StringBuffer filteredLog = new StringBuffer();
			while (filter.ready())
			{
				filteredLog.append(filter.readLine());
			}
			HL2LogScanner s = new HL2LogScanner(new StringReader(filteredLog
					.toString()));
			DatabaseHL2LogParser p = new DatabaseHL2LogParser(s);
			Symbol parseTree = p.parse();

			Connection con = DatabaseManager.getInstance().getConnection();
			AttackVisualizer av = new AttackVisualizer(con);
			av.visualize();

		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			System.exit(1);
		}
	}

	public void testScanner(String fileToScan)
	{
		try
		{
			PrintStream ps = new PrintStream(PATH + "scanresults.txt");
			ps.append("Start at: " + (new Date()).toString() + "\n");
			ps.append("Lexing [" + fileToScan + "]\n");

			HL2LogFilterReader filter = new HL2LogFilterReader(new FileReader(
					fileToScan));

			StringBuffer filteredLog = new StringBuffer();
			while (filter.ready())
			{
				filteredLog.append(filter.readLine());
			}

			HL2LogScanner scanner = new HL2LogScanner(new StringReader(
					filteredLog.toString()));

			Symbol s;
			do
			{
				s = scanner.next_token();
				ps.append("token: " + s + "\n");
			}
			while (s.sym != HL2LogSymbols.EOF);

			ps.append("Scan finished.");
			System.out.println("Scan finished.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void testParser(String fileToScan)
	{

		try
		{
			System.out.println("Parsing [" + fileToScan + "]");
			HL2LogFilterReader filter = new HL2LogFilterReader(new FileReader(
					fileToScan));

			StringBuffer filteredLog = new StringBuffer();
			while (filter.ready())
			{
				filteredLog.append(filter.readLine());
			}

			HL2LogScanner s = new HL2LogScanner(new StringReader(filteredLog
					.toString()));

			HL2LogParser p = new HL2LogParser(s);

			Symbol parseTree;

			if (do_debug_parse)
				parseTree = p.debug_parse();
			else
				parseTree = p.parse();

			System.out.println("Parsing done.");

			List result = (List) parseTree.value;
			for (Object o : result)
			{
				System.out.println(o);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
			System.exit(1);
		}
	}

}
