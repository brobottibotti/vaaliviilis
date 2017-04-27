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
        <h1>Alla näet vastauksesi</h1>
        <%
            List <Kysymykset> kysymykset = (List<Kysymykset>) request.getAttribute("kysymykset");
            List <Vastaukset> vastaukset = (List<Vastaukset>) request.getAttribute("vastaukset");
        %>
        <br>
            <% 
            for (int i = 0; i < vastaukset.size(); i++) {
                %>
                <b>Kysymys <%= i + 1%>: <%= kysymykset.get(i).getKysymys()%></b>
                <br>
                Vastauksesi: <%=vastaukset.get(i).getVastaus()%>
                <br><br>
            <% }
            %>
        </div>
    </body>
</html>
