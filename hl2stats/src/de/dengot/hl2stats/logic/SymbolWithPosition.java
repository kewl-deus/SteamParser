package de.dengot.hl2stats.logic;

import java.lang.reflect.Field;
import java.util.HashMap;

public class SymbolWithPosition extends java_cup.runtime.Symbol
{
	private int line;

	private int column;

	private int position;

	private HashMap<Integer, String> symMap;

	public SymbolWithPosition(int type, int line, int column, int position)
	{
		this(type, line, column, -1, -1, position, null);
	}

	public SymbolWithPosition(int type, int line, int column, int position,
			Object value)
	{
		this(type, line, column, -1, -1, position, value);
	}

	public SymbolWithPosition(int type, int line, int column, int position,
			int left, int right, Object value)
	{
		super(type, left, right, value);
		this.line = line;
		this.column = column;
		this.position = position;
	}

	public int getLine()
	{
		return line;
	}

	public int getColumn()
	{
		return column;
	}

	public int getPosition()
	{
		return position;
	}

	public String toString()
	{
		return "line " + line + ", column " + column + ", sym: "
				+ getSymName(sym)
				+ (value == null ? "" : (", value: '" + value + "'"));
	}

	private String getSymName(int symNo)
	{
		if (this.symMap == null)
		{
			this.initSymMap();
		}
		return this.symMap.get(symNo);
	}

	private void initSymMap()
	{
		this.symMap = new HashMap<Integer, String>();
		try
		{
			Field[] syms = HL2LogSymbols.class.getDeclaredFields();
			for (Field symConst : syms)
			{
				symMap.put(symConst.getInt(null), symConst.getName());
			}
		}
		catch (SecurityException e)
		{
			System.out.println("Unable to init SymMap");
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Unable to init SymMap");
		}
		catch (IllegalAccessException e)
		{
			System.out.println("Unable to init SymMap");
		}
	}

	public int getSymbolNo()
	{
		return super.sym;
	}
}
