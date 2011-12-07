package de.dengot.hl2stats.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.CategoryItemRenderer;
import org.jfree.data.CategoryDataset;
import org.jfree.data.DefaultCategoryDataset;
import org.jfree.text.TextBlockAnchor;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

import de.dengot.hl2stats.model.DbStructure;

public class AttackVisualizer extends ApplicationFrame
{
    private final String WILDCARD_LABEL = "*** ALL ***";

    private final String WILDCARD_VALUE = "%";

    private Connection con;

    private ChartPanel chartPanel;

    private JComboBox cmbAttacker = new JComboBox();

    private JComboBox cmbEnemy = new JComboBox();

    private JComboBox cmbHitgroup = new JComboBox();

    private DefaultComboBoxModel mdlAttacker = new DefaultComboBoxModel();

    private DefaultComboBoxModel mdlEnemy = new DefaultComboBoxModel();

    private DefaultComboBoxModel mdlHitgroup = new DefaultComboBoxModel();

    public AttackVisualizer(Connection con)
    {
        super("Attack Visualizer");
        this.con = con;
        this.buildComponentTree();
        this.registerListeners();
        this.updateData();
    }

    private void buildComponentTree()
    {
        // create the chart...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart = createChart(dataset);
        // add the chart to a panel...
        this.chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(900, 700));
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.getContentPane().add(mainPanel);
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel();
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        controlPanel.setLayout(new GridLayout(2, 3));
        controlPanel.add(new Label("Attacker"));
        controlPanel.add(new Label("Enemy"));
        controlPanel.add(new Label("Hitgroup"));
        controlPanel.add(this.cmbAttacker);
        controlPanel.add(this.cmbEnemy);
        controlPanel.add(this.cmbHitgroup);
        // Connect Models
        this.cmbAttacker.setModel(this.mdlAttacker);
        this.cmbEnemy.setModel(this.mdlEnemy);
        this.cmbHitgroup.setModel(this.mdlHitgroup);
        // Fill Models with Data
        fillModel("attacker", this.mdlAttacker);
        fillModel("victim", this.mdlEnemy);
        fillModel("hitgroup", this.mdlHitgroup);
    }

    private Connection getConnection()
    {
        return this.con;
    }

    private void fillModel(String sourceColumn, DefaultComboBoxModel mdl)
    {
        String sql;
        ResultSet rs;
        mdl.addElement(WILDCARD_LABEL);
        try
        {
            Statement stmt = this.getConnection().createStatement();
            sql = "SELECT DISTINCT " + sourceColumn + " FROM "
                    + DbStructure.VIEW_ATTACK + " ORDER BY " + sourceColumn;
            rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                mdl.addElement(rs.getString(sourceColumn));
            }
            rs.close();
        } catch (SQLException e)
        {
            System.out
                    .println("SQLException by filling Model: " + sourceColumn);
            System.out.println(e.getMessage());
        }

    }

    private void registerListeners()
    {
        this.cmbAttacker.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                updateData();
            }
        });

        this.cmbEnemy.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                updateData();
            }
        });

        this.cmbHitgroup.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                updateData();
            }
        });
    }

    void updateData()
    {
        DefaultCategoryDataset dataset = this.fetchData();
        this.setData(dataset);
    }

    private String getSelectedItem(JComboBox box)
    {
        String item = box.getSelectedItem().toString();
        if (item.equals(WILDCARD_LABEL))
        {
            item = WILDCARD_VALUE;
        }
        return item;
    }

    DefaultCategoryDataset fetchData()
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String sql;
        ResultSet rs;
        String attacker = getSelectedItem(this.cmbAttacker);
        String enemy = getSelectedItem(this.cmbEnemy);
        String hitgroup = getSelectedItem(this.cmbHitgroup);
        final int ROW_LIMIT = 10;
        int rows = 0;

        try
        {
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
                weaponList.append(",");
                if (++rows > ROW_LIMIT)
                {
                    break;
                }
            }

            if (weaponList.length() > 0)
            {
                weaponList = new StringBuffer(weaponList.substring(0,
                        weaponList.length() - 2));
            }

            // System.out.println("WeaponList: " + weaponList.toString());
            rs.close();

            // DAMAGE TAKEN
            sql = "SELECT weapon, SUM(damage) AS sum_damage FROM "
                    + DbStructure.VIEW_ATTACK
                    + " WHERE attacker LIKE '"
                    + enemy
                    + "'"
                    + " AND victim LIKE '"
                    + attacker
                    + "'"
                    + " AND hitgroup LIKE '"
                    + hitgroup
                    + "'"
                    + (weaponList.length() > 0 ? " AND weapon in ("
                            + weaponList.toString() + ")" : "")
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

    public void setData(CategoryDataset dataset)
    {
        this.chartPanel.setChart(this.createChart(dataset));
        this.chartPanel.revalidate();
    }

    private JFreeChart createChart(CategoryDataset dataset)
    {

        JFreeChart chart = ChartFactory.createBarChart3D("Weapons", // chart
                // title
                "Weapon", // domain axis label
                "Damage", // range axis label
                dataset, // data
                PlotOrientation.HORIZONTAL, // orientation
                false, // include legend
                true, // tooltips
                false // urls
                );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setForegroundAlpha(1.0f);

        // left align the category labels...
        CategoryAxis axis = plot.getDomainAxis();
        CategoryLabelPositions p = axis.getCategoryLabelPositions();

        CategoryLabelPosition left = new CategoryLabelPosition(
                RectangleAnchor.LEFT, TextBlockAnchor.CENTER_LEFT,
                TextAnchor.CENTER_LEFT, 0.0);
        axis.setCategoryLabelPositions(CategoryLabelPositions
                .replaceLeftPosition(p, left));

        axis.setMaxCategoryLabelWidthRatio(3.0f);

        // Plot Values
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        // set up gradient paints for series...
        GradientPaint paintGiven = new GradientPaint(0.0f, 0.0f, Color.green,
                0.0f, 0.0f, Color.blue);
        GradientPaint paintTaken = new GradientPaint(0.0f, 0.0f, Color.red,
                0.0f, 0.0f, Color.orange);

        renderer.setSeriesPaint(0, Color.GREEN);
        renderer.setSeriesPaint(1, Color.RED);

        // renderer.setSeriesPaint(0, paintGiven);
        // renderer.setSeriesPaint(1, paintTaken);

        return chart;

    }

    public void visualize()
    {
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);

    }

}
