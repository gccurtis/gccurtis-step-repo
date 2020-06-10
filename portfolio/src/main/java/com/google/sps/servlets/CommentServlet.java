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

import com.google.sps.data.Comment;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@WebServlet("/comments")
public class CommentServlet extends HttpServlet {
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("message", request.getParameter("comment"));
    commentEntity.setProperty("timestamp", System.currentTimeMillis());
    datastore.put(commentEntity);
    response.sendRedirect("/index.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    ArrayList<Comment> comments = new ArrayList<Comment>();
    //UserService userService = UserServiceFactory.getUserService();
    //if (!userService.isUserLoggedIn()) {
    //  response.setContentType("application/json;");
    //  response.getWriter().println(gson.toJson("Not Logged In"));
    //  return;
    //}
    Query query = new Query("Comment").addSort("timestamp", Query.SortDirection.DESCENDING);
    int limit = Integer.parseInt(request.getParameter("numberOfComments"));
    List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(limit));
    for(Entity entity: results) {
      String message = (String) entity.getProperty("message");
      long timestamp = (long) entity.getProperty("timestamp");
      long id = entity.getKey().getId(); 
      comments.add(new Comment(id,message, timestamp));
    }

    String json = gson.toJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
