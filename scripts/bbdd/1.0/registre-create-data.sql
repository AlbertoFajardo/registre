
-- <<<<<<<<<<<< Agrupaci� Geogr�fica >>>>>>>>>>>>>>>>>

INSERT INTO BTIPAGR (FLDCTAGG,FLDDTAGG,FLDFBAJA,FLDCTAGS) VALUES (1,'COMUNIDAD AUTONOMA',0,0);
INSERT INTO BTIPAGR (FLDCTAGG,FLDDTAGG,FLDFBAJA,FLDCTAGS) VALUES (2,'PROVINCIA',0,1);
INSERT INTO BTIPAGR (FLDCTAGG,FLDDTAGG,FLDFBAJA,FLDCTAGS) VALUES (5,'ISLA-COMARCA',0,2);
INSERT INTO BTIPAGR (FLDCTAGG,FLDDTAGG,FLDFBAJA,FLDCTAGS) VALUES (90,'MUNICIPIO',0,5);
INSERT INTO BTIPAGR (FLDCTAGG,FLDDTAGG,FLDFBAJA,FLDCTAGS) VALUES (99,'LOCALIDAD',0,90);

-- <<<<<<<<<<<< Idioma >>>>>>>>>>>>>>>>>

INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('1','Castellano');
INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('2','Catal�n');
INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('3','Biling�e');
INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('4','Vasco');
INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('5','Gallego');
INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('6','Bable');
INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('A','Ingles');
INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('B','Frances');
INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('C','Aleman');
INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('D','Ruso');
INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('E','Italiano');
INSERT INTO BZIDIOM (FZMCIDI, FZMDIDI) VALUES ('F','Portugues');

-- <<<<<<<<<<<< Agrupacions  >>>>>>>>>>>>>>>

INSERT INTO bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) VALUES (4, 1, 0, 'COMUNITAT AUT. ILLES BALEARS', NULL, NULL);

INSERT INTO bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) VALUES (7, 2, 0, 'ILLES BALEARS', 4, 1);

INSERT INTO bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) VALUES (1, 5, 0, 'EIVISSA', 7, 2);
INSERT INTO bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) VALUES (2, 5, 0, 'MALLORCA', 7, 2);
INSERT INTO bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) VALUES (3, 5, 0, 'MENORCA', 7, 2);
INSERT INTO bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) VALUES (4, 5, 0, 'FORMENTERA', 7, 2);

insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (1, 90, 0, 'Alar�', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (2, 90, 0, 'Alaior', 3, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (3, 90, 0, 'Alc�dia', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (4, 90, 0, 'Algaida', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (5, 90, 0, 'Andratx', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (6, 90, 0, 'Art�', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (7, 90, 0, 'Banyalbufar', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (8, 90, 0, 'Binissalem', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (9, 90, 0, 'B�ger', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (10, 90, 0, 'Bunyola', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (11, 90, 0, 'Calvi�', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (12, 90, 0, 'Campanet', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (13, 90, 0, 'Campos', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (14, 90, 0, 'Capdepera', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (15, 90, 0, 'Ciutadella de Menorca', 3, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (16, 90, 0, 'Consell', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (17, 90, 0, 'Costitx', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (18, 90, 0, 'Dei�', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (19, 90, 0, 'Escorca', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (20, 90, 0, 'Esporles', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (21, 90, 0, 'Estellencs', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (22, 90, 0, 'Felanitx', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (23, 90, 0, 'Ferreries', 3, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (24, 90, 0, 'Formentera', 4, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (25, 90, 0, 'Fornalutx', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (26, 90, 0, 'Eivissa', 1, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (27, 90, 0, 'Inca', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (28, 90, 0, 'Lloret de Vistalegre', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (29, 90, 0, 'Lloseta', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (30, 90, 0, 'Llub�', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (31, 90, 0, 'Llucmajor', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (32, 90, 0, 'Ma�', 3, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (33, 90, 0, 'Manacor', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (34, 90, 0, 'Mancor de la Vall', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (35, 90, 0, 'Maria de la Salut', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (36, 90, 0, 'Marratx�', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (37, 90, 0, 'Mercadal (Es)', 3, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (38, 90, 0, 'Montu�ri', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (39, 90, 0, 'Muro', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (40, 90, 0, 'Palma', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (41, 90, 0, 'Petra', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (42, 90, 0, 'Pollen�a', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (43, 90, 0, 'Porreres', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (44, 90, 0, 'Pobla (Sa)', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (45, 90, 0, 'Puigpunyent', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (46, 90, 0, 'Sant Antoni de Portmany', 1, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (47, 90, 0, 'Sencelles', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (48, 90, 0, 'Sant Josep de sa Talaia', 1, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (49, 90, 0, 'Sant Joan', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (50, 90, 0, 'Sant Joan de Labritja', 1, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (51, 90, 0, 'Sant Lloren� des Cardassar', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (52, 90, 0, 'Sant Llu�s', 3, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (53, 90, 0, 'Santa Eug�nia', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (54, 90, 0, 'Santa Eul�ria des Riu', 1, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (55, 90, 0, 'Santa Margalida', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (56, 90, 0, 'Santa Maria del Cam�', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (57, 90, 0, 'Santany�', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (58, 90, 0, 'Selva', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (59, 90, 0, 'Salines (Ses)', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (60, 90, 0, 'Sineu', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (61, 90, 0, 'S�ller', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (62, 90, 0, 'Son Servera', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (63, 90, 0, 'Valldemossa', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (64, 90, 0, 'Castell (Es)', 3, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (65, 90, 0, 'Vilafranca de Bonany', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (901, 90, 0, 'Ariany', 2, 5);
insert into bagruge (fabcagge, fabctagg, fabfbaja, fabdagge, fabcagsu, fabctasu) values (902, 90, 0, 'Migjorn Gran (Es)', 3, 5);