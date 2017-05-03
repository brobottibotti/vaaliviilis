/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vaalikone;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import persist.Ehdokkaat;

/**
 *
 * @author tomi1404
 */
public class Kirjautuminen extends HttpServlet {

    private final static Logger logger = Logger.getLogger(Loki.class.getName());

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
    
    /**
     * Tässä servletissä haetaan ehdokkaan kirjautumisessa käytetty käyttäjätunnus ja salasana. 
     * Ehdokkaan tunnukset muutetaan koodissa MD5-muotoon ja niitä verrataan kannassa oleviin tunnuksiin, 
     * jotka ovat myös MD5-muodossa. Tämän perusteella käyttäjän kirjautuminen, joko hylätään tai hyväksytään.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        

        RequestDispatcher errTunnus = request.getRequestDispatcher("index.jsp");

        //Haetaan formista käyttäjän käyttäjätunnus ja salasana, muutetaan  
        //tunnukset MD5-muotoon syöttämällä algoritmille tunnukset.
        String tunnusKentta = request.getParameter("tunnus");
        String salasanaKentta = request.getParameter("salasana");
        String tunnusKenttaMD5 = "";
        String salasanaKenttaMD5 = "";
        Toiminta toiminta = new Toiminta();

        if (tunnusKentta == null || tunnusKentta.length() == 0) {
            request.setAttribute("errTunnus", "Syötä tunnukset");
            errTunnus.forward(request, response);
        } else {
            tunnusKenttaMD5 = toiminta.kryptaa(tunnusKentta);
        }

        if (salasanaKentta == null || salasanaKentta.length() == 0) {
            request.setAttribute("errTunnus", "Syötä tunnukset");
            errTunnus.forward(request, response);
        } else {
            salasanaKenttaMD5 = toiminta.kryptaa(salasanaKentta);
        }

        //Luodaan tietokantayhteys
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();

        //Haetaan käyttäjätunnukset tietokannasta ja viedään ne listaan
        Query kt = em.createQuery("SELECT e.kayttajatunnus FROM Ehdokkaat e");
        List<Ehdokkaat> tunnukset = kt.getResultList();

        //Etsitään löytyykö listasta käyttäjän käyttäjätunnusta. 
        //Jos löytyy niin etsitään kyseisen käyttäjänimen salasanaa erillisellä 
        //tietokantahaulla ja verrataan sitä käyttäjän syöttämään salasanaa.
        if (tunnukset.contains(tunnusKenttaMD5)) {
            logger.log(Level.INFO, "Käyttäjätunnus oikein!");
            Query sn = em.createQuery("SELECT e.salasana FROM Ehdokkaat e WHERE e.kayttajatunnus=?1");
            sn.setParameter(1, tunnusKenttaMD5);
            String salasana = sn.getSingleResult().toString();

            if (salasana.contains(salasanaKenttaMD5)) {
                logger.log(Level.INFO, "Käyttäjätunnus oikein, salasana oikein!");
                Query id = em.createQuery("SELECT e.ehdokasId FROM Ehdokkaat e WHERE e.kayttajatunnus=?1 AND e.salasana=?2");
                id.setParameter(1, tunnusKenttaMD5);
                id.setParameter(2, salasanaKenttaMD5);

                //Ehdokkaat sivulle käyttäjä viedään ID:n avulla
                Ehdokkaat ehdokkaat = em.find(Ehdokkaat.class, Integer.parseInt(id.getSingleResult().toString()));
                //Testitulostus                 
                logger.log(Level.INFO, "eID: {0}", new Object[]{ehdokkaat.getEhdokasId()});
                logger.log(Level.INFO, ehdokkaat.getEhdokasId().toString());
                HttpSession session = request.getSession(true);
                session.setAttribute("ehdokas", ehdokkaat);
                session.setAttribute("func", "Ehdokas");
                response.sendRedirect("Ehdokas");

            } else {
                logger.log(Level.INFO, "Salasana väärin!");
                request.setAttribute("errTunnus", "Antamasi käyttäjätunnus tai salasana oli väärin");
                errTunnus.forward(request, response);
            }
        } else {
            logger.log(Level.INFO, "Käyttäjätunnus väärin!");
            request.setAttribute("errTunnus", "Antamasi käyttäjätunnus tai salasana oli väärin");
            errTunnus.forward(request, response);
        }
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
