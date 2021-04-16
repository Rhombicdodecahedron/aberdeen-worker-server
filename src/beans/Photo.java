package beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Alexis
 */
@Entity
@Table(name = "t_photo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Photo.findAll", query = "SELECT p FROM Photo p"),
    @NamedQuery(name = "Photo.findByPkPhoto", query = "SELECT p FROM Photo p WHERE p.pkPhoto = :pkPhoto"),
    @NamedQuery(name = "Photo.findByDateAjout", query = "SELECT p FROM Photo p WHERE p.dateAjout = :dateAjout")})
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_photo")
    private Integer pkPhoto;
    @Basic(optional = false)
    @Lob
    @Column(name = "photo")
    private byte[] photo;
    @Basic(optional = false)
    @Column(name = "date_ajout")
    @Temporal(TemporalType.DATE)
    private Date dateAjout;

    public Photo() {
    }

    public Photo(Integer pkPhoto) {
        this.pkPhoto = pkPhoto;
    }

    public Photo(Integer pkPhoto, byte[] photo, Date dateAjout) {
        this.pkPhoto = pkPhoto;
        this.photo = photo;
        this.dateAjout = dateAjout;
    }

    public Integer getPkPhoto() {
        return pkPhoto;
    }

    public void setPkPhoto(Integer pkPhoto) {
        this.pkPhoto = pkPhoto;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
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
        hash += (pkPhoto != null ? pkPhoto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Photo)) {
            return false;
        }
        Photo other = (Photo) object;
        if ((this.pkPhoto == null && other.pkPhoto != null) || (this.pkPhoto != null && !this.pkPhoto.equals(other.pkPhoto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Photo[ pkPhoto=" + pkPhoto + " ]";
    }
    
}
