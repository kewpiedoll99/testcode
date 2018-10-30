package com.barclayadunn.helloworld.health

import com.barclayadunn.helloworld.core.Template
import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheck.Result
import com.google.common.base.Optional

/**
 * Created by barclay.dunn on 8/28/15.
 */
class TemplateHealthCheck extends HealthCheck {
  private val template: Template = null

  def this(template: Template) {
    this()
    this.template = template
  }

  @throws(classOf[Exception])
  protected def check: Nothing = {
    template.render(Optional.of("woo"))
    template.render(Optional.absent[String])
    return Result.healthy
  }
}
