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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import liquibase.exception.LiquibaseException;

@Path("migrations")
@ApplicationScoped
public class MigrationResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationResource.class);

  @Inject
  MigrationService service;

  @DELETE
  @Produces("application/json")
  public Response dropAll() throws LiquibaseException {
    LOGGER.info("Drop database");

    service.dropAll();

    return Response.status(Response.Status.NO_CONTENT).build();
  }

  @GET
  @Path("status")
  @Produces("application/json")
  public Response getStatus() throws LiquibaseException {
    LOGGER.info("Get status");

    Set<ChangeSetStatusDTO> result = service.getStatus()
        .stream()
        .map(s -> new ChangeSetStatusDTO(s))
        .collect(Collectors.toSet());

    if (result.isEmpty()) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }

    return Response.status(Response.Status.OK).entity(result).build();
  }

  @GET
  @Path("unrun")
  @Produces("application/json")
  public Response listUnrunChangeSets() throws LiquibaseException {
    LOGGER.info("List unrun changeSets");

    List<ChangeSetDTO> changeSets = service.listUnrunChangeSets()
        .stream()
        .map(changeSet -> new ChangeSetDTO(changeSet))
        .collect(Collectors.toList());

    if (changeSets.isEmpty()) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }

    return Response.status(Response.Status.OK).entity(changeSets).build();
  }

  @POST
  @Produces("application/json")
  public Response update() throws LiquibaseException {
    LOGGER.info("Update database");

    Set<ChangeSetStatusDTO> result = service.update()
        .stream()
        .map(s -> new ChangeSetStatusDTO(s))
        .collect(Collectors.toSet());

    if (result.isEmpty()) {
      return Response.status(Response.Status.NO_CONTENT).build();
    }

    return Response.status(Response.Status.OK).entity(result).build();
  }
}
