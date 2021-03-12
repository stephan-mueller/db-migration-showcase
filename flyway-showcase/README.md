## Flyway Showcase

The showcase provides a hello world application based on [Quarkus](https://quarkus.io) microservice framework with a Postgres database 
provided as a Docker image, which demonstrates Flyway migration during the startup of the application (for further
details please check the [Quarkus Flyway Guide](https://quarkus.io/guides/flyway)). 
In addition to that the `flyway-maven-plugin` is included in the `pom.xml` enabling Flyway migration to be integrated into a build and 
deployment lifecycle or to be run manually.     

### About Flyway

Flyway is an open-source database migration tool. It strongly favors simplicity and convention over configuration.
It is based around just 7 basic commands: **Migrate**, **Clean**, **Info**, **Validate**, **Undo**, **Baseline** and **Repair**.
Migrations can be written in SQL or Java (e.g. for advanced data transformations or dealing with LOBs).

Flyway provides a command-line client which enables you to manage migrations manually. If you are on the JVM, it is recommended to use the 
Java API for migrating the database on application startup. Alternatively, you can also use the Maven plugin or Gradle plugin.

Flyway supports a wide variety of databases like Oracle, SQL Server (including Amazon RDS and Azure SQL Database), Azure Synapse 
(Formerly Data Warehouse), DB2, MySQL (including Amazon RDS, Azure Database & Google Cloud SQL), Aurora MySQL, MariaDB, Percona XtraDB 
Cluster, TestContainers, PostgreSQL (including Amazon RDS, Azure Database, Google Cloud SQL & Heroku), Aurora PostgreSQL, Redshift, 
CockroachDB, SAP HANA, Sybase ASE, Informix, H2, HSQLDB, Derby, Snowflake, SQLite and Firebird.

Maintaining a secure connection to databases is highly desirable in a production environment. Flyway can easily be configured to use SSL to 
establish a secure connection as and when required, provided the relevant database and JDBC driver also support SSL. For further information
please check the [SSL support](https://flywaydb.org/documentation/configuration/ssl) documentation.

In order to log into a database, the typical approach is to set the username and password in the Flyway configuration file. Unfortunately 
are the properties stored in plain text which means that anyone can see the credentials. Flyway comes with additional 
[authentication](https://flywaydb.org/documentation/configuration/authentication) mechanisms and support for 
[secrets management](https://flywaydb.org/documentation/configuration/secretsManagement) solutions that tackle these concerns and enables 
you to successfully handle sensitive data.


### How Flyway works

The easiest scenario is when you point Flyway to an empty database. It will try to locate its schema history table which is called 
`flyway_schema_version` and will be used to track the state of the database. As the database is empty, Flyway won't find it and will create
it instead.

Afterwards Flyway will begin scanning the filesystem or the classpath of the application for migrations which can be written in either SQL
or Java. The migrations are then sorted based on their version number and applied in order. As each migration gets applied, the schema 
history table is updated accordingly.

With the metadata and the initial state in place, it is to possible to migrate to newer versions. Flyway will once again scan the 
filesystem or the classpath of the application for migrations. The migrations are checked against the schema history table. If their 
version number is lower or equal to the one of the version marked as current, they are ignored. All newer versions are then sorted by 
version number and executed in order with the schema history table updated accordingly.

For further information please check the [getting started](https://flywaydb.org/documentation/getstarted/how) guide and 
the [concepts](https://flywaydb.org/documentation/concepts/migrations).


### Migration scripts

The showcase contains two DDL / DML files which are located in the directory `src/main/resources/db/migration`

_V1__create_table_quarkus.sql_ - creates table `QUARKUS` in schema `public`
```sql
CREATE TABLE quarkus (
    id   INT,
    name VARCHAR(20)
);
```

_V2__insert_table_quarkus.sql_ - inserts a single row into table `QUARKUS`
```sql
INSERT INTO quarkus(id, name) VALUES (1, 'QUARKED');
```


### How to run

Before running the application or the `flyway-maven-plugin` the database has to be started via `docker`:

```shell script
docker-compose up database
```

If everything worked you can access the database with a database client via `jdbc:postgresql://localhost:5432/db-migration`. 
When starting the database for the first time, the database is empty and does not contain any tables.


#### Migrate the database with the `flyway-maven-plugin`

If the migration should be run as part of a build pipeline or manually the `flyway-maven-plugin` can be used. In addition to that the 
[plugin](https://flywaydb.org/documentation/usage/maven/) supports all commands like the Flyway 
[command-line tool](https://flywaydb.org/documentation/usage/commandline/).

The use the plugin it has to be configured in the `pom.xml`.
```xml
<plugin>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-maven-plugin</artifactId>
    <version>7.6.0</version>
    <configuration>
        <url>jdbc:postgresql://localhost:5432/db-migration</url>
        <user>postgres</user>
        <password>postgres</password>
        <schemas>
            <schema>public</schema>
        </schemas>
        <createSchemas>true</createSchemas>
        <locations>
            <location>filesystem:${project.basedir}/src/main/resources/db/migration</location>
        </locations>
    </configuration>
</plugin>
```

The `flyway-maven-plugin` supports the following goals which can be run via `mvn flyway:<goal>`.

| **Name** | **Description** |
| :--- | :--- |
| migrate | Migrates the database |
| clean | Drops all objects in the configured schemas |
| info | Prints the details and status information about all the migrations |
| validate | Validates the applied migrations against the ones available on the classpath |
| undo | Undoes the most recently applied versioned migration (commercial version only) |
| baseline | Baselines an existing database, excluding all migrations up to and including baselineVersion |
| repair | Repairs the schema history table |


When accessing a database to be migrated for the first time you should check the migration state by using `flyway:info` 
```shell
mvn flyway:info
```

```shell
[INFO] --- flyway-maven-plugin:7.6.0:info (default-cli) @ flyway-showcase ---
[INFO] Flyway Community Edition 7.6.0 by Redgate
[INFO] Database: jdbc:postgresql://localhost:5432/db-migration (PostgreSQL 12.6)
[INFO] Schema version: << Empty Schema >>
[INFO] 
[INFO] +-----------+---------+----------------------+------+--------------+---------+
| Category  | Version | Description          | Type | Installed On | State   |
+-----------+---------+----------------------+------+--------------+---------+
| Versioned | 1       | create table quarkus | SQL  |              | Pending |
| Versioned | 2       | insert table quarkus | SQL  |              | Pending |
+-----------+---------+----------------------+------+--------------+---------+
```

If the database is not empty or you want to reset the database to a clean state use `flyway:clean`.
```shell
mvn flyway:clean
```

```shell
[INFO] --- flyway-maven-plugin:7.6.0:clean (default-cli) @ flyway-showcase ---
[INFO] Flyway Community Edition 7.6.0 by Redgate
[INFO] Database: jdbc:postgresql://localhost:5432/db-migration (PostgreSQL 12.6)
[INFO] Successfully dropped pre-schema database level objects (execution time 00:00.000s)
[INFO] Successfully cleaned schema "public" (execution time 00:00.033s)
[INFO] Successfully dropped post-schema database level objects (execution time 00:00.000s)
```

To run the migration use `flyway:migrate`
```shell
mvn flyway:migrate
```

```
[INFO] --- flyway-maven-plugin:7.6.0:migrate (default-cli) @ flyway-showcase ---
[INFO] Flyway Community Edition 7.6.0 by Redgate
[INFO] Database: jdbc:postgresql://localhost:5432/db-migration (PostgreSQL 12.6)
[INFO] Successfully validated 2 migrations (execution time 00:00.018s)
[INFO] Creating Schema History table "public"."flyway_schema_history" ...
[INFO] Current version of schema "public": << Empty Schema >>
[INFO] Migrating schema "public" to version "1 - create table quarkus"
[INFO] Migrating schema "public" to version "2 - insert table quarkus"
[INFO] Successfully applied 2 migrations to schema "public" (execution time 00:00.080s)
[INFO] Flyway Community Edition 7.6.0 by Redgate
```

After migration was successful the result of `flyway:info` should look like this
```shell
[INFO] --- flyway-maven-plugin:7.6.0:info (default-cli) @ flyway-showcase ---
[INFO] Flyway Community Edition 7.6.0 by Redgate
[INFO] Database: jdbc:postgresql://localhost:5432/db-migration (PostgreSQL 12.6)
[INFO] Schema version: 2
[INFO] 
[INFO] +-----------+---------+----------------------+------+---------------------+---------+
| Category  | Version | Description          | Type | Installed On        | State   |
+-----------+---------+----------------------+------+---------------------+---------+
| Versioned | 1       | create table quarkus | SQL  | 2021-03-11 20:46:55 | Success |
| Versioned | 2       | insert table quarkus | SQL  | 2021-03-11 20:46:55 | Success |
+-----------+---------+----------------------+------+---------------------+---------+
```

If you want to migrate to a specific version you have to use the `target` attribute.
```shell
mvn flyway:migrate -Dflyway.target=1.0
```

```shell
[INFO] --- flyway-maven-plugin:7.6.0:migrate (default-cli) @ flyway-showcase ---
[INFO] Flyway Community Edition 7.6.0 by Redgate
[INFO] Database: jdbc:postgresql://localhost:5432/db-migration (PostgreSQL 12.6)
[INFO] Successfully validated 2 migrations (execution time 00:00.020s)
[INFO] Creating Schema History table "public"."flyway_schema_history" ...
[INFO] Current version of schema "public": << Empty Schema >>
[INFO] Migrating schema "public" to version "1 - create table quarkus"
[INFO] Successfully applied 1 migration to schema "public" (execution time 00:00.045s)
[INFO] Flyway Community Edition 7.6.0 by Redgate
```

After migration to version `1` was successful the result of `flyway:info` should look like this
```shell
[INFO] --- flyway-maven-plugin:7.6.0:info (default-cli) @ flyway-showcase ---
[INFO] Flyway Community Edition 7.6.0 by Redgate
[INFO] Database: jdbc:postgresql://localhost:5432/db-migration (PostgreSQL 12.6)
[INFO] Schema version: 1
[INFO] 
[INFO] +-----------+---------+----------------------+------+---------------------+---------+
| Category  | Version | Description          | Type | Installed On        | State   |
+-----------+---------+----------------------+------+---------------------+---------+
| Versioned | 1       | create table quarkus | SQL  | 2021-03-11 20:50:30 | Success |
| Versioned | 2       | insert table quarkus | SQL  |                     | Pending |
+-----------+---------+----------------------+------+---------------------+---------+
```


#### Automatic database migration during application startup

If the application should run the automatic database migration during the application startup it needs to be compiled and packaged using
`Maven`. The Maven lifecycle creates a jar package and uses the `quarkus-maven-plugin` to create a runnable
JAR (fat JAR) which contains the application and the Quarkus application server. The fat JAR will be copied into a
Docker image using Spotify's `dockerfile-maven-plugin` during the package phase.

```shell script
mvn clean package
docker-compose up application
```

If the migration was successful you should see the following data in the `flyway_schema_history` table.

```sql
SELECT * FROM flyway_schema_history;
```

| installed\_rank | version | description | type | script | checksum | installed\_by | installed\_on | execution\_time | success |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | 1 | create table quarkus | SQL | db/migration/V1\_\_create\_table\_quarkus.sql | 817554297 | postgres | 2021-03-11 20:08:07.100974 | 10 | true |
| 2 | 2 | insert table quarkus | SQL | db/migration/V2\_\_insert\_table\_quarkus.sql | 1914223276 | postgres | 2021-03-11 20:08:07.131095 | 7 | true |
