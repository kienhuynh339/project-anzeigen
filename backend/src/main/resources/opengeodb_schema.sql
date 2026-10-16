/*
 * MySQL
 */


--SET NAMES 'utf8';

--BEGIN;
/*
 * Table structure for table 'geodb_typ'
 */

CREATE TABLE geodb_typ (
  type                 integer not null primary key,
  fld                  varchar(10) not null default '',
  name                 varchar(250) not null default ''
); --TYPE=InnoDB CHARACTER SET utf8;

INSERT INTO geodb_typ VALUES (1,'adm0','Staat');
INSERT INTO geodb_typ VALUES (2,'adm1','Bundesland / Kanton');
INSERT INTO geodb_typ VALUES (3,'adm2','Regierungsbezirk / Bezirk');
INSERT INTO geodb_typ VALUES (4,'adm3','Landkreis');
INSERT INTO geodb_typ VALUES (5,'adm4','Verwaltungszusammenschluss');
INSERT INTO geodb_typ VALUES (6,'ort','Stadt / Gemeinde');
INSERT INTO geodb_typ VALUES (7,'ortsteil','Orts- / Stadtteil');
INSERT INTO geodb_typ VALUES (8,'gemteil','unspezifizierter Gemeindeteil');
INSERT INTO geodb_typ VALUES (9,'wohnplatz','Wohnplatz');

/*
 * Table structure for table 'geodb_adm0'
 */

CREATE TABLE geodb_adm0 (
  adm0                 char(2) NOT NULL default '',
  name                 varchar(50) NOT NULL default '',
  PRIMARY KEY (adm0)
);--TYPE=InnoDB CHARACTER SET utf8;

/*
 * Table structure for table 'geodb_adm1'
 */

CREATE TABLE geodb_adm1 (
  adm0                 char(2) NOT NULL default '',
  adm1                 char(2) NOT NULL default '',
  gs                   char(2) NOT NULL default '',
  name                 varchar(50) NOT NULL default '',
  PRIMARY KEY (adm0,adm1)
 -- UNIQUE KEY adm0 (adm0,adm1)
); --TYPE=InnoDB CHARACTER SET utf8;

/*
 * Table structure for table 'geodb_locations'
 */

CREATE TABLE geodb_locations (
  id                   integer not null primary key,
  typ                  integer default NULL,
  name                 varchar(250) default NULL,
  name_int             varchar(250) default NULL,
  gs                   varchar(8) default NULL,
  adm0                 char(2) default NULL,
  adm1                 char(2) default NULL,
  adm2                 varchar(250) default NULL,
  adm3                 varchar(250) default NULL,
  adm4                 varchar(250) default NULL,
  ort                  varchar(250) default NULL,
  ortsteil             varchar(250) default NULL,
  gemteil              varchar(250) default NULL,
  wohnplatz            varchar(250) default NULL,
  breite               float default NULL,
  laenge               float default NULL,
  kfz                  char(3) default NULL,
  plz                  text
  -- UNIQUE KEY id (id),
--   KEY id_2 (id),
--   KEY ort (ort),
--   KEY staat (adm0),
--   KEY land (adm1),
--   KEY regbez (adm2),
--   KEY landkreis (adm3)
); --TYPE=InnoDB CHARACTER SET utf8;
CREATE INDEX idx_locations_id
    ON geodb_locations(id);

CREATE INDEX idx_locations_ort
    ON geodb_locations(ort);

CREATE INDEX idx_locations_adm0
    ON geodb_locations(adm0);

CREATE INDEX idx_locations_adm1
    ON geodb_locations(adm1);

CREATE INDEX idx_locations_adm2
    ON geodb_locations(adm2);

CREATE INDEX idx_locations_adm3
    ON geodb_locations(adm3);


/*
 * Table structure for table 'geodb_areacodes'
 */

CREATE TABLE geodb_areacodes (
  id                   integer NOT NULL,
  plz                  varchar(5),
  --UNIQUE KEY idp (id,plz),
  CONSTRAINT uq_geodb_areacodes_id_plz UNIQUE (id, plz)
  --KEY plz (plz)
); --TYPE=InnoDB CHARACTER SET utf8;
CREATE INDEX idx_geodb_areacodes_plz
    ON geodb_areacodes(plz);
-- /*
--  * Table structure for table 'geodb_population'
--  */
--
-- CREATE TABLE geodb_population (
--   id                   integer NOT NULL default '0',
--   population_min       integer default NULL,
--   population_max       integer default NULL,
--   due_date             date default NULL,
-- --   UNIQUE KEY id_due_date (id,due_date),
--   CONSTRAINT uq_geodb_population_id_due_date
--       UNIQUE (id, due_date)
-- --   KEY id (id),
-- --   KEY population_min (population_min),
-- --   KEY population_max (population_max),
-- --   KEY due_date (due_date)
-- ); --TYPE=InnoDB CHARACTER SET utf8;
-- CREATE INDEX idx_geodb_population_id
--     ON geodb_population(id);
--
-- CREATE INDEX idx_geodb_population_min
--     ON geodb_population(population_min);
--
-- CREATE INDEX idx_geodb_population_max
--     ON geodb_population(population_max);
--
-- CREATE INDEX idx_geodb_population_due_date
--     ON geodb_population(due_date);
-- --COMMIT;
--


