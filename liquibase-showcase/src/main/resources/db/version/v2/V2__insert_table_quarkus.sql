--liquibase formatted sql
--changeset quarkus:2:insert-table-quarkus
--labels 2
INSERT INTO quarkus(id, name) VALUES (1, 'QUARKED');