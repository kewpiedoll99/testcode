package com.barclayadunn.helloworld.filter

import java.lang.annotation.{Retention, Target, ElementType, RetentionPolicy}

/**
 * Created by barclay.dunn on 8/28/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(Array(ElementType.METHOD))
trait DateRequired {

}
