package com.barclayadunn.helloworld.core

import java.lang.String.format
/**
 * Created by barclay.dunn on 8/28/15.
 */
class Template {

  private val content: String = null
  private val defaultName: String = null

  def this(content: String, defaultName: String) {
    this()
    this.content = content
    this.defaultName = defaultName
  }

  def render(name: String): String = {
    return format(content, name.or(defaultName))
  }
}
