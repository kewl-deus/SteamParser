package cx.ath.janiwe.util.web;

import java.util.HashSet;
import java.util.Set;

import de.hdi.gui.web.taglib.table.TableRow;

/**
 * Diese Klasse bietet eine einfache Implementierung des TableRow-Interface aus
 * der GUI-Styleguide-Tag-Library. Es wird eine Liste von Child-Rows verwaltet,
 * die bei einer als hierarchie dargestellten Liste als Kind-Zeilen unterhalb
 * dieser Zeile liegen. Ausserdem wird ein Java-Objekt als Model verwaltet
 * (Model-Objekt), für das dieses Table-Row-Objekt als Wrapper funktioniert. Es
 * werdem diverse Methoden zum Verwalten der Kind-Zeilen und zum Aktivieren bzw.
 * Deaktivieren von Funktionen in der Tabelle angeboten.
 * 
 * Von dieser Klasse kann abgeleitet werden, um für eine bestimmte Klasse von
 * Model-Objekten einen Mechanismus zum Ermitteln der Zellen-Werte bzw. des Typs
 * zu implementieren. Zu diesem Zweck können die Methoden
 * {@link #getCellValue(String)} und {@link #getType()} überschrieben werden.
 * 
 * @author Sascha Gärtner
 */
public class SimpleTableRow implements TableRow
{
    /**
     * In dieser Variablen wird das Model-Objekt abgelegt, für welches die Zeile
     * als Wrapper dient.
     */
    final protected Object model;

    private String type;

    private Set<String> disabledFunctions;

    /**
     * Erzeugt eine neue Zeile für das übergebene Model-Objekt
     * 
     * @param model
     *            Das Model-Objekt, für das die Zeile erzeugt werden soll
     */
    public SimpleTableRow(Object model)
    {
        this.model = model;
        disabledFunctions = new HashSet<String>();
    }

    /**
     * Ermittelt den Wert, den diese Tabellenzeile in der angegebenen Spalte hat
     * (Wert der Zelle). Der Wert ist ein Array von Java-Objekten, auf die beim
     * Erzeugen des Zelleninhalts in der JSP zugegriffen werden kann.
     * 
     * Diese Methode kann in einer Subklasse überschrieben werden, um für eine
     * bestimmte Klasse von Model-Objekten einen individuellen Algorithmus zum
     * Ermitteln des Zellenwertes zu implementieren. Zu diesem Zweck kann auf
     * das Model-Objekt über die Variable {@link #model} zugegriffen werden.
     * Standardmäßig liefert diese Methode für jede Spalte ein Array mit dem
     * Model-Objekt als einziges Element.
     * 
     * @param columnName
     *            Der Name der Spalte, für die der Zellenwert ermittelt werden
     *            soll
     * 
     * @return Der Zellenwert, den die Zeile in der angegebenen Spalte hat
     */
    public Object[] getCellValue(String columnName)
    {
        if (model == null)
        {
            return new String[] { "" };
        }
        else
        {
            return new Object[] { model };
        }
    }

    /**
     * Ermittelt, ob die Funktion mit dem angegebenen Namen in dieser Zeile
     * aktiviert ist.
     * 
     * @param functionName
     *            Der Name der Funktion, für die der Aktivierungszustand
     *            ermittelt werden soll
     * 
     * @return <code>true</code>, wenn die Funktion aktiviert ist, sonst
     *         <code>false</code>
     * 
     * @see #enableFunction(String)
     * @see #disableFunction(String)
     */
    public final boolean isFunctionEnabled(String functionName)
    {
        return !this.disabledFunctions.contains(functionName);
    }

    /**
     * Aktiviert die Funktion mit dem angegebenen Namen in dieser Tabellenzeile.
     * 
     * @param functionName
     *            Der Name der Funktion, die aktiviert werden soll
     * 
     * @see #isFunctionEnabled(String)
     * @see #disableFunction(String)
     */
    public final void enableFunction(String functionName)
    {
        this.disabledFunctions.remove(functionName);
    }

    /**
     * Deaktiviert die Funktion mit dem angegebenen Namen in dieser
     * Tabellenzeile.
     * 
     * @param functionName
     *            Der Name der Funktion, die deaktiviert werden soll
     * 
     * @see #isFunctionEnabled(String)
     * @see #enableFunction(String)
     */
    public final void disableFunction(String functionName)
    {
        this.disabledFunctions.add(functionName);
    }

    /**
     * Ermittelt, ob die Zeile Kind-Zeilen hat.
     * 
     * @return <code>true</code>, wenn die Zeile Kind-Zeilen hat ,sonst
     *         <code>false</code>
     */
    public final boolean hasChildRows()
    {
        // Derzeit wird keine Hierarchie unterstützt
        return false;
    }

    /**
     * Ermittelt, ob die Kind-Zeilen standardmäßig angezeigt werden sollen, der
     * Baum bei dieser Zeile folglich aufgeklappte dargestellt wird.
     * 
     * @return <code>true</code>, wenn die Kind-Zeilen angezeigt werden
     *         sollen ,sonst <code>false</code>
     */
    public final boolean showChildRows()
    {
        // Derzeit wird keine Hierarchie unterstützt
        return false;
    }

    /**
     * Ermittelt Kind-Zeilen dieser Zeile.
     * 
     * @return Eine Liste der Kind-Zeilen dieser Zeile
     */
    public final TableRow[] getChildRows()
    {
        // Derzeit wird keine Hierarchie unterstützt
        return new TableRow[0];
    }

    /**
     * Ermittelt die Anzahl der Kind-Zeilen dieser Zeile.
     * 
     * @return Die Anzahl der Kind-Zeilen dieser Zeile
     */
    public final int getChildRowCount()
    {
        // Derzeit wird keine Hierarchie unterstützt
        return 0;
    }

    /**
     * Liefert das Model-Objekt, das von dieser Zeile verwaltet wird.
     * 
     * @return Das Model-Objekt, das von dieser Zeile verwaltet wird
     */
    public final Object getModel()
    {
        return model;
    }

    /**
     * Liefert den Typ dieser Zeile. Für einen bestimmten Typ kann im Tag der
     * GUI-Styleguide-Tag-Library ein individuelles Icon zur Anzeige in einem
     * Baum konfiguriert werden. Der Standardtyp ist <code>null</code>.
     * 
     * Diese Methode kann in einer Subklasse überschrieben werden, um für eine
     * bestimmte Klasse von Model-Objekten einen individuellen Algorithmus zum
     * Ermitteln des Typs zu implementieren. Zu diesem Zweck kann auf das
     * Model-Objekt über die Variable {@link #model} zugegriffen werden.
     * Standardmäßig wird der vom Table-Row-Objekt in seiner Funktion als
     * Wrapper verwaltete Typ geliefert.
     * 
     * @return Den Typ der Zeile
     * 
     * @see #setType(String)
     */
    public String getType()
    {
        return this.type;
    }

    /**
     * Setzt den vom Table-Row-Objekt in seiner Funktion als Wrapper verwalteten
     * Typ der Zeile
     * 
     * @param type
     *            Der neue Typ der Zeile
     * 
     * @see #getType()
     */
    public final void setType(String type)
    {
        this.type = type;
    }
}
