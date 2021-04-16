package wrk.db;

import beans.Photo;
import beans.Utilisateur;
import exceptions.MyDBException;
import helpers.DateTimeLib;
import helpers.SystemLib;
import wrk.db.jpadao.JpaDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe renferme le worker WrkDatabase de l'application.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public class WrkDataBase {

    /**
     * Instance de la classe JpaDao
     */
    private JpaDao<Utilisateur, Object> utilisateur;
    /**
     * Instance de la classe JpaDao
     */
    private JpaDao<Photo, Object> photo;

    /**
     * Constructeur de la classe WrkDatabase
     */
    public WrkDataBase() {
    }

    /**
     * Cette méthode permet de se connecter à la base de données. Elle retourne
     * true si la connexion s'est bien effectuée ou false.
     *
     * @param jpa_pu représente le nom de la persistence unit
     * @return un boolean true si la connexion s'est bien effectuée ou false
     * @throws MyDBException représente une erreur que la base de données
     * rencontre.
     */
    public boolean connexionDB(String jpa_pu) throws MyDBException {
        boolean result = false;
        try {
            utilisateur = new JpaDao<>(jpa_pu, Utilisateur.class);
            photo = new JpaDao<>(jpa_pu, Photo.class);
            if (utilisateur.estConnectee() && photo.estConnectee()) {
                result = true;
            }
        } catch (MyDBException e) {
            throw new MyDBException(SystemLib.getCurrentMethod(), e.getMessage());
        }
        return result;
    }

    /**
     * Cette méthode permet de se deconnecter de la base de données. Elle
     * retourne true si la déconnexion s'est bien effectuée ou false.
     *
     * @return un boolean true si la déconnexion s'est bien effectuée ou false.
     * @throws MyDBException représente une erreur que la base de données
     * rencontre.
     */
    public boolean deconnexionDB() throws MyDBException {
        boolean result = false;
        if (utilisateur != null && photo != null) {
            try {
                utilisateur.deconnecter();
                photo.deconnecter();
                if (!utilisateur.estConnectee() && !photo.estConnectee()) {
                    result = true;
                }
            } catch (MyDBException e) {
                throw new MyDBException(SystemLib.getCurrentMethod(), e.getMessage());
            }
        }
        return result;
    }

    /**
     * Cette méthode permet de rechercher un utilisateur dans la base de données
     * en fonction de son login. Elle retourne un objet "Utilisateur" si un
     * utilisateur ayant le même login existe dans la base de données ou null.
     *
     * @param login représente le login de l'utilisateur recherché.
     * @return un objet Utilisateur de la personne ayant le même login dans la
     * base de données. Retourne null si aucun utilisateur avec le login n'a été
     * trouvé.
     * @throws MyDBException représente une erreur que la base de données
     * rencontre.
     */
    public Utilisateur rechercheUtilisateur(String login) throws MyDBException {
        Utilisateur result = null;
        if (login != null) {
            String utilisateurRecherche = login.toLowerCase();
            List<Utilisateur> liste = getAllUsers();
            for (Utilisateur user : liste) {
                if (user.getLogin().toLowerCase().equals(utilisateurRecherche)) {
                    result = user;
                }
            }
        }
        return result;
    }

    /**
     * Cette méthode permet de modifier un utilisateur existant dans la base de
     * données. En cas de problème, elle throw une Exception "MyDBException"
     * avec un code d'erreur.
     *
     * @param user représente l'objet de l'utilisateur souhaitant être modifié.
     * @param login représente le login modifié.
     * @param password représente le mot de passe modifié.
     * @throws MyDBException représente une erreur que la base de données
     * rencontre.
     */
    public void modifieUtilisateur(Utilisateur user, String login, String password) throws MyDBException {
        if (utilisateur != null && utilisateur.estConnectee()) {
            if (user != null) {
                try {
                    if (login != null && password != null) {
                        if (rechercheUtilisateur(login) == null) {
                            user.setLogin(login);
                            user.setPassword(password);
                            utilisateur.modifier(user);
                        } else {
                            throw new Exception("L'utilisateur " + login + " existe déjà");
                        }
                    } else {
                        throw new Exception("login et mot de passe ne doivent pas être null");
                    }
                } catch (Exception e) {
                    throw new MyDBException(SystemLib.getFullMethodName(), e.getMessage());
                }
            }
        }
    }

    /**
     * Cette méthode permet d'ajoute un nouvel utilisateur à la base de données.
     * En cas de problème, elle throw une Exception "MyDBException" avec un code
     * d'erreur.
     *
     * @param login représente le login du nouvel utilisateur.
     * @param password représente le mot de passe du nouvel utilisateur.
     * @throws MyDBException représente une erreur que la base de données
     * rencontre.
     */
    public void ajouteUtilisateur(String login, String password) throws MyDBException {
        if (utilisateur != null && utilisateur.estConnectee()) {
            try {
                if (login != null && password != null) {
                    if (rechercheUtilisateur(login) == null) {
                        Utilisateur newUtilisateur = new Utilisateur();
                        newUtilisateur.setLogin(login);
                        newUtilisateur.setPassword(password);
                        newUtilisateur.setDateAjout(DateTimeLib.getNow());
                        utilisateur.creer(newUtilisateur);
                    } else {
                        throw new Exception("L'utilisateur " + login + " existe déjà");
                    }
                } else {
                    throw new Exception("Le login et le mot de passe ne doivent pas être null");
                }
            } catch (Exception e) {
                throw new MyDBException(SystemLib.getCurrentMethod(), e.getMessage());
            }
        }
    }

    /**
     * Cette méthode permet de supprimer un utilisateur de la base de données.
     * En cas de problème, elle throw une Exception "MyDBException" avec un code
     * d'erreur.
     *
     * @param user représente l'objet de l'utilisateur souhaitant être supprimé.
     * @throws MyDBException une erreur que la base de données rencontre.
     */
    public void supprimeUtilisateur(Utilisateur user) throws MyDBException {
        if (utilisateur != null && utilisateur.estConnectee()) {
            if (user != null) {
                try {
                    utilisateur.effacer(user.getPkUtilisateur());
                } catch (Exception ex) {
                    throw new MyDBException(SystemLib.getCurrentMethod(), ex.getMessage());
                }
            }
        }
    }

    /**
     * Cette méthode permet de récupérer tous les utilisateurs se trouvant dans
     * la base de données. En cas de problème, elle throw une Exception
     * "MyDBException" avec un code d'erreur.
     *
     * @return la liste de tous les utilisateurs se trouvant dans la base de
     * données.
     * @throws MyDBException représente une erreur que la base de données
     * rencontre.
     */
    public List<Utilisateur> getAllUsers() throws MyDBException {
        List<Utilisateur> result = null;
        if (utilisateur != null && utilisateur.estConnectee()) {
            try {
                result = utilisateur.lireListe();
            } catch (Exception e) {
                throw new MyDBException(SystemLib.getCurrentMethod(), e.getMessage());
            }
        }
        return result;
    }

    /**
     * Cette méthode permet d'ajouter une image dans la base de données. En cas
     * de problème, elle throw une Exception "MyDBException" avec un code
     * d'erreur.
     *
     * @param image représente l'image en byte[] à ajouter dans la base de
     * données.
     * @throws MyDBException représente une erreur que la base de données
     * rencontre.
     */
    public void ajouteImage(byte[] image) throws MyDBException {
        if (photo != null && photo.estConnectee()) {
            if (image != null) {
                try {
                    Photo newPhoto = new Photo();
                    newPhoto.setPhoto(image);
                    newPhoto.setDateAjout(DateTimeLib.getNow());
                    photo.creer(newPhoto);
                } catch (Exception e) {
                    throw new MyDBException(SystemLib.getCurrentMethod(), e.getMessage());

                }
            }
        }

    }

    /**
     * Cette méthode permet de tester la connexion avec la base de données. Elle
     * retourne un boolean en fonction du résultat.
     *
     * @return true si la connexion à la base de données fonctionne sinon false.
     */
    public boolean testConnexionDB() {
        boolean result = false;
        if (utilisateur != null && photo != null) {
            if (utilisateur.estConnectee() && photo.estConnectee()) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Cette méthode permet de recevoir la liste d'images stockées dans la base
     * de données. En cas de problème, elle throw une Exception "MyDBException"
     * avec un code d'erreur.
     *
     * @return la liste des images en byte[] stockées dans la base de données.
     * @throws MyDBException représente une erreur que la base de données
     * rencontre.
     */
    public List<byte[]> listePhotos() throws MyDBException {
        List<byte[]> result = null;
        if (photo != null && photo.estConnectee()) {
            try {
                result = new ArrayList<>();
                List<Photo> temp = photo.lireListe();
                if (temp != null) {
                    for (Photo photo : photo.lireListe()) {
                        result.add(photo.getPhoto());
                    }
                }

            } catch (Exception e) {
                throw new MyDBException(SystemLib.getCurrentMethod(), e.getMessage());
            }
        }
        return result;
    }

    /**
     * Cette méthode permet de tester si le nom d'utilisateur et le mot de passe
     * de connexion passés en paramètre sont corrects.
     *
     * @param login représente le nom d'utilisateur du visiteur voulant se
     * connecté.
     * @param password représente le mot de passe du visiteur voulant se
     * connecté.
     * @return un boolean true si les connexions sont bonnes ou false si
     * l'utilisateur n'existe pas ou que le login ou le mot de passe sont faux.
     * @throws MyDBException représente une erreur que la base de données
     * rencontre.
     */
    public boolean connexionUilisateur(String login, String password) throws MyDBException {
        boolean result = false;
        if (photo != null && photo.estConnectee()) {
            if (login != null && password != null) {
                try {
                    Utilisateur utilisateur = rechercheUtilisateur(login);
                    if (utilisateur != null) {
                        if (utilisateur.getPassword().equals(password)) {
                            result = true;
                        }
                    }
                } catch (Exception e) {
                    throw new MyDBException(SystemLib.getCurrentMethod(), e.getMessage());
                }
            }
        }
        return result;
    }
}
