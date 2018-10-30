package com.barclayadunn.helloworld.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.base.MoreObjects
import org.hibernate.validator.constraints.Length

/**
 * Created by barclay.dunn on 8/28/15.
 */
class Saying {
  private var id: Long = 0L
  @Length(max = 3) private var content: String = null

  def this() {
    this()
  }

  def this(id: Long, content: String) {
    this()
    this.id = id
    this.content = content
  }

  @JsonProperty def getId: Long = {
    return id
  }

  @JsonProperty def getContent: String = {
    return content
  }

  override def toString: String = {
    return MoreObjects.toStringHelper(this).add("id", id).add("content", content).toString
  }
}
