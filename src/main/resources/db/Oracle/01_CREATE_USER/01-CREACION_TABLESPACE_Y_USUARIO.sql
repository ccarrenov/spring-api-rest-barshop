--SET PARA IMPRIMIR EN SALIDA POR DEFAULT
SET SERVEROUTPUT  ON;
--BLOQUE ANONIMO CREAR O REEMPLAZAR TABLESPACE
DECLARE
  --VARIABLE QUE VERIFICA SI EXISTE TABLESPACE
  V_EXIST_TBLSP NUMBER;
  --VARIABLE QUE VERIFICA SI EXISTE TABLESPACE IDX
  V_EXIST_TBLSP_IDX NUMBER;  
  --VARIABLE QUE VERIFICA SI EXISTE TABLESPACE TEMPORAL  
  V_EXIST_TBLSP_TMP NUMBER;
  --VARIABLE DE LA NUEVA RUTA DEL TABLESPACE Y TABLESPACE TEMPORAL
  V_RUTA_TBLSP VARCHAR2(50);
  --VARIABLE DONDE SE ALMACENA EL NOMBRE DEL TABLESPACE  
  V_NOM_TBLSP VARCHAR2(50);
  --VARIABLE DONDE SE ALMACENA EL NOMBRE DEL TABLESPACE IDX
  V_NOM_TBLSP_IDX VARCHAR2(50);  
  --VARIABLE DONDE SE ALMACENA EL NOMBRE DEL TABLESPACE TEMPORAL  
  V_NOM_TBLSP_TMP VARCHAR2(50);  
  --VARIABLE DEL NOMBRE DEL DATAFILE
  V_NOMBRE_DT VARCHAR2(50);   
  --VARIABLE DEL NOMBRE DEL DATAFILE
  V_NOMBRE_DT_IDX VARCHAR2(50);      
  --VARIABLE DEL NOMBRE DEL TEMP
  V_NOMBRE_DT_TMP VARCHAR2(50); 
  --VARIABLE PARA TAMA�O .DBF DE DATOS
  V_TAMANIO_DT_FILE VARCHAR(10);
  --VARIABLE PARA TAMA�O .DBF DATOS INDICES
  V_TAMANIO_DT_IDX VARCHAR(10);  
  --VARIABLE PARA TAMA�O .DBF TEMPORAL
  V_TAMANIO_DT_TMP VARCHAR(10); 
  --VARIABLE QUE ALMACENA COMANDOS PARA EJECUTAR
  V_COMMAND VARCHAR(450);
  --VARIABLE QUE VERIFICA SI EXISTE EL ROL
  V_EXIT_ROL NUMBER;
  --VARIABLE CON EL NOMBRE DEL ROL
  V_NOM_ROL VARCHAR(50);
  --VARIABLE CON EL NOMBRE DE USUARIO
  V_NOM_USER VARCHAR(50);
  --VARIABLE CON LA PASSWORD DEL USUARIO
  V_PASS_WORD VARCHAR(50);
  --VARIABLE QUE VERIFICA SI EXISTE EL USUARIO
  V_EXIT_USER VARCHAR(50);
BEGIN
  V_NOM_USER := 'barshop_bbdd';
  V_PASS_WORD := '15B8ijW21DcsPCSwsax392';
  V_NOM_TBLSP := 'SDAT_' || V_NOM_USER;
  V_NOM_TBLSP_IDX := 'SDAT_' || V_NOM_USER || '_IDX';
  V_NOM_TBLSP_TMP := 'SDAT_' || V_NOM_USER || '_TMP';
  V_NOMBRE_DT := '''DT_' || V_NOM_USER || '.DBF''';
  V_NOMBRE_DT_IDX := '''DT_' || V_NOM_USER || '_IDX.DBF''';  
  V_NOMBRE_DT_TMP := '''DT_' || V_NOM_USER || '_TMP.DBF''';
  V_RUTA_TBLSP := '';/*'''C:\';*/
  V_TAMANIO_DT_FILE := '100M';
  V_TAMANIO_DT_IDX := '50M';
  V_TAMANIO_DT_TMP := '100M';
  V_COMMAND := '';
  V_NOM_ROL := V_NOM_USER || '_ROL';

  ---------------------- TABLESPACE DATA -----------------------------------------------------------------------------------------------
  DBMS_OUTPUT.PUT_LINE('BUSCANDO SI EXISTE TABLESPACE DE DATOS ... ' || V_NOM_TBLSP);    
  SELECT COUNT(*)
  INTO V_EXIST_TBLSP
  FROM DBA_DATA_FILES
  WHERE TABLESPACE_NAME = V_NOM_TBLSP;
  DBMS_OUTPUT.PUT_LINE('RESULTADO ... ' || V_EXIST_TBLSP);      
  
  IF V_EXIST_TBLSP > 0 THEN
    DBMS_OUTPUT.PUT_LINE('ELIMINANDO TABLESPACE ' || V_NOM_TBLSP ||' ...');    
    V_COMMAND := 'DROP TABLESPACE '|| V_NOM_TBLSP ||' INCLUDING CONTENTS AND DATAFILES CASCADE CONSTRAINTS';
    DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND);        
    EXECUTE IMMEDIATE V_COMMAND;
  END IF;
    
  DBMS_OUTPUT.PUT_LINE('CREANDO TABLESPACE DE DATOS ... ' || V_NOM_TBLSP); 
  V_COMMAND := 'CREATE TABLESPACE '|| V_NOM_TBLSP ||' DATAFILE ' || V_RUTA_TBLSP|| V_NOMBRE_DT || ' SIZE ' ||V_TAMANIO_DT_FILE;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND);   
  EXECUTE IMMEDIATE V_COMMAND;  
  ---------------------- TABLESPACE INDICE  --------------------------------------------------------------------------------------------
  DBMS_OUTPUT.PUT_LINE('BUSCANDO SI EXISTE TABLESPACE DE INDICES ... ' || V_NOM_TBLSP_IDX);    
  SELECT COUNT(*)
  INTO V_EXIST_TBLSP_IDX
  FROM DBA_DATA_FILES
  WHERE TABLESPACE_NAME = V_NOM_TBLSP_IDX;
  DBMS_OUTPUT.PUT_LINE('RESULTADO ... ' || V_EXIST_TBLSP_IDX);      
  
  IF V_EXIST_TBLSP_IDX > 0 THEN
    DBMS_OUTPUT.PUT_LINE('ELIMINANDO TABLESPACE ' || V_NOM_TBLSP_IDX ||' ...');    
    V_COMMAND := 'DROP TABLESPACE '|| V_NOM_TBLSP_IDX ||' INCLUDING CONTENTS AND DATAFILES CASCADE CONSTRAINTS';
    DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND);        
    EXECUTE IMMEDIATE V_COMMAND;
  END IF;
    
  DBMS_OUTPUT.PUT_LINE('CREANDO TABLESPACE DE INDICES ... ' || V_NOM_TBLSP_IDX); 
  V_COMMAND := 'CREATE TABLESPACE '|| V_NOM_TBLSP_IDX ||' DATAFILE ' || V_RUTA_TBLSP|| V_NOMBRE_DT_IDX || ' SIZE ' ||V_TAMANIO_DT_IDX;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND);   
  EXECUTE IMMEDIATE V_COMMAND; 
  ---------------------- TABLESPACE TEMPORAL    ----------------------------------------------------------------------------------------
  DBMS_OUTPUT.PUT_LINE('BUSCANDO SI EXISTE TABLESPACE TEMPORAL ... ' || V_NOM_TBLSP_TMP);      
  SELECT COUNT(*)
  INTO V_EXIST_TBLSP_TMP
  FROM DBA_TEMP_FILES
  WHERE TABLESPACE_NAME = V_NOM_TBLSP_TMP;  
  DBMS_OUTPUT.PUT_LINE('RESULTADO ... ' || V_EXIST_TBLSP_TMP);      
  
  IF V_EXIST_TBLSP_TMP > 0 THEN
    DBMS_OUTPUT.PUT_LINE('ELIMINANDO TABLESPACE TEMPORAL' || V_NOM_TBLSP_TMP ||' ...'); 
    V_COMMAND := 'DROP TABLESPACE '|| V_NOM_TBLSP_TMP ||' INCLUDING CONTENTS AND DATAFILES CASCADE CONSTRAINTS';
    DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND);       
    EXECUTE IMMEDIATE V_COMMAND;
  END IF;  

  DBMS_OUTPUT.PUT_LINE('CREANDO TABLESPACE TEMPORAL ... ' || V_NOM_TBLSP_TMP); 
  V_COMMAND := 'CREATE TEMPORARY TABLESPACE '|| V_NOM_TBLSP_TMP ||' TEMPFILE ' || V_RUTA_TBLSP|| V_NOMBRE_DT_TMP || ' SIZE ' ||V_TAMANIO_DT_FILE;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND);   
  EXECUTE IMMEDIATE V_COMMAND;  

  ---------------------- CREAR O REEMPLAZAR ROL    ----------------------------------------------------------------------------------------
  DBMS_OUTPUT.PUT_LINE('BUSCANDO SI EXISTE ROL ... ' || V_EXIT_ROL);      
  SELECT COUNT(ROLE) 
  INTO V_EXIT_ROL
  FROM DBA_ROLES
  WHERE ROLE = V_NOM_ROL;
  DBMS_OUTPUT.PUT_LINE('RESULTADO ... ' || V_EXIT_ROL);      

  IF V_EXIT_ROL = 0 THEN
    DBMS_OUTPUT.PUT_LINE('CREANDO ROL ... ' || V_NOM_ROL); 
    V_COMMAND := 'CREATE ROLE ' || V_NOM_ROL;
    DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND); 
    EXECUTE IMMEDIATE V_COMMAND;    
  END IF;
  
  DBMS_OUTPUT.PUT_LINE('MODIFICANDO PERMISOS ... ' || V_NOM_ROL); 
  V_COMMAND := 'GRANT CREATE SESSION TO ' || V_NOM_ROL;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND); 
  EXECUTE IMMEDIATE V_COMMAND;  
  
  V_COMMAND := 'GRANT CREATE ANY TABLE TO ' || V_NOM_ROL;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND); 
  EXECUTE IMMEDIATE V_COMMAND;
  
  V_COMMAND := 'GRANT CREATE ROLE TO ' || V_NOM_ROL;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND); 
  EXECUTE IMMEDIATE V_COMMAND;
  
  V_COMMAND := 'GRANT CREATE USER TO ' || V_NOM_ROL;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND); 
  EXECUTE IMMEDIATE V_COMMAND;
  
  V_COMMAND := 'GRANT CREATE VIEW TO ' || V_NOM_ROL;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND); 
  EXECUTE IMMEDIATE V_COMMAND;
  
  V_COMMAND := 'GRANT CREATE ANY INDEX TO ' || V_NOM_ROL;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND); 
  EXECUTE IMMEDIATE V_COMMAND;
  
  V_COMMAND := 'GRANT CREATE TRIGGER TO ' || V_NOM_ROL;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND); 
  EXECUTE IMMEDIATE V_COMMAND;
  
  V_COMMAND := 'GRANT CREATE PROCEDURE TO ' || V_NOM_ROL;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND); 
  EXECUTE IMMEDIATE V_COMMAND;
  
  V_COMMAND := 'GRANT CREATE SEQUENCE TO ' || V_NOM_ROL;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND); 
  EXECUTE IMMEDIATE V_COMMAND;
  
  --V_COMMAND := 'GRANT EXECUTE ON DBMS_CRYPTO TO ' || V_NOM_ROL;
  --DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND);  
  --EXECUTE IMMEDIATE V_COMMAND;
  
  ---------------------- CREAR O REEMPLAZAR USER    ----------------------------------------------------------------------------------------
  DBMS_OUTPUT.PUT_LINE('BUSCANDO SI EXISTE USUARIO ... ' || V_NOM_USER);        
  SELECT COUNT(USERNAME)
  INTO V_EXIT_USER
  FROM DBA_USERS
  WHERE USERNAME = V_NOM_USER;
  DBMS_OUTPUT.PUT_LINE('RESULTADO ... ' || V_EXIT_USER);      

  IF V_EXIT_USER > 0 THEN
    DBMS_OUTPUT.PUT_LINE('BORRANDO USUARIO ... ' || V_NOM_USER); 
    V_COMMAND := 'DROP USER ' || V_NOM_USER || ' CASCADE';
    DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND);
    EXECUTE IMMEDIATE V_COMMAND;    
  END IF;

  DBMS_OUTPUT.PUT_LINE('CREANDO USUARIO ... ' || V_NOM_USER); 
  V_COMMAND := 'CREATE USER ' || V_NOM_USER || ' IDENTIFIED BY "' ||V_PASS_WORD 
  || '" DEFAULT TABLESPACE ' || V_NOM_TBLSP || ' TEMPORARY TABLESPACE ' || V_NOM_TBLSP_TMP || ' ACCOUNT UNLOCK';
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND);
  EXECUTE IMMEDIATE V_COMMAND;
  
  DBMS_OUTPUT.PUT_LINE('DAR PERMISOS AL USUARIO ' || V_NOM_USER || ' SOBRE TABLESPACES DEFAULT ' || V_NOM_TBLSP ||  '... '); 
  V_COMMAND := 'GRANT UNLIMITED TABLESPACE TO ' || V_NOM_USER;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND);  
  EXECUTE IMMEDIATE V_COMMAND;
  
  DBMS_OUTPUT.PUT_LINE('OTORGAR LOS PERMISOS DEL ROL '||V_NOM_ROL || ' SOBRE EL USUARIO ' || V_NOM_USER || ' ... '); 
  V_COMMAND := 'GRANT ' || V_NOM_ROL ||' TO ' || V_NOM_USER;
  DBMS_OUTPUT.PUT_LINE('EJECUTANDO COMANDO: ' || V_COMMAND);   
  EXECUTE IMMEDIATE V_COMMAND;
  
  DBMS_OUTPUT.PUT_LINE('RESUMEN: ');
  DBMS_OUTPUT.PUT_LINE('USUARIO -> ' || V_NOM_USER);
  DBMS_OUTPUT.PUT_LINE('PASSWORD -> ' || V_PASS_WORD);
  
--BLOQUE DE EXCEPTION
EXCEPTION 
  --EXCEPTION GENERICA
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('ERROR SQL : ' || SQLERRM);
END;
