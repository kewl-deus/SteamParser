package de.dengot.hl2stats.model;

public enum Weapon
{

	hegrenade("High Explosive Frag Grenade", "hegrenade.gif"), galil(
			"IMI Galil", "galil.gif"), ak47("AK-47", "ak47.gif"), scout(
			"Steyr Scout", "scout.gif"), sg552("SIG SG-552 Commando",
			"sg552.gif"), awp("A.I. Arctic Warfare Magnum", "awp.gif"), g3sg1(
			"Heckler & Koch G3/SG-1", "g3sg1.gif"), famas("FAMAS", "famas.gif"), m4a1(
			"Colt M4A1 Carbine", "m4a1.gif"), aug("Steyr Aug", "aug.gif"), sg550(
			"SIG SG-550 Sniper Rifle", "sg550.gif"), glock(
			"Glock 18 Select Fire", "glock.gif"), usp(
			"Heckler & Koch USP .45ACP Tactical", "usp.gif"), p228(
			"SIG Sauer P228", "p228.gif"), deagle("IMI Desert Eagle .50 AE",
			"deagle.gif"), elite("Dual Beretta 96G Elites", "elite.gif"), fiveseven(
			"Fabrique Nationale Fiveseven", "fiveseven.gif"), m3(
			"Benelli M3 Super90", "m3.gif"), xm1014(
			"Heckler & Koch / Benelli XM1014", "xm1014.gif"), mac10(
			"IMI MAC-10", "mac10.gif"), tmp("Steyr Tactical Machine Pistol",
			"tmp.gif"), mp5navy("Heckler & Koch MP5 Navy", "mp5navy.gif"), ump45(
			"Heckler & Koch UMP .45", "ump45.gif"), p90(
			"Fabrique Nationale FN-P90", "p90.gif"), m249(
			"Fabrique Nationale M249 PARA", "m249.gif"), suicide("Suicide",
			null), unknown("Unknown", null);

	private final String longName;

	private final String imageFile;

	Weapon(String longName, String imageFile)
	{
		this.longName = longName;
		this.imageFile = imageFile;
	}

	public String toString()
	{
		return this.longName;
	}

	public String getLongName()
	{
		return this.longName;
	}

	public String getImageFile()
	{
		return this.imageFile;
	}

}