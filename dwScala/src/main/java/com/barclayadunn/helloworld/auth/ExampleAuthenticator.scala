package com.barclayadunn.helloworld.auth

import com.barclayadunn.helloworld.core.User
import com.google.common.base.Optional
import io.dropwizard.auth.Authenticator
import io.dropwizard.auth.basic.BasicCredentials

/**
 * Created by barclay.dunn on 8/28/15.
 */
class ExampleAuthenticator extends Authenticator[BasicCredentials, User] {
  @Override
  def authenticate (BasicCredentials credentials): Optional[User] throws AuthenticationException {
    if ("secret".equals(credentials.getPassword())) {
      return Optional.of(new User(credentials.getUsername()));
    }
    Optional.absent();
  }
}
