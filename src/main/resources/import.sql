/* Populate Table Customer*/
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Antoni','L','antoni@gmail.com','2023-12-15','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Hendra','L','hendra@gmail.com','2023-12-17','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Samuel','L','Samuel@gmail.com','2023-12-17','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Rakuel','L','Rakuel@gmail.com','2023-12-17','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Takuel','L','Takuel@gmail.com','2023-12-17','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Mree','L','Mree@gmail.com','2023-12-17','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Seira','L','Seira@gmail.com','2023-12-17','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Renal','L','Renal@gmail.com','2023-12-17','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Tako','L','Tako@gmail.com','2023-12-17','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Maik','L','Maik@gmail.com','2023-12-17','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Robin','L','Robin@gmail.com','2023-12-17','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Neil','L','Neil@gmail.com','2023-12-17','');
INSERT INTO customers(name, last_name, email, created_at, photo) VALUES('Lea','L','Lea@gmail.com','2023-12-17','');

/* Populate table Product */
INSERT INTO products(name,price,created_at) VALUES('Panasonic',250000, NOW());
INSERT INTO products(name,price,created_at) VALUES('Sony',123000, NOW());
INSERT INTO products(name,price,created_at) VALUES('Apple',149000, NOW());
INSERT INTO products(name,price,created_at) VALUES('Hewlett',699000, NOW());
INSERT INTO products(name,price,created_at) VALUES('Bianchi',299000, NOW());

/* We create some invoices */
INSERT INTO invoices(description,observation,customer_id,created_at) VALUES ("Office equipment invoice", null, 1, NOW());
INSERT INTO invoices_items(amount, invoice_id, product_id) VALUES(1,1,1);
INSERT INTO invoices_items(amount, invoice_id, product_id) VALUES(2,1,4);
INSERT INTO invoices_items(amount, invoice_id, product_id) VALUES(1,1,7);
INSERT INTO invoices_items(amount, invoice_id, product_id) VALUES(1,1,5);
INSERT INTO invoices(description,observation,customer_id,created_at) VALUES ("Invoice Bicycle", "Some important note", 1, NOW());
INSERT INTO invoices_items(amount, invoice_id, product_id) VALUES(3,2,5);

/* Create Data User */
INSERT Into Users(username,password,enabled) VALUES ('andres','$2a$10$nwsX/Iu2BRfGjnn23yLQLO/cHWbr2dyzlNR6ajgWmc5KvrEt3jgCS',1);
INSERT Into Users(username,password,enabled) VALUES ('admin','$2a$10$Ik2ploR6mqvhFp4SULA4t.vUtc.QJHudjQvNhGt7dlxLan2gypBta',1);
INSERT INTO Authorities(user_id, authority) VALUES(1,'ROLE_USER');
INSERT INTO Authorities(user_id, authority) VALUES(2,'ROLE_USER');
INSERT INTO Authorities(user_id, authority) VALUES(2,'ROLE_ADMIN');