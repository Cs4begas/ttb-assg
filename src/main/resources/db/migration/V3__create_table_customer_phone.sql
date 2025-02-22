CREATE TABLE customer_phone (
    phone_seq SERIAL PRIMARY KEY,
    customer_no VARCHAR(255) REFERENCES customer_info(customer_no),
    phone_type VARCHAR(1) CHECK (phone_type IN ('H', 'M', 'O')),
    phone_no VARCHAR(255),
    create_by VARCHAR(255),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Added DEFAULT
    update_by VARCHAR(255),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Added DEFAULT
);

COMMENT ON COLUMN customer_phone.phone_type IS 'H/M/O';