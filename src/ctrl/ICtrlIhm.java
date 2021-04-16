package ctrl;

import beans.Utilisateur;

import java.util.List;

/**
 * Application "JavaFXMVC2Base".<br>
 * <p>
 * Interface MVC2 "contrôleur pour l'Ihm".
 *
 * @author <a href="mailto:friedlip@edufr.ch">Paul Friedli</a>
 * @version 1.02
 * @since 9 mai 2020
 */
public interface ICtrlIhm {

    void etatIhmChargeeEtInvisible();

    void actionIhmVaQuitter();

    void actionAllumerRobot();

    void actionEteindreRobot();

    void demarrerServeur();

    void arreterServeur();

    void etatIhmVisible();

    void supprimeUtilisateur(Utilisateur user);

    void ajouteUtilisateur(String login, String password);

    void modifieUtilisateur(Utilisateur user, String login, String password);

    List<Utilisateur> getAllUsers();

    void testConnexionDB();

    void connexionUilisateur(String user, String password);

}
