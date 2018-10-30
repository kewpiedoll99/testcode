package com.barclayadunn.helloworld.resources

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import javax.validation.Valid
import javax.ws.rs._
import javax.ws.rs.core.MediaType

import com.barclayadunn.helloworld.api.Saying
import com.barclayadunn.helloworld.core.Template
import com.codahale.metrics.annotation.Timed
import io.dropwizard.jersey.caching.CacheControl
import io.dropwizard.jersey.params.DateTimeParam
import org.slf4j.{Logger, LoggerFactory}

/**
 * Created by barclay.dunn on 8/28/15
 */
@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
class HelloWorldResource {
  private val LOGGER: Logger = LoggerFactory.getLogger(classOf[HelloWorldResource])
  private var template: Template = null
  private var counter: AtomicLong = null

  def this(template: Template) {
    this()
    this.template = template
    this.counter = new AtomicLong
  }

  @GET
  @Timed(name = "get-requests")
  @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS) def sayHello(@QueryParam("name") name: Nothing): Saying = {
    new Saying(counter.incrementAndGet, template.render(name))
  }

  @POST def receiveHello(@Valid saying: Saying) {
    LOGGER.info("Received a saying: {}", saying)
  }

  @GET
  @Path("/date")
  @Produces(MediaType.TEXT_PLAIN) def receiveDate(@QueryParam("date") dateTimeParam: DateTimeParam): String = {
    if (dateTimeParam.!=(null)) {
      val actualDateTimeParam: DateTimeParam = dateTimeParam
      LOGGER.info("Received a date: {}", actualDateTimeParam)
       actualDateTimeParam.get.toString
    }
    else {
      LOGGER.warn("No received date")
       null
    }
  }
}
