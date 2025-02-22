CREATE TABLE customer_info (
    customer_no VARCHAR(255) PRIMARY KEY,
    id_type VARCHAR(2) CHECK (id_type IN ('CI', 'PP')),
    id_no VARCHAR(255),
    title_name_th VARCHAR(255),
    first_name_th VARCHAR(255),
    last_name_th VARCHAR(255),
    title_name_en VARCHAR(255),
    first_name_en VARCHAR(255),
    last_name_en VARCHAR(255),
    birth_date DATE,
    occupation_code VARCHAR(255),
    working_place VARCHAR(255),
    salary NUMERIC,
    create_by VARCHAR(255),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(255),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN customer_info.id_type IS 'CI/PP';