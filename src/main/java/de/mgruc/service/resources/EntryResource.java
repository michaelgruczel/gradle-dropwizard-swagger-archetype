package de.mgruc.service.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.sun.jersey.api.NotFoundException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import de.mgruc.service.auth.User;
import de.mgruc.service.core.ValueEntry;
import de.mgruc.service.db.ServiceDAO;

@Path("/entry")
@Api("/entry")
@Produces(MediaType.APPLICATION_JSON)
public class EntryResource {

	private final ServiceDAO serviceDAO;

	public EntryResource(ServiceDAO serviceDAO) {
		this.serviceDAO = serviceDAO;
	}

	@POST
	@Timed
	@Path("/removeAll")
	@ApiOperation(value = "Remove all values", notes = "use basic auth for this method", response = String.class)
	public void removeAll(@Auth User user) {
		serviceDAO.deleteAll();
	}

	@POST
	@UnitOfWork
	@ApiOperation(value = "Add a value", notes = "-", response = ValueEntry.class)
	@Consumes(MediaType.APPLICATION_JSON)
	public ValueEntry addEntry(
			@ApiParam(value = "element", required = true) ValueEntry element) {
		serviceDAO.insert(element.getId(), element.getContent());
		return element;
	}

	@GET
	@UnitOfWork
	@ApiOperation(value = "Find a value by ID", notes = "-", response = ValueEntry.class)
	@Path("/{id}")
	public ValueEntry getEntry(
			@ApiParam(value = "ID of the element", required = true) @PathParam("id") Integer id) {
		String result = serviceDAO.findValueById(id);
		if (result == null) {
			throw new NotFoundException("No such thing");
		}
		return new ValueEntry(id, result);
	}

}
