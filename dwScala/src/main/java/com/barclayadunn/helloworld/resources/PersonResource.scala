package com.barclayadunn.helloworld.resources

import com.barclayadunn.helloworld.core.Person
import com.barclayadunn.helloworld.db.PersonDAO
import com.barclayadunn.helloworld.views.PersonView
import com.google.common.base.Optional
import io.dropwizard.hibernate.UnitOfWork
import io.dropwizard.jersey.params.LongParam

import javax.ws.rs.GET
import javax.ws.rs.NotFoundException
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

import scala.reflect.io.Path

@Path("/people/{personId}")
@Produces(MediaType.APPLICATION_JSON)
class PersonResource(peopleDAO: PersonDAO) {

    @GET
    @UnitOfWork
    def getPerson(@PathParam("personId") personId: LongParam): Person = {
        findSafely(personId.get())
    }

    @GET
    @Path("/view_freemarker")
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    def getPersonViewFreemarker(@PathParam("personId") personId: LongParam): PersonView = {
      new PersonView(PersonView.Template.FREEMARKER, findSafely(personId.get()))
    }

    @GET
    @Path("/view_mustache")
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    def getPersonViewMustache(@PathParam("personId") LongParam personId): PersonView = {
        new PersonView(PersonView.Template.MUSTACHE, findSafely(personId.get()))
    }

    def findSafely(personId: Long): Person = {
        val person: Optional[Person] = peopleDAO.findById(personId)
        if (!person.isPresent) {
            throw new NotFoundException("No such user.")
        }
        person.get()
    }
}
