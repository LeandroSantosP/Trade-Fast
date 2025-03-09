CREATE TABLE IF NOT EXISTS fastDb.order (
   id VARCHAR(100) PRIMARY KEY,
   assert_code VARCHAR(50) NOT NULL,
   type VARCHAR(100) NOT NULL,
   quantity INTEGER,
   price INTEGER NOT NULL,
   fk_owner_id CHAR(36) NOT NULL,
   FOREIGN KEY (fk_owner_id) REFERENCES fastDb.users(id),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS fastDb.transaction (
   id CHAR(36) PRIMARY KEY,
   price INTEGER NOT NULL,
   merchant_id CHAR(36) NOT NULL, /* should be an fk ? */
   customer_id CHAR(36) NOT NULL,
   status ENUM('pending', 'payed', 'failed', 'refunded') DEFAULT 'pending',
   payment_method VARCHAR(100),
   fk_order_id VARCHAR(100) NOT NULL,
   FOREIGN KEY (fk_order_id) REFERENCES fastDb.order(id),
   description VARCHAR(255),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);