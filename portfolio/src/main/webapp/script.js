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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  fetch('/data').then(response => response.json()).then((data) => {
    const greeting = data[Math.floor(Math.random()*data.length)];
    const greetingContainer = document.getElementById('greeting-container');
    greetingContainer.innerText = greeting;
  });
}

function getData(){
  const name = document.getElementById('text-input').value; 
  fetch('/data2?name='+name).then(response => response.text()).then((data) => {
    document.getElementById('data-display').innerText = data;
  });
}

function removeChildren(node){
  const children = node.childNodes;
  while(node.hasChildNodes()){
    node.removeChild(children[0]);
  }
}

function deleteComment(id){
  var oReq = new XMLHttpRequest();
  oReq.open("POST", "/delete-comment");
  oReq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  oReq.send("id="+id);
  loadComments();
}

function loadComments(){
  const numberOfComments = document.getElementById("numberOfComments").value;
  const commentSection = document.getElementById('comment-section');
  removeChildren(commentSection);
  fetch('/comments?numberOfComments='+numberOfComments).then(response => response.json()).then((comments) => {
    for(i=0;i<comments.length;i++){
      const commentContainer = document.createElement("li");
      const commentId = comments[i].id;
      commentContainer.innerText = comments[i].message;
      commentContainer.setAttribute("id",comments[i].id);
      commentContainer.setAttribute("onClick","deleteComment(this.id)");
      commentSection.appendChild(commentContainer);
    }});
}
