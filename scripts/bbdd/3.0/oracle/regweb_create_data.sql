--IDIOMA
INSERT INTO RWE_IDIOMA (id,lang,nombre,orden) VALUES (1,'ca','Catal�n',0);
INSERT INTO RWE_IDIOMA (id,lang,nombre,orden) VALUES (2,'es','Castellano',1);

--IDIOMA REGISTRO
INSERT INTO RWE_IDIOMA_REGISTRO (id,nombre,codigo,orden) VALUES (1,'Catal�n','ca',0);
INSERT INTO RWE_IDIOMA_REGISTRO (id,nombre,codigo,orden) VALUES (2,'Castellano','es',1);
INSERT INTO RWE_IDIOMA_REGISTRO (id,nombre,codigo,orden) VALUES (3,'Gallego','gl',2);
INSERT INTO RWE_IDIOMA_REGISTRO (id,nombre,codigo,orden) VALUES (4,'Euskera','eu',3);
INSERT INTO RWE_IDIOMA_REGISTRO (id,nombre,codigo,orden) VALUES (5,'Ingl�s','en',4);

--ROL
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (1,'RWE_SUPERADMIN','Administrador',1);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (2,'RWE_ADMIN','Admin. Entitat',2);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (3,'RWE_USUARI','Operador',3);
