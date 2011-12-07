package cx.ath.janiwe.util.web;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class StaticFormatter {

	public static final DecimalFormat PERCENT_DECIMAL_FORMATTER = new DecimalFormat("##0.00");
	public static final SimpleDateFormat GERMAN_DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
}
