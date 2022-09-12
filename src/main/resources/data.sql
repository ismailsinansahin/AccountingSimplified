insert into roles(insert_date_time, insert_user_id, is_deleted, last_update_date_time,
                  last_update_user_id, description)
values ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1, 'Root User'),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1, 'Admin'),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1, 'Manager'),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1, 'Employee');

insert into addresses(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                      address_line1, address_line2, city, state, country, zip_code)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        '7925 Jones Branch Dr, #3300', 'Tysons', 'Virginia', 'VA', 'USA', '22102-1234'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Future Street', 'Times Square', 'Atlanta', 'Alabama', 'USA', '54321-4321'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'North Street', 'Circle Square', 'San Francisco', 'California', 'USA', '65245-8546'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'West Street', 'Triangle Square', 'Los Angeles', 'California', 'USA', '54782-5214'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'East Street', 'Cube Square', 'Los Angeles', 'California', 'USA', '54782-5214'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'South Street', 'Times Square', 'Los Angeles', 'California', 'USA', '54782-5214');;

insert into companies(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                      title, phone, website, address_id, company_status)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'CYDEO','1-652-852-8888', 'cydeo.com', 1, 'ACTIVE'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Green Tech','1-652-852-3246', 'greentech.com', 2, 'ACTIVE'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Blue Tech','1-215-654-5268', 'bluetech.com', 3, 'ACTIVE'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Red Tech','1-215-846-2642', 'redtech.com', 4, 'PASSIVE');

insert into users(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                  username, password, firstname, lastname, phone, role_id, company_id)
values ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1,
        'jt@cydeo.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'Jay', 'Thunder', '1-852-564-5874', 1, 1),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1,
        'mg@greentech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'Mary', 'Grant', '1-234-345-4362', 2, 2),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1,
        'rn@redtech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'Robert', 'Noah', '1-852-564-5874', 3, 2),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1,
        'mt@greentech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'Mike', 'Times', '1-854-741-8569', 4, 2),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1,
        'cb@greentech.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'Chris', 'Brown', '1-235-258-3544', 2, 3);

insert into clients_vendors(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                            company_name, client_vendor_type, phone, website, address_id, company_id)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Orange', 'CLIENT', '1-251-321-4141', 'orange.com', 5, 2),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Green Tech', 'VENDOR', '1-652-852-3246', 'greentech.com', 6, 2);

insert into categories(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                       description, company_id)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Computers', 2),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Phones', 2);

insert into products(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     name, quantity_in_stock, quantity_in_invoice, low_limit_alert, product_unit, category_id)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Apple IPhone-13', 10, 0, 5, 'PCS', 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Samsung Galaxy S22', 10, 0, 5, 'PCS', 1);

insert into invoices(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     invoice_no, invoice_status, invoice_type, date, company_id, client_vendor_id)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'P-001', 'APPROVED', 'PURCHASE', '2021-08-01 00:00:00', 2, 1),
       ('2021-01-05 00:00:01', 1, false, '2021-01-05 00:00:00', 1,
        'P-002', 'AWAITING_APPROVAL', 'PURCHASE', '2021-08-02 00:00:00', 2, 2);
--        ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
--         'P-INV-003', 'AWAITING_APPROVAL', 'PURCHASE', '2021-08-03 00:00:00', 1, 1),
--        ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
--         'S-INV-001', 'APPROVED', 'SALES', '2021-08-03 00:00:00', 1, 1),
--        ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
--         'S-INV-002', 'APPROVED', 'SALES', '2021-08-04 00:00:00', 1, 2),
--        ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
--         'S-INV-003', 'AWAITING_APPROVAL', 'SALES', '2021-08-05 00:00:00', 1, 1);

insert into invoice_products(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                             quantity, price, tax, amount, profit_loss, product_id, invoice_id)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         10, 100, 100, 1100, 0, 1, 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         10, 200, 200, 2200, 0, 2, 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         10, 150, 150, 1650, 0, 1, 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        10, 100, 100, 1100, 0, 1, 2),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        10, 100, 100, 1100, 0, 1, 2),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        10, 100, 100, 1100, 0, 1, 2);
--        ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
--          10, 0, 0, 0, 0, 1, 4),
--        ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
--          10, 0, 0, 0, 0, 2, 4),
--        ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
--          10, 100, 10, 110, 0, 1, 5),
--        ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
--          10, 0, 0, 0, 0, 1, 6);