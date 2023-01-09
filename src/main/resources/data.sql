insert into roles(insert_date_time, insert_user_id, is_deleted, last_update_date_time,
                  last_update_user_id, description)
values ('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1, 'Root User'),
       ('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1, 'Admin'),
       ('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1, 'Manager'),
       ('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1, 'Employee');

insert into addresses(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                      address_line1, address_line2, city, state, country, zip_code)
values ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        '7925 Jones Branch Dr, #3300', 'Tysons', 'Virginia', 'VA', 'United States', '22102-1234'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'Future Street', 'Times Square', 'Atlanta', 'Alabama', 'United States', '54321-4321'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'North Street', 'Circle Square', 'San Francisco', 'California', 'United States', '65245-8546'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'West Street', 'Triangle Square', 'Los Angeles', 'California', 'United States', '54782-5214'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'East Street', 'Cube Square', 'Los Angeles', 'California', 'United States', '54782-5214'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'South Street', 'Times Square', 'Los Angeles', 'California', 'United States', '54782-5214'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'SouthWest Street', 'Puzzle Square', 'Los Angeles', 'California', 'United States', '65654-8989'),
       ('2022-09-15 00:00:00', 1, false, '2022-09-15 00:00:00', 1,
        'OwerWest Street', 'Android Square', 'Los Angeles', 'Phoneix', 'United States', '65654-8989');;

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
-- COMPANY-3 / Blue Tech / MANAGER
('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1,
 'manager@bluetech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
 'Tom', 'Hanks', '+1 (356) 258-3544', 3, 3, true),
-- COMPANY-4 / Red Tech / ADMIN
('2022-09-09 00:00:00', 1, false, '2022-09-09 00:00:00', 1,
 'admin@redtech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
 'John', 'Doe', '+1 (659) 756-1265', 2, 4, true);

insert into clients_vendors(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                            client_vendor_type, client_vendor_name, phone, website, address_id, company_id)
values
-- COMPANY-2 / Green Tech
('2022-09-15T00:00',2, false,'2022-09-15T00:00',2,'CLIENT','Orange Tech', '+1 (251) 321-4155', 'https://www.orange.com', 5, 2),
('2022-09-15T00:00',2, false,'2022-09-15T00:00',2,'CLIENT','Ower Tech', '+1 (251) 321-4141', 'https://www.ower.com', 8, 2),
('2022-09-15T00:00',2, false,'2022-09-15T00:00',2,'VENDOR','Photobug Tech', '+1 (652) 852-3246', 'https://www.photobug.com', 6, 2),
('2022-09-15T00:00',2, false,'2022-09-15T00:00',2,'VENDOR','Wordtune Tech','+1 (652) 852-3246','https://www.wordtune.com', 7, 2),
-- COMPANY-3 / Blue Tech
('2022-09-15T00:00',3, false,'2022-09-15T00:00',3,'CLIENT', 'Reallinks Tech', '+1 (652) 852-9544','https://www.reallinks.com', 3, 3),
('2022-09-15T00:00',3, false,'2022-09-15T00:00',3,'VENDOR', 'Livetube Tech', '+1 (652) 852-2055','https://www.livetube.com', 4, 3),
('2022-09-15T00:00',3, false,'2022-09-15T00:00',3,'CLIENT', 'Key Tech', '+1 (652) 852-7896','https://www.keytech.com', 1, 3),
('2022-09-15T00:00',3, false,'2022-09-15T00:00',3,'VENDOR', 'Mod Tech', '+1 (652) 852-3648','https://www.modtech.com', 2, 3);


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
 VALUES
-- COMPANY-2 / Green Tech
    ('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, 'HP Elite 800G1 Desktop Computer Package', 8, 5,'PCS', 1),
    ('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, '2021 Apple MacBook Pro', 0, 5,'PCS', 1),
    ('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, 'Apple iPhone-13', 0, 5,'PCS', 2),
    ('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, 'SAMSUNG Galaxy S22',0, 5,'PCS', 2),
-- COMPANY-3 / Blue Tech
    ('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 'Samsung Galaxy S20 (renewed)', 10, 5, 'PCS', 3),
    ('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 'Samsung Galaxy S22', 20, 5, 'PCS', 3);


insert into invoices(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     date,invoice_no, invoice_type, invoice_status, client_vendor_id, company_id)
values
-- COMPANY-2 / Green Tech
 ('2022-09-09 00:00', 2, 'false', '2022-09-09 00:00', 2, '2022-09-09', 'P-001', 'PURCHASE', 'APPROVED', 2, 2),
 ('2022-09-10 00:00', 2, 'false', '2022-09-10 00:00', 2, '2022-09-10', 'P-002', 'PURCHASE', 'APPROVED', 3, 2),
 ('2022-09-17 00:00', 2, 'false', '2022-09-17 00:00', 2, '2022-09-17', 'S-001', 'SALES', 'APPROVED', 1, 2),
 ('2022-10-19 00:00', 2, 'false', '2022-10-19 00:00', 2, '2022-10-19', 'S-002', 'SALES', 'AWAITING_APPROVAL', 1, 2),
 ('2022-11-20 00:00', 2, 'false', '2022-11-20 00:00', 2, '2022-11-20', 'S-003', 'SALES', 'AWAITING_APPROVAL', 1, 2),

-- COMPANY-3 / Blue Tech
('2022-09-09 00:00', 3, 'false', '2022-09-09 00:00', 3, '2022-09-09', 'P-001', 'PURCHASE', 'APPROVED', 5, 3),
('2022-09-10 00:00', 3, 'false', '2022-09-10 00:00', 3, '2022-09-10', 'P-002', 'PURCHASE', 'APPROVED', 5, 3),
('2022-09-13 00:00', 3, 'false', '2022-09-13 00:00', 3, '2022-09-13', 'S-001', 'SALES', 'APPROVED', 4, 3),
('2022-11-18 00:00', 3, 'false', '2022-11-18 00:00', 3, '2022-11-18', 'S-002', 'SALES', 'AWAITING_APPROVAL', 4, 3),
('2022-11-19 00:00', 3, 'false', '2022-11-19 00:00', 3, '2022-11-19', 'S-003', 'SALES', 'AWAITING_APPROVAL', 4, 3),
('2022-11-20 00:00', 3, 'false', '2022-11-20 00:00', 3, '2022-11-20', 'S-004', 'SALES', 'AWAITING_APPROVAL', 6, 3),
('2022-11-21 00:00', 3, 'false', '2022-11-21 00:00', 3, '2022-11-21', 'S-005', 'SALES', 'AWAITING_APPROVAL', 6, 3);

insert into invoice_products(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                             price,quantity, remaining_quantity, tax, profit_loss, invoice_id, product_id)
values
-- COMPANY-2 / Green Tech
('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, 250, 5, 3, 10, 0, 1, 1),     --purchase APPROVED
('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, 250, 5, 5, 10, 0, 2, 1),     --purchase APPROVED total cost (with tax) 2750
('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, 300, 2, 0, 10, 110, 3, 1),    --sale APPROVED     total sale (with tax) 660 & profit : 110 with tax
('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, 200, 2, 0, 10, 0, 4, 1),     --sale AWAITING_APPROVAL after approval total sale (with tax) :1100 & profit (with tax) : 0
('2022-09-15 00:00', 2, 'false', '2022-09-15 00:00', 2, 300, 5, 0, 10, 0, 5, 1),      --sale AWAITING_APPROVAL after approval total sale (with tax) :2750 & profit (with tax) : 275

-- COMPANY-3 / Blue Tech
('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 200, 20, 0, 10, 0, 6, 5),     --purchase APPROVED
('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 200, 20, 10, 10, 0, 6, 5),    --purchase APPROVED
('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 900, 10, 10, 10, 0, 7, 6),    --purchase APPROVED
('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 1000, 10, 10, 10, 0, 7, 6),  --purchase APPROVED  total cost (with tax) 29700
('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 300, 10, 0, 10, 1100, 8, 5),  --sale APPROVED     total sale (with tax) 3300 & profit : 1100 with tax
('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 300, 20, 0, 10, 2200, 8, 5),  --sale APPROVED     total sale (with tax): 9900 & total profit : 3300 with tax
('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 1200, 6, 0, 10, 0, 9, 6),     --sale AWAITING_APPROVAL after approval total sale (with tax) : 17820 & profit (with tax) : 3300+1980=5280
('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 1200, 5, 0, 10, 0, 10, 6),     --sale AWAITING_APPROVAL after approval total sale (with tax) : 24420 & profit (with tax) : 5280+1540=6820
('2022-09-15 00:00', 3, 'false', '2022-09-15 00:00', 3, 1200, 2, 0, 10, 0, 11, 6);    --sale AWAITING_APPROVAL after approval total sale (with tax) : 27060 & profit (with tax) : 6820+440=7260

