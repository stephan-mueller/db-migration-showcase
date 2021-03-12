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
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.output.CleanResult;
import org.flywaydb.core.api.output.MigrateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("migrations")
@ApplicationScoped
public class MigrationResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationResource.class);

  @Inject
  MigrationService service;

  @DELETE
  @Produces("application/json")
  public Response clean() {
    LOGGER.info("Clean database");

    CleanResult result = service.clean();

    return Response.status(Response.Status.OK).entity(result).build();
  }

  @POST
  @Produces("application/json")
  public Response migrate() {
    LOGGER.info("Migrate database");

    MigrateResult result = service.migrate();

    return Response.status(Response.Status.OK).entity(result).build();
  }

  @GET
  @Path("applied")
  @Produces("application/json")
  public Response getAppliedVersions() {
    LOGGER.info("Get applied versions");

    List<MigrationInfoDTO> migrations = Arrays.stream(service.getAppliedVersions())
        .map(i -> new MigrationInfoDTO(i))
        .collect(Collectors.toList());

    if (migrations.isEmpty()) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }

    return Response.status(Response.Status.OK).entity(migrations).build();
  }

  @GET
  @Path("current")
  @Produces("application/json")
  public Response getCurrentVersion() {
    LOGGER.info("Get current version");

    Optional<MigrationInfo> current = service.getCurrentVersion();

    if (current.isEmpty()) {
      return Response.status(Response.Status.OK).entity("No version available").build();
    }

    return Response.status(Response.Status.OK).entity(current.get().getVersion()).build();
  }

  @GET
  @Path("info")
  @Produces("application/json")
  public Response getInfo() {
    LOGGER.info("Get info");

    List<MigrationInfoDTO> migrations = Arrays.stream(service.getInfo())
        .map(i -> new MigrationInfoDTO(i))
        .collect(Collectors.toList());

    if (migrations.isEmpty()) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }

    return Response.status(Response.Status.OK).entity(migrations).build();
  }

  @GET
  @Path("pending")
  @Produces("application/json")
  public Response getPendingVersion() {
    LOGGER.info("Get pending versions");

    List<MigrationInfoBaseDTO> migrations = Arrays.stream(service.getPendingVersions())
        .map(i -> new MigrationInfoBaseDTO(i))
        .collect(Collectors.toList());

    if (migrations.isEmpty()) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }

    return Response.status(Response.Status.OK).entity(migrations).build();
  }
}
