ALTER TABLE audit.revinfo 
ADD COLUMN client_ip varchar(50), 
ADD COLUMN proxy_ip varchar(50), 
ADD COLUMN reason varchar(255);