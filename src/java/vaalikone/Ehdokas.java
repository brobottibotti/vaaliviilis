/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vaalikone;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import persist.Ehdokkaat;
import persist.Kysymykset;
import persist.Vastaukset;

/**
 * Toiminnaltaan samanlainen  kuin Vaalikone mutta normaalin toiminnan lisäksi 
 * Ehdokas myös suorittaa Ehdokkaiden vastausten tallentamisen. 
 * Kutsuu myös tallennus.jsp tiedostoa
 * @author tomi1404
 */
public class Ehdokas extends HttpServlet {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        EntityManagerFactory emf = (EntityManagerFactory) getServletContext().getAttribute("emf");
        EntityManager em = emf.createEntityManager();
        Query qK = em.createNamedQuery("Kysymykset.findSorted");
        List kysymysLista = qK.getResultList();
        int kysymystenMaara = kysymysLista.size();
        session.setAttribute("kyssariKoko", kysymystenMaara);
        int kysymys_id;
        logger.log(Level.INFO, "KyssarienMaaramuuttuja {0}", new Object[]{kysymystenMaara});
        logger.log(Level.INFO, "KyssarienMaaramuuttuja {0}", new Object[]{(session.getAttribute("kyssariKoko"))});

        RequestDispatcher loppusivu = request.getRequestDispatcher("ehdokas-loppusivu.jsp");

        // hae http-sessio ja luo uusi jos vanhaa ei ole vielä olemassa


        //hae käyttäjä-olio http-sessiosta
        Kayttaja usr = (Kayttaja) session.getAttribute("usrobj");

        //jos käyttäjä-oliota ei löydy sessiosta, luodaan sinne sellainen
        if (usr == null) {
            usr = new Kayttaja(kysymystenMaara);
            logger.log(Level.FINE, "Luotu uusi käyttäjä-olio");
            session.setAttribute("usrobj", usr);
        }

        // Hae tietokanta-yhteys contextista


        //hae url-parametri func joka määrittää toiminnon mitä halutaan tehdä.
        //func=haeEhdokas: hae tietyn ehdokkaan tiedot ja vertaile niitä käyttäjän vastauksiin
        //Jos ei määritelty, esitetään kysymyksiä.

        String strFunc = session.getAttribute("func").toString();
        Ehdokkaat ehdokas = (Ehdokkaat) session.getAttribute("ehdokas");
        if (strFunc == "Ehdokas") {

            //hae parametrinä tuotu edellisen kysymyksen nro
            String strKysymys_id = request.getParameter("q");

            //hae parametrina tuotu edellisen kysymyksen vastaus
            String strVastaus = request.getParameter("vastaus");

            // Jos kysymyksen numero (kysId) on asetettu, haetaan tuo kysymys
            // muuten haetaan kysnro 1

            if (strKysymys_id == null) {
                kysymys_id = 1;
            } else {
                kysymys_id = parseInt(strKysymys_id);
                //jos vastaus on asetettu, tallenna se session käyttäjä-olioon
                if (strVastaus != null) {
                    usr.addVastaus(kysymys_id, parseInt(strVastaus));
                }
                //Ehdokkaat ehdokas = (Ehdokkaat) session.getAttribute("ehdokas");
                Vastaukset vastaukset = new Vastaukset(ehdokas.getEhdokasId(), kysymys_id);
                Query t = em.createQuery("SELECT v FROM Vastaukset v WHERE v.vastauksetPK.ehdokasId=?1 AND v.vastauksetPK.kysymysId=?2");
                t.setParameter(1, ehdokas.getEhdokasId());
                t.setParameter(2, kysymys_id);
                List<Ehdokkaat> testi = t.getResultList();
                logger.log(Level.INFO, "Onko aiempaa Id:tä: {0}", new Object[]{testi});

                try {
                    if (testi.size() < 1) {
                        em.getTransaction().begin();
                        vastaukset.setVastaus(parseInt(strVastaus));
                        vastaukset.setKommentti(request.getParameter("kommentti"));
                        em.persist(vastaukset);
                        em.getTransaction().commit();
                        String paskaString = Integer.toString(kysymys_id);
                        logger.log(Level.INFO, paskaString);
                    } else {
                        em.getTransaction().begin();
                        vastaukset.setKommentti(request.getParameter("kommentti"));
                        vastaukset.setVastaus(parseInt(strVastaus));
                        em.merge(vastaukset);
                        em.getTransaction().commit();
                        String testiString = Integer.toString(kysymys_id);
                        logger.log(Level.INFO, testiString);
                    }

                } catch (Exception e) {
                    logger.log(Level.INFO, "Vastaus arvon testaus: {0}", new Object[]{strVastaus});

                }
                String paskaString = Integer.toString(kysymys_id);
                logger.log(Level.INFO, paskaString);
                //määritä seuraavaksi haettava kysymys
                kysymys_id++;
            }

            //jos kysymyksiä on vielä jäljellä, hae seuraava
            if (kysymys_id <= kysymystenMaara) {
                try {


                    //Hae haluttu kysymys tietokannasta
                    Query q = em.createQuery(
                            "SELECT k FROM Kysymykset k WHERE k.kysymysId=?1");
                    q.setParameter(1, kysymys_id);

                    //Lue haluttu kysymys listaan
                    List<Kysymykset> kysymysList = q.getResultList();
                    request.setAttribute("kysymykset", kysymysList);
                    request.getRequestDispatcher("/tallennus.jsp")
                            .forward(request, response);

                } finally {
                    // Sulje tietokantayhteys
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }

                }

                //jos kysymykset loppuvat, lasketaan tulos!
            } else {

                //Tyhjennetään piste-array jotta pisteet eivät tuplaannu mahdollisen refreshin tapahtuessa
                for (int i = 0; i <= kysymystenMaara; i++) {
                    usr.pisteet.set(i, new Tuple<>(0, 0));
                }

                //Hae lista ehdokkaista
                Query qE = em.createQuery(
                        "SELECT e.ehdokasId FROM Ehdokkaat e");
                List<Integer> ehdokasList = qE.getResultList();

                //iteroi ehdokaslista läpi
                for (int i = 1; i < ehdokasList.size(); i++) {

                    //Hae lista ehdokkaiden vastauksista
                    Query qV = em.createQuery(
                            "SELECT v FROM Vastaukset v WHERE v.vastauksetPK.ehdokasId=?1");
                    qV.setParameter(1, i);
                    List<Vastaukset> vastausList = qV.getResultList();

                    //iteroi vastauslista läpi
                    for (Vastaukset eVastaus : vastausList) {
                        int pisteet;

                        //hae käyttäjän ehdokaskohtaiset pisteet
                        pisteet = usr.getPisteet(i);

                        //laske oman ja ehdokkaan vastauksen perusteella pisteet 
                        pisteet += laskePisteet(usr.getVastaus(i), eVastaus.getVastaus());

                        logger.log(Level.INFO, "eID: {0} / k: {1} / kV: {2} / eV: {3} / p: {4}", new Object[]{i, eVastaus.getVastauksetPK().getKysymysId(), usr.getVastaus(i), eVastaus.getVastaus(), pisteet});
                        usr.addPisteet(i, pisteet);
                    }

                }

                strFunc = "EhdokasLoppu";
            }



        }
        if (strFunc.equals("EhdokasLoppu")) {
            Query qk = em.createQuery("SELECT k FROM Kysymykset k");
            Query qv = em.createQuery("SELECT v FROM Vastaukset v WHERE v.vastauksetPK.ehdokasId=?1");
            qv.setParameter(1, ehdokas.getEhdokasId());
            Query qe = em.createQuery("SELECT e FROM Ehdokkaat e WHERE e.ehdokasId=?1");
            qe.setParameter(1, ehdokas.getEhdokasId());
            List<Vastaukset> vastausList = qv.getResultList();
            List<Kysymykset> kysymysList = qk.getResultList();
            List<Ehdokkaat> ehdokasLista = qe.getResultList();
            em.close();
            request.setAttribute("vastaukset", vastausList);
            request.setAttribute("kysymykset", kysymysList);
            request.setAttribute("ehdokas", ehdokasLista);
            session.invalidate();
            loppusivu.forward(request, response);
        }
    }

    private Integer laskePisteet(Integer kVastaus, Integer eVastaus) {
        int pisteet = 0;
        if (kVastaus - eVastaus == 0) {
            pisteet = 3;
        }
        if (kVastaus - eVastaus == 1 || kVastaus - eVastaus == -1) {
            pisteet = 2;
        }
        if (kVastaus - eVastaus == 2 || kVastaus - eVastaus == -2 || kVastaus - eVastaus == 3 || kVastaus - eVastaus == -3) {
            pisteet = 1;
        }

        //if (kVastaus - eVastaus == 4 || kVastaus - eVastaus == -4) pisteet = 0;
        return pisteet;

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
