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
    END
'
;