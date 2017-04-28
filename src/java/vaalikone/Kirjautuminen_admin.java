/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vaalikone;

import java.io.IOException;
import java.io.PrintWriter;
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
import persist.Adminit;

/**
 *
 * @author elias1403
 */
public class Kirjautuminen_admin extends HttpServlet {
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
    
    
 public String crypt(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        MessageDigest digester;
        try {
            digester = MessageDigest.getInstance("MD5");

            digester.update(str.getBytes());
            byte[] hash = digester.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/adminkirjautuminen.jsp")
                            .forward(request, response);
        
        HttpSession session = request.getSession(true);
        RequestDispatcher vaaraTunnus = request.getRequestDispatcher("adminkirjautuminen.jsp");
        
        //Haetaan formista käyttäjän käyttäjätunnus ja salasana, muutetaan  
        //tunnukset MD5-muotoon syöttämällä algoritmille tunnukset.
        String tunnusKentta = crypt(request.getParameter("tunnus_admin"));
        String salasanaKentta = crypt(request.getParameter("passu_admin"));
        
        //Luodaan tietokantayhteys
        EntityManagerFactory emf
                = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        
        //Haetaan käyttäjätunnukset tietokannasta ja viedään ne listaan
        Query kt = em.createQuery("SELECT a.tunnus FROM Adminit a");
        List<Adminit> tunnukset = kt.getResultList(); 
        
        
        //Etsitään löytyykö listasta käyttäjän käyttäjätunnusta. 
        //Jos löytyy niin etsitään kyseisen käyttäjänimen salasanaa erillisellä 
        //tietokantahaulla ja verrataan sitä käyttäjän syöttämään salasanaa.
        if(tunnukset.contains(tunnusKentta)){
            logger.log(Level.INFO,"Käyttäjätunnus oikein!");            
            Query sn = em.createQuery("SELECT e.salasana FROM Adminit e WHERE e.tunnus=?1");
            sn.setParameter(1, tunnusKentta);
            String salasana = sn.getSingleResult().toString();
                if(salasana.contains(salasanaKentta)){
                    logger.log(Level.INFO,"Käyttäjätunnus oikein, salasana oikein!");
                    Query id = em.createQuery("SELECT e.id FROM Adminit e WHERE e.tunnus=?1 AND e.salasana=?2");
                    id.setParameter(1, tunnusKentta);
                    id.setParameter(2, salasanaKentta);
                    Adminit adminit = new Adminit (Integer.parseInt(id.getSingleResult().toString()));
                    
                    //Testitulostus                 
                    logger.log(Level.INFO, "eID: {0}", new Object[]{ adminit.getId()});
                    logger.log(Level.INFO,adminit.getId().toString());
                    
                    session.setAttribute("adminit", adminit);
                    session.setAttribute("func", "adminit");
                    response.sendRedirect("Admin");
                }else{
                    logger.log(Level.INFO,"Salasana väärin!");
                    request.setAttribute("vaaraTunnus","Antamasi käyttäjätunnus tai salasana oli väärin");
                    vaaraTunnus.forward(request, response);
                }
        }else{
            logger.log(Level.INFO,"Käyttäjätunnus väärin!");
            String paskaString = Integer.toString(tunnukset.size());
            logger.log(Level.INFO,paskaString);
            request.setAttribute("vaaraTunnus","Antamasi käyttäjätunnus tai salasana oli väärin");
            vaaraTunnus.forward(request, response);
        }
        

        
//        for (int i=0; i < tunnukset.size(); i++){
//        logger.log(Level.INFO, "eID: {0} ", new Object[]{tunnukset});
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