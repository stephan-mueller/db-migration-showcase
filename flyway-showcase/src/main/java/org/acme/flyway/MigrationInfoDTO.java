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

import org.flywaydb.core.api.MigrationInfo;

import java.util.Date;

public class MigrationInfoDTO extends MigrationInfoBaseDTO {

  private final Date installedOn;

  private final String installedBy;

  private final Integer executionTime;

  private final Integer installedRank;

  public MigrationInfoDTO(final MigrationInfo migrationInfo) {
    super(migrationInfo);
    this.installedOn = migrationInfo.getInstalledOn();
    this.installedBy = migrationInfo.getInstalledBy();
    this.executionTime = migrationInfo.getExecutionTime();
    this.installedRank = migrationInfo.getInstalledRank();
  }

  public Date getInstalledOn() {
    return installedOn;
  }

  public String getInstalledBy() {
    return installedBy;
  }

  public Integer getInstalledRank() {
    return installedRank;
  }

  public Integer getExecutionTime() {
    return executionTime;
  }

  @Override
  public String toString() {
    return "MigrationInfoBaseDTO{" +
           "type=" + getType() +
           ", checksum=" + getChecksum() +
           ", version=" + getVersion() +
           ", description='" + getDescription() + '\'' +
           ", script='" + getScript() + '\'' +
           ", state=" + getState() +
           ", installedOn=" + installedOn +
           ", installedBy='" + installedBy + '\'' +
           ", executionTime=" + executionTime +
           ", installedRank=" + installedRank +
           ", physicalLocation='" + getPhysicalLocation() + '\'' +
           '}';
  }
}
