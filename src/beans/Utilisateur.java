package beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alexis
 */
@Entity
@Table(name = "t_utilisateur")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Utilisateur.findAll", query = "SELECT u FROM Utilisateur u"),
        @NamedQuery(name = "Utilisateur.findByPkUtilisateur", query = "SELECT u FROM Utilisateur u WHERE u.pkUtilisateur = :pkUtilisateur"),
        @NamedQuery(name = "Utilisateur.findByLogin", query = "SELECT u FROM Utilisateur u WHERE u.login = :login"),
        @NamedQuery(name = "Utilisateur.findByPassword", query = "SELECT u FROM Utilisateur u WHERE u.password = :password"),
        @NamedQuery(name = "Utilisateur.findByDateAjout", query = "SELECT u FROM Utilisateur u WHERE u.dateAjout = :dateAjout")})
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_utilisateur")
    private Integer pkUtilisateur;
    @Basic(optional = false)
    @Column(name = "login")
    private String login;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "date_ajout")
    @Temporal(TemporalType.DATE)
    private Date dateAjout;

    public Utilisateur() {
    }

    public Utilisateur(Integer pkUtilisateur) {
        this.pkUtilisateur = pkUtilisateur;
    }

    public Utilisateur(Integer pkUtilisateur, String login, String password, Date dateAjout) {
        this.pkUtilisateur = pkUtilisateur;
        this.login = login;
        this.password = password;
        this.dateAjout = dateAjout;
    }

    public Integer getPkUtilisateur() {
        return pkUtilisateur;
    }

    public void setPkUtilisateur(Integer pkUtilisateur) {
        this.pkUtilisateur = pkUtilisateur;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pkUtilisateur != null ? pkUtilisateur.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        if ((this.pkUtilisateur == null && other.pkUtilisateur != null) || (this.pkUtilisateur != null && !this.pkUtilisateur.equals(other.pkUtilisateur))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return pkUtilisateur + " : " + login;
    }

}
