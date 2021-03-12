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
import org.flywaydb.core.api.MigrationState;
import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.MigrationVersion;

public class MigrationInfoBaseDTO {

  private final MigrationType type;

  private final Integer checksum;

  private final MigrationVersion version;

  private final String description;

  private final String script;

  private final MigrationState state;

  private final String physicalLocation;

  public MigrationInfoBaseDTO(final MigrationInfo migrationInfo) {
    super();
    this.type = migrationInfo.getType();
    this.checksum = migrationInfo.getChecksum();
    this.version = migrationInfo.getVersion();
    this.description = migrationInfo.getDescription();
    this.script = migrationInfo.getScript();
    this.state = migrationInfo.getState();
    this.physicalLocation = migrationInfo.getPhysicalLocation();
  }

  public MigrationType getType() {
    return type;
  }

  public Integer getChecksum() {
    return checksum;
  }

  public MigrationVersion getVersion() {
    return version;
  }

  public String getDescription() {
    return description;
  }

  public String getScript() {
    return script;
  }

  public MigrationState getState() {
    return state;
  }

  public String getPhysicalLocation() {
    return physicalLocation;
  }

  @Override
  public String toString() {
    return "MigrationInfoBaseDTO{" +
           "type=" + type +
           ", checksum=" + checksum +
           ", version=" + version +
           ", description='" + description + '\'' +
           ", script='" + script + '\'' +
           ", state=" + state +
           ", physicalLocation='" + physicalLocation + '\'' +
           '}';
  }
}
