package vaalikone;

import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static javax.persistence.Persistence.createEntityManagerFactory;
import javax.persistence.Query;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Web application lifecycle listener.
 *
 * @author Jonne
 */
public class ServletListener implements ServletContextListener {

    /**
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        EntityManagerFactory emf =
                createEntityManagerFactory("Vaalikone5PU");
        sce.getServletContext().setAttribute("emf", emf);
        Toiminta toiminta = new Toiminta();
        try{
        sce.getServletContext().setAttribute("toiminta", toiminta);
        EntityManager em = emf.createEntityManager();
        Query qK = em.createNamedQuery("Kysymykset.findAll");
        Query qT = em.createNamedQuery("Ehdokkaat.findAll");
        List ehdokasLista = qT.getResultList();
        List kysymysLista = qK.getResultList();
        int kysymystenMaara = kysymysLista.size() + 1;
        sce.getServletContext().setAttribute("maara", kysymystenMaara);
        }catch(Exception e){}


        Loki.init();

    }

    /**
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        EntityManagerFactory emf =
                (EntityManagerFactory) sce.getServletContext().getAttribute("emf");
        emf.close();
    }

    private Object getServletContext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}