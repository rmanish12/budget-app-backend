create table categories (
	id serial primary key,
	type_id int4 references types (id),
	name varchar(255) not null
);