package com.barclayadunn.helloworld.filter

import javax.ws.rs.container.{ResourceInfo, DynamicFeature}
import javax.ws.rs.core.FeatureContext

/**
 * Created by barclay.dunn on 8/28/15.
 */
class DateRequiredFeature extends DynamicFeature {
  @Override
  def configure( resourceInfo: ResourceInfo,  context: FeatureContext) {
    if (resourceInfo.getResourceMethod().getAnnotation(DateRequired.getClass) != null)
    {
      context.register(DateNotSpecifiedFilter.getClass);
    }
  }
}
