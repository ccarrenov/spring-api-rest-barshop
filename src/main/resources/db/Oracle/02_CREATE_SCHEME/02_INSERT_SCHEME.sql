-- -----------------------------------------------------
-- Insert barshop_bbdd
-- -----------------------------------------------------
INSERT INTO country (id, name, country_code, two_digit_iso, three_digit_iso, country_calling_code) 
				VALUES (country_seq.NEXTVAL, 'Chile', 56, 'CL', 'CHL', '+56');
COMMIT;