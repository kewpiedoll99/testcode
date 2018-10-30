package com.barclayadunn.helloworld.views

import com.barclayadunn.helloworld.core.Person
import io.dropwizard.views.View

/**
 * Created by barclay.dunn on 8/28/15
 */
class PersonView(template: String, person: Person) extends View(template) {

  object Template extends Enumeration {
    type Template = Value
    val FREEMARKER, MUSTACHE = Value
  }

  class Template {
    private var templateName: String = null

    private[views] def this(templateName: String) {
      this()
      this.templateName = templateName
    }

    def getTemplateName: String = {
      templateName
    }
  }

  def getPerson: Person = {
    person
  }
}
