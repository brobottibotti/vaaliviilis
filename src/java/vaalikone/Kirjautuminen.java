/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vaalikone;

import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tomi1404
 */
public class Kirjautuminen extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //tunnukset tietokannasta
        //MD5
        
        //luo tietokantayhteys
        String kayttajatunnus = request.getParameter("kayttajatunnus");
        String salasana = request.getParameter("salasana");
        
        //Tietokantayhteyden luominen
        EntityManagerFactory emf
                = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
        Query kt = em.createQuery("SELECT KAYTTAJATUNNUS FROM APP.EHDOKKAAT");
        
        String tunnusKentta = request.getParameter("tunnus");
        String salasanaKentta = request.getParameter("salasana");
        
        if (tunnusKentta.equals(kayttajatunnus) && salasanaKentta.equals(salasana)){
            response.sendRedirect("Ehdokas");
        } else { 
            response.sendRedirect("/Login failed");
        }
        
//        String hyvaTunnus = "MattiM";
//        String vahvaSalasana = "Qwerty1";
        
//        if (hyvaTunnus.equals(testiTunnus) && vahvaSalasana.equals(testiSalasana)) {
//            response.sendRedirect("Ehdokas");  
//        } else {
//            response.sendRedirect("/Login failed");
//        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
