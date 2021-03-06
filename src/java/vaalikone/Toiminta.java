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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import persist.Ehdokkaat;
import persist.Kysymykset;

/**
 * Suorittaa Admin.jsp:n lähettämät toiminnot ja uudelleen ohjaa selaimen takaisin Admin.java servlettiin.
 * @author tomi1404
 */
public class Toiminta extends HttpServlet {

    private final static Logger logger = Logger.getLogger(Loki.class.getName());
    private int poistaK = 0;
    private int poistaE = 0;
    private int LisaaEId = 0;
    private String LisaaES;
    private String LisaaEE;
    private String LisaaEP;
    private String LisaaEK;
    private int LisaaEI;
    private String LisaaMAHE;
    private String LisaaEA;
    private String kysymys = "0";
    private String ehdokastila;
    private String LisaaEME;
    public int kysymystenMaara;
    public int ehdokkaidenMaara;
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
    Kirjautuminen_admin akirjaus;


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        response.setContentType("text/html;charset=UTF-8");

    }
/**
 * Poistaa kysymykset
 * @param p 
 * Poistettavan kysymyksen ID
 */
    public void kysymysPoistaminen(int p) {
        EntityManager em = manageri();
        em.getTransaction().begin();
        Kysymykset poistettavaK = em.find(Kysymykset.class, p);
        em.remove(poistettavaK);
        em.getTransaction().commit();
    }
/**
 * Poistaa ehdokkaan
 * @param e 
 * Numero jolla etsitään poistettavan ehdokkaan entity ja poistetaan se
 */
    public void ehdokasPoistaminen(int e) {
        EntityManager em = manageri();
        em.getTransaction().begin();
        Ehdokkaat poistettavaE = em.find(Ehdokkaat.class, e);
        em.remove(poistettavaE);
        em.getTransaction().commit();
        logger.log(Level.INFO, "eID: {0}", new Object[]{e});

    }
/**
 * Lisätään kysymyksiä
 * @param k
 * Kysymys string muodossa
 * @param o 
 * Kysymyksen ID
 */
    public void lisaaKysymys(String k, int o) {
        EntityManager em = manageri();
        em.getTransaction().begin();
        Kysymykset kysymykset = new Kysymykset();
        kysymykset.setKysymysId(o);
        kysymykset.setKysymys(k);
        em.merge(kysymykset);
        em.getTransaction().commit();

    }
/**
 * Entitymanager -metodi
 * @return 
 */
    public EntityManager manageri() {
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        return em;
    }
/**
 * Kirjautumisessa käytettävä kryptauskoodi
 * @param str
 * Kryptattava arvo
 * @return 
 * Palauttaa MD5-kryptatun stringin
 */
    public String kryptaa(String str) {
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
/**
 * Lisää uuden ehdokkaan tai muokkaa olemassaolevaa ehdokasta
 * @param id
 * Ehdokkaan ID
 * @param s
 * Sukunimi
 * @param e
 * Etunimi
 * @param p
 * Puolue
 * @param k
 * Kotipaikkakunta
 * @param ik
 * Ikä
 * @param mahe
 * Mitä asisoita haluat edistää
 * @param a
 * Ammatti
 * @param me
 * Miksi eduskuntaan
 */
    public void lisaaEhdokas(int id, String s, String e, String p, String k, int ik, String mahe, String a, String me) {
        EntityManager em = manageri();
        em.getTransaction().begin();
        Query t = em.createQuery("SELECT e FROM Ehdokkaat e WHERE e.ehdokasId=?1");
        t.setParameter(1, id);
        List<Ehdokkaat> testi = t.getResultList();

        if (testi.size() < 1) {
            Ehdokkaat uusiEhdokas = new Ehdokkaat(LisaaEId);
            uusiEhdokas.setSukunimi(s);
            uusiEhdokas.setEtunimi(e);
            uusiEhdokas.setPuolue(p);
            uusiEhdokas.setKotipaikkakunta(k);
            uusiEhdokas.setIkä(ik);
            uusiEhdokas.setMitaAsioitaHaluatEdistaa(mahe);
            uusiEhdokas.setAmmatti(a);
            uusiEhdokas.setMiksiEduskuntaan(me);
            uusiEhdokas.setKayttajatunnus("");
            uusiEhdokas.setSalasana("");
            em.persist(uusiEhdokas);
            ehdokastila = "eLsuccess";
        } else {
            Ehdokkaat uusiEhdokas = em.find(Ehdokkaat.class, id);
            uusiEhdokas.setSukunimi(s);
            uusiEhdokas.setEtunimi(e);
            uusiEhdokas.setPuolue(p);
            uusiEhdokas.setKotipaikkakunta(k);
            uusiEhdokas.setIkä(ik);
            uusiEhdokas.setMitaAsioitaHaluatEdistaa(mahe);
            uusiEhdokas.setAmmatti(a);
            uusiEhdokas.setMiksiEduskuntaan(me);
            em.merge(uusiEhdokas);
            ehdokastila = "eMsuccess";

        }
        em.getTransaction().commit();

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
     * Admin JSP:n lähettämän tiedon käsittelylogiikka
     * Handles the HTTP
     * <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        EntityManager em = manageri();


        Query qL = em.createQuery("SELECT k.kysymysId FROM Kysymykset k");
        List kysymykset = qL.getResultList();

        if (request.getParameter("kPNappi") != null) {
            poistaK = Integer.parseInt(request.getParameter("poistaK"));
            kysymysPoistaminen(poistaK);
            response.sendRedirect("Admin?ADD=kPsuccess");
        }
        if (request.getParameter("kLNappi") != null) {
            int koko;
            for (int i = 1; true; i++) {
                logger.log(Level.INFO, "i arvo: {0}", new Object[]{i});
                if (!kysymykset.contains(i)) {
                    koko = i;
                    logger.log(Level.INFO, "koko arvo: {0}", new Object[]{koko});
                    break;
                }
            }
            kysymys = request.getParameter("kysymys");
            lisaaKysymys(kysymys, koko);
            request.setAttribute("kLNappi", "testi");
            logger.log(Level.INFO, "eID: {0}", new Object[]{request.getParameter("kLNappi")});
            response.sendRedirect("Admin?ADD=kLsuccess");
        }
        if (request.getParameter("ePNappi") != null) {

            poistaE = Integer.parseInt(request.getParameter("poistaE"));
            ehdokasPoistaminen(poistaE);
            response.sendRedirect("Admin?ADD=ePsuccess");
        }

        if (request.getParameter("eLNappi") != null) {

            LisaaEId = Integer.parseInt(request.getParameter("LisaaEId"));
            LisaaES = request.getParameter("LisaaES");
            LisaaEE = request.getParameter("LisaaEE");
            LisaaEP = request.getParameter("LisaaEP");
            LisaaEK = request.getParameter("LisaaEK");
            LisaaEI = Integer.parseInt(request.getParameter("LisaaEI"));
            LisaaEME = request.getParameter("LisaaEME");
            LisaaMAHE = request.getParameter("LisaaMAHE");
            LisaaEA = request.getParameter("LisaaEA");
            logger.log(Level.INFO, "ES: {0} / EE: {1} / EP: {2} / EK: {3} / EI: {4} / MAHE: {5} / EA: {6}", new Object[]{LisaaEId, LisaaES, LisaaEE, LisaaEP, LisaaEK, LisaaEI, LisaaMAHE, LisaaEA});
            lisaaEhdokas(LisaaEId, LisaaES, LisaaEE, LisaaEP, LisaaEK, LisaaEI, LisaaMAHE, LisaaEA, LisaaEME);
            response.sendRedirect("Admin?ADD=" + ehdokastila);

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
