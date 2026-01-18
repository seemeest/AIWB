alter table users add column if not exists full_name varchar(255);
alter table users add column if not exists birth_date date;

update users set full_name = 'Unknown' where full_name is null;
update users set birth_date = date '1970-01-01' where birth_date is null;

alter table users alter column full_name set not null;
alter table users alter column birth_date set not null;
