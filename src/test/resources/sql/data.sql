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
    VALUES (''11111111-1111-1111-1111-111111111115'', ''Atlaan'');
    INSERT INTO resource (id, name, selling_price_per_ton, storage_price_per_ton_per_day)
    VALUES (''11111111-1111-1111-1111-111111111114'', ''Beton'', 27, 24);
    INSERT INTO warehouse (id, resource_id, vendor_id, used_capacity_in_ton, warehouse_number, max_capacity_in_ton)
    VALUES (''11111111-1111-1111-1111-111111111116'',
        ''11111111-1111-1111-1111-111111111114'', -- resource_id (UUID of related resource entity)
        ''11111111-1111-1111-1111-111111111115'',
        450,
        3,
        warehouseMaxCapacityInTon);

END
'
;