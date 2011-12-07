package cx.ath.janiwe.util.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.hdi.gui.web.taglib.table.TableModel;
import de.hdi.gui.web.taglib.table.TableRow;

/**
 * Diese Klasse bietet eine einfache Implementierung des TableModel-Interface
 * aus der GUI-Styleguide-Tag-Library. Es wird eine Liste von Table-Rows
 * verwaltet. Ausserdem sind Methoden vorhanden, die das Model einer bestimmten
 * Table-Row, z.B. anhand von Parametern eines HTTP-Requests, ermitteln können.
 * 
 * @author Sascha Gärtner
 */
public class SimpleTableModel implements TableModel
{
    private TableRow[] rows;

    private final Map<String, SortableColumn> sortableColumns;

    private SortableColumn currentSortableColumn;

    /**
     * Erzeugt ein neues Tabellen-Modell ohne Zeilen.
     */
    public SimpleTableModel()
    {
        this.rows = new TableRow[0];
        this.sortableColumns = new HashMap<String, SortableColumn>();
    }

    /**
     * Erzeugt ein neues Tabellen-Modell mit den übergebenen Zeilen.
     * 
     * @param rows
     *            Die Zeilen, die in das neue Tabellen-Modell aufzunehmen sind
     * 
     * @see #SimpleTableModel(Collection)
     */
    public SimpleTableModel(TableRow[] rows)
    {
        this();
        addRows(rows);
    }

    /**
     * Erzeugt ein neues Tabellen-Modell mit den übergebenen Zeilen.
     * 
     * @param rows
     *            Die Zeilen, die in das neue Tabellen-Modell aufzunehmen sind
     * 
     * @see #SimpleTableModel(TableRow[])
     */
    public SimpleTableModel(Collection<TableRow> rows)
    {
        this();
        addRows(rows);
    }

    /**
     * Ermittelt, ob die angegebene Spalte sortierbar ist.
     * 
     * @param columnName
     *            Der Name der Spalte, für welche die Sortierbarkeit
     *            festgestellt werden soll
     * 
     * @return <code>true</code>, falls die Spalte sortiebar ist, sonst
     *         <code>false</code>
     * 
     * @see #getColumnSortDirection(String)
     */
    public final boolean isColumnSortable(String columnName)
    {
        return this.sortableColumns.containsKey(columnName);
    }

    /**
     * Ermittelt die aktuelle Sortierrichtung für dir angegebene Spalte.
     * 
     * @param columnName
     *            Der Name der Spalte, für welche die Sortierrichtung
     *            festgestellt werden soll
     * 
     * @return Die Sortierrichtung der Spalte, ausgedrückt durch einen der Werte
     *         <ul class="ListeVonRueckgabewerten">
     *         <li>{@link TableModel#SORT_DIRECTION_NONE }</li>
     *         <li>{@link TableModel#SORT_DIRECTION_ASCENDING}</li>
     *         <li>{@link TableModel#SORT_DIRECTION_DESCENDING}</li>
     *         </ul>
     * 
     * @see #isColumnSortable(String)
     */
    public final int getColumnSortDirection(String columnName)
    {
        if (this.currentSortableColumn != null)
        {
            if (this.currentSortableColumn.name.equals(columnName))
            {
                return this.currentSortableColumn.direction;
            }
        }
        return SORT_DIRECTION_NONE;
    }

    /**
     * Ermittelt die Anzahl von Zeilen in der Tabelle.
     * 
     * @return Die Anzahl der Zeilen in der Tabelle
     */
    public int getRowCount()
    {
        return this.rows.length;
    }

    /**
     * Fügt dem Tabellen-Modell eine sortierbare Spalte hinzu. Der Name der
     * Spalte wird dem Konstruktor der Klasse {@link SortableColumn} als
     * Parameter übergeben ({@link SortableColumn#SortableColumn(String)}).
     * Soll ein individueller Vergleichsalgorithmus implementiert werden, muss
     * eine Ableitung von der Klasse {@link SortableColumn} erfolgen, in der die
     * Methode {@link SortableColumn#compareRows(TableRow, TableRow)}
     * entsprechend überschrieben wird.
     * 
     * @param column
     *            Der Name der sortierbaren Spalte mit dem
     *            Vergleichsalgorithmus.
     * 
     * @since 2.4
     */
    public final void addSortableColumn(SortableColumn column)
    {
        this.sortableColumns.put(column.name, column);
        if (this.currentSortableColumn == null)
        {
            this.currentSortableColumn = column;
            resort();
        }
    }

    /**
     * Fügt dem Tabellen-Modell eine sortierbare Spalte hinzu. Die Sortierung
     * erfolgt hierbei mit der Standard-Implementierung der Klasse
     * {@link SortableColumn}. Hierbei muß das erste Objekt im Wert einer Zelle ({@link TableRow#getCellValue(java.lang.String)})
     * das Interface {@link Comparable} implementieren. Die
     * Standard-Implementierung nutzt die Methode
     * {@link Comparable#compareTo(java.lang.Object)} um die Sortierung anhand
     * der Zellenwerte durchzuführen.
     * 
     * @param columnName
     *            Der Name der Spalte, die sortierbar sein soll
     * 
     * @since 2.4
     */
    public final void addSortableColumn(String columnName)
    {
        addSortableColumn(new SortableColumn(columnName));
    }

    /**
     * Fügt dem Tabellen-Modell eine neue Zeile hinzu.
     * 
     * @param row
     *            Die neu hinzuzufügende Zeile
     * 
     * @see #addRows(TableRow[])
     * @see #addRows(Collection)
     */
    public final void addRow(TableRow row)
    {
        addRows(new TableRow[] { row });
    }

    /**
     * Fügt dem Tabellen-Modell neue Zeilen hinzu.
     * 
     * @param rows
     *            Die neu hinzuzufügenden Zeilen
     * 
     * @see #addRow(TableRow)
     * @see #SimpleTableModel(Collection)
     */
    public final void addRows(TableRow[] rows)
    {
        TableRow[] newRows = new TableRow[this.rows.length + rows.length];
        int i = 0;
        for (int j = 0; j < this.rows.length; j++)
        {
            newRows[i] = this.rows[j];
            i++;
        }
        for (int j = 0; j < rows.length; j++)
        {
            newRows[i] = rows[j];
            i++;
        }
        this.rows = newRows;
        resort();
    }

    /**
     * Fügt dem Tabellen-Modell neue Zeilen hinzu.
     * 
     * @param rows
     *            Die neu hinzuzufügenden Zeilen
     * 
     * @see #addRow(TableRow)
     * @see #SimpleTableModel(TableRow[])
     */
    public final void addRows(Collection<TableRow> rows)
    {

        addRows((TableRow[]) rows.toArray(new TableRow[0]));

    }

    /**
     * Entfernt die übergebene Zeile aus der Tabelle
     * 
     * @param row
     *            Die Zeile, die aus der Tabelle entfernt werden soll
     * 
     * @see #removeRows(TableRow[])
     * @see #removeRow(String)
     * @see #removeRows(Collection)
     * @see #removeRows(String[])
     * 
     * @since 2.4
     */
    public final void removeRow(TableRow row)
    {
        removeRows(new TableRow[] { row });
    }

    /**
     * Entfernt die Zeile mit der übergebenen ID aus der Tabelle
     * 
     * @param id
     *            Die ID der Zeile, die aus der Tabelle entfernt werden soll
     * 
     * @see #removeRows(TableRow[])
     * @see #removeRow(TableRow)
     * @see #removeRows(Collection)
     * @see #removeRows(String[])
     * 
     * @since 2.4
     */
    public final void removeRow(String id)
    {
        removeRows(new String[] { id });
    }

    /**
     * Entfernt die übergebenen Zeilen aus der Tabelle
     * 
     * @param rows
     *            Die Zeilen, die aus der Tabelle entfernt werden sollen
     * 
     * @see #removeRow(String)
     * @see #removeRow(TableRow)
     * @see #removeRows(Collection)
     * @see #removeRows(String[])
     * 
     * @since 2.4
     */
    public final void removeRows(TableRow[] rows)
    {
        removeRows(Arrays.asList(rows));
    }

    /**
     * Entfernt die übergebenen Zeilen aus der Tabelle
     * 
     * @param rows
     *            Enthält die Zeilen, die aus der Tabelle entfernt werden sollen
     * 
     * @see #removeRow(String)
     * @see #removeRow(TableRow)
     * @see #removeRows(String[])
     * @see #removeRows(TableRow[])
     * 
     * @since 2.4
     */
    public final void removeRows(Collection<TableRow> rows)
    {
        Collection<TableRow> newRows = Arrays.asList(this.rows);
        newRows.removeAll(rows);
        this.rows = (TableRow[]) rows.toArray(new TableRow[0]);
        resort();
    }

    /**
     * Entfernt die Zeilen mit den übergebenen IDs aus der Tabelle
     * 
     * @param ids
     *            Die IDs der Zeilen, die aus der Tabelle entfernt werden sollen
     * 
     * @see #removeRow(String)
     * @see #removeRow(TableRow)
     * @see #removeRows(Collection)
     * @see #removeRows(TableRow[])
     * 
     * @since 2.4
     */
    public final void removeRows(String[] ids)
    {
        TableRow[] rows = new TableRow[ids.length];
        for (int i = 0; i < ids.length; i++)
        {
            rows[i] = getRow(ids[i]);
        }
        removeRows(rows);
    }

    /**
     * Ersetzt die übergebene alte Tabellenzeile mit der ebenfalls übergebenen
     * neuen Zeile.
     * 
     * @param oldRow
     *            Die alte Zeile, die mit der neuen Zeile ersetzt werden soll
     * @param newRow
     *            Die neue Zeile, mit der die alte Zeile ersetzt werden soll
     * 
     * @see #replaceRow(String, TableRow)
     * 
     * @since 2.4
     */
    public final void replaceRow(TableRow oldRow, TableRow newRow)
    {
        if (oldRow != null && newRow != null)
        {
            for (int i = 0; i < this.rows.length; i++)
            {
                if (this.rows[i] == oldRow)
                {
                    this.rows[i] = newRow;
                }
            }
            resort();
        }
    }

    /**
     * Ersetzt die Tabellenzeile mit der übergebenen ID mit der ebenfalls
     * übergebenen neuen Zeile.
     * 
     * @param oldRowId
     *            Die ID der zu ersetzenden Zeile
     * @param newRow
     *            Die neue Zeile, mit der die alte Zeile ersetzt werden soll
     * 
     * @see #replaceRow(TableRow, TableRow)
     * 
     * @since 2.4
     */
    public final void replaceRow(String oldRowId, TableRow newRow)
    {
        replaceRow(getRow(oldRowId), newRow);
    }

    /**
     * Wandelt den als String übergebenen Index der Zeile in einen als Integer
     * formatierten Index um.
     * 
     * @param rowId
     *            Der als String formatierte Index
     * 
     * @return Der als Integer formatierte Index
     * 
     * @since 2.4
     */
    protected int getRowIndex(String rowId)
    {
        return Integer.parseInt(rowId);
    }

    /**
     * Ermittelt die Zeile mit der angebenen ID. Bei der ID handelt es sich um
     * einen bei <code>0</code> beginnenden Index des aktuell sichtbaren
     * Bereichs.
     * 
     * @param rowId
     *            Die ID der zu ermittelnden Zeile
     * 
     * @return Die ermittelte Zeile oder <code>null</code>, falls keine Zeile
     *         ermittelt werden konnte
     * 
     * @see #getRow(DataAccessManager)
     * @see #getRowModel(String)
     * @see #getRowModel(DataAccessManager)
     */
    public final TableRow getRow(String rowId)
    {
        int index = getRowIndex(rowId);
        if (index < this.rows.length)
        {
            return this.rows[index];
        }
        else
        {
            return null;
        }
    }

    /**
     * Ermittelt das Model-Objekt (Das Java-Objekt, das von einer Zeile als
     * Modell verwaltet wird) für die Zeile, die anhand der angebenen ID
     * ermittelt wurde.
     * 
     * @param rowId
     *            Die ID der Zeile, deren Model-Objekt ermittelt werden soll
     * 
     * @return Das ermittelt Model-Objekt oder <code>null</code>, falls kein
     *         Model-Objekt ermittelt werden konnte
     * 
     * @see #getRow(String)
     * @see #getRow(DataAccessManager)
     * @see #getRowModel(DataAccessManager)
     */
    public final Object getRowModel(String rowId)
    {
        try
        {
            SimpleTableRow row = (SimpleTableRow) getRow(rowId);
            if (row != null)
            {
                return row.getModel();
            }
            else
            {
                return null;
            }
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    /**
     * Sortiert die Tabelle nach der übergebenen Spalte in der für die Spalte
     * zuletzt verwendeten Richtung.
     * 
     * @param columnName
     *            Der Name der Spalte, nach der sortiert werden soll.
     * 
     * @see #resort(DataAccessManager)
     * @see #resort()
     * @see #resort(String, int)
     * 
     * @since 2.4
     */
    public final void resort(String columnName)
    {
        SortableColumn column = (SortableColumn) this.sortableColumns
                .get(columnName);
        if (column != null)
        {
            this.currentSortableColumn = column;
        }
        resort();
    }

    /**
     * Sortiert die Tabelle nach der übergebenen Spalte in der übergebenen
     * Richtung.
     * 
     * @param columnName
     *            Der Name der Spalte, nach der sortiert werden soll.
     * @param direction
     *            Die Richtung, in der sortiert werden soll
     *            <ul class="ListeVonEingabewerten">
     *            <li>{@link TableModel#SORT_DIRECTION_ASCENDING}</li>
     *            <li>{@link TableModel#SORT_DIRECTION_DESCENDING}</li>
     *            </ul>
     * 
     * @see #resort(DataAccessManager)
     * @see #resort(String)
     * @see #resort()
     * 
     * @since 2.4
     */
    public final void resort(String columnName, int direction)
    {
        SortableColumn column = (SortableColumn) this.sortableColumns
                .get(columnName);
        if (column != null)
        {
            column.direction = direction;
            this.currentSortableColumn = column;
        }
        resort();
    }

    /**
     * Aktualisiert die Sortierung in der Tabelle.
     * 
     * @see #resort(DataAccessManager)
     * @see #resort(String)
     * @see #resort(String, int)
     * 
     * @since 2.4
     */
    public final void resort()
    {
        if (this.currentSortableColumn != null && this.rows.length > 0)
        {
            Arrays.sort(this.rows, this.currentSortableColumn);
        }
        rowsUpdated();
    }

    /**
     * Diese Methode wird aufgerufen, wenn sich der Inhalt der Tabelle geändert
     * hat.
     * 
     * @since 2.4
     */
    public void rowsUpdated()
    {
    }

    /**
     * Liefert alle Zeilen des Tabellen-Modells.
     * 
     * @return Eine Liste aller Zeilen des Tabellen-Modells
     * 
     * @see #getRows(String)
     */
    public TableRow[] getRows()
    {
        return this.rows;
    }

    /**
     * Liefert alle Zeilen des angegebenen Typs aus dem Tabellen-Modell.
     * 
     * @param type
     *            Der Typ, den die gewünschten Zeilen haben sollen
     * 
     * @return Eine Liste aller Zeilen des angegebenen Typs
     * 
     * @see #getRows()
     */
    public final TableRow[] getRows(String type)
    {
        Collection<TableRow> foundRows = new ArrayList<TableRow>();
        for (int i = 0; i < this.rows.length; i++)
        {
            try
            {
                SimpleTableRow currentRow = (SimpleTableRow) this.rows[i];
                if (currentRow.getType() == null)
                {
                    if (type == null)
                    {
                        foundRows.add(currentRow);
                    }
                }
                else
                {
                    if (type != null)
                    {
                        if (currentRow.getType().equals(type))
                        {
                            foundRows.add(currentRow);
                        }
                    }
                }
            }
            catch (ClassCastException e)
            {
            }
        }
        return (TableRow[]) foundRows.toArray(new TableRow[0]);
    }
}
