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
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/comments")
public class CommentServlet extends HttpServlet {
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  // private ArrayList<String> comments = new ArrayList<String>();
  
  @Override
  public void init(){
    //comments.add("This is the first message, and is hardcoded.");
  }


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long timestamp = System.currentTimeMillis();
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("message",request.getParameter("comment"));
    commentEntity.setProperty("timestamp",timestamp);
    datastore.put(commentEntity);
    response.sendRedirect("/index.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    Query query = new Query("Comment").addSort("timestamp",Query.SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    ArrayList<Comment> comments = new ArrayList<Comment>();
    int i = 0;
    for(Entity entity : results.asIterable()){
      if (i == Integer.parseInt(request.getParameter("numberOfComments"))){
        break;
      }
      String message = (String) entity.getProperty("message");
      long timestamp = (long) entity.getProperty("timestamp");
      long id = entity.getKey().getId(); 
      comments.add(new Comment(id,message, timestamp));
      i++;
    }
    String json = gson.toJson(comments);
    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}