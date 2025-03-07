CREATE TABLE IF NOT EXISTS users (
   id VARCHAR(150) PRIMARY KEY,
   first_name VARCHAR(100) NOT NULL,
   last_name VARCHAR(100) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   password VARCHAR(200),
   salt INTEGER DEFAULT 10,
   created_at DATETIME
);

CREATE TABLE IF NOT EXISTS roles (
   id VARCHAR(150) PRIMARY KEY,
   value VARCHAR(100) UNIQUE NOT NULL
);

ALTER TABLE
   users
ADD
   COLUMN fk_role_id VARCHAR(150) NOT NULL
AFTER
   password;

ALTER TABLE
   users
ADD
   FOREIGN KEY (fk_role_id) REFERENCES roles(id);