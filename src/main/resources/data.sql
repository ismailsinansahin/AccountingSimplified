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
        'Future Street', 'Times Square', 'Atlanta', 'Alabama', 'USA', '54321-4321');

insert into companies(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                      title, phone, website, establishment_date, address_id, company_status)
values ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Blue Tech','1-215-654-5268', 'bluetech.com', '2021-01-05', 1, 'ACTIVE'),
       ('2021-01-05 00:00:00', 1, false, '2021-01-05 00:00:00', 1,
        'Green Tech','1-215-846-2642', 'greentech.com', '2021-02-16', 2, 'ACTIVE');

insert into users(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                  username, password, firstname, lastname, phone, role_id, company_id)
values ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1,
        'mt@bluetech.com', 'Abc1', 'Mike', 'Times', '1-854-741-8569', 1, 1),
       ('2021-09-09 00:00:00', 1, false, '2021-09-09 00:00:00', 1,
        'cb@greentech.com', 'Abc1', 'Chris', 'Brown', '1-235-258-3544', 1, 2);
