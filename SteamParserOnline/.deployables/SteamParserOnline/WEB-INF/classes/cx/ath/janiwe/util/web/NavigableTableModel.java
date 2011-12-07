package cx.ath.janiwe.util.web;

import java.util.Collection;

import de.hdi.gui.web.taglib.table.Navigator;
import de.hdi.gui.web.taglib.table.TableRow;

/**
 * Diese Klasse bietet eine Spezialisierung des {@link SimpleTableModel}, die
 * ein abschnittweises Navigieren bei der Anzeige der Tabelle erm�glicht. Zu
 * diesem Zweck wird das Navigator-Interface aus der GUI-Styleguide-Tag-Library
 * implementiert.
 * 
 * @author Sascha G�rtner
 */
public final class NavigableTableModel extends SimpleTableModel implements
        Navigator
{
    /**
     * Definiert die standardm��ige Anzahl von Zeilen, die bei der Anzeige der
     * Tabelle maximal auf einer Seite dargestellt werden.
     */
    public static final int DEFAULT_ROWS_PER_PAGE = 20;

    private int rowsPerPage = DEFAULT_ROWS_PER_PAGE;

    private int currentPage = 1;

    private TableRow[] currentRows;

    /**
     * Erzeugt ein neues Tabellen-Modell ohne Zeilen und mit der standardm��igen
     * Zeilenzahl pro Seite ({@link #DEFAULT_ROWS_PER_PAGE}).
     * 
     * @see #NavigableTableModel(TableRow[])
     * @see #NavigableTableModel(Collection)
     * @see #NavigableTableModel(int)
     * @see #NavigableTableModel(int, TableRow[])
     * @see #NavigableTableModel(int, Collection)
     */
    public NavigableTableModel()
    {
        this(DEFAULT_ROWS_PER_PAGE);
    }

    /**
     * Erzeugt ein neues Tabellen-Modell mit den �bergebenen Zeilen und der
     * standardm��igen Zeilenzahl pro Seite ({@link #DEFAULT_ROWS_PER_PAGE}).
     * 
     * @param rows
     *            Die Zeilen f�r das neue Tabellen-Modell
     * 
     * @see #NavigableTableModel()
     * @see #NavigableTableModel(Collection)
     * @see #NavigableTableModel(int)
     * @see #NavigableTableModel(int, TableRow[])
     * @see #NavigableTableModel(int, Collection)
     */
    public NavigableTableModel(TableRow[] rows)
    {
        this(DEFAULT_ROWS_PER_PAGE, rows);
    }

    /**
     * Erzeugt ein neues Tabellen-Modell mit den �bergebenen Zeilen und der
     * standardm��igen Zeilenzahl pro Seite ({@link #DEFAULT_ROWS_PER_PAGE}).
     * 
     * @param rows
     *            Die Zeilen f�r das neue Tabellen-Modell
     * 
     * @see #NavigableTableModel()
     * @see #NavigableTableModel(TableRow[])
     * @see #NavigableTableModel(int)
     * @see #NavigableTableModel(int, TableRow[])
     * @see #NavigableTableModel(int, Collection)
     */
    public NavigableTableModel(Collection<TableRow> rows)
    {
        this(DEFAULT_ROWS_PER_PAGE, rows);
    }

    /**
     * Erzeugt ein neues Tabellen-Modell ohne Zeilen und mit der �bergebenen
     * Zeilenzahl pro Seite.
     * 
     * @param rowsPerPage
     *            Die maximale Zeilenzahl pro Seite bei der Anzeige der Tabelle
     * 
     * @see #NavigableTableModel()
     * @see #NavigableTableModel(TableRow[])
     * @see #NavigableTableModel(Collection)
     * @see #NavigableTableModel(int, TableRow[])
     * @see #NavigableTableModel(int, Collection)
     */
    public NavigableTableModel(int rowsPerPage)
    {
        super();
        this.rowsPerPage = rowsPerPage;
        updateCurrentRows();
    }

    /**
     * Erzeugt ein neues Tabellen-Modell mit den �bergebenen Zeilen und der
     * �bergebenen Zeilenzahl pro Seite.
     * 
     * @param rowsPerPage
     *            Die maximale Zeilenzahl pro Seite bei der Anzeige der Tabelle
     * @param rows
     *            Die Zeilen f�r das neue Tabellen-Modell
     * 
     * @see #NavigableTableModel()
     * @see #NavigableTableModel(TableRow[])
     * @see #NavigableTableModel(Collection)
     * @see #NavigableTableModel(int)
     * @see #NavigableTableModel(int, Collection)
     */
    public NavigableTableModel(int rowsPerPage, TableRow[] rows)
    {
        super(rows);
        this.rowsPerPage = rowsPerPage;
        updateCurrentRows();
    }

    /**
     * Erzeugt ein neues Tabellen-Modell mit den �bergebenen Zeilen und der
     * �bergebenen Zeilenzahl pro Seite.
     * 
     * @param rowsPerPage
     *            Die maximale Zeilenzahl pro Seite bei der Anzeige der Tabelle
     * @param rows
     *            Die Zeilen f�r das neue Tabellen-Modell
     * 
     * @see #NavigableTableModel()
     * @see #NavigableTableModel(TableRow[])
     * @see #NavigableTableModel(Collection)
     * @see #NavigableTableModel(int)
     * @see #NavigableTableModel(int, TableRow[])
     */
    public NavigableTableModel(int rowsPerPage, Collection<TableRow> rows)
    {
        super(rows);
        this.rowsPerPage = rowsPerPage;
        updateCurrentRows();
    }

    /**
     * Liefert die maxiamel Anzahl von Zeilen, die auf einer Seite dargestellt
     * werden.
     * 
     * @return Die maxiamel Anzahl von Zeilen, die auf einer Seite dargestellt
     *         werden
     */
    public final int getRowsPerPage()
    {
        return this.rowsPerPage;
    }

    /**
     * Liefert die Nummer der aktuell sichtbaren Seite beginnend bei
     * <code>1</code>.
     * 
     * @return die Nummer der aktuell sichtbaren Seiten
     */
    public final int getCurrentPage()
    {
        return this.currentPage;
    }

    /**
     * Navigiert, wenn m�glich, zur n�chsten Seite
     * 
     * @see #gotoPreviousPage()
     * @see #gotoPage(int)
     * @see #gotoPage(DataAccessManager)
     */
    public final void gotoNextPage()
    {
        this.currentPage++;
        updateCurrentRows();
    }

    /**
     * Navigiert, wenn m�glich, zur vorherigen Seite
     * 
     * @see #gotoNextPage()
     * @see #gotoPage(int)
     * @see #gotoPage(DataAccessManager)
     */
    public final void gotoPreviousPage()
    {
        this.currentPage--;
        updateCurrentRows();
    }

    /**
     * Navigiert, wenn m�glich, zur Seite mit der �bergebenen Nummer.
     * 
     * @param newPage
     *            Nummer der Seite, zu der navigiert werden soll
     * 
     * @see #gotoNextPage()
     * @see #gotoPreviousPage()
     * @see #gotoPage(DataAccessManager)
     */
    public final void gotoPage(int newPage)
    {
        this.currentPage = newPage;
        updateCurrentRows();
    }

    /**
     * Ermittelt die gesamte Anzahl von Zeilen in der Tabelle. <b>Verh�lt sich
     * wie die Methode {@link SimpleTableModel#getRowCount()} aus der
     * Superklasse!</b>
     * 
     * @return Die gesamte Anzahl der Zeilen in der Tabelle
     * 
     * @see #getRowCount()
     */
    public int getTotalRowCount()
    {
        return super.getRowCount();
    }

    /**
     * Liefert die Anzahl der Zeilen des Tabellen-Modells, die auf der aktuellen
     * Seite sichtbar sind. <b>Dieses Verhalten unterscheidet sich von dem
     * Verhalten der geerbten Methode!</b>
     * 
     * @return Die Anzahl der aktuell sichtbaren Zeilen des Tabellen-Modells
     * 
     * @see #getTotalRowCount()
     */
    public final int getRowCount()
    {
        return this.currentRows.length;
    }

    /**
     * Liefert die Zeilen des Tabellen-Modells, die auf der aktuellen Seite
     * sichtbar sind. <b>Dieses Verhalten unterscheidet sich von dem Verhalten
     * der geerbten Methode!</b>
     * 
     * @return Eine Liste der aktuell sichtbaren Zeilen des Tabellen-Modells
     */
    public final TableRow[] getRows()
    {
        return this.currentRows;
    }

    /**
     * Wandelt den als String �bergebenen Index der Zeile in einen als Integer
     * formatierten Index um. Der �bergeben Index bezieht sich dabei (beginnend
     * mit 0) auf die aktuelle Seite, der zur�ckgelieferte Index auf die gesamte
     * Tabelle. <b>Dieses Verhalten unterscheidet sich von dem Verhalten der
     * geerbten Methode!</b>
     * 
     * @param rowId
     *            Der Index der Zeile bezogen auf die aktuelle Seite
     * 
     * @return Der Index der Zeile bezogen auf die gesamte Tabelle
     * 
     * @see SimpleTableModel#getRowIndex(String)
     * 
     * @since 2.4
     */
    protected int getRowIndex(String rowId)
    {
        int rowIndex = super.getRowIndex(rowId);
        if (this.currentPage > 1)
        {
            rowIndex += (this.currentPage - 1) * this.rowsPerPage;
        }
        return rowIndex;
    }

    /**
     * Diese Methode aktualisiert die aktuellen Rows gem�� der aktuellen Seite.
     * Zuvor wird die aktuelle Seite ggf. korrigiert.
     */
    private void updateCurrentRows()
    {
        TableRow[] allRows = super.getRows();
        int lastPage = 0;
        if (this.currentPage == 0)
        {
            this.currentPage = 1;
        }
        if (this.rowsPerPage == 0)
        {
            this.rowsPerPage = DEFAULT_ROWS_PER_PAGE;
        }
        int rowsOnCurrentPage = this.rowsPerPage;
        final int modulo = (allRows.length % this.rowsPerPage);
        if (modulo == 0)
        {
            lastPage = allRows.length / this.rowsPerPage;
        }
        else
        {
            lastPage = (allRows.length + this.rowsPerPage - modulo)
                    / this.rowsPerPage;
        }
        if (this.currentPage > lastPage)
        {
            this.currentPage = lastPage;
        }
        if (this.currentPage == lastPage)
        {
            rowsOnCurrentPage = modulo;
        }
        this.currentRows = new TableRow[rowsOnCurrentPage];
        for (int i = 0; i < rowsOnCurrentPage; i++)
        {
            this.currentRows[i] = allRows[(this.currentPage - 1)
                    * this.rowsPerPage + i];
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn sich der Inhalt der tabelle ge�ndert
     * hat. In dieser Methode wird daraufhin der Inhalt der aktuellen Seite
     * entsprechend aktualisiert.
     * 
     * @see SimpleTableModel#rowsUpdated()
     * 
     * @since 2.4
     */
    public void rowsUpdated()
    {
        updateCurrentRows();
    }
}
