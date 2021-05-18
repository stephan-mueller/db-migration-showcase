--liquibase formatted sql
--changeset quarkus:1:create-table-quarkus
--labels 1
CREATE TABLE quarkus (
    id   INT,
    name VARCHAR(20)
);