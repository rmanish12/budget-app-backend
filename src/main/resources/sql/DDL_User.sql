create table users (
	id serial primary key,
	email varchar(50) unique not null,
	password varchar(255) not null constraint password_length_check check (length(password) >= 6),
	first_name varchar(20) not null,
	last_name varchar(20),
	date_of_birth date not null ,
	gender varchar(20) not null ,
	is_active boolean default true,
	role varchar(50) not null,
	created_at timestamp not null default now(),
	updated_at timestamp not null default now()
);