/*
 * Created on 27.10.2005
 */
package cx.ath.janiwe.steamparser.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import cx.ath.janiwe.steamparser.Config;
import cx.ath.janiwe.steamparser.db.DatabaseManager;
import cx.ath.janiwe.steamparser.logic.Game;
import cx.ath.janiwe.steamparser.parser.CounterStrikeParser;
import de.dengot.steamparser.logic.StorageService;

public class MainGui
{

    private static final String LOG_EXTENSION = ".log";

    private JFrame window;

    private JFrame errorWindow;

    private JTextArea errorArea;

    private JTextPane fileNamePane;

    private ButtonGroup gameTypeGroup;

    private JRadioButton gameTypeCS;

    private JRadioButton gameTypeDM;

    public MainGui()
    {
        window = new JFrame("Steamparser")
        {
            /**
             * Overriden Method from JFrame that is needed to quit the
             * application, if the GUI window is closed.
             */
            protected void processWindowEvent(WindowEvent e)
            {
                super.processWindowEvent(e);
                if (e.getID() == WindowEvent.WINDOW_CLOSING)
                {
                    quit();
                }
            }

        };
        window.setContentPane(createContent());
        window.pack();
        window.setVisible(true);

        errorWindow = new JFrame("Fehlerkonsole")
        {
            /**
             * Overriden Method from JFrame that is needed to quit the
             * application, if the GUI window is closed.
             */
            protected void processWindowEvent(WindowEvent e)
            {
                super.processWindowEvent(e);
                if (e.getID() == WindowEvent.WINDOW_CLOSING)
                {
                    errorWindow.setVisible(false);
                }
            }

        };

        JPanel errorContent = new JPanel();
        errorContent
                .setLayout(new BoxLayout(errorContent, BoxLayout.PAGE_AXIS));
        errorArea = new JTextArea(20, 80);
        errorArea.setEditable(false);
        errorArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        errorContent.add(new JScrollPane(errorArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                errorArea.setText("");
                errorArea.setCaretPosition(0);
            }
        });
        errorContent.add(clearButton);
        errorWindow.setContentPane(errorContent);
        errorWindow.pack();
        errorWindow.setVisible(false);

        PrintStream outStream = new PrintStream(new OutputStream()
        {
            private ArrayList<Byte> byteList;
            {
                byteList = new ArrayList<Byte>();
            }

            public void write(int b)
            {
                byteList.add(new Byte((byte) b));
            }

            public void flush()
            {
                byte[] bytes = new byte[byteList.size()];

                for (int i = 0; i < bytes.length; i++)
                {
                    bytes[i] = ((Byte) byteList.get(i)).byteValue();
                }

                byteList.clear();

                errorArea.append(new String(bytes));
                errorArea.setCaretPosition(errorArea.getText().length() - 1);
                errorWindow.setVisible(true);
            }
        }, true);
        System.setOut(outStream);
        System.setErr(outStream);
    }

    private JPanel createContent()
    {

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        // Oberer Teil, Dateiname + Button
        JPanel upperPane = new JPanel();
        upperPane.setLayout(new BoxLayout(upperPane, BoxLayout.LINE_AXIS));

        JLabel fileLabel = new JLabel("Ordner:");
        upperPane.add(fileLabel);
        upperPane.add(Box.createRigidArea(new Dimension(10, 0)));
        fileNamePane = new JTextPane();
        fileNamePane.setText(Config.getInstance().getGuiDefaultPath());
        fileNamePane.setEditable(false);
        fileNamePane.setMinimumSize(new Dimension(50, 20));
        fileNamePane.setPreferredSize(new Dimension(400, 20));
        fileNamePane.setMaximumSize(new Dimension(1000, 20));
        upperPane.add(fileNamePane);
        upperPane.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton selectDirButton = new JButton("Durchsuchen...");
        selectDirButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                selectDir();
            }
        });

        upperPane.add(selectDirButton);
        contentPane.add(upperPane);

        // Unterer Teil, 4 Buttons
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.LINE_AXIS));
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // Radiobuttons
        JPanel gameSwitchPane = new JPanel();
        gameSwitchPane.setLayout(new BoxLayout(gameSwitchPane,
                BoxLayout.PAGE_AXIS));

        gameTypeGroup = new ButtonGroup();
        gameTypeCS = new JRadioButton("CS:S");
        gameTypeDM = new JRadioButton("HL2DM");

        gameTypeGroup.add(gameTypeCS);
        gameTypeGroup.add(gameTypeDM);

        gameSwitchPane.add(gameTypeCS);
        gameSwitchPane.add(gameTypeDM);

        lowerPanel.add(gameSwitchPane);
        lowerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton addToDbButton = new JButton("Einlesen");
        addToDbButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addToDB();
            }
        });
        lowerPanel.add(addToDbButton);

        lowerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton initDbButton = new JButton("Datenbank reinitialisieren");
        initDbButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                initDB();
            }
        });
        lowerPanel.add(initDbButton);

        lowerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton showConsoleButton = new JButton("Konsole");
        showConsoleButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                errorWindow.setVisible(true);
            }
        });
        lowerPanel.add(showConsoleButton);

        lowerPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton quitButton = new JButton("Beenden");
        quitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                quit();
            }
        });
        lowerPanel.add(quitButton);

        contentPane.add(lowerPanel);

        return contentPane;

    }

    private void selectDir()
    {
        String text = fileNamePane.getText();
        JFileChooser dirChooser = new JFileChooser(text);
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.showDialog(window, "Auswählen");
        fileNamePane.setText(dirChooser.getSelectedFile() != null ? dirChooser
                .getSelectedFile().getAbsolutePath() : "");
    }

    private void addToDB()
    {
        if (JOptionPane.showConfirmDialog(window,
                "Logs des Verzeichnisses wirklich der Datenbank hinzufügen?",
                "Bestätigung", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {

            File logDir = new File(fileNamePane.getText());
            if (logDir.exists())
            {
                String gameType = null;
                Enumeration<AbstractButton> e = gameTypeGroup.getElements();
                while (e.hasMoreElements())
                {
                    AbstractButton current = e.nextElement();
                    if (current.isSelected())
                    {
                        if (current == gameTypeCS)
                        {
                            gameType = "cs";
                        }
                        else if (current == gameTypeDM)
                        {
                            gameType = "hl2dm";
                        }
                        break;
                    }
                }
                if (gameType != null)
                {
                    new SteamThread(logDir, gameType).start();
                }
                else
                {
                    JOptionPane.showMessageDialog(window,
                            "Kein Game-Typ ausgewählt!", "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(window,
                        "Log-Verzeichnis existiert nicht!", "Fehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void initDB()
    {
        if (JOptionPane.showConfirmDialog(window, "Datenbank killen?",
                "Bestätigung", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            try
            {
                DatabaseManager.getInstance().createDB();
                JOptionPane.showMessageDialog(window, "OK");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void quit()
    {
        System.exit(0);
    }

    class SteamThread extends Thread
    {
        private File logDir;

        private String gameType;

        public SteamThread(File logDir, String gameType)
        {
            this.logDir = logDir;
            this.gameType = gameType;
        }

        public void run()
        {
            try
            {
                CounterStrikeParser p = new CounterStrikeParser();
                Game game = new Game();
                for (File f : logDir.listFiles(new FileFilter()
                {
                    public boolean accept(File pathname)
                    {
                        if (pathname.isFile()
                                && pathname.getName().endsWith(LOG_EXTENSION))
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                }))
                {

                    if (game.getGameStopped() != null)
                    {
                        StorageService.getInstance().put(game, gameType);
                        game = new Game();
                    }
                    p.parse(f, game);

                }
                StorageService.getInstance().put(game, gameType);
                JOptionPane.showMessageDialog(window, "OK");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
