-- 
-- PostgreSQL DB DDL
-- 

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF-8';
SELECT pg_catalog.set_config('search_path','',false);



CREATE SCHEMA IF NOT EXISTS forex_schema;

ALTER SCHEMA forex_schema OWNER TO $FOREX_DB_USER$;


