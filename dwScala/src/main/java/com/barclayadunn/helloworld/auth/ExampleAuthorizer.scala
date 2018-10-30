package com.barclayadunn.helloworld.auth

import com.barclayadunn.helloworld.core.User

/**
 * Created by barclay.dunn on 8/28/15.
 */
class ExampleAuthorizer extends Authorizer[User] {
  @Override
  def authorize(user: User,  role: String) {
    if (role.equals("ADMIN") && !user.getName().startsWith("chief")) {
      return false;
    }
    return true;
  }
}
