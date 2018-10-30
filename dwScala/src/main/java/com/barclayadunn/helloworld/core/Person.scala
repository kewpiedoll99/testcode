package com.barclayadunn.helloworld.core

import javax.persistence._

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id

/**
 * Created by barclay.dunn on 8/28/15
 */
@Entity
@Table(name = "people")
@NamedQueries(Array(new NamedQuery(name = "com.example.helloworld.core.Person.findAll", query = "SELECT p FROM Person p")))
class Person(@Column(name = "fullName", nullable = false) fullName: String,
             @Column(name = "jobTitle", nullable = false) jobTitle: String) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) private var id: Long = 0L

  def getId: Long = {
    id
  }

  def setId(id: Long) {
    this.id = id
  }
}
