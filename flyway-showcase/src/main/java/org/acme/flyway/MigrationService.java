/*
 * Copyright (C) Stephan Mueller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.acme.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.output.CleanResult;
import org.flywaydb.core.api.output.MigrateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MigrationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationService.class);

  @Inject
  Flyway flyway;

  public void checkMigration() {
    System.out.println(flyway.info().current().getVersion().toString());
  }

  public CleanResult clean() {
    LOGGER.debug("Clean database");
    return flyway.clean();
  }

  public MigrateResult migrate() {
    LOGGER.debug("Migrate database");
    return flyway.migrate();
  }

  public MigrationInfo[] getInfo() {
    LOGGER.debug("Get info");
    return flyway.info().all();
  }

  public MigrationInfo[] getAppliedVersions() {
    LOGGER.debug("Get applied versions");
    return flyway.info().applied();
  }

  public Optional<MigrationInfo> getCurrentVersion() {
    LOGGER.debug("Get current version");
    return Optional.<MigrationInfo>ofNullable(flyway.info().current());
  }

  public MigrationInfo[] getPendingVersions() {
    LOGGER.debug("Get pending versions");
    return flyway.info().pending();
  }
}