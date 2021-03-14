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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.liquibase.LiquibaseFactory;
import liquibase.Liquibase;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.ChangeSetStatus;
import liquibase.changelog.RanChangeSet;
import liquibase.exception.LiquibaseException;

@ApplicationScoped
public class MigrationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationService.class);

  @Inject
  LiquibaseFactory liquibaseFactory;

  public void dropAll() throws LiquibaseException {
    LOGGER.debug("Drop database");
    try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
      liquibase.dropAll();
    }
  }

  public List<ChangeSetStatus> update() throws LiquibaseException {
    LOGGER.debug("Migrate database");
    try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
      liquibase.dropAll();
      liquibase.validate();
      liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());

      return liquibase.getChangeSetStatuses(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
    }
  }

  public List<ChangeSetStatus> getStatus() throws LiquibaseException {
    LOGGER.debug("Get status");
    try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
      return liquibase.getChangeSetStatuses(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
    }
  }

  public List<ChangeSet> listUnrunChangeSets() throws LiquibaseException {
    LOGGER.debug("List unrun change sets");
    try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
      return liquibase.listUnrunChangeSets(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
    }
  }
}