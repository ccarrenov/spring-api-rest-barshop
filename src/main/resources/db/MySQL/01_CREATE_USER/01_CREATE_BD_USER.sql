-- -----------------------------------------------------
-- Schema barshop_bbdd
-- -----------------------------------------------------
-- CREATE USER DB
CREATE USER 'barshop_user'@'%' IDENTIFIED BY '15B8ijW21DcsPCSwsax392';
-- CREATE DATABASE
CREATE DATABASE IF NOT EXISTS `barshop_bbdd` DEFAULT CHARACTER SET utf8 ;
-- GRANT USER IN BD
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, REFERENCES, CREATE VIEW, TRIGGER, DROP, ALTER, INDEX ON * TO 'barshop_user'@'%';
