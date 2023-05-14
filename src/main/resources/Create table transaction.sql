CREATE TABLE transaction_data(
	id_transaction INT AUTO_INCREMENT,
    customer_name varchar(100) not null,
    npwp varchar(45) not null,
    destination_account varchar(20) not null,
    currency_pair varchar(10) not null,
    amount1 DOUBLE not null,
    amount2 DOUBLE not null,
    type_transaction varchar(1) not null,
    rate_kurs DOUBLE not null,
    payment_method varchar(45) not null,
    bank_name varchar(40) not null,
    comments varchar(255) not null,
    nic varchar(20) not null,
    transaction_status varchar(30) not null,
    va_number varchar(255),
    created_date timestamp not null,
    last_update timestamp,
    PRIMARY KEY(id_transaction)
);

CREATE TABLE transaction_threshold(
	id_threshold INT AUTO_INCREMENT,
	used_amount DOUBLE not null,
    nic varchar(50) not null,
    created_date timestamp not null,
    last_update timestamp,
    PRIMARY KEY(id_threshold)
);