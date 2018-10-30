package com.barclayadunn.helloworld.resources

import javax.ws.rs.core.MediaType

import com.barclayadunn.helloworld.core.Person
import com.barclayadunn.helloworld.db.PersonDAO
import io.dropwizard.hibernate.UnitOfWork

import javax.ws.rs.{Path, Produces, GET, POST}

@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
class PeopleResource(peopleDAO: PersonDAO) {

    @POST
    @UnitOfWork
    def createPerson(person: Person): Person = {
        peopleDAO.create(person)
    }

    @GET
    @UnitOfWork
    def listPeople(): List[Person] = {
      peopleDAO.findAll
    }
}
