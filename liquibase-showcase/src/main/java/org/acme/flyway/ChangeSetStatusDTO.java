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

import java.util.Date;

import liquibase.change.CheckSum;
import liquibase.changelog.ChangeSetStatus;

public class ChangeSetStatusDTO extends ChangeSetDTO {

  private final boolean willRun;

  private final Date dateLastExecuted;

  private final boolean previouslyRan;

  private final CheckSum currentCheckSum;

  private final CheckSum storedCheckSum;

  public ChangeSetStatusDTO(final ChangeSetStatus changeSetStatus) {
    super(changeSetStatus.getChangeSet());
    this.willRun = changeSetStatus.getWillRun();
    this.dateLastExecuted = changeSetStatus.getDateLastExecuted();
    this.previouslyRan = changeSetStatus.getPreviouslyRan();
    this.currentCheckSum = changeSetStatus.getCurrentCheckSum();
    this.storedCheckSum = changeSetStatus.getStoredCheckSum();
  }

  public boolean isWillRun() {
    return willRun;
  }

  public Date getDateLastExecuted() {
    return dateLastExecuted;
  }

  public boolean isPreviouslyRan() {
    return previouslyRan;
  }

  public CheckSum getCurrentCheckSum() {
    return currentCheckSum;
  }

  public CheckSum getStoredCheckSum() {
    return storedCheckSum;
  }

  @Override
  public String toString() {
    return "ChangeSetStatusDTO{" +
           "id='" + getId() + '\'' +
           ", author='" + getAuthor() + '\'' +
           ", labels=" + getLabels() +
           ", description='" + getDescription() + '\'' +
           ", filePath='" + getFilePath() + '\'' +
           ", storedFilePath='" + getStoredFilePath() + '\'' +
           ", runOrder='" + getRunOrder() + '\'' +
           ", alwaysRun=" + isAlwaysRun() +
           ", runOnChange=" + isRunOnChange() +
           ", ignore=" + isIgnore() +
           ", comments='" + getComments() + '\'' +
           ", willRun=" + willRun +
           ", dateLastExecuted=" + dateLastExecuted +
           ", previouslyRan=" + previouslyRan +
           ", currentCheckSum=" + currentCheckSum +
           ", storedCheckSum=" + storedCheckSum +
           '}';
  }
}
