alter table if exists users drop constraint if exists FKp56c1712k691lhsyewcssf40f;

drop table if exists roles cascade;
drop table if exists users cascade;

create table roles (id bigserial not null, name varchar(255) not null unique, primary key (id));
create table users (id bigserial not null, role_id bigint, "e-mail" varchar(255) not null unique,
                    full_name varchar(255) not null,notification_type varchar(255)
                        not null check (notification_type in ('TELEGRAM','EMAIL')),
                    password varchar(255) not null, provider varchar(255)
                        check (provider in ('LOCAL','GOOGLE')), telegram varchar(255), age integer, primary key (id));

alter table if exists users alter column age set data type integer;
alter table if exists users add constraint FKp56c1712k691lhsyewcssf40f foreign key (role_id) references roles;