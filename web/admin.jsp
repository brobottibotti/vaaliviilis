<%-- 
    Document   : admin
    Created on : Apr 25, 2017, 9:44:22 AM
    Author     : tomi1404
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*,vaalikone.Vaalikone,persist.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Vaalikone Admin</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="style.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="container">
        <c:forEach var="kysymys" items="${kLista}">
            <c:out value="${kysymys.kysymysId}"/> 
            <c:out value="${kysymys.kysymys}"/> 

            <br>
        </c:forEach>



        <form action="Toiminta" method="POST" onsubmit="window.location.reload()">
            Poistettavan kysymyksen id:<br>
            <input type="number" name="poistaK">
            <br>
            <input type="submit" name="kPNappi" value="Submit">
        </form> 
        <c:choose>
            <c:when test="${param.ADD == 'kPsuccess'}">
                <div>Kysymys Lisätty</div>
            </c:when>
        </c:choose>




        <form action="Toiminta" method="POST" onsubmit="window.location.reload()">
            Lisättävä Kysymys:<br>
            <input type="text" name="kysymys" required>
            <br>
            <input type="submit" name="kLNappi" value="Submit">
        </form> 

        <c:choose>
            <c:when test="${param.ADD == 'kLsuccess'}">
                <div>Kysymys Lisätty</div>
            </c:when>
        </c:choose>

        <c:forEach var="ehdokas" items="${eLista}">
            <c:out value="${ehdokas.ehdokasId}"/> 
            <c:out value="${ehdokas.etunimi}"/> 
            <c:out value="${ehdokas.sukunimi}"/> 
            <c:out value="${ehdokas.puolue}"/> 
            <br>
        </c:forEach>


        <form action="Toiminta" method="POST" onsubmit="window.location.reload()">
            Poistettavan ehdokkaan ID:<br>
            <input type="number" name="poistaE" required>
            <br>
            <input type="submit" name="ePNappi" value="Submit">
        </form> 

        <c:choose>
            <c:when test="${param.ADD == 'ePsuccess'}">
                <div>Ehdokas Poistettu</div>
            </c:when>
        </c:choose>


        <form action="Toiminta" method="POST" onsubmit="window.location.reload()">
            Ehdokkaan ID:<br>
            <input type="text" name="LisaaEId" required>
            <br>
            Ehdokkaan Sukunimi:<br>
            <input type="text" name="LisaaES" required>
            <br>
            Ehdokkaan Etunimi: <br>
            <input type="text" name="LisaaEE" required>
            <br>
            Ehdokkaan Puolue:<br>
            <input type="text" name="LisaaEP" required>
            <br>
            Ehdokkaan Kotipaikkakunta:<br>
            <input type="text" name="LisaaEK" required>
            <br>
            Ehdokkaan Ikä:<br>
            <input type="text" name="LisaaEI" required>
            <br>
            Miksi eduskuntaan?<br>
            <input type="text" name="LisaaEME" required>
            <br>
            Mitä asioita haluat edistää?<br>
            <input type="text" name="LisaaMAHE" required>
            <br>
            Ammatti?<br>
            <input type="text" name="LisaaEA" required>
            <br>
            <input type="submit" name="eLNappi" value="Submit">
        </form> 

        <c:choose>
            <c:when test="${param.ADD == 'eLsuccess'}">
                <div>Ehdokas lisätty</div>
            </c:when>
            <c:when test="${param.ADD == 'eMsuccess'}">
                <div>Ehdokas muutettu</div>
            </c:when>
        </c:choose>
        </div>
    </body>
</html>
