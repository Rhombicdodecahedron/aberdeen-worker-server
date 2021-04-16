package wrk;

import beans.Utilisateur;
import enumeration.LogLevel;
import enumeration.Ordres;
import exceptions.MyDBException;
import exceptions.MyRobotException;
import java.util.List;

/**
 * Interface MVC2 "contrôleur pour l'Ihm".
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public interface IWrkCtrl {

    void connexionRobot(String ip, int id, int pw) throws MyRobotException;

    void deconnexionRobot() throws MyRobotException;

    void stopThreads() throws Exception;

    void demarrerServeur(int port) throws Exception;

    void arreterServeur() throws Exception;

    boolean connexionDB(String jpa_pu) throws MyDBException;

    void supprimeUtilisateur(Utilisateur user) throws MyDBException;

    void ajouteUtilisateur(String login, String password) throws MyDBException;

    void modifieUtilisateur(Utilisateur user, String login, String password) throws MyDBException;

    List<Utilisateur> getAllUser() throws MyDBException;

    boolean testConnexionDB();

    boolean connexionUilisateur(String login, String password) throws MyDBException;

    String recoisLog(LogLevel level, String msg);

    void envoieMessageClient(Ordres level, String msg);

    String getIpServeur();

    boolean deconnexionDB() throws MyDBException;
}
