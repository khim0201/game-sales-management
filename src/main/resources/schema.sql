CREATE TABLE IF NOT EXISTS game_sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_no INT NOT NULL,
    game_name VARCHAR(20) NOT NULL,
    game_code VARCHAR(5) NOT NULL,
    type INT NOT NULL,
    cost_price DECIMAL(10,2) NOT NULL,
    tax DECIMAL(10,2) NOT NULL,
    sale_price DECIMAL(10,2) NOT NULL,
    date_of_sale DATETIME NOT NULL,
    INDEX idx_game_no (game_no),
    INDEX idx_date_of_sale (date_of_sale),
    INDEX idx_sale_price (sale_price),
    CONSTRAINT chk_game_no CHECK (game_no BETWEEN 1 AND 100),
    CONSTRAINT chk_type CHECK (type IN (1, 2)),
    CONSTRAINT chk_cost_price CHECK (cost_price <= 100)
)

CREATE TABLE IF NOT EXISTS import_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    total_records INT,
    processed_records INT,
    status VARCHAR(20) NOT NULL,
    start_time DATETIME,
    end_time DATETIME,
    error_message TEXT,
    INDEX idx_start_time (start_time)
)


CREATE TABLE IF NOT EXISTS sales_statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    statistics_date DATE NOT NULL,
    game_no INT,
    total_sales_count INT NOT NULL,
    total_sales_amount DECIMAL(10,2) NOT NULL,
    INDEX idx_date (statistics_date),
    INDEX idx_game_no (game_no)
)