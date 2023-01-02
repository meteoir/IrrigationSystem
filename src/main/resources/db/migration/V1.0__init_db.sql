-- create database irrigation_system;
create table if not exists users
(
  id BIGSERIAL PRIMARY KEY,
  created timestamp not null,
  updated timestamp,
  name text not null,
  email text not null,
  role character varying(50) not null,
  status character varying(50) not null,
  multiplier BIGINT not null
);

create table if not exists plot_of_land
(
  id BIGSERIAL PRIMARY KEY,
  created timestamp not null,
  updated timestamp,
  name text not null,
  geohash text,
  irrigation_zone jsonb not null
);

create table if not exists irrigation_config
(
  id BIGSERIAL PRIMARY KEY,
  created timestamp not null,
  updated timestamp,
  plot_of_land_id BIGINT not null REFERENCES plot_of_land (id),
  cron text not null,
  water_amount BIGINT not null,
  minutes INTEGER not null
);

create index idx_users_created on users (created);
create index idx_users_name on users (name);
create index idx_users_email on users (email);
create index idx_users_role on users (role);
create index idx_users_status on users (status);

create index idx_plot_of_land_name on plot_of_land (name);
create index idx_plot_of_land_geohash on plot_of_land (geohash);

create index idx_irrigation_config_plot_id on irrigation_config (plot_of_land_id);
create index idx_irrigation_config_cron on irrigation_config (cron);