CREATE TABLE customer_address (
    address_seq SERIAL PRIMARY KEY,
    customer_no VARCHAR(255) REFERENCES customer_info(customer_no),
    address_type VARCHAR(1) CHECK (address_type IN ('P', 'R', 'O')),
    address_no VARCHAR(255),
    moo VARCHAR(255),
    building_village VARCHAR(255),
    floor VARCHAR(255),
    room_no VARCHAR(255),
    soi VARCHAR(255),
    road VARCHAR(255),
    sub_district VARCHAR(255),
    district VARCHAR(255),
    province_code VARCHAR(255),
    postal_code VARCHAR(255),
    create_by VARCHAR(255),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(255),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN customer_address.address_type IS 'P/R/O';