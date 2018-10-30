package com.barclayadunn.helloworld

import com.barclayadunn.helloworld.auth.{ExampleAuthorizer, ExampleAuthenticator}
import com.barclayadunn.helloworld.cli.RenderCommand
import com.barclayadunn.helloworld.core.{User, Template, Person}
import com.barclayadunn.helloworld.db.PersonDAO
import com.barclayadunn.helloworld.filter.DateRequiredFeature
import com.barclayadunn.helloworld.health.TemplateHealthCheck
import com.barclayadunn.helloworld.resources._
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.configuration.{EnvironmentVariableSubstitutor, SubstitutingSourceProvider}
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.hibernate.HibernateBundle
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.{Environment, Bootstrap}
import io.dropwizard.views.ViewBundle
import io.dropwizard.auth.AuthValueFactoryProvider
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature

/**
 * Created by barclay.dunn on 8/28/15
 */
class HelloWorldApplication extends Application[HelloWorldConfiguration] {

  @throws(classOf[Exception])
  def main(args: Array[String]) {
    new HelloWorldApplication().run(args)
  }

  private val hibernateBundle: HibernateBundle[HelloWorldConfiguration] = new HibernateBundle[HelloWorldConfiguration]((classOf[Person])) {
    def getDataSourceFactory(configuration: HelloWorldConfiguration): DataSourceFactory = {
      configuration.getDataSourceFactory
    }
  }

  override def getName: String = {
    "hello-world"
  }

  override def initialize(bootstrap: Bootstrap[HelloWorldConfiguration]) {
    bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider, new EnvironmentVariableSubstitutor(false)))
    bootstrap.addCommand(new RenderCommand)
    bootstrap.addBundle(new AssetsBundle)
    bootstrap.addBundle(new MigrationsBundle[HelloWorldConfiguration]() {
      def getDataSourceFactory(configuration: HelloWorldConfiguration): DataSourceFactory = {
        configuration.getDataSourceFactory
      }
    })
    bootstrap.addBundle(hibernateBundle)
    bootstrap.addBundle(new ViewBundle[HelloWorldConfiguration]() {
      override def getViewConfiguration(configuration: HelloWorldConfiguration): Map[String, Map[String, String]] = {
        configuration.getViewRendererConfiguration
      }
    })
  }

  def run(configuration: HelloWorldConfiguration, environment: Environment) {
    val dao: PersonDAO = new PersonDAO(hibernateBundle.getSessionFactory)
    val template: Template = configuration.buildTemplate
    environment.healthChecks.register("template", new TemplateHealthCheck(template))
    environment.jersey.register(classOf[DateRequiredFeature])
    environment.jersey.register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder[User]().setAuthenticator(new ExampleAuthenticator).setAuthorizer(new ExampleAuthorizer).setRealm("SUPER SECRET STUFF").buildAuthFilter))
    environment.jersey.register(new AuthValueFactoryProvider.Binder[User](classOf[User]))
    environment.jersey.register(classOf[RolesAllowedDynamicFeature])
    environment.jersey.register(new HelloWorldResource(template))
    environment.jersey.register(new ViewResource)
    environment.jersey.register(new ProtectedResource)
    environment.jersey.register(new PeopleResource(dao))
    environment.jersey.register(new PersonResource(dao))
    environment.jersey.register(new FilteredResource)
  }
}
