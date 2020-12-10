create table budget (
	id serial primary key,
	type_id int4 references types (id),
	amount float8 not null,
	description varchar(255),
	date_of_transaction date not null,
	category_id int4 references categories(id),
	user_id int4 references users (id),
	created_at timestamp not null default now(),
	updated_at timestamp not null default now()
);