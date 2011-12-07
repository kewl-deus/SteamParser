DROP TABLE t_hitgroup CASCADE;
CREATE TABLE t_hitgroup(
	id varchar(64) PRIMARY KEY,
	name varchar(64)
);

INSERT INTO t_hitgroup (id,  name) VALUES ('GENERIC','generic');
INSERT INTO t_hitgroup (id,  name) VALUES ('LEFT_ARM','left arm');
INSERT INTO t_hitgroup (id,  name) VALUES ('RIGHT_ARM','right arm');
INSERT INTO t_hitgroup (id,  name) VALUES ('CHEST','chest');
INSERT INTO t_hitgroup (id,  name) VALUES ('HEAD','head');
INSERT INTO t_hitgroup (id,  name) VALUES ('LEFT_LEG','left leg');
INSERT INTO t_hitgroup (id,  name) VALUES ('RIGHT_LEG','right leg');
INSERT INTO t_hitgroup (id,  name) VALUES ('STOMACH','stomach');
INSERT INTO t_hitgroup (id,  name) VALUES ('UNKNOWN','unknown');

DROP TABLE t_weapon CASCADE;
CREATE TABLE t_weapon(
	id varchar(64) PRIMARY KEY,
	name varchar(64),
	imagepath varchar(64),
	gameType varchar(64)
);

INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('hegrenade','High Explosive Frag Grenade','hegrenade.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('flashbang','FlashBang Grenade','flashbang.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('galil','IMI Galil','galil.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('ak47','AK-47','ak47.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('scout','Steyr Scout','scout.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('sg552','SIG SG-552 Commando','sg552.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('awp','A.I. Arctic Warfare Magnum','awp.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('g3sg1','Heckler & Koch G3/SG-1','g3sg1.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('famas','FAMAS','famas.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('m4a1','Colt M4A1 Carbine','m4a1.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('aug','Steyr Aug','aug.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('sg550','SIG SG-550 Sniper Rifle','sg550.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('glock','Glock 18 Select Fire','glock.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('usp','Heckler & Koch USP .45ACP Tactical','usp.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('p228','SIG Sauer P228','p228.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('deagle','IMI Desert Eagle .50 AE','deagle.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('elite','Dual Beretta 96G Elites','elite.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('fiveseven','Fabrique Nationale Fiveseven','fiveseven.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('m3','Benelli M3 Super90','m3.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('xm1014','Heckler & Koch / Benelli XM1014','xm1014.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('mac10','IMI MAC-10','mac10.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('tmp','Steyr Tactical Machine Pistol','tmp.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('mp5navy','Heckler & Koch MP5 Navy','mp5navy.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('ump45','Heckler & Koch UMP .45','ump45.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('p90','Fabrique Nationale FN-P90','p90.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('m249','Fabrique Nationale M249 PARA','m249.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('knife','Knife','knife.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('suicide','Suicide','suicide.gif','cs');
INSERT INTO t_weapon(id,name,imagepath, gameType) VALUES ('unknown','Unknown','unknown.gif','cs');

INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('c357','Magnum Colt .357','357.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('ar2','Combine Rifle','ar2.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('combine_ball','Combine Ball','combine_ball.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('crossbow_bolt','Crossbow','crossbow_bolt.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('crowbar','Crowbar','crowbar.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('env_explosion','Explosion in Environment','env_explosion.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('grenade_frag','Frag Grenade','grenade_frag.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('physics','Physics','physics.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('pistol','Pistol','pistol.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('rpg_missile','Raketenwerfer','rpg_missile.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('shotgun','Shotgun','shotgun.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('slam','S.L.A.M.','slam.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('smg1','SubMachine Gun','smg1.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('smg1_grenade','Impact Grenade','smg1_grenade.gif','hl2dm');
INSERT INTO t_weapon(id, name, imagepath, gameType) VALUES ('stunstick','Schlagstock','stunstick.gif','hl2dm');

drop table t_nickname cascade;
create table t_nickname
(
	id int IDENTITY primary key,
	nickname varchar(64),
	player int,
	constraint uNickname unique(nickname)
);

drop table t_player cascade;
create table t_player
(
	id int IDENTITY primary key,
	firstname varchar(64),
	lastname varchar(64),
	preferred_nick varchar(64),
	constraint uPlayerNick unique(preferred_nick),
	constraint uPlayerName unique(firstname, lastname)
);

DROP TABLE t_map CASCADE;
CREATE TABLE t_map(
	mapName varchar(64) PRIMARY KEY
);

DROP TABLE t_session CASCADE;
CREATE TABLE t_session(
	id int PRIMARY KEY,
	gameStarted timestamp,
	gameStopped timestamp,
	gameType varchar(64),
	CONSTRAINT uSession UNIQUE (gameStarted,gameStopped)
);

DROP TABLE t_kill CASCADE;
CREATE TABLE t_kill(
	attacker varchar(64),
	victim varchar(64),
	weapon varchar(64),
	map varchar(64),
	gameSession int,
	headshot boolean,
	gameType varchar(64),
	CONSTRAINT fkKillKiller FOREIGN KEY (attacker) REFERENCES t_nickname(nickname),
	CONSTRAINT fkKillVictim FOREIGN KEY (victim) REFERENCES t_nickname(nickname),
	CONSTRAINT fkKillWeapon FOREIGN KEY (weapon) REFERENCES t_weapon(id),
	CONSTRAINT fkKillMap FOREIGN KEY (map) REFERENCES t_map(mapName),
	CONSTRAINT fkKillGameSession FOREIGN KEY (gameSession) REFERENCES t_session(id)
);

DROP TABLE t_damage CASCADE;
CREATE TABLE t_damage(
	attacker varchar(64),
	victim varchar(64),
	weapon varchar(64),
	map varchar(64),
	gameSession int,
	healthDamage int,
	armorDamage int,
	hitgroup varchar(64),
	gameType varchar(64),
	CONSTRAINT fkDamageAttacker FOREIGN KEY (attacker) REFERENCES t_nickname(nickname),
	CONSTRAINT fkDamageVictim FOREIGN KEY (victim) REFERENCES t_nickname(nickname),
	CONSTRAINT fkDamageWeapon FOREIGN KEY (weapon) REFERENCES t_weapon(id),
	CONSTRAINT fkDamageMap FOREIGN KEY (map) REFERENCES t_map(mapName),
	CONSTRAINT fkDamageGameSession FOREIGN KEY (gameSession) REFERENCES t_session(id),
	CONSTRAINT fkDamageHitgroup FOREIGN KEY (hitgroup) REFERENCES t_hitgroup(id)
);

DROP VIEW v_kill IF EXISTS CASCADE;
create view v_kill as
	select coalesce(p1.preferred_nick,'Computer') as attacker, coalesce(p2.preferred_nick,'Computer') as victim,
	k.weapon, k.map, k.gamesession, k.headshot, k.gametype
	from t_kill k 
	left join t_nickname n1 on k.attacker = n1.nickname
	left join t_player p1 on n1.player = p1.id
	left join t_nickname n2 on k.victim = n2.nickname
	left join t_player p2 on n2.player = p2.id
	where not (p1.preferred_nick is null and p2.preferred_nick is null);
	
DROP VIEW v_kill_bots IF EXISTS CASCADE;
create view v_kill_bots as
	select coalesce(p1.preferred_nick,k.attacker) as attacker, coalesce(p2.preferred_nick, k.victim) as victim,
	k.weapon, k.map, k.gamesession, k.headshot, k.gametype
	from t_kill k
	left join t_nickname n1 on k.attacker = n1.nickname
	left join t_player p1 on n1.player = p1.id
	left join t_nickname n2 on k.victim = n2.nickname
	left join t_player p2 on n2.player = p2.id;

DROP VIEW v_damage IF EXISTS CASCADE;
create view v_damage as
	select coalesce(p1.preferred_nick,'Computer') as attacker, coalesce(p2.preferred_nick,'Computer') as victim,
	d.weapon, d.map, d.gamesession, d.healthdamage, d.armordamage, d.hitgroup, d.gametype
	from t_damage d
	left join t_nickname n1 on d.attacker = n1.nickname
	left join t_player p1 on n1.player = p1.id
	left join t_nickname n2 on d.victim = n2.nickname
	left join t_player p2 on n2.player = p2.id
	where not (p1.preferred_nick is null and p2.preferred_nick is null);

DROP VIEW v_damage_bots IF EXISTS CASCADE;
create view v_damage_bots as
	select coalesce(p1.preferred_nick, d.attacker) as attacker, coalesce(p2.preferred_nick, d.victim) as victim,
	d.weapon, d.map, d.gamesession, d.healthdamage, d.armordamage, d.hitgroup, d.gametype
	from t_damage d
	left join t_nickname n1 on d.attacker = n1.nickname
	left join t_player p1 on n1.player = p1.id
	left join t_nickname n2 on d.victim = n2.nickname
	left join t_player p2 on n2.player = p2.id;

DROP VIEW v_kills_per_time IF EXISTS CASCADE;
CREATE VIEW v_kills_per_time AS
	SELECT k.attacker, count(k.*) AS kills, k.gamesession, DATEDIFF('mi', s.gamestarted,s.gamestopped) AS timeplayed FROM v_kill k, t_session s
	WHERE s.id = k.gamesession
	GROUP BY k.attacker, k.gamesession, s.gameStarted, s.gamestopped;

DROP VIEW v_sessionstats_allplayers IF EXISTS CASCADE;
create view v_sessionstats_allplayers as
	select ps.name, ps.gamestarted, ps.gamestopped, coalesce(k.kills,0) as kills_in_session,
	coalesce(d.deaths,0) as deaths_in_session, coalesce(h.headshots,0) as headshots_in_session,
	(coalesce(k.kills,0) - coalesce(d.deaths,0)) as frags_in_session,
	(coalesce(k.kills,0) - coalesce(h.headshots,0)) as nonheadshots_in_session
	from
	(select s.id, s.gamestarted, s.gamestopped, p.preferred_nick as name from t_session s, t_player p) ps
	left join
	(select attacker, gamesession, count(attacker) as kills
	from v_kill group by attacker, gamesession) k on ((ps.name = k.attacker) and (ps.id = k.gamesession))
	left join
	(select victim, gamesession, count(victim) as deaths
	from v_kill group by victim, gamesession) d on ((ps.name = d.victim) and (ps.id = d.gamesession))
	left join
	(select attacker, gamesession, count(attacker) as headshots
	from v_kill where headshot
	group by attacker, gamesession) h on ((ps.name = h.attacker) and (ps.id = h.gamesession))
	order by ps.gamestarted, kills_in_session desc, ps.name;

DROP SEQUENCE s_session CASCADE;
CREATE SEQUENCE s_session;

