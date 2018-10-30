package com.barclayadunn.helloworld.core

import java.security.Principal

/**
 * Created by barclay.dunn on 8/28/15.
 */
class User extends Principal {
private val name: String = null

def this (name: String) {
this ()
this.name = name
}

def getName: String = {
return name
}

def getId: Int = {
return (Math.random * 100).toInt
}
}
