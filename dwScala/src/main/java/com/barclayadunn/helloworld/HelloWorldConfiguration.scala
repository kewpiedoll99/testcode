package com.barclayadunn.helloworld

import javax.validation.Valid

import com.barclayadunn.helloworld.core.Template
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.collect.ImmutableMap
import io.dropwizard.Configuration
import org.hibernate.validator.constraints.NotEmpty
import io.dropwizard.db.DataSourceFactory
import scala.reflect.internal.util.Collections

/**
 * Created by barclay.dunn on 8/28/15
 */

class HelloWorldConfiguration extends Configuration {
  @NotEmpty private var template: String = null
  @NotEmpty private var defaultName: String = "Stranger"
  @Valid
  @NotNull private var database: DataSourceFactory = new DataSourceFactory
  @NotNull private var viewRendererConfiguration: Map[String, Map[String, String]] = Collections.emptyMap

  @JsonProperty def getTemplate: String = {
    return template
  }

  @JsonProperty def setTemplate(template: String) {
    this.template = template
  }

  @JsonProperty def getDefaultName: String = {
    return defaultName
  }

  @JsonProperty def setDefaultName(defaultName: String) {
    this.defaultName = defaultName
  }

  def buildTemplate: Template = {
    return new Template(template, defaultName)
  }

  @JsonProperty("database") def getDataSourceFactory: DataSourceFactory = {
    return database
  }

  @JsonProperty("database") def setDataSourceFactory(dataSourceFactory: DataSourceFactory) {
    this.database = dataSourceFactory
  }

  @JsonProperty("viewRendererConfiguration") def getViewRendererConfiguration: Map[String, Map[String, String]] = {
    return viewRendererConfiguration
  }

  @JsonProperty("viewRendererConfiguration") def setViewRendererConfiguration(viewRendererConfiguration: Map[String, Map[String, String]]) {
    val builder = ImmutableMap.builder
    import scala.collection.JavaConversions._
    for (entry <- viewRendererConfiguration.entrySet) {
      builder.put(entry.getKey, ImmutableMap.copyOf(entry.getValue))
    }
    this.viewRendererConfiguration = builder.build
  }
}
