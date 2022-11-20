insert into roles(insert_date_time, insert_user_id, is_deleted, last_update_date_time,
                  last_update_user_id, description)
values ('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1, 'Root User'),
       ('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1, 'Admin'),
       ('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1, 'Manager'),
       ('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1, 'Employee');

insert into addresses(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                      address_line1, address_line2, city, state, country, zip_code)
values ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        '7925 Jones Branch Dr, #3300', 'Tysons', 'Virginia', 'VA', 'USA', '22102-1234'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'Future Street', 'Times Square', 'Atlanta', 'Alabama', 'USA', '54321-4321'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'North Street', 'Circle Square', 'San Francisco', 'California', 'USA', '65245-8546'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'West Street', 'Triangle Square', 'Los Angeles', 'California', 'USA', '54782-5214'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'East Street', 'Cube Square', 'Los Angeles', 'California', 'USA', '54782-5214'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'South Street', 'Times Square', 'Los Angeles', 'California', 'USA', '54782-5214'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'SouthWest Street', 'Puzzle Square', 'Los Angeles', 'California', 'USA', '65654-8989');

insert into companies(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                      title, phone, website, address_id, company_status)
values ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'CYDEO','+1 (652) 852-8888', 'https://www.cydeo.com', 1, 'ACTIVE'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'Green Tech','+1 (652) 852-3246', 'https://www.greentech.com', 2, 'ACTIVE'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'Blue Tech','+1 (215) 654-5268', 'https://www.bluetech.com', 3, 'ACTIVE'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'Red Tech','+1 (215) 846-2642', 'https://www.redtech.com', 4, 'PASSIVE');

insert into users(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                  username, password, firstname, lastname, phone, role_id, company_id, enabled)
values
-- COMPANY-1 / CYDEO / ROOT USER
('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1,
 'root@cydeo.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
 'Robert', 'Martin', '+1 (852) 564-5874', 1, 1, true),
-- COMPANY-2 / Green Tech / ADMIN-1
('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1,
 'admin@greentech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
 'Mary', 'Grant', '+1 (234) 345-4362', 2, 2, true),
-- COMPANY-2 / Green Tech / ADMIN-2
('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1,
 'admin2@greentech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
 'Garrison', 'Short', '+1 (234) 356-7865', 2, 2, true),
-- COMPANY-2 / Green Tech / MANAGER
('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1,
 'manager@greentech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
 'Robert', 'Noah', '+1 (234) 564-5874', 3, 2, true),
-- COMPANY-2 / Green Tech / EMPLOYEE
('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1,
 'employee@greentech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
 'Mike', 'Times', '+1 (234) 741-8569', 4, 2, true),
-- COMPANY-3 / Blue Tech / ADMIN
('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1,
 'admin@bluetech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
 'Chris', 'Brown', '+1 (356) 258-3544', 2, 3, true),
-- COMPANY-4 / Red Tech / ADMIN
('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1,
 'admin@redtech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
 'John', 'Doe', '+1 (659) 756-1265', 2, 4, true);

insert into clients_vendors(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                            client_vendor_type, company_name, phone, website, address_id, company_id)
values
-- -- COMPANY-2
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  'Orange', 'CLIENT', '+1 (251) 321-4141', 'https://www.orange.com', 5, 2),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  'Green Tech', 'VENDOR', '+1 (652) 852-3246', 'https://www.greentech.com', 6, 2),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  'Purple Tech', 'VENDOR', '+1 (652) 852-3246', 'https://www.purpletech.com', 7, 2),
-- -- COMPANY-3
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  'Gray Tech', 'CLIENT', '+1 (652) 852-9544', 'https://www.greytech.com', 3, 3),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  'Brown Tech', 'VENDOR', '+1 (652) 852-2055', 'https://www.browntech.com', 4, 3);

-- COMPANY-2 / Green Tech
('2022-09-15T00:00',2, false,'2022-09-15T00:00',2,'CLIENT','Orange Tech', '+1 (251) 321-4141', 'https://www.orange.com', 5, 2),
('2022-09-15T00:00',2, false,'2022-09-15T00:00',2,'VENDOR','Photobug Tech', '+1 (652) 852-3246', 'https://www.photobug.com', 6, 2),
('2022-09-15T00:00',2, false,'2022-09-15T00:00',2,'VENDOR','Wordtune Tech','+1 (652) 852-3246','https://www.wordtune.com', 7, 2),
-- COMPANY-3 / Blue Tech
('2022-09-15T00:00',3, false,'2022-09-15T00:00',3,'CLIENT', 'Reallinks Tech', '+1 (652) 852-9544','https://www.reallinks.com', 3, 3),
('2022-09-15T00:00',3, false,'2022-09-15T00:00',3,'VENDOR', 'Livetube Tech', '+1 (652) 852-2055','https://www.livetube.com', 4, 3);



insert into categories(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                       description, company_id)
values
-- COMPANY-2 / Green Tech
('2022-09-15 00:00:00', 2, false, '2022-09-15 00:00:00', 2, 'Computer', 2),
('2022-09-15 00:00:00', 2, false, '2022-09-15 00:00:00', 2, 'Phone', 2),
-- COMPANY-3 / Blue Tech
('2022-09-15 00:00:00', 3, false, '2022-09-15 00:00:00', 3, 'Phone', 3),
('2022-09-15 00:00:00', 3, false, '2022-09-15 00:00:00', 3, 'TV', 3),
('2022-09-15 00:00:00', 3, false, '2022-09-15 00:00:00', 3, 'Monitor', 3);

insert into products(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     name, quantity_in_stock, low_limit_alert, product_unit, category_id)
-- values ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--         'Casper Excalibur D-550', 0, 5, 'PCS', 1),
--        ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--         'Apple Mac Book Pro 14 inch', 20, 5, 'PCS', 1),
--        ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--         'Apple IPhone-13', 0, 5, 'PCS', 2),
--        ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--         'Samsung Galaxy S22', 20, 5, 'PCS', 2),
--        ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--         'Apple IPhone-13', 0, 5, 'PCS', 3),
--        ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--         'Samsung Galaxy S22', 20, 5, 'PCS', 3);
 VALUES
-- COMPANY-2 / Green Tech
    ('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, 'HP Elite 800G1 Desktop Computer Package',8,5,'PCS',1),
    ('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, '2021 Apple MacBook Pro',0,5,'PCS',1),
    ('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, 'Apple IPhone-13',0,5,'PCS',2),
    ('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, 'SAMSUNG Galaxy S22',0,5,'PCS',2),
-- COMPANY-3 / Blue Tech
    ('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 'Samsung Galaxy S20 (renewed)', 0, 5, 'PCS', 3),
    ('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 'Samsung Galaxy S22', 0, 5, 'PCS', 3);


insert into invoices(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     date,invoice_no, invoice_type, invoice_status, client_vendor_id, company_id)
values
-- COMPANY-2 / Green Tech
 ('2022-09-10 00:00','1','false','2022-09-11 00:00',1,'2022-09-11','P-001','PURCHASE','APPROVED',2,2),
 ('2022-09-10 00:00','1','false','2022-09-11 00:00',1,'2022-09-11','P-002','PURCHASE','APPROVED',3,2),
 ('2022-09-11 00:00','1','false','2022-09-12 00:00',1,'2022-09-12','S-001','SALES','APPROVED',1,2),
 ('2022-09-12 00:00','1','false','2022-09-13 00:00',1,'2022-09-13','S-002','SALES','AWAITING_APPROVAL',1,2);

-- COMPANY-3 / Blue Tech


-- -- COMPANY-2
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  'P-001', 'APPROVED', 'PURCHASE', '2022-08-01 00:00:00', 2, 1),
-- ('2022-09-06 00:00:01', 1, false, '2022-09-06 00:00:00', 1,
--  'P-002', 'APPROVED', 'PURCHASE', '2022-08-02 00:00:00', 2, 2),
-- ('2022-09-07 00:00:00', 1, false, '2022-09-07 00:00:00', 1,
--  'S-001', 'APPROVED', 'SALES', '2022-09-03 00:00:00', 2, 2),
-- ('2022-09-08 00:00:00', 1, false, '2022-09-08 00:00:00', 1,
--  'S-002', 'APPROVED', 'SALES', '2022-10-04 00:00:00', 2, 2),
-- ('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1,
--  'P-003', 'AWAITING_APPROVAL', 'PURCHASE', '2022-10-05 00:00:00', 2, 2),
-- -- COMPANY-3
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  'P-001', 'APPROVED', 'PURCHASE', '2022-08-01 00:00:00', 3, 1),
-- ('2022-09-06 00:00:01', 1, false, '2022-09-06 00:00:00', 1,
--  'P-002', 'APPROVED', 'PURCHASE', '2022-08-02 00:00:00', 3, 2),
-- ('2022-09-07 00:00:00', 1, false, '2022-09-07 00:00:00', 1,
--  'S-001', 'APPROVED', 'SALES', '2022-09-03 00:00:00', 3, 2),
-- ('2022-09-08 00:00:00', 1, false, '2022-09-08 00:00:00', 1,
--  'S-002', 'APPROVED', 'SALES', '2022-10-04 00:00:00', 3, 2),
-- ('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1,
--  'P-003', 'AWAITING_APPROVAL', 'PURCHASE', '2022-10-05 00:00:00', 3, 2);
--
insert into invoice_products(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                              price,quantity, remaining_quantity, tax, total, profit_loss, invoice_id, product_id)

    values
-- COMPANY-2 / Green Tech
--         one product enough for two sale
--  ('2022-09-15 00:00',1,'false','2022-09-15 00:00',1,227.27,10,8,10,2272.73,0,1,1),
--         second product required for second sale
 ('2022-09-15 00:00',1,'false','2022-09-15 00:00',1,227.27,5,3,10,1136.36,0,1,1),
 ('2022-09-15 00:00',1,'false','2022-09-15 00:00',1,227.27,5,5,10,1136.36,0,2,1),
 ('2022-09-15 00:00',1,'false','2022-09-15 00:00',1,272.73,2,0,10,545.45,100,3,1),
 ('2022-09-15 00:00',1,'false','2022-09-15 00:00',1,181.82,5,0,10,909.09,0,4,1);



-- values
-- -- COMPANY-2
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 1, 1),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 2, 1),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 1, 1),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 1, 2),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 1, 2),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 1, 2),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 1, 3),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 2, 3),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 1, 3),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 1, 4),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 1, 4),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 1, 4),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 100, 100, 1100, 0, 10, 2, 5),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 100, 100, 1100, 0, 10, 2, 5),
-- -- COMPANY-3
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 1, 6),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 2, 6),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 1, 6),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 1, 7),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 1, 7),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
--  10, 100, 100, 1100, 0, 0, 1, 7),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 1, 8),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 2, 8),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 1, 8),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 1, 9),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 1, 9),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 200, 200, 2200, 1100, 0, 1, 9),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 100, 100, 1100, 0, 10, 2, 10),
-- ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 3,
--  10, 100, 100, 1100, 0, 10, 2, 10);