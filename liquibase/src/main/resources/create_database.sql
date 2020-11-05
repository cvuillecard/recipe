-- LOCALHOST DATABASE
create database recipe;
create user smoothman with encrypted password 'smoothy';
grant all privileges on database recipe to smoothman;

-- TEST DATABASE
create database recipe_test;
create user test with encrypted password 'test';
grant all privileges on database recipe_test to test;
