INSERT INTO "PUBLIC"."ADMINISTRATOR" VALUES 
(1, 'Adminska adresa', 'admin@admin.com', 'Adminko', 'Adminic', 'admin123', '434343', 'ADMINISTRATOR', 'admin123');

INSERT INTO "PUBLIC"."DELIVERER" VALUES 
(1, 'Dostavljacka adresa', 'deliver@deliver.com', 'Dostavljac', 'Dostavljevic', 'deliver123', '999555', 'DELIVERER', 'deliver123');

INSERT INTO "PUBLIC"."CUSTOMER" VALUES 
(1, 'Neka adresa', 'pera@pera.com', 'Pera', 'Peric', 'pera123', '1312232', 'CUSTOMER', 'pera123', NULL, NULL);

INSERT INTO "PUBLIC"."ARTICLE_CATEGORY" VALUES 
(1, 'food'), 
(2, 'drinks'), 
(3, 'stuff');

INSERT INTO "PUBLIC"."ARTICLE" VALUES 
(1, 'neki opis', 0, 'kola', 50.0, 10, 2), 
(2, 'sta god', 8, 'ananas', 200.0, 5, 1), 
(3, 'voda ko voda', 0, 'voda', 30.0, 20, 2), 
(4, 'Neki TV', 10, 'televizor', 35000.0, 8, 3);

INSERT INTO "PUBLIC"."CART" VALUES 
(1, 'DELIVERED', TIMESTAMP '2019-08-29 01:49:21.524', 31500.0, 1, 1), 
(2, 'DELIVERED', TIMESTAMP '2019-08-30 01:49:41.865', 548.0, 1, 1), 
(3, 'CANCELED', TIMESTAMP '2019-08-30 01:49:53.306', 400.0, 1, 1), 
(4, 'ORDERED', TIMESTAMP '2019-08-30 01:51:09.503', 300.0, 1, NULL);

INSERT INTO "PUBLIC"."ITEM" VALUES 
(1, 1, 4, NULL), 
(2, 3, 1, NULL), 
(3, 2, 2, NULL), 
(4, 1, 3, NULL), 
(5, 8, 1, NULL), 
(6, 10, 3, 4);

INSERT INTO "PUBLIC"."FAVORITE_ARTICLES" VALUES 
(1, 1), 
(4, 1);