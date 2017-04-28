
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
        <img id="headerimg" src="images/Logo.png" width="500" height="144" alt=""/>
            <div class ="kirjautuminen">
                <h1>Ehdokas login</h1>
                <form action="Kirjautuminen" method="POST">
                    Tunnus:<br>
                    <input type="text" name="tunnus">
                    <br>
                    Salasana:<br>
                    <input type="password" name="salasana">
                    <br>
                    <input type="submit" value="Submit">
                    <br>
                    <%
                    String errTunnus=(String)request.getAttribute("errTunnus");
                    if(errTunnus==null){errTunnus="";}
                    %>
                    <%=errTunnus%>
                </form>
            </div>
            <div class="kysymys">
                <h1>Vaalikoneen JSP-sivut</h1>
            </div>
            <br>
            <form action="Vaalikone">
                <input id="submitnappi" type="submit" value="Aloita" name="btnAloita" />                   
            </form>
        </div>
    </body>
</html>