package de.dengot.hl2stats.actions;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java_cup.runtime.Symbol;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.jfree.data.DefaultCategoryDataset;

import de.dengot.hl2stats.ICommandIds;
import de.dengot.hl2stats.logic.DatabaseHL2LogParser;
import de.dengot.hl2stats.logic.DatabaseManager;
import de.dengot.hl2stats.logic.HL2LogFilterReader;
import de.dengot.hl2stats.logic.HL2LogScanner;
import de.dengot.hl2stats.model.DbStructure;
import de.dengot.hl2stats.model.KeyLabelPair;
import de.dengot.hl2stats.ui.ChartView;

public class ParseLogfileAction extends Action
{

    private final IWorkbenchWindow window;

    public ParseLogfileAction(String text, IWorkbenchWindow window)
    {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_PARSE);
        // Associate the action with a pre-defined command, to allow key
        // bindings.
        setActionDefinitionId(ICommandIds.CMD_PARSE);
        setImageDescriptor(de.dengot.hl2stats.HL2StatsPlugin
                .getImageDescriptor("/icons/sample3.gif"));
    }

    public void run()
    {
        System.out.println(this.getClass().getName()
                + ".run() ausgeführt an Objekt: " + this.toString());
        try
        {
            String fileToScan = "U:\\Eigene Dateien\\Eclipse Workspace\\hl2stats\\test\\"
                    + "l1007001.log";
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
            p.parse();

            IWorkbenchPage page = window.getActivePage();
            ChartView chartView = (ChartView) page.findView(ChartView.ID);

            chartView.visualize(this.fetchData(), "Given/Taken Damage",
                    "Hitpoints", "Weapon");

            chartView.setFocus();
        } catch (IOException e)
        {
            System.out.println(this.getClass().getName() + ".run(): "
                    + e.getMessage());
        } catch (Exception e)
        {
            System.out.println(this.getClass().getName() + ".run(): "
                    + e.getMessage());
        }
    }

    private DefaultCategoryDataset fetchData()
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String sql;
        ResultSet rs;
        String attacker = KeyLabelPair.DB_WILDCARD.getKey();
        String enemy = KeyLabelPair.DB_WILDCARD.getKey();
        String hitgroup = KeyLabelPair.DB_WILDCARD.getKey();
        Connection con = DatabaseManager.getInstance().getConnection();
        final int ROW_LIMIT = 10;
        int rows = 0;

        try
        {
            // // KILLS
            // sql = "SELECT weapon, kill_count FROM "
            // + DbStructure.VIEW_KILL_COUNT + " WHERE attacker LIKE '"
            // + attacker + "'" + " AND victim LIKE '" + enemy + "'"
            // + " ORDER BY kill_count desc";
            // rs = con.createStatement().executeQuery(sql);
            // while (rs.next())
            // {
            // dataset.addValue(rs.getDouble("kill_count"), "KILLS", rs
            // .getString("weapon"));
            // }
            // rs.close();
            //
            // // DEATHS
            // sql = "SELECT weapon, kill_count FROM "
            // + DbStructure.VIEW_KILL_COUNT + " WHERE attacker LIKE '"
            // + enemy + "'" + " AND victim LIKE '" + attacker + "'"
            // + " ORDER BY kill_count desc";
            // rs = con.createStatement().executeQuery(sql);
            // while (rs.next())
            // {
            // dataset.addValue(rs.getDouble("kill_count") * -1, "DEATHS", rs
            // .getString("weapon"));
            // }
            // rs.close();

            // DAMAGE GIVEN
            StringBuffer weaponList = new StringBuffer();
            sql = "SELECT weapon, SUM(damage) AS sum_damage FROM "
                    + DbStructure.VIEW_ATTACK + " WHERE attacker LIKE '"
                    + attacker + "'" + " AND victim LIKE '" + enemy + "'"
                    + " AND hitgroup LIKE '" + hitgroup + "'"
                    + " GROUP BY weapon ORDER BY sum_damage DESC";
            rs = con.createStatement().executeQuery(sql);
            rows = 0;
            while (rs.next())
            {
                String weapon = rs.getString("weapon");
                dataset.addValue(rs.getDouble("sum_damage"), "GIVEN", weapon);

                weaponList.append("'");
                weaponList.append(weapon);
                weaponList.append("'");
                if (++rows < ROW_LIMIT)
                {
                    weaponList.append(",");
                } else
                {
                    break;
                }
            }
            rs.close();

            // DAMAGE TAKEN
            sql = "SELECT weapon, SUM(damage) AS sum_damage FROM "
                    + DbStructure.VIEW_ATTACK + " WHERE attacker LIKE '"
                    + enemy + "'" + " AND victim LIKE '" + attacker + "'"
                    + " AND hitgroup LIKE '" + hitgroup + "'"
                    + " AND weapon in (" + weaponList.toString() + ")"
                    + " GROUP BY weapon ORDER BY sum_damage DESC";
            rs = con.createStatement().executeQuery(sql);

            rows = 0;
            while (rs.next() && rows++ < ROW_LIMIT)
            {
                dataset.addValue(rs.getDouble("sum_damage") * -1, "TAKEN", rs
                        .getString("weapon"));
            }
            rs.close();

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        } finally
        {
            return dataset;
        }
    }
}