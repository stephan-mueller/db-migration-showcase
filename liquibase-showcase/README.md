## Liquibase Showcase

The showcase provides a hello world application based on [Quarkus](https://quarkus.io) microservice framework with a Postgres database
provided as a Docker image, which demonstrates Liquibase migration during the startup of the application (for further
details please check the [Quarkus Liquibase Guide](https://quarkus.io/guides/liquibase)).
In addition to that the `liquibase-maven-plugin` is included in the `pom.xml` enabling Liquibase migration to be integrated into a build 
and deployment lifecycle or to be run manually.


### About Liquibase

Liquibase is an open-source database migration tool started by Nathan Voxland in 2006 to enable easier management of database changes, 
especially in agile software development environments. In the last 15 years teams around the world trusted Liquibase to easily track and 
deploy database changes. It’s even been built into solutions such as Spinnaker and Appian.

Liquibase provides a command-line client which enables you to manage migrations manually. If you are on the JVM, it is recommended to use 
the Java API for migrating the database on application startup. Alternatively, you can also use the Maven plugin.

Liquibase relies on the community to ensure broad database support. Relational databases like MySQL, MariaDB, PostgreSQL, Oracle, SQL Server, Azure SQL, 
Sybase_Enterprise, Sybase Anywhere, DB2, Apache Derby, HSQL, H2, Informix, Firebird, SQLite, Vertica, SAP MaxDB, Snowflake, CockroachDB,
Amazon RDS, Amazon Aurora and Cassandra have been reported to work for users out-of-the-box. If you’re looking for guaranteed or certified 
support for a particular database, go to Liquibase.com for information. Additional databases, as well as enhancements are available through
Liquibase extensions.


### How Liquibase works

Liquibase uses changesets to represent a single change to your database. Each changeset has an “id” and “author” attribute which, along 
with the directory and file name of the changelog file, uniquely identify it. There are four different ways to define your changes: 
[SQL](https://docs.liquibase.com/concepts/basic/sql-format.html), [XML](https://docs.liquibase.com/concepts/basic/xml-format.html), 
[YAML](https://docs.liquibase.com/concepts/basic/yaml-format.html), and [JSON](https://docs.liquibase.com/concepts/basic/json-format.html) 
formats. You can pick the format that is most comfortable for you to use. _Please note that some commercial versions (Business and 
Enterprise) of Liquibase supports the SQL format only._

Liquibase uses a changelog to explicitly list database changes in order. The changelog acts as a ledger of changes and contains a list of 
changesets (units of change) that Liquibase can execute on a database. Which changeSets have or have not been deployed is tracked in a 
tracking table called a `DATABASECHANGELOG`. If your database does not already contain a tracking table, Liquibase will create it for you. 
Liquibase also prevents conflicts from different callers’ updates on a secondary table called `DATABASECHANGELOGLOCK`.

Liquibase also provides advanced features such as contexts, [labels](https://docs.liquibase.com/concepts/advanced/labels.html), and preconditions to precisely control when and where a changeset is 
deployed.

For further information please check the [quickstart](https://www.liquibase.org/get-started/quickstart) guide and
the [concepts](https://www.liquibase.org/get-started/core-usage).


### Migration scripts

The showcase contains a list of changesets which are located in the directory `src/main/resources/db`. Like mentioned before does Liquibase
support different ways to define changes. To ease comparation between Flyway and Liquibase a mix of XML and SQL changesets is used.

The most common way to organize changelogs is by major releases. The _db.changelog-master.xml_ includes the changelog for the releases in 
the correct order. Please note: Each of the included XML files needs to be in the same format as a standard XML database changelog. Futher 
information and best practices how to keep changelogs organized can be found at [liquibase.org](https://www.liquibase.org/get-started/best-practices) 
and in the [blog article](https://blog.codecentric.de/en/2015/01/managing-database-migrations-using-liquibase/) by Thomas Jasper.


_db.changelog-master.xml_
```xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">

    <include file="version/v1/db.changelog.v1.xml" relativeToChangelogFile="true" />
    <include file="version/v2/db.changelog.v2.xml" relativeToChangelogFile="true" />
</databaseChangeLog>
```

_db.changelog.v1.xml_
```xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">

    <changeSet id="1" author="quarkus" labels="1">
        <sqlFile path="V1__create_table_quarkus.sql" relativeToChangelogFile="true" />
    </changeSet>
</databaseChangeLog>
```

_V1__create_table_quarkus.sql_ - creates table `QUARKUS` in schema `public`
```sql
--liquibase formatted sql
--changeset quarkus:1:create-table-quarkus
--labels 1
CREATE TABLE quarkus (
    id   INT,
    name VARCHAR(20)
);
```

_db.changelog.v2.xml_
```xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                   xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">

    <changeSet id="2" author="quarkus" labels="2">
        <sqlFile path="V2__insert_table_quarkus.sql" relativeToChangelogFile="true" />
    </changeSet>
</databaseChangeLog>
```

_V2__insert_table_quarkus.sql_ - inserts a single row into table `QUARKUS`
```sql
--liquibase formatted sql
--changeset quarkus:2:insert-table-quarkus
--labels 2
INSERT INTO quarkus(id, name) VALUES (1, 'QUARKED');
```

### How to run

Before running the application or the `liquibase-maven-plugin` the database has to be started via `docker`:

```shell script
docker compose up database
```

If everything worked you can access the database with a database client via `jdbc:postgresql://localhost:5432/db-migration`.
When starting the database for the first time, the database is empty and does not contain any tables.


#### Migrate the database with the `liquibase-maven-plugin`

If the migration should be run as part of a build pipeline or manually the `liquibase-maven-plugin` can be used. In addition to that the
[plugin](https://docs.liquibase.com/tools-integrations/maven/home.html) supports nearly all commands like the Liquibase
[command-line interface](https://docs.liquibase.com/tools-integrations/cli/home.html).

The use the plugin it has to be configured in the `pom.xml`.
```xml
<plugin>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-maven-plugin</artifactId>
    <version>4.3.1</version>
    <configuration>
        <!-- typically extracted in liquibase.properties -->
        <url>jdbc:postgresql://localhost:5432/db-migration</url>
        <username>postgres</username>
        <password>postgres</password>
        <changeLogDirectory>${project.basedir}/src/main/resources/db</changeLogDirectory>
        <changeLogFile>db.changelog-master.xml</changeLogFile>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.2.18</version>
        </dependency>
    </dependencies>
</plugin>
```

The `liquibase-maven-plugin` supports the following goals (and others) which can be run via `mvn liquibase:<goal>`. The complete list of 
all supported maven goals can be found [here](https://docs.liquibase.com/tools-integrations/maven/commands/home.html).

| **Name** | **Description** |
| :--- | :--- |
| diff | allows you to compare two databases of the same type, or different types, to one another |
| dropAll | drops all database objects owned by the user |
| generateChangelog |  creates a changelog file that has a sequence of changesets which describe how to re-create the current state of the database |
| rollback | allows you to roll back change you made to the database |
| status | produces a list of pending changesets with additional information that includes the id, author, and file path name |
| update | deploys any changes that are in the changelog file and that have not been deployed to your database yet |


When accessing a database to be migrated for the first time you should check the migration state by using `liquibase:status`
```shell
mvn liquibase:status
```

```shell
[INFO] --- liquibase-maven-plugin:4.3.1:status (default-cli) @ liquibase-showcase ---
[INFO] ------------------------------------------------------------------------
[project, pluginDescriptor]
[INFO] 
[INFO] 
[INFO] Liquibase Community 4.3.1 by Datical
[INFO] Starte Liquibase am 17:09:18 (Version 4.3.1 #26, kompiliert am 2021-02-12 17:41+0000)
[INFO] Executing on Database: jdbc:postgresql://localhost:5432/db-migration
2 change sets have not been applied to postgres@jdbc:postgresql://localhost:5432/db-migration
     version/v1/db.changelog.v1.xml::1::quarkus
     version/v2/db.changelog.v2.xml::2::quarkus
```

If the database is not empty or you want to reset the database to a clean state use `liquibase:dropAll`.
```shell
mvn liquibase:dropAll
```

```shell
[INFO] --- liquibase-maven-plugin:4.3.1:dropAll (default-cli) @ liquibase-showcase ---
[INFO] ------------------------------------------------------------------------
[project, pluginDescriptor]
[INFO] 
[INFO] 
[INFO] Liquibase Community 4.3.1 by Datical
[INFO] Starte Liquibase am 17:06:20 (Version 4.3.1 #26, kompiliert am 2021-02-12 17:41+0000)
[INFO] Executing on Database: jdbc:postgresql://localhost:5432/db-migration
[INFO] Changelog-Protokoll erfolgreich gesperrt.
[INFO] Cannot load service: liquibase.hub.HubService: Provider liquibase.hub.core.OnlineHubService could not be instantiated
[INFO] Successfully released change log lock
[INFO] Changelog-Protokoll erfolgreich gesperrt.
[INFO] Dropping Database Objects in schema: db-migration.public
[INFO] Successfully deleted all supported object types in schema db-migration.public.
[INFO] Successfully released change log lock
```

To run the migration use `liquibase:update`
```shell
mvn liquibase:update
```

```shell
[INFO] --- liquibase-maven-plugin:4.3.1:update (default-cli) @ liquibase-showcase ---
[INFO] ------------------------------------------------------------------------
[project, pluginDescriptor]
[INFO] 
[INFO] 
[INFO] Liquibase Community 4.3.1 by Datical
[INFO] Starte Liquibase am 17:10:58 (Version 4.3.1 #26, kompiliert am 2021-02-12 17:41+0000)
[INFO] Executing on Database: jdbc:postgresql://localhost:5432/db-migration
[INFO] Changelog-Protokoll erfolgreich gesperrt.
[INFO] Creating database history table with name: databasechangelog
[INFO] Reading from databasechangelog
[INFO] Cannot load service: liquibase.hub.HubService: Provider liquibase.hub.core.OnlineHubService could not be instantiated
[INFO] Successfully released change log lock
[INFO] Changelog-Protokoll erfolgreich gesperrt.
[INFO] SQL in file V1__create_table_quarkus.sql executed
[INFO] ChangeSet version/v1/db.changelog.v1.xml::1::quarkus ran successfully in 16ms
[INFO] SQL in file V2__insert_table_quarkus.sql executed
[INFO] ChangeSet version/v2/db.changelog.v2.xml::2::quarkus ran successfully in 7ms
[INFO] Successfully released change log lock
```

After migration was successful the result of `liquibase:status` should look like this
```shell
[INFO] --- liquibase-maven-plugin:4.3.1:status (default-cli) @ liquibase-showcase ---
[INFO] ------------------------------------------------------------------------
[project, pluginDescriptor]
[INFO] 
[INFO] 
[INFO] Liquibase Community 4.3.1 by Datical
[INFO] Starte Liquibase am 17:11:33 (Version 4.3.1 #26, kompiliert am 2021-02-12 17:41+0000)
[INFO] Executing on Database: jdbc:postgresql://localhost:5432/db-migration
[INFO] Reading from databasechangelog
postgres@jdbc:postgresql://localhost:5432/db-migration is up to date
```

If you want to migrate to a specific version you have to use the `labels` attribute.
```shell
mvn liquibase:update -Dliquibase.labels=1
```

```shell
[INFO] --- liquibase-maven-plugin:4.3.1:update (default-cli) @ liquibase-showcase ---
[INFO] ------------------------------------------------------------------------
[project, pluginDescriptor]
[INFO] 
[INFO] 
[INFO] Liquibase Community 4.3.1 by Datical
[INFO] Starte Liquibase am 17:12:10 (Version 4.3.1 #26, kompiliert am 2021-02-12 17:41+0000)
[INFO] Executing on Database: jdbc:postgresql://localhost:5432/db-migration
[INFO] Changelog-Protokoll erfolgreich gesperrt.
[INFO] Creating database history table with name: databasechangelog
[INFO] Reading from databasechangelog
[INFO] Cannot load service: liquibase.hub.HubService: Provider liquibase.hub.core.OnlineHubService could not be instantiated
[INFO] Successfully released change log lock
[INFO] Changelog-Protokoll erfolgreich gesperrt.
[INFO] SQL in file V1__create_table_quarkus.sql executed
[INFO] ChangeSet version/v1/db.changelog.v1.xml::1::quarkus ran successfully in 15ms
[INFO] Successfully released change log lock

```

After migration to version `1` was successful the result of `liquibase:status` should look like this

```shell
[INFO] --- liquibase-maven-plugin:4.3.1:status (default-cli) @ liquibase-showcase ---
[INFO] ------------------------------------------------------------------------
[project, pluginDescriptor]
[INFO] 
[INFO] 
[INFO] Liquibase Community 4.3.1 by Datical
[INFO] Starte Liquibase am 17:13:00 (Version 4.3.1 #26, kompiliert am 2021-02-12 17:41+0000)
[INFO] Executing on Database: jdbc:postgresql://localhost:5432/db-migration
[INFO] Reading from databasechangelog
1 change sets have not been applied to postgres@jdbc:postgresql://localhost:5432/db-migration
     version/v2/db.changelog.v2.xml::2::quarkus

```


#### Automatic database migration during application startup

If the application should run the automatic database migration during the application startup it needs to be compiled and packaged using
`Maven`. The Maven lifecycle creates a jar package and uses the `quarkus-maven-plugin` to create a runnable
JAR (fat JAR) which contains the application and the Quarkus application server. The fat JAR will be copied into a
Docker image using Spotify's `dockerfile-maven-plugin` during the package phase.

```shell script
mvn clean package
docker compose up application
```

If the migration was successful you should see the following data in the `xxx` table.

```sql
SELECT * FROM databasechangelog;
```

| id | author | filename | dateexecuted | orderexecuted | exectype | md5sum | description | comments | tag | liquibase | contexts | labels | deployment\_id |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | quarkus | db/version/v1/db.changelog.v1.xml | 2021-03-14 19:46:30.725283 | 1 | EXECUTED | 8:4516226769c0b9ce2013de0069fe8e25 | sqlFile |  | NULL | 4.2.2 | NULL | 1 | 5747590712 |
| 2 | quarkus | db/version/v2/db.changelog.v2.xml | 2021-03-14 19:46:30.738426 | 2 | EXECUTED | 8:78b45cad83349876aab5a5a469485567 | sqlFile |  | NULL | 4.2.2 | NULL | 2 | 5747590712 |

