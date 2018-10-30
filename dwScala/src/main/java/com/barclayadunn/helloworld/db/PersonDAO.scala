package com.barclayadunn.helloworld.db

import com.barclayadunn.helloworld.core.Person
import com.google.common.base.Optional
import io.dropwizard.hibernate.AbstractDAO
import org.hibernate.SessionFactory

/**
 * Created by barclay.dunn on 8/28/15
 */
class PersonDAO(factory: SessionFactory) extends AbstractDAO[Person](factory) {
  def findById(id: Long): Optional[Person] = {
    Optional.fromNullable(get(id))
  }

  def create(person: Person): Person = {
    persist(person)
  }

  def findAll: List[Person] = {
    list(namedQuery("com.example.helloworld.core.Person.findAll")).asInstanceOf[List[Person]]
  }
}
