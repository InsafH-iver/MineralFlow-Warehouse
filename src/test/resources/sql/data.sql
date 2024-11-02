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

    INSERT INTO vendor (id, name)
    VALUES (''11111111-1111-1111-1111-111111111115'', ''Atlaan''),
           (''11111111-1111-1111-1111-111111111121'', ''WMCA-enco'');
    INSERT INTO resource (id, name, selling_price_per_ton, storage_price_per_ton_per_day)
    VALUES (''11111111-1111-1111-1111-111111111114'', ''Beton'', 27, 24),
     (''11111111-1111-1111-1111-111111111120'', ''Silver'', 345, 12.4);

    INSERT INTO warehouse (id, resource_id, vendor_id, used_capacity_in_ton, warehouse_number, max_capacity_in_ton)
    VALUES (''11111111-1111-1111-1111-111111111116'',
        ''11111111-1111-1111-1111-111111111114'', -- resource_id (UUID of related resource entity)
        ''11111111-1111-1111-1111-111111111115'',
        450,
        3,
        warehouseMaxCapacityInTon),
        (''11111111-1111-1111-1111-111111111122'',
        ''11111111-1111-1111-1111-111111111120'', -- resource_id (UUID of related resource entity)
        ''11111111-1111-1111-1111-111111111121'',
        123,
        7,
        warehouseMaxCapacityInTon),
        (''33333333-1111-1111-1111-111111111202'',
        ''33333333-1111-1111-1111-111111111201'', -- resource_id (UUID of related resource entity)
        ''33333333-1111-1111-1111-111111111200'',
        0,
        16,
        warehouseMaxCapacityInTon);
    ------------------------------- stockportions warehouse 2
    INSERT INTO stock_portion (id, amount_in_ton, storage_cost_per_ton_per_day, arrival_time)
    VALUES
        (''1e4d8c48-bd52-4c9c-8a57-9cb0f2dd76d1'', 10.5, 25.00, ''2024-10-01 08:30:00 +00:00''),
        (''2a6f2d33-ecab-423b-b69f-d5c59bb9ef22'', 15.2, 30.00, ''2024-10-02 09:45:00 +00:00''),
        (''3b9f2e8d-95c7-4d8e-a2c5-6a8abfe1f643'', 20.0, 22.50, ''2024-10-03 14:00:00 +00:00''),
        (''4c2f35f4-75be-46c7-bb49-7514fd7bbd0a'', 8.7, 28.75, ''2024-10-04 11:15:00 +00:00''),
        (''5d4a89e9-88a8-4f4f-9f9f-b2c61dd7e819'', 12.3, 26.50, ''2024-10-05 16:00:00 +00:00'');

    INSERT INTO warehouse_stock_portions (warehouse_id, stock_portions_id)
    VALUES
        (''11111111-1111-1111-1111-111111111113'', ''1e4d8c48-bd52-4c9c-8a57-9cb0f2dd76d1''),
        (''11111111-1111-1111-1111-111111111113'', ''2a6f2d33-ecab-423b-b69f-d5c59bb9ef22''),
        (''11111111-1111-1111-1111-111111111116'', ''3b9f2e8d-95c7-4d8e-a2c5-6a8abfe1f643''),
        (''11111111-1111-1111-1111-111111111116'', ''4c2f35f4-75be-46c7-bb49-7514fd7bbd0a''),
        (''11111111-1111-1111-1111-111111111113'', ''5d4a89e9-88a8-4f4f-9f9f-b2c61dd7e819'');
END
'
;

-- Scenario testing reprocessing order lines
INSERT INTO purchase_order (id, purchase_order_number, status, vendor_id, vessel_number)
VALUES ('33333333-1111-1111-1111-111111111130', 'PO2345', 'WAITING',
        '33333333-1111-1111-1111-111111111200',
        'VE123456');

INSERT INTO delivery_ticket (id, delivery_time, unloading_request_id)
values ('33333333-1111-1111-1111-511111111131',
        TIMESTAMPTZ '2024-02-02 02:02:02+00',
        '33333311-1111-1111-1111-111111111131');

INSERT INTO warehouse_delivery_tickets(delivery_tickets_id, warehouse_id)
values ('33333333-1111-1111-1111-511111111131',
        '33333333-1111-1111-1111-111111111202');

INSERT INTO order_line (id, amount_in_ton, has_been_completed, resource_id, selling_price_per_ton)
VALUES ('22222233-1111-1111-1111-511111111131', 50, FALSE,
        '33333333-1111-1111-1111-111111111201',130);

INSERT INTO purchase_order_order_lines (purchase_order_id, order_lines_id)
VALUES ('33333333-1111-1111-1111-111111111130',
        '22222233-1111-1111-1111-511111111131');
-- scenario 1

INSERT INTO delivery_ticket (id, delivery_time, unloading_request_id)
values ('11111111-1111-1111-1111-111111111130',
        TIMESTAMPTZ '2024-02-02 02:02:02+00',
        '11111111-1111-1111-1111-111111111131');

INSERT INTO warehouse_delivery_tickets(delivery_tickets_id, warehouse_id)
values ('11111111-1111-1111-1111-111111111130',
        '11111111-1111-1111-1111-111111111122');


----------------------------setup for PO
-- Insert Resources
INSERT INTO resource (id, name, description, selling_price_per_ton, storage_price_per_ton_per_day) VALUES
                                                                                                       ('d1e8481c-1e3c-4d04-a5f6-7f12f8dff6c7', 'testmaterial', 'High-quality petroleum coke', 150.00, 1.50),
                                                                                                       ('d2e8481c-1e3c-4d04-a5f6-7f12f8dff6c8', 'nogtestmateriaal', 'Byproduct from metal production', 120.00, 1.20);

-- Insert Customer (Buyer)
INSERT INTO buyer (id, name, address) VALUES ('56efaea4-953c-44bf-9f41-9700fffa2f28',
                                              'buyer',
                                              'somewhere over the rainbow');

-- Insert Vendor
INSERT INTO vendor (id, name, address) VALUES ('b33df3fe-71be-4c00-94fc-20f4b83dfe12',
                                               'seller',
                                               'somewhere before the rainbow');
----------------------------
