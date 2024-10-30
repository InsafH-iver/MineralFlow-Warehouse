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

    INSERT INTO warehouse (id, resource_id, vendor_id, used_capacity_in_ton, warehouse_number,max_capacity_in_ton)
    VALUES (''22222222-2222-2222-2222-222222222224'',
            ''22222222-2222-2222-2222-222222222223'', -- resource_id (UUID of related resource entity)
            ''22222222-2222-2222-2222-222222222222'',
            0,
            3,
            warehouseMaxCapacityInTon);
    ------------------------------- stockportions warehouse 2
    INSERT INTO stock_portion (id, amount_in_ton, storage_cost_per_ton_per_day, arrival_time)
    VALUES
        (''1e4d8c48-bd52-4c9c-8a57-9cb0f2dd76d1'', 10.5, 25.00, ''2024-10-01 08:30:00''),
        (''2a6f2d33-ecab-423b-b69f-d5c59bb9ef22'', 15.2, 30.00, ''2024-10-02 09:45:00''),
        (''3b9f2e8d-95c7-4d8e-a2c5-6a8abfe1f643'', 20.0, 22.50, ''2024-10-03 14:00:00''),
        (''4c2f35f4-75be-46c7-bb49-7514fd7bbd0a'', 8.7, 28.75, ''2024-10-04 11:15:00''),
        (''5d4a89e9-88a8-4f4f-9f9f-b2c61dd7e819'', 12.3, 26.50, ''2024-10-05 16:00:00'');

    INSERT INTO warehouse_stock_portions (warehouse_id, stock_portions_id)
    VALUES
        (''11111111-1111-1111-1111-111111111113'', ''1e4d8c48-bd52-4c9c-8a57-9cb0f2dd76d1''),
        (''11111111-1111-1111-1111-111111111113'', ''2a6f2d33-ecab-423b-b69f-d5c59bb9ef22''),
        (''22222222-2222-2222-2222-222222222224'', ''3b9f2e8d-95c7-4d8e-a2c5-6a8abfe1f643''),
        (''22222222-2222-2222-2222-222222222224'', ''4c2f35f4-75be-46c7-bb49-7514fd7bbd0a''),
        (''11111111-1111-1111-1111-111111111113'', ''5d4a89e9-88a8-4f4f-9f9f-b2c61dd7e819'');
    -- Insert Invoice for Acme Supplies
    INSERT INTO invoice (id, vendor_id, creation_date)
    VALUES (''11111111-1111-1111-1111-111111111114'', ''11111111-1111-1111-1111-111111111111'', ''2024-10-30'');

    -- Insert InvoiceLines for each StockPortion in Acme Supplies warehouses
    INSERT INTO invoice_line (id, stock_portion_id, resource_id)
    VALUES
        (''11111111-1111-1111-1111-111111111116'', ''1e4d8c48-bd52-4c9c-8a57-9cb0f2dd76d1'', ''11111111-1111-1111-1111-111111111112''),
        (''11111111-1111-1111-1111-111111111117'', ''2a6f2d33-ecab-423b-b69f-d5c59bb9ef22'', ''11111111-1111-1111-1111-111111111112''),
        (''11111111-1111-1111-1111-111111111118'', ''5d4a89e9-88a8-4f4f-9f9f-b2c61dd7e819'', ''11111111-1111-1111-1111-111111111112'');
    -- Example of inserting entries to link Invoice with InvoiceLines
    INSERT INTO invoice_invoice_lines(invoice_id, invoice_lines_id)
    VALUES
        (''11111111-1111-1111-1111-111111111114'', ''11111111-1111-1111-1111-111111111116''),
        (''11111111-1111-1111-1111-111111111114'', ''11111111-1111-1111-1111-111111111117''),
        (''11111111-1111-1111-1111-111111111114'', ''11111111-1111-1111-1111-111111111118'');
END
'
;