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
import java.io.BufferedReader;
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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/delete-comment")
public class DeleteServlet extends HttpServlet {
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  
  @Override
  public void init(){
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Key k = KeyFactory.createKey("Comment",Long.parseLong(request.getParameter("id")));
    try{
      Entity e = datastore.get(k);
      datastore.delete(k);
    }catch(Exception e){
      System.out.println("Wrong key");
    }
  }
}
