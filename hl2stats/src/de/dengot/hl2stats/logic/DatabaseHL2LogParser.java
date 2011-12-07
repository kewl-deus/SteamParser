package de.dengot.hl2stats.logic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java_cup.runtime.Symbol;
import de.dengot.hl2stats.model.Attack;
import de.dengot.hl2stats.model.DbStructure;
import de.dengot.hl2stats.model.Kill;

public class DatabaseHL2LogParser extends HL2LogParser
{

	private PreparedStatement insertAttack;

	private PreparedStatement insertKill;

	public DatabaseHL2LogParser(HL2LogScanner s)
	{
		super(s);
	}

	@Override
	void addAttack(Attack a)
	{
		super.addAttack(a);

		synchronized (this)
		{
			try
			{
				this.insertAttack.clearParameters();
				int param = 0;
				this.insertAttack.setString(++param, a.getDate());
				this.insertAttack.setString(++param, a.getTime());
				this.insertAttack.setString(++param, a.getAttacker());
				this.insertAttack.setString(++param, a.getVictim());
				this.insertAttack.setString(++param, a.getWeapon());
				this.insertAttack.setInt(++param, a.getDamage());
				this.insertAttack.setInt(++param, a.getDamage_armor());
				this.insertAttack.setInt(++param, a.getHealth());
				this.insertAttack.setInt(++param, a.getArmor());
				this.insertAttack.setString(++param, a.getHitgroup());

				int retCode = this.insertAttack.executeUpdate();
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}
		}
	}

	@Override
	void addKill(Kill k)
	{
		super.addKill(k);

		synchronized (this)
		{
			try
			{
				this.insertKill.clearParameters();
				int param = 0;
				this.insertKill.setString(++param, k.getDate());
				this.insertKill.setString(++param, k.getTime());
				this.insertKill.setString(++param, k.getAttacker());
				this.insertKill.setString(++param, k.getVictim());
				this.insertKill.setString(++param, k.getWeapon());
				this.insertKill.setBoolean(++param, k.isHeadshot());

				int retCode = this.insertKill.executeUpdate();
			}
			catch (SQLException e)
			{
				System.out.println(e.getMessage());
			}
		}
	}

	private void createInsertAttack() throws Exception
	{

		/*
		 * attack_date varchar(64), attack_time varchar(64), attaker
		 * varchar(64), victim varchar(64), weapon varchar(64), damage int,
		 * damage_armor int, health int, armor int, hitgroup varchar(64)
		 */
		this.insertAttack = DatabaseManager
				.getInstance()
				.getConnection()
				.prepareStatement(
						"INSERT INTO "
								+ DbStructure.TABLE_ATTACK
								+ "(attack_date, attack_time,"
								+ "attacker, victim, weapon, damage, damage_armor, health, armor, hitgroup) VALUES"
								+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	}

	private void createInsertKill() throws Exception
	{

		this.insertKill = DatabaseManager.getInstance().getConnection()
				.prepareStatement(
						"INSERT INTO " + DbStructure.TABLE_KILL
								+ "(kill_date, kill_time,"
								+ "attacker, victim, weapon, headshot) VALUES"
								+ "(?,?,?,?,?,?)");
	}

	@Override
	public Symbol parse() throws Exception
	{
		this.createInsertAttack();
		this.createInsertKill();
		Symbol result = super.parse();
		Statement stmt = DatabaseManager.getInstance().getConnection()
				.createStatement();
		String sql = "SELECT count(*) FROM ";
		ResultSet rs;
		for (DbStructure dbs : DbStructure.values())
		{
			rs = stmt.executeQuery(sql + dbs.getTechnicalName());
			rs.next();
			System.out.println(dbs.getTechnicalName() + " : " + rs.getInt(1) + " rows.");
			rs.close();
		}
		stmt.close();

		return result;
	}

}
