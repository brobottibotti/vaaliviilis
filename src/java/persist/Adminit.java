/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author elias1403
 */
@Entity
@Table(name = "ADMINIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Adminit.findAll", query = "SELECT a FROM Adminit a"),
    @NamedQuery(name = "Adminit.findById", query = "SELECT a FROM Adminit a WHERE a.id = :id"),
    @NamedQuery(name = "Adminit.findByEtunimi", query = "SELECT a FROM Adminit a WHERE a.etunimi = :etunimi"),
    @NamedQuery(name = "Adminit.findBySukunimi", query = "SELECT a FROM Adminit a WHERE a.sukunimi = :sukunimi"),
    @NamedQuery(name = "Adminit.findByTunnus", query = "SELECT a FROM Adminit a WHERE a.tunnus = :tunnus"),
    @NamedQuery(name = "Adminit.findBySalasana", query = "SELECT a FROM Adminit a WHERE a.salasana = :salasana")})
public class Adminit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 25)
    @Column(name = "ETUNIMI")
    private String etunimi;
    @Size(max = 25)
    @Column(name = "SUKUNIMI")
    private String sukunimi;
    @Size(max = 32)
    @Column(name = "TUNNUS")
    private String tunnus;
    @Size(max = 32)
    @Column(name = "SALASANA")
    private String salasana;

    public Adminit() {
    }

    public Adminit(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getTunnus() {
        return tunnus;
    }

    public void setTunnus(String tunnus) {
        this.tunnus = tunnus;
    }

    public String getSalasana() {
        return salasana;
    }

    public void setSalasana(String salasana) {
        this.salasana = salasana;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Adminit)) {
            return false;
        }
        Adminit other = (Adminit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persist.Adminit[ id=" + id + " ]";
    }
    
}
