package com.barclayadunn.helloworld.cli

import com.barclayadunn.helloworld.HelloWorldConfiguration
import com.barclayadunn.helloworld.core.Template
import com.google.common.base.Optional
import io.dropwizard.cli.ConfiguredCommand
import io.dropwizard.setup.Bootstrap
import net.sourceforge.argparse4j.impl.Arguments
import org.slf4j.LoggerFactory

/**
 * Created by barclay.dunn on 8/28/15.
 */
class RenderCommand extends ConfiguredCommand[HelloWorldConfiguration] {
  private val LOGGER: Nothing = LoggerFactory.getLogger(classOf[RenderCommand])

  def this() {
    this()
    `super`("render", "Render the template data to console")
  }

  override def configure(subparser: Nothing) {
    super.configure(subparser)
    subparser.addArgument("-i", "--include-default").action(Arguments.storeTrue).dest("include-default").help("Also render the template with the default name")
    subparser.addArgument("names").nargs("*")
  }

  @throws(classOf[Exception])
  protected def run(bootstrap: Bootstrap[HelloWorldConfiguration], namespace: Nothing, configuration: HelloWorldConfiguration) {
    val template: Template = configuration.buildTemplate
    if (namespace.getBoolean("include-default")) {
      LOGGER.info("DEFAULT => {}", template.render(Optional.absent[String]))
    }

    import scala.collection.JavaConversions._

    for (name <- namespace.getList[String]("names")) {
      {
        var i: Int = 0
        while (i < 1000) {
          {
            LOGGER.info("{} => {}", name, template.render(Optional.of(name)))
            Thread.sleep(1000)
          }
          ({
            i += 1;
            i - 1
          })
        }
      }
    }
  }
}
