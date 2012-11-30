drop table if exists FACTION;
drop table if exists UNIT;
drop table if exists TYPE;

create table FACTION
(ID int primary key auto_increment,
NAME varchar_ignorecase(255) not null);

create table UNIT
(ID int primary key auto_increment,
NAME varchar_ignorecase(255) not null,
TYPE tinyint not null,
POINT tinyint not null);

create table TYPE
(ID int primary key auto_increment,
TYPE varchar_ignorecase(255) not null);

insert into faction (name) VALUES
('Cryx'),('Khador'),('Cygnar'),('Protectorate of Menoth'),('Retribution of Scyrah'),
('Four Star Syndicate'),('Highborn Covenant'),('Searforge Covenant'),('Talion Charter'),
('Circle of Orboros'),('Legion of Everblight'),('Skorne'),('Trollbloods'),('Blindwater Congregation'),('Thornfall Alliance');

insert into TYPE (type) VALUES
('Warcaster'),('Warlock'),('Warjack'),('Warbeast'),('Unit'),('Attachment'),('Solo');

insert into unit ( name , type , point ) values
('General Ossrum',1,-6),('Ghordson Driller',3,6),('Horgenhold Forge Guard',5,8),('Sylys Wyshnalyrr',6,2),('Eiryss, Angel of Retribution',7,2);
