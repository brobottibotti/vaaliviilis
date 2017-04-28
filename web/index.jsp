
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Diginide vaalikone</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="style.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="container">
            <img id="headerimg" src="Logo.png" width="720" />
            <div class ="kirjautuminen">
                <h1>Ehdokkaalle</h1>
                <form action="Kirjautuminen" method="POST">
                    Tunnus:<br>
                    <input type="text" name="tunnus">
                    <br>
                    Salasana:<br>
                    <input type="password" name="salasana">
                    <br>
                    <input type="submit" value="Kirjaudu">
                    <br>
                    <%
                    String errTunnus=(String)request.getAttribute("errTunnus");
                    if(errTunnus==null){errTunnus="";}
                    %>
                    <%=errTunnus%>
                </form>
            </div>
            <div class="kysymys">
                <h1>Äänestäjälle</h1>
            </div>
            <br>
            <form action="Vaalikone">
                <input id="submitnappi" type="submit" value="Kysymyksiin" name="btnAloita" />                   
            </form>
        </div>
    </body>
</html>