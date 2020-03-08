  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      document.getElementById("demo").innerHTML =
      this.responseText;
      document.getElementById("first-header").innerHTML = "404 - File Not Found";
      document.getElementById("second-sub-header").innerHTML = "The Page/Resource you are looking for could not be found or is unavailable. Ensure you are using the correct URL/Domain name or contact the Site Owner";
      var get=document.getElementsByTagName("A");
      for(i=0; i<get.length; i++){
        get[i].removeAttribute("onmouseover");
        get[i].removeAttribute("onmouseout");
      }
    }
  };
  xhttp.open("GET", "/errors/ResellerHosting.php", true);
  xhttp.send();