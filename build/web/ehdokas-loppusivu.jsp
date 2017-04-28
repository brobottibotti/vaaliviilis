<%-- 
    Document   : ehdokas-loppusivu
    Created on : Apr 27, 2017, 10:02:03 AM
    Author     : jani1406
--%>
<%@page import="persist.Kysymykset"%>
<%@page import="persist.Vastaukset"%>
<%@page import="java.util.List"%>
<%@page import="persist.Ehdokkaat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Vastaukset</title>
        <link href="style.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="container">
        <img id="headerimg" src="Logo.png" width="720" />
        <br><br>
        <h3>Omat tiedot</h3>
        <%
            List <Ehdokkaat> ehdokas = (List<Ehdokkaat>) request.getAttribute("ehdokas");
            List <Kysymykset> kysymykset = (List<Kysymykset>) request.getAttribute("kysymykset");
            List <Vastaukset> vastaukset = (List<Vastaukset>) request.getAttribute("vastaukset");
        %>
            <% for (Ehdokkaat tiedot : ehdokas) { %>
                <b>Etunimi:</b> <%=tiedot.getEtunimi()%><br>
                <b>Sukunimi:</b> <%=tiedot.getSukunimi()%><br>
                <b>Ikä:</b> <%=tiedot.getIkä()%><br>
                <b>Ammatti:</b> <%=tiedot.getAmmatti()%><br>
                <b>Puolue:</b> <%=tiedot.getPuolue()%><br>
                <b>Kotipaikkakunta:</b> <%=tiedot.getKotipaikkakunta()%><br>
                <b>Miksi haluan eduskuntaan:</b> <%=tiedot.getMiksiEduskuntaan()%><br>
                <b>Mitä asioita haluaisin edistää:</b> <%=tiedot.getMitaAsioitaHaluatEdistaa()%><br><br>
            <% } %>
            
            <h2>Alla näet vastauksesi</h2>
            <% for (int i = 0; i < vastaukset.size(); i++) { %>
                <b>Kysymys <%= i + 1%>: <%=kysymykset.get(i).getKysymys()%></b><br>
                Vastauksesi: <%=vastaukset.get(i).getVastaus()%><br>
                Kommenttisi: <%=vastaukset.get(i).getKommentti()%><br><br>
            <% } %>
            <div class="kysymys"><small>1=Täysin eri mieltä 2=Osittain eri mieltä 3=En osaa sanoa, 4=Osittain samaa mieltä 5=Täysin samaa mieltä</small>
        </div>
    </body>
</html>