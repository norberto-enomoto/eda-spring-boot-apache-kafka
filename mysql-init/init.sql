DELETE FROM mysql.user WHERE user='root' AND host='localhost';
FLUSH PRIVILEGES;