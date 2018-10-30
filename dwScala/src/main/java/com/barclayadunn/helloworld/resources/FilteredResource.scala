package com.barclayadunn.helloworld.resources

import javax.ws.rs.GET
import javax.ws.rs.Path

import com.barclayadunn.helloworld.filter.DateRequired

@Path("/filtered")
class FilteredResource {

    @GET
    @DateRequired
    @Path("hello")
    def sayHello(): String = {
        "hello"
    }
}
