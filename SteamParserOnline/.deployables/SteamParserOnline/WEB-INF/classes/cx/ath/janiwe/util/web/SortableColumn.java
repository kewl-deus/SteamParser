package cx.ath.janiwe.util.web;

import java.util.Comparator;

import de.hdi.gui.web.taglib.table.TableModel;
import de.hdi.gui.web.taglib.table.TableRow;

/**
 * Definiert eine sortierbare Spalte. Enthält neben dem Namen der Spalte ({@link #name})
 * die aktuelle Sortierrichtung ({@link #direction}) und den
 * Vergleichsalgorithmus ({@link #compareRows(TableRow, TableRow)}). Die
 * Instanzen dieser Klasse werden einem
 * {@link cx.ath.janiwe.util.web.SimpleTableModel} über die Methode
 * {@link cx.ath.janiwe.util.web.SimpleTableModel#addSortableColumn(SortableColumn)}
 * hinzugefügt.
 * 
 * @author Sascha Gärtner
 * 
 * @since 2.4
 */
public class SortableColumn implements Comparator<TableRow>
{
    /**
     * Enthält den Namen der Spalte, für die eine Sortierung erfolgen soll.
     */
    protected final String name;

    /**
     * Enthält die Richtung, in der eine Sortierung erfolgen soll ({@link TableModel#SORT_DIRECTION_ASCENDING}
     * oder {@link TableModel#SORT_DIRECTION_DESCENDING}).
     */
    protected int direction;

    /**
     * Erzeugt eine neue Instanz für die Spalte mit dem übergebenen Namen. Die
     * Sortierrichtung ist standardmäßig auf &quot;Aufsteigend&quot; gesetzt ({@link TableModel#SORT_DIRECTION_ASCENDING}).
     * 
     * @param name
     *            Der Name der Spalte, für die eine Sortierung definiert wird
     */
    public SortableColumn(String name)
    {
        this.name = name;
        this.direction = TableModel.SORT_DIRECTION_ASCENDING;
    }

    /**
     * Diese Methode wird bei der Sortierung von dem Sortieralgorithmus
     * aufgerufen, um zwei Werte miteinander zu vergleichen. Die Implementierung
     * konvertiert die Parameter in Referenzen auf den Typ {@link TableRow} und
     * ruft die Methode {@link #compareRows(TableRow, TableRow)} auf, die den
     * eigentlichen Vergleichsalgorithmus enthält. Beim aufruf der Methode wird
     * die Sortierrichtung ({@link #direction}) derart verwendet, dass im
     * Vergleichsalgorithmus die Sortierrichtung nicht berücksichtigt werden
     * muss.
     * 
     * @param self
     *            Der erste Wert, der verglichen werden soll
     * @param another
     *            Der zweite Wert, der verglichen werden soll
     * 
     * @return Einen Wert der angibt, ob der Wert in <code>self</code> gemäß
     *         der Sortierung kleiner (&lt;0), gleich (=0) oder größer (&gt;0)
     *         ist, als der Wert in <code>another</code>.
     * 
     * @see #compareRows(TableRow, TableRow)
     */
    public final int compare(TableRow self, TableRow another)
    {
        if (this.direction == TableModel.SORT_DIRECTION_ASCENDING)
        {
            return compareRows(self, another);
        }
        else
        {
            return compareRows(another, self);
        }
    }

    /**
     * Diese Methode enthält den eigentlichen Vergleichsalgorithmus, mit dem
     * zwei Tabellenzeilen bei der Sortierung miteinander Verglichen werden. Die
     * Methode liefert einen Wert kleiner als 0, wenn die Tabellenzeile im
     * Parameter <code>self</code> in der Sortierung vor der Tabellenzeile im
     * Parameter <code>another</code> kommt. Sind die beiden Tabellenzeilen
     * gleich, liefert die Methode den Wert 0, andernfalls einen wert größer als
     * 0. Die Standard-Implementierung in dieser Klasse ruft jeweils das erste
     * Objekt des Zellenwertes der Tabellenzeilen in der Spalte ab, für die
     * diese SortableColumn definiert ist ({@link #name}). Diese Objekte
     * müssen das Interface {@link Comparable} implementieren, so dass die
     * Methode {@link Comparable#compareTo(java.lang.Object)} zum Vergleichen
     * genutzt werden kann.
     * 
     * @param self
     *            Die erste Tabellenzeile, die verglichen werden soll
     * @param another
     *            Die zweite Tabellenzeile, die verglichen werden soll
     * 
     * @return Einen Wert der angibt, ob die Tabellenzeile in <code>self</code>
     *         gemäß der Sortierung kleiner (&lt;0), gleich (=0) oder größer
     *         (&gt;0) ist, als die Tabellenzeile in <code>another</code>.
     * 
     * @see #compare(Object, Object)
     */
    public int compareRows(TableRow self, TableRow another)
    {
        Object[] selfCellValue = self.getCellValue(this.name);
        if (selfCellValue.length > 0)
        {
            Object[] anotherCellValue = another.getCellValue(this.name);
            if (anotherCellValue.length > 0)
            {
                Comparable selfRelevantValue = (Comparable) selfCellValue[0];
                Comparable anotherRelevantValue = (Comparable) anotherCellValue[0];
                if (selfRelevantValue != null && anotherRelevantValue != null)
                {
                    return selfRelevantValue.compareTo(anotherRelevantValue);
                }
            }
        }
        return 0;
    }
}
