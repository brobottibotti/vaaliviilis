<%-- 
    Document   : adminkirjautuminen
    Created on : Apr 26, 2017, 11:21:34 AM
    Author     : elias1403
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Diginide vaalikone - Admin</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="style.css" rel="stylesheet" type="text/css">

    </head>
    <body>
        <div id="container">

            <img id="headerimg" src="Logo.png" width="720" />
            <div class ="kirjautuminen">
                <h1>Administrator</h1>
                <form action="Kirjautuminen_admin" method="POST">
                    Tunnus:<br>
                    <input type="text" name="tunnus_admin">
                    <br>
                    Passu:<br>
                    <input type="password" name="passu_admin">
                    <br>
                    <input type="submit" value="Submit">
                    <br>
                     <%
                    String vaaraTunnus=(String)request.getAttribute("vaaraTunnus");
                    if(vaaraTunnus==null){vaaraTunnus="";}
                    %>
                    <%=vaaraTunnus%>
                </form>
            </div>
        </div>
    </body>
</html>