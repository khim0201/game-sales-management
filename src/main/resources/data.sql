INSERT INTO game_sales (game_no, game_name, game_code, type, cost_price, tax, sale_price, date_of_sale)
VALUES
    (1, 'MonsterWorld', 'WOW', 1, 50.00, 4.50, 54.50, '2024-04-01 10:00:00'),
    (2, 'MyWorld', 'MC', 2, 30.00, 2.70, 32.70, '2024-04-01 11:00:00'),
    (3, 'KingsdomWorld', 'LOL', 1, 45.00, 4.05, 49.05, '2024-04-01 12:00:00'),
    (4, 'Wolf', 'SEKR', 2, 60.00, 5.40, 65.40, '2024-04-01 13:00:00'),
    (5, 'LordOfTheRings', 'ELDR', 2, 70.00, 6.30, 76.30, '2024-04-01 14:00:00');

INSERT INTO import_progress (file_name, total_records, processed_records, status, start_time, end_time)
VALUES
    ('test_import.csv', 5, 5, 'COMPLETED', '2024-04-01 09:00:00', '2024-04-01 09:00:01');

INSERT INTO sales_statistics (statistics_date, game_no, total_sales_count, total_sales_amount)
VALUES
    ('2024-04-01', 1, 1, 54.50),
    ('2024-04-01', 2, 1, 32.70),
    ('2024-04-01', 3, 1, 49.05),
    ('2024-04-01', 4, 1, 65.40),
    ('2024-04-01', 5, 1, 76.30); 