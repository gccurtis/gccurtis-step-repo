// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;

@WebServlet(name = "HomeServlet",urlPatterns = {"/"})
public class HomeServlet extends HttpServlet {
DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
    UserService userService = UserServiceFactory.getUserService();
    String redirectUrl = userService.createLoginURL("/");
    String userEmail = "None";
    String redirectUrl = userService.createLogintURL("/");
    int token = 0;
    if (userService.isUserLoggedIn()) {
      token = new SecureRandom().nextInt();
      userEmail = userService.getCurrentUser().getEmail();
      redirectUrl = userService.createLogoutURL("/");
      Entity userEntity = new Entity("User");
      userEntity.setProperty("email", userEmail);
      userEntity.setProperty("token", token);
      userEntity.setProperty("timestamp",System.currentTimeMillis());
      datastore.put(userEntity);
    }
    response.setContentType("text/html");
    response.sendRedirect(String.format("/frontPage.html?email=%&url=%s&token=%s",userEmail,java.net.URLEncoder.encode(redirectUrl,"UTF-8"),token);
  }
}
