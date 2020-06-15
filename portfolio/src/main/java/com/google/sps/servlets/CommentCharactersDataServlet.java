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
import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@WebServlet("/comment-characters-data")
public class CommentCharactersDataServlet extends HttpServlet {
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    ArrayList<Comment> comments = new ArrayList<Comment>();
    Query query = new Query("Comment").addSort("timestamp",Query.SortDirection.DESCENDING);
    int limit = Integer.parseInt(request.getParameter("numberOfComments"));
    String total = "";
    List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(limit));
    for (Entity entity: results) {
      total += (String) entity.getProperty("message");
    }
    char[] allCharacters = total.toCharArray();
    Map<Character, Integer> characterFrequencies = new HashMap<Character, Integer>();
    for (char trueCharacter: allCharacters){
      char character = Character.toUpperCase(trueCharacter);
      characterFrequencies.put(character, characterFrequencies.getOrDefault(character, 0)+1)
    }
    String json = gson.toJson(characterFrequencies);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
