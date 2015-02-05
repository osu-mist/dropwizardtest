package edu.oregonstate.mist.dropwizardtest.resources

import io.dropwizard.auth.Auth
import io.dropwizard.hibernate.UnitOfWork
import io.dropwizard.jersey.params.LongParam
import edu.oregonstate.mist.dropwizardtest.auth.AuthenticatedUser
import edu.oregonstate.mist.dropwizardtest.core.Employee
import edu.oregonstate.mist.dropwizardtest.*
import edu.oregonstate.mist.dropwizardtest.db.EmployeeDAO

import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.Consumes
//import javax.ws.rs.NotFoundException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
//import javax.ws.rs.core.Response.Status

import com.codahale.metrics.annotation.Metered
import com.codahale.metrics.annotation.Timed
import com.google.common.base.Optional

@Path('employee')
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    private final EmployeeDAO employeeDAO

    public EmployeeResource(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO
    }

    @GET
    @Timed
    @Path('{id: \\d+}')
    @UnitOfWork
    public Employee getById(@PathParam('id') LongParam id) {
        final Optional<Employee> employee = employeeDAO.findById(id.get())

        if (employee.isPresent()) {
            return employee.get()
        //} else {
            //throw new NotFoundException("No such employee.")
        }
    }

    @POST
    @Metered
    @Path('{id: \\d+}')
    public Response setById(@PathParam('id') Integer id, String requestBody) {
        return Response.ok(requestBody).build()
    }

    @GET
    @Path('{id: \\d+}/OnidLoginId')
    @Produces(MediaType.TEXT_PLAIN)
    public String getOnidLoginIdById(@PathParam('id') Integer id, @Auth AuthenticatedUser user) {
    }
}
