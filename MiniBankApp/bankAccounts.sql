CREATE TABLE BankAccounts (
    user_id SERIAL PRIMARY KEY,
    user_fullname VARCHAR(100) NOT NULL,
    user_accountnumber CHAR(8) NOT NULL UNIQUE,
    user_pin CHAR(4) NOT NULL,
    user_balance DECIMAL(15, 2) DEFAULT 0.00 CHECK (user_balance >= 0),
    user_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


select * from BankAccounts;