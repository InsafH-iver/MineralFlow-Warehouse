INSERT INTO Vendor (id, name)
VALUES ('33333333-1111-1111-1111-111111111200', 'johnson & Johnson');
INSERT INTO resource (id, name, selling_price_per_ton, storage_price_per_ton_per_day)
VALUES ('33333333-1111-1111-1111-111111111201', 'Fentanyl', 1000, 13);

DO
'
DECLARE
    warehouseMaxCapacityInTon CONSTANT INTEGER := 500;
BEGIN
    INSERT INTO vendor (id, name)
    VALUES (''11111111-1111-1111-1111-111111111111'', ''Acme Supplies'');
    INSERT INTO resource (id, name, selling_price_per_ton, storage_price_per_ton_per_day)
    VALUES (''11111111-1111-1111-1111-111111111112'', ''Gips'', 1.0, 13);
    INSERT INTO warehouse (id, resource_id, vendor_id, used_capacity_in_ton, warehouse_number, max_capacity_in_ton)
    VALUES (''11111111-1111-1111-1111-111111111113'',
            ''11111111-1111-1111-1111-111111111112'', -- resource_id (UUID of related resource entity)
            ''11111111-1111-1111-1111-111111111111'',
            120,
            2,
            warehouseMaxCapacityInTon);
    ---------------- warehouse 2

    -- Vendor Table
    INSERT INTO vendor (id, name)
    VALUES (''22222222-2222-2222-2222-222222222222'', ''Global Logistics'');

    -- Resource Table
    INSERT INTO resource (id, name, selling_price_per_ton, storage_price_per_ton_per_day)
    VALUES (''22222222-2222-2222-2222-222222222223'', ''Goud'', 5.0, 1.0);

    INSERT INTO warehouse (id, resource_id, vendor_id, used_capacity_in_ton, warehouse_number, max_capacity_in_ton)
    VALUES (''22222222-2222-2222-2222-222222222224'',
            ''22222222-2222-2222-2222-222222222223'', -- resource_id (UUID of related resource entity)
            ''22222222-2222-2222-2222-222222222222'',
            0,
            3,
            warehouseMaxCapacityInTon),
           (''33333333-1111-1111-1111-111111111202'',
            ''33333333-1111-1111-1111-111111111201'', -- resource_id (UUID of related resource entity)
            ''33333333-1111-1111-1111-111111111200'',
            120,
            16,
            warehouseMaxCapacityInTon);
    ------------------------------- stockportions warehouse 2
    INSERT INTO stock_portion (id, amount_in_ton, amount_left_in_ton, storage_cost_per_ton_per_day, arrival_time)
    VALUES
        (''1e4d8c48-bd52-4c9c-8a57-9cb0f2dd76d1'', 10.5, 10.5, 25.00, ''2024-10-01 08:30:00 +00:00''),
        (''2a6f2d33-ecab-423b-b69f-d5c59bb9ef22'', 15.2, 15.2, 30.00, ''2024-10-02 09:45:00 +00:00''),
        (''3b9f2e8d-95c7-4d8e-a2c5-6a8abfe1f643'', 20.0, 20.0, 22.50, ''2024-10-03 14:00:00 +00:00''),
        (''4c2f35f4-75be-46c7-bb49-7514fd7bbd0a'', 8.7, 8.7, 28.75, ''2024-10-04 11:15:00 +00:00''),
        (''5d4a89e9-88a8-4f4f-9f9f-b2c61dd7e819'', 12.3, 12.3, 26.50, ''2024-10-05 16:00:00 +00:00'');

    INSERT INTO warehouse_stock_portions (warehouse_id, stock_portions_id)
    VALUES
        (''11111111-1111-1111-1111-111111111113'', ''1e4d8c48-bd52-4c9c-8a57-9cb0f2dd76d1''),
        (''11111111-1111-1111-1111-111111111113'', ''2a6f2d33-ecab-423b-b69f-d5c59bb9ef22''),
        (''22222222-2222-2222-2222-222222222224'', ''3b9f2e8d-95c7-4d8e-a2c5-6a8abfe1f643''),
        (''22222222-2222-2222-2222-222222222224'', ''4c2f35f4-75be-46c7-bb49-7514fd7bbd0a''),
        (''11111111-1111-1111-1111-111111111113'', ''5d4a89e9-88a8-4f4f-9f9f-b2c61dd7e819'');


    ----------------------------setup for PO
    -- Insert Resources
    INSERT INTO resource (id, name, description, selling_price_per_ton, storage_price_per_ton_per_day) VALUES
                                                                                                           (''d1e8481c-1e3c-4d04-a5f6-7f12f8dff6c7'', ''petcoke'', ''High-quality petroleum coke'', 150.00, 1.50),
                                                                                                           (''d2e8481c-1e3c-4d04-a5f6-7f12f8dff6c8'', ''slak'', ''Byproduct from metal production'', 120.00, 1.20);

    -- Insert Customer (Buyer)
    INSERT INTO buyer (id, name, address) VALUES (''56efaea4-953c-44bf-9f41-9700fffa2f28'',
                                                  ''Joske Vermeulen'',
                                                  ''Trammesantlei 122, Schoten, Belgium'');

    -- Insert Vendor
    INSERT INTO vendor (id, name, address) VALUES (''b33df3fe-71be-4c00-94fc-20f4b83dfe12'',
                                                   ''De klant van KDG'',
                                                   ''Het adres van de klant van KDG'');
    ----------------------------

END
'
;

INSERT INTO stock_portion (id, arrival_time, amount_in_ton, amount_left_in_ton, storage_cost_per_ton_per_day)
VALUES
    ('44444433-1111-1111-1111-111111111130', timestamptz'2024-10-01T12:00:00Z', 100.0, 20.0, 5.0),
    ('44444433-1111-1111-1111-111111111131', timestamptz'2024-11-02T12:00:00Z', 200.0, 100.0, 4.5);

INSERT INTO warehouse_stock_portions(stock_portions_id, warehouse_id)
values ('44444433-1111-1111-1111-111111111130',
        '33333333-1111-1111-1111-111111111202'),
       ('44444433-1111-1111-1111-111111111131',
        '33333333-1111-1111-1111-111111111202');

INSERT INTO purchase_order (id, purchase_order_number, status, vendor_id, vessel_number)
VALUES ('33333333-1111-1111-1111-111111111130', 'PO2345', 'OPEN',
        '33333333-1111-1111-1111-111111111200',
        'VE123456');

INSERT INTO order_line (id, amount_in_ton, has_been_completed, resource_id, selling_price_per_ton)
VALUES ('22222233-1111-1111-1111-511111111131', 50, FALSE,
        '33333333-1111-1111-1111-111111111201',130);

INSERT INTO purchase_order_order_lines (purchase_order_id, order_lines_id)
VALUES ('33333333-1111-1111-1111-111111111130',
        '22222233-1111-1111-1111-511111111131');
