package edu.oregonstate.mist.dropwizardtest.resources

import io.dropwizard.auth.Auth
import io.dropwizard.hibernate.UnitOfWork
import io.dropwizard.jersey.params.LongParam
import edu.oregonstate.mist.dropwizardtest.auth.AuthenticatedUser
import edu.oregonstate.mist.dropwizardtest.core.Employee
import edu.oregonstate.mist.dropwizardtest.*
import edu.oregonstate.mist.dropwizardtest.db.EmployeeDAO

import javax.ws.rs.OPTIONS
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.Consumes
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import com.codahale.metrics.annotation.Metered
import com.codahale.metrics.annotation.Timed
import com.google.common.base.Optional
import javax.validation.Valid

@Path('employee')
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    private final EmployeeDAO employeeDAO

    public EmployeeResource(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO
    }

    @OPTIONS
    public List options() {
        return ['OPTIONS', 'PUT']
    }

    @PUT
    @UnitOfWork
    public Employee setEmployee(@Valid Employee employee) {
        return employeeDAO.set(employee)
    }

    @OPTIONS
    @Path('{id: \\d+}')
    public List optionsById() {
        return ['OPTIONS', 'GET']
    }

    @GET
    @Timed
    @Path('{id: \\d+}')
    @UnitOfWork
    public Employee getById(@PathParam('id') LongParam id) {
        final Optional<Employee> employee = employeeDAO.findById(id.get())

        if (!employee.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND)
        }

        return employee.get()
    }

    @OPTIONS
    @Path('{id: \\d+}/OnidLoginId')
    public List optionsOnid() {
        return ['OPTIONS', 'GET']
    }

    @GET
    @Path('{id: \\d+}/OnidLoginId')
    @Produces(MediaType.TEXT_PLAIN)
    @UnitOfWork
    public String getOnidLoginIdById(@PathParam('id') LongParam id, @Auth AuthenticatedUser user) {
        final Optional<Employee> employee = employeeDAO.findById(id.get())

        if (!employee.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND)
        }

        return employee.get().onidLoginId
    }
}
