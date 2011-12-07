package de.dengot.hl2stats.model;

public class KeyLabelPair<K, L>
{
	public static final KeyLabelPair<String, String> DB_WILDCARD = new KeyLabelPair<String, String>(
			"%", "*** ALL ***");

	private K key;

	private L label;

	public KeyLabelPair(K key, L label)
	{
		this.key = key;
		this.label = label;
	}

	public K getKey()
	{
		return key;
	}

	public void setKey(K key)
	{
		this.key = key;
	}

	public L getLabel()
	{
		return label;
	}

	public void setLabel(L label)
	{
		this.label = label;
	}

	public String toString()
	{
		return this.label.toString();
	}

}
