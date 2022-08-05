insert into roles(insert_date_time, insert_user_id, is_deleted, last_update_date_time,
                  last_update_user_id, role)
values ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1, 'Root User'),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1, 'Admin'),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1, 'Manager'),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1, 'Employee');

insert into addresses(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                      address_line1, address_line2, city, state, country, zip_code)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        '15th Avenue', 'Ronaldinho Street', 'Montgomery', 'Alabama', 'USA', '12345-1234'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Future Street', 'Times Square', 'Atlanta', 'Alabama', 'USA', '54321-4321'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'North Street', 'Circle Square', 'San Francisco', 'California', 'USA', '65245-8546'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'West Street', 'Triangle Square', 'Los Angeles', 'California', 'USA', '54782-5214');

insert into companies(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                      title, phone, website, address_id, company_status)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Blue Tech','1-215-654-5268', 'bluetech.com', 1, 'ACTIVE'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Green Tech','1-215-846-2642', 'greentech.com', 2, 'ACTIVE');

insert into users(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                  username, password, firstname, lastname, phone, role_id, company_id)
values ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1,
        'mt@bluetech.com', 'Abc1', 'Mike', 'Times', '1-854-741-8569', 1, 1),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1,
        'cb@greentech.com', 'Abc1', 'Chris', 'Brown', '1-235-258-3544', 1, 2);

insert into clients_vendors(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                            company_name, phone, website, client_vendor_type, address_id, company_id)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Zara','1-236-958-4521', 'zara.com', 'CLIENT', 3, 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'MediaMarkt','1-365-125-9341', 'mediamarkt.com', 'VENDOR', 4, 1);

insert into categories(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                       description, company_id)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Clothes', 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Electronics', 1);

insert into products(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     name, quantity_in_stock, invoiced_quantity, low_limit_alert, product_unit, category_id, company_id)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Jean', 10, 3, 5, 'PCS', 1, 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'T-shirt', 10, 2, 5, 'PCS', 1, 1);

insert into invoices(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     invoice_no, invoice_status, invoice_type, date, company_id, client_vendor_id)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'P-INV-001', 'APPROVED', 'PURCHASE', '2021-08-01 00:00:00', 1, 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'P-INV-002', 'APPROVED', 'PURCHASE', '2021-08-02 00:00:00', 1, 2),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'P-INV-003', 'AWAITING_APPROVAL', 'PURCHASE', '2021-08-03 00:00:00', 1, 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'S-INV-001', 'APPROVED', 'SALES', '2021-08-03 00:00:00', 1, 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'S-INV-002', 'APPROVED', 'SALES', '2021-08-04 00:00:00', 1, 2),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'S-INV-003', 'AWAITING_APPROVAL', 'SALES', '2021-08-05 00:00:00', 1, 1);

insert into invoice_products(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                             quantity_in_invoice, purchase_price, purchase_tax, purchase_cost, saled_price, saled_tax, saled_amount,
                             profit_loss, product_id, invoice_id)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         1, 100, 10, 110, 0, 0, 0, 0, 1, 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         2, 200, 10, 440, 0, 0, 0, 0, 2, 1),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         2, 150, 10, 330, 0, 0, 0, 0, 1, 2),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         1, 150, 10, 165, 0, 0, 0, 0, 2, 2),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         2, 0, 0, 0, 250, 10, 550, 275, 1, 3),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         1, 0, 0, 0, 200, 10, 220, 0, 2, 3),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         1, 0, 0, 0, 250, 10, 275, 110, 1, 4),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         2, 0, 0, 0, 250, 10, 550, 165, 2, 4),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
         1, 100, 10, 110, 0, 0, 0, 0, 1, 5),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        1, 0, 0, 0, 200, 10, 220, 0, 1, 6);





















