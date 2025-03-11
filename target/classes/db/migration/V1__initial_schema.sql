CREATE TABLE IF NOT EXISTS fastDb.users (
   id CHAR(36) PRIMARY KEY,
   first_name VARCHAR(100) NOT NULL,
   last_name VARCHAR(100) NOT NULL,
   email VARCHAR(100) UNIQUE NOT NULL,
   password VARCHAR(200),
   salt INTEGER DEFAULT 10,
   created_at DATETIME
);

CREATE TABLE IF NOT EXISTS fastDb.roles (
   id CHAR(36) PRIMARY KEY,
   value VARCHAR(100) UNIQUE NOT NULL
);

ALTER TABLE
   fastDb.users
ADD
   COLUMN fk_role_id CHAR(36) NOT NULL
AFTER
   password;

ALTER TABLE
   fastDb.users
ADD
   FOREIGN KEY (fk_role_id) REFERENCES fastDb.roles(id) ON DELETE CASCADE;