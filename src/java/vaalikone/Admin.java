/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vaalikone;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import persist.Ehdokkaat;
import persist.Kysymykset;

/**
 *
 * @author tomi1404
 */
public class Admin extends HttpServlet {

    private final static Logger logger = Logger.getLogger(Loki.class.getName());
    int poistaK = 0;
    int poistaE = 0;
    int LisaaEId = 0;
    String LisaaES;
    String LisaaEE;
    String LisaaEP;
    String LisaaEK;
    int LisaaEI;
    String LisaaMAHE;
    String LisaaEA;
    String kysymys = "0";

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
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        if (session.getAttribute("func").equals("adminit")) {
            PrintWriter out = response.getWriter();

            logger.log(Level.FINE, "Luotu uusi käyttäjä-olio");

            EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
            EntityManager em = emf.createEntityManager();


            request.getRequestDispatcher("/admin.jsp")
                    .forward(request, response);

            Query qK = em.createNamedQuery("Kysymykset.findSorted");
            Query qT = em.createNamedQuery("Ehdokkaat.findSorted");
            List ehdokasLista = qT.getResultList();
            List kysymysLista = qK.getResultList();
            session.setAttribute("eLista", ehdokasLista);
            session.setAttribute("kLista", kysymysLista);
            logger.log(Level.INFO, "eID: {0}", new Object[]{kysymysLista.size()});

        }

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
