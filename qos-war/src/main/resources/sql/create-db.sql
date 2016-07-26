-- Drop table
DROP TABLE IF EXISTS User;

CREATE TABLE User (
  ID int AUTO_INCREMENT PRIMARY KEY,
  email varchar(120) NOT NULL,
  password varchar(60) NOT NULL,
  enabled tinyint  default 0,
  role enum('USER','ADMIN') default 'USER',
  display_name varchar(40) NOT NULL,
  loginCount INT,
  currentLoginAt DATE ,
  lastLoginAt DATE NULL,
  currentLoginIp varchar(120),
  lastLoginIp varchar(120)
);

-- Create INDEX
create unique index email_idx on User(email);
create unique index userName_idx on User(user_name);
