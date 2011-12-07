create table t_kill(kill_date varchar(64),kill_time varchar(64),attacker varchar(64),victim varchar(64),weapon varchar(64),headshot bit);

create table t_attack(attack_date varchar(64),attack_time varchar(64),attacker varchar(64),victim varchar(64),weapon varchar(64),damage int,damage_armor int,health int,armor int,hitgroup varchar(64));

create table t_hitgroup(name varchar(64), description varchar(64));
INSERT INTO t_hitgroup(name, description) VALUES ('chest','Brust');
INSERT INTO t_hitgroup(name, description) VALUES ('generic','Sonst');
INSERT INTO t_hitgroup(name, description) VALUES ('head','Kopf');
INSERT INTO t_hitgroup(name, description) VALUES ('left arm','Linker Arm');
INSERT INTO t_hitgroup(name, description) VALUES ('left leg','Linkes Bein');
INSERT INTO t_hitgroup(name, description) VALUES ('right arm','Rechter Arm');
INSERT INTO t_hitgroup(name, description) VALUES ('right leg','Rechtes Bein');
INSERT INTO t_hitgroup(name, description) VALUES ('stomach','Bauch');

create table t_weapon(name varchar(64), description varchar(64), game varchar(64));
INSERT INTO t_weapon(name, description, game) VALUES('hegrenade','High Explosive Frag Grenade','cs');
INSERT INTO t_weapon(name, description, game) VALUES('galil','IMI Galil','cs');
INSERT INTO t_weapon(name, description, game) VALUES('ak47','AK-47','cs');
INSERT INTO t_weapon(name, description, game) VALUES('scout','Steyr Scout','cs');
INSERT INTO t_weapon(name, description, game) VALUES('sg552','SIG SG-552 Commando','cs');
INSERT INTO t_weapon(name, description, game) VALUES('awp','A.I. Arctic Warfare Magnum','cs');
INSERT INTO t_weapon(name, description, game) VALUES('g3sg1','Heckler & Koch G3/SG-1','cs');
INSERT INTO t_weapon(name, description, game) VALUES('famas','FAMAS','cs');
INSERT INTO t_weapon(name, description, game) VALUES('m4a1','Colt M4A1 Carbine','cs');
INSERT INTO t_weapon(name, description, game) VALUES('aug','Steyr Aug','cs');
INSERT INTO t_weapon(name, description, game) VALUES('sg550','SIG SG-550 Sniper Rifle','cs');
INSERT INTO t_weapon(name, description, game) VALUES('glock','Glock 18 Select Fire','cs');
INSERT INTO t_weapon(name, description, game) VALUES('usp','Heckler & Koch USP .45ACP Tactical','cs');
INSERT INTO t_weapon(name, description, game) VALUES('p228','SIG Sauer P228','cs');
INSERT INTO t_weapon(name, description, game) VALUES('deagle','IMI Desert Eagle .50 AE','cs');
INSERT INTO t_weapon(name, description, game) VALUES('elite','Dual Beretta 96G Elites','cs');
INSERT INTO t_weapon(name, description, game) VALUES('fiveseven','Fabrique Nationale Fiveseven','cs');
INSERT INTO t_weapon(name, description, game) VALUES('m3','Benelli M3 Super90','cs');
INSERT INTO t_weapon(name, description, game) VALUES('xm1014','Heckler & Koch / Benelli XM1014','cs');
INSERT INTO t_weapon(name, description, game) VALUES('mac10','IMI MAC-10','cs');
INSERT INTO t_weapon(name, description, game) VALUES('tmp','Steyr Tactical Machine Pistol','cs');
INSERT INTO t_weapon(name, description, game) VALUES('mp5navy','Heckler & Koch MP5 Navy','cs');
INSERT INTO t_weapon(name, description, game) VALUES('ump45','Heckler & Koch UMP .45','cs');
INSERT INTO t_weapon(name, description, game) VALUES('p90','Fabrique Nationale FN-P90','cs');
INSERT INTO t_weapon(name, description, game) VALUES('m249','Fabrique Nationale M249 PARA','cs');
INSERT INTO t_weapon(name, description, game) VALUES('suicide','Suicide','cs');
INSERT INTO t_weapon(name, description, game) VALUES('unknown','Unknown','cs');



create table t_xaxis(id int, name varchar(64), source varchar(64));
INSERT INTO t_xaxis(id, name, source) VALUES (1, 'Hitpoints','v_attack');
INSERT INTO t_xaxis(id, name, source) VALUES (2, 'Frags','v_kill_count');
INSERT INTO t_xaxis(id, name, source) VALUES (3, 'Headshots','v_headshot_count');


create table t_yaxis(name varchar(64), source varchar(64), xaxis_dependency int);
INSERT INTO t_yaxis(name, source, xaxis_dependency) VALUES ('Weapon','weapon', 1);
--INSERT INTO t_yaxis(name, source, xaxis_dependency) VALUES ('Weapon','weapon', 2);
--INSERT INTO t_yaxis(name, source, xaxis_dependency) VALUES ('Weapon','weapon', 3);
INSERT INTO t_yaxis(name, source, xaxis_dependency) VALUES ('Attacker','attacker', 1);
--INSERT INTO t_yaxis(name, source, xaxis_dependency) VALUES ('Attacker','attacker', 2);
--INSERT INTO t_yaxis(name, source, xaxis_dependency) VALUES ('Attacker','attacker', 3);
INSERT INTO t_yaxis(name, source, xaxis_dependency) VALUES ('Enemy','victim', 1);
--INSERT INTO t_yaxis(name, source, xaxis_dependency) VALUES ('Enemy','victim', 2);
--INSERT INTO t_yaxis(name, source, xaxis_dependency) VALUES ('Enemy','victim', 3);
--INSERT INTO t_yaxis(name, source, xaxis_dependency) VALUES ('Hitgroup','hitgroup', 1);

create view v_kill(attacker, victim, weapon, headshot) as select k.attacker, k.victim, w.description, k.headshot from t_kill as k, t_weapon as w where k.weapon = w.name;

create view v_kill_count(attacker, victim, weapon, kill_count) as select attacker, victim, weapon, count(*) from v_kill group by attacker, victim, weapon;

create view v_headshot (attacker, victim, weapon) as select attacker, victim, weapon from v_kill where headshot = 1;

create view v_headshot_count(attacker, victim, weapon, headshot_count) as select attacker, victim, weapon, count(*) from v_headshot group by attacker, victim, weapon;

--select k.attacker, k.victim, k.weapon, h.attacker, k.kill_count, h.headshot_count, (h.headshot_count / k.kill_count)*100 from v_kill_count as k left join v_headshot_count as h on k.attacker=h.attacker and k.victim = h.victim and k.weapon = h.weapon

create view v_attack(attacker, victim, weapon, hitgroup, damage, damage_armor) as select a.attacker, a.victim, w.description, h.description, sum(a.damage), sum(a.damage_armor) from t_attack as a, t_weapon as w, t_hitgroup as h where a.weapon = w.name and a.hitgroup = h.name group by a.attacker, a.victim, w.description, h.description;

create view v_player (player) as select distinct attacker from t_attack;
