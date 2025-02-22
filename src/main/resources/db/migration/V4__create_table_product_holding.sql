CREATE TABLE Product_Holdings (
    customer_no VARCHAR(255),
    product_type VARCHAR(2) CHECK (product_type IN ('SA', 'TD', 'IM', 'AL')),
    account_no VARCHAR(255),
    account_name VARCHAR(255),
    account_open_date DATE,
    create_by VARCHAR(255),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(255),
    update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (customer_no, product_type, account_no),
    FOREIGN KEY (customer_no) REFERENCES customer_info(customer_no)
);

COMMENT ON COLUMN Product_Holdings.product_type IS 'SA/TD/IM/AL';