insert into t_player(firstname, lastname, preferred_nick) values('Dennis', 'Gottschalk', 'kewl-deus');
insert into t_player(firstname, lastname, preferred_nick) values('Jan Niklaas', 'Wechselberg', 'JaNiWe');
insert into t_player(firstname, lastname, preferred_nick) values('Dirk', 'Müller', 'Obelix');
insert into t_player(firstname, lastname, preferred_nick) values('Martin', 'Morys', 'Neothorn');
insert into t_player(firstname, lastname, preferred_nick) values('Ansgar', 'Eylers', 'Ineluki');
insert into t_player(firstname, lastname, preferred_nick) values('Christoper', 'Günzel', 'Sanus Fortis');
insert into t_player(firstname, lastname, preferred_nick) values('Jan', 'Hungerland', 'Starship');
insert into t_nickname(nickname, player) values('kewl-deus', (select id from t_player where firstname like 'Dennis' and lastname like 'Gottschalk'));
insert into t_nickname(nickname, player) values('kewldeus', (select id from t_player where firstname like 'Dennis' and lastname like 'Gottschalk'));
insert into t_nickname(nickname, player) values('deus', (select id from t_player where firstname like 'Dennis' and lastname like 'Gottschalk'));
insert into t_nickname(nickname, player) values('DieHard', (select id from t_player where firstname like 'Dennis' and lastname like 'Gottschalk'));

insert into t_nickname(nickname, player) values('Sanus Fortis', (select id from t_player where firstname like 'Christoper' and lastname like 'Günzel'));
insert into t_nickname(nickname, player) values('Sanus_Fortis', (select id from t_player where firstname like 'Christoper' and lastname like 'Günzel'));
insert into t_nickname(nickname, player) values('SanusFortis', (select id from t_player where firstname like 'Christoper' and lastname like 'Günzel'));

insert into t_nickname(nickname, player) values('JaNiWe', (select id from t_player where firstname like 'Jan Niklaas' and lastname like 'Wechselberg'));
insert into t_nickname(nickname, player) values('Obelix', (select id from t_player where firstname like 'Dirk' and lastname like 'Müller'));
insert into t_nickname(nickname, player) values('Neothorn', (select id from t_player where firstname like 'Martin' and lastname like 'Morys'));
insert into t_nickname(nickname, player) values('UNC', (select id from t_player where firstname like 'Martin' and lastname like 'Morys'));
insert into t_nickname(nickname, player) values('INELUKI', (select id from t_player where firstname like 'Ansgar' and lastname like 'Eylers'));
insert into t_nickname(nickname, player) values('=)Starship(=', (select id from t_player where firstname like 'Jan' and lastname like 'Hungerland'));
