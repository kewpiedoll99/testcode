package com.barclayadunn.helloworld.filter

import javax.ws.rs.WebApplicationException
import javax.ws.rs.container.{ContainerRequestContext, ContainerRequestFilter}
import javax.ws.rs.core.{Response, HttpHeaders}

/**
 * Created by barclay.dunn on 8/28/15.
 */
class DateNotSpecifiedFilter extends ContainerRequestFilter {
  @Override
  def filter( requestContext: ContainerRequestContext) {
    val dateHeader = requestContext.getHeaderString(HttpHeaders.DATE);

    if (dateHeader == null) {
      val cause = new IllegalArgumentException("Date Header was not specified");
      throw new WebApplicationException(cause, Response.Status.BAD_REQUEST);
    }
  }
}
