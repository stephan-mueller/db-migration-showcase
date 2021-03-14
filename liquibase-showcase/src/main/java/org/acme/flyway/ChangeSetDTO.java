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

import liquibase.Labels;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.RanChangeSet;

public class ChangeSetDTO {

  private final String id;

  private final String author;

  private final Labels labels;

  private final String description;

  private final String filePath;

  private final String storedFilePath;

  private final String runOrder;

  private final boolean alwaysRun;

  private final boolean runOnChange;

  private final boolean ignore;

  private final String comments;

  public ChangeSetDTO(final ChangeSet changeSet) {
    this.id = changeSet.getId();
    this.author = changeSet.getAuthor();
    this.description = changeSet.getDescription();
    this.labels = changeSet.getLabels();
    this.filePath = changeSet.getFilePath();
    this.storedFilePath = changeSet.getStoredFilePath();
    this.runOrder = changeSet.getRunOrder();
    this.alwaysRun = changeSet.isAlwaysRun();
    this.runOnChange = changeSet.isRunOnChange();
    this.ignore = changeSet.isIgnore();
    this.comments = changeSet.getComments();
  }

  public ChangeSetDTO(final RanChangeSet changeSet) {
    this.id = changeSet.getId();
    this.author = changeSet.getAuthor();
    this.description = changeSet.getDescription();
    this.labels = changeSet.getLabels();
    this.filePath = null;
    this.storedFilePath = null;
    this.runOrder = null;
    this.alwaysRun = false;
    this.runOnChange = false;
    this.ignore = false;
    this.comments = changeSet.getComments();
  }

  public String getId() {
    return id;
  }

  public String getAuthor() {
    return author;
  }

  public Labels getLabels() {
    return labels;
  }

  public String getDescription() {
    return description;
  }

  public String getFilePath() {
    return filePath;
  }

  public String getStoredFilePath() {
    return storedFilePath;
  }

  public String getRunOrder() {
    return runOrder;
  }

  public boolean isAlwaysRun() {
    return alwaysRun;
  }

  public boolean isRunOnChange() {
    return runOnChange;
  }

  public boolean isIgnore() {
    return ignore;
  }

  public String getComments() {
    return comments;
  }

  @Override
  public String toString() {
    return "ChangeSetDTO{" +
           "id='" + id + '\'' +
           ", author='" + author + '\'' +
           ", labels=" + labels +
           ", description='" + description + '\'' +
           ", filePath='" + filePath + '\'' +
           ", storedFilePath='" + storedFilePath + '\'' +
           ", runOrder='" + runOrder + '\'' +
           ", alwaysRun=" + alwaysRun +
           ", runOnChange=" + runOnChange +
           ", ignore=" + ignore +
           ", comments='" + comments + '\'' +
           '}';
  }
}
