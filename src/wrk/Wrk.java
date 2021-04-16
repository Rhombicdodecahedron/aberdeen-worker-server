package wrk;

import beans.PacketTCP;
import beans.Utilisateur;
import ch.emf.info.robot.links.bean.RobotState;
import ctrl.ICtrlWrk;
import enumeration.Ordres;
import enumeration.LogLevel;
import exceptions.MyDBException;
import exceptions.MyRobotException;
import wrk.db.WrkDataBase;
import wrk.log.WrkLog;
import wrk.robot.WrkRobot;
import wrk.tcp.dispatcher.WrkDispatcher;
import wrk.tcp.WrkServer;

import java.util.List;

/**
 * Cette classe renferme le worker principal de l'application.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public class Wrk implements IWrkCtrl {

    /**
     * Instance de l'interface ICtrlWrk
     */
    private ICtrlWrk refCtrl;
    /**
     * Instance de la classe WrkRobot
     */
    private WrkRobot wrkRobot;
    /**
     * Instance de la classe WrkServer
     */
    private WrkServer wrkServer;
    /**
     * Instance de la classe WrkLog
     */
    private final WrkLog wrkLog;
    /**
     * Instance de la classe WrkDataBase
     */
    private final WrkDataBase wrkDataBase;
    /**
     * Instance de la classe WrkDispatcher
     */
    private final WrkDispatcher wrkDispatcher;

    /**
     * Constructeur de la classe Worker de l'application. Il définit les
     * instances de WrkLog, WrkDispatcher, WrkDataBase, WrkRobot et WrkServer
     */
    public Wrk() {
        this.refCtrl = null;
        wrkLog = new WrkLog();
        wrkDispatcher = new WrkDispatcher(this);
        wrkDataBase = new WrkDataBase();
        wrkRobot = new WrkRobot(this);
        wrkRobot.start();
    }

    /**
     * Cette méthode permet de connecter l'application à un robot 7Links. Pour
     * ce faire, cette méthode a besoin de l'adresse Ip du robot à atteindre,
     * son id et son pw. En cas d'erreur, elle throw une exception si une erreur
     * est survenue ou rien du tout.
     *
     * @param ip représente l'adresse ip du robot à atteindre. (ex.:
     * 192.168.1.20)
     * @param id représente l'id du robot à atteindre. (ex.: 7373)
     * @param pw représente le pw du robot à atteindre. (ex.: 8145827)
     * @throws MyRobotException représente une erreur que le robot rencontre.
     */
    @Override
    public void connexionRobot(String ip, int id, int pw) throws MyRobotException {
        wrkRobot.connectRobot(ip, id, pw);
    }

    /**
     * Cette méthode permet de déconnecter le robot de l'application. En cas
     * d'erreur, elle throw l'Exception MyRobotException avec le message
     * d'erreur.
     *
     * @throws MyRobotException représente une erreur que le robot rencontre.
     */
    @Override
    public void deconnexionRobot() throws MyRobotException {
        wrkRobot.disconnectRobot();
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
    @Override
    public boolean connexionDB(String jpa_pu) throws MyDBException {
        return wrkDataBase.connexionDB(jpa_pu);
    }

    /**
     * Cette méthode permet de se deconnecter de la base de données. Elle
     * retourne true si la déconnexion s'est bien effectuée ou false.
     *
     * @return un boolean true si la déconnexion s'est bien effectuée ou false.
     * @throws MyDBException représente une erreur que la base de données
     * rencontre.
     */
    @Override
    public boolean deconnexionDB() throws MyDBException {
        return wrkDataBase.deconnexionDB();
    }

    /**
     * Cette méthode permet de tester la connexion avec la base de données. Elle
     * retourne un boolean en fonction du résultat.
     *
     * @return true si la connexion à la base de données fonctionne sinon false.
     */
    @Override
    public boolean testConnexionDB() {
        return wrkDataBase.testConnexionDB();
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
    @Override
    public List<Utilisateur> getAllUser() throws MyDBException {
        return wrkDataBase.getAllUsers();
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
    @Override
    public void ajouteUtilisateur(String login, String password) throws MyDBException {
        wrkDataBase.ajouteUtilisateur(login, password);
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
    @Override
    public void modifieUtilisateur(Utilisateur user, String login, String password) throws MyDBException {
        wrkDataBase.modifieUtilisateur(user, login, password);
    }

    /**
     * Cette méthode permet de supprimer un utilisateur de la base de données.
     * En cas de problème, elle throw une Exception "MyDBException" avec un code
     * d'erreur.
     *
     * @param user représente l'objet de l'utilisateur souhaitant être supprimé.
     * @throws MyDBException une erreur que la base de données rencontre.
     */
    @Override
    public void supprimeUtilisateur(Utilisateur user) throws MyDBException {
        wrkDataBase.supprimeUtilisateur(user);
    }

    /**
     * Cette méthode génère un log contenant un niveau, une date et un message
     * ou null si le message ou l'ordre passés en paramètre sont null.
     *
     * @param level représente le niveau du log défini dans l'énumération
     * "LogLevel" (WARNING, INFO, ERROR, SUCCESS)
     * @param message représente message du log. Cela peut représenter un
     * message d'erreur ou de succès.
     * @return un String contenant le message de log, la date et le niveau de
     * log. (ex.: [SUCCESS] 01.01.2021 : message ) ou null en cas d'erreur
     */
    @Override
    public String recoisLog(LogLevel level, String message) {
        return wrkLog.log(level, message);
    }

    /**
     *
     * Cette méthode permet de demarrer le serveur tcp sur le port qui lui ait
     * donné en paramètre.
     *
     * @param port représente le port du serveur tcp.
     * @throws Exception représente l'erreur que le serveur à rencontré.
     */
    @Override
    public void demarrerServeur(int port) throws Exception {
        wrkServer = new WrkServer(this);
        wrkServer.demarrerServeur(port);
        wrkServer.start();
    }

    /**
     * Cette méthode permet d'arrêter le serveur tcp. En cas de problème, elle
     * throw une Exception.
     *
     * @throws Exception représente une erreur que le serveur rencontre.
     */
    @Override
    public void arreterServeur() throws Exception {
        if (wrkServer != null) {
            wrkServer.arreterServeur();
            wrkServer.setRunning(false);
            wrkServer.join();
            wrkServer = null;
        }
    }

    /**
     * Cette méthode permet de traiter un ordre sérialisé reçu par un client
     * tcp. Elle va de se fait désérialiser cet ordre pour correctement le
     * répartir dans l'application.
     *
     * @param ordre représente l'ordre sérialisé envoyé par le client TCP.
     */
    public void ordreToExecutor(String ordre) {
        wrkDispatcher.dispatchOrdre(ordre);
    }

    /**
     * Cette méthode permet de demander au contrôleur d'afficher une log.
     *
     * @param level représente le niveau de la log. (ex. : SUCCESS, ERROR,
     * WARNING, INFO)
     * @param msg représente le message de log.
     */
    public void envoieLog(LogLevel level, String msg) {
        refCtrl.afficheLog(wrkLog.log(level, msg));
    }

    /**
     *
     * Cette méthode permet de demander au contrôler d'afficher l'état de
     * connexion au robot.
     *
     * @param connected représente le statut de connexion.
     */
    public void actionRecoisConnexion(boolean connected) {
        refCtrl.onConnectionStateReceived(connected);
    }

    /**
     * Cette méthode permet d'envoyer un message avec un ordre spécifique à tous
     * les clients connectés au serveur tcp.
     *
     * @param ordre représente l'ordre à envoyer aux clients.
     * @param msg représente le message à envoyer aux clients.
     */
    @Override
    public void envoieMessageClient(Ordres ordre, String msg) {
        if (wrkServer != null) {
            wrkServer.envoieMessageClient(ordre, msg);
        }
    }

    /**
     * Cette méthode permet de faire bouger le robot connecté avec les valeurs
     * reçues en paramètre.
     *
     * @param vitesseGauche représente la vitesse de la chenille gauche.
     * @param vitesseDroite représente la vitesse de la chenille droite.
     */
    public void bougerRobot(int vitesseDroite, int vitesseGauche) {
        wrkRobot.bougetRobot(vitesseDroite, vitesseGauche);
    }

    /**
     * Cette méthode permet de renvoyer l'état du robot aux clients et au
     * contrôleur de l'application serveur.
     *
     * @param robotState
     */
    public void actionRecoieStateRobot(RobotState robotState) {
        refCtrl.actionRecoieStateRobot(robotState);
        wrkServer.envoieInfosRobot(robotState);
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
        wrkDataBase.ajouteImage(image);
    }

    public void actionConnexionRobot() {
        refCtrl.actionAllumerRobot();
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
    @Override
    public boolean connexionUilisateur(String login, String password) throws MyDBException {
        return wrkDataBase.connexionUilisateur(login, password);
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
        return wrkDataBase.listePhotos();

    }

    /**
     * Cette méthode permet d'envoyer un paquet tcp contenant un ordre et des
     * informations à tous les clients connectés au serveur tcp.
     *
     * @param pkt représente l'object PacketTCP contenant un ordre et les
     * informations à envoyer aux clients.
     */
    public void envoiePaquetClient(PacketTCP pkt) {
        wrkServer.envoiePaquetTCP(pkt);
    }

    /**
     * Cette méthode permet de retourner l'adresse ip du serveur tcp. Si aucune
     * adresse ip n'est trouvée, la méthode renverra null.
     *
     * @return l'adresse ip du serveur tcp ou null si aucune n'est trouvée.
     */
    @Override
    public String getIpServeur() {
        return wrkServer.getIPServeur();
    }

    /**
     * Cette méthode permet d'enlever le robot de sa station de chargement.
     */
    public void undockRobot() {
        wrkRobot.undockRobot();
    }

    /**
     * Cette méthode permet de mettre le robot sur sa station de chargement.
     */
    public void dockRobot() {
        wrkRobot.dockRobot();
    }

    /**
     * Cette méthode permet de stopper le thread WrkRobot et WrkServer. Elle
     * permet aussi de stopper le serveur tcp s'il est lancé.
     *
     * @throws Exception renvoie l'erreur survenue lors de l'arrêt des threads.
     */
    @Override
    public void stopThreads() throws Exception {
        wrkRobot.setRunning(false);
        wrkRobot.join();
        wrkRobot = null;
        if (wrkServer != null) {
            wrkServer.setRunning(false);
            wrkServer.arreterServeur();
            wrkServer.join();
            wrkServer = null;
        }
        System.gc();
    }

    /**
     * Setter de l'interface ICtrlWrk
     *
     * @param refCtrl représente l'interface ICtrlWrk
     */
    public void setRefCtrl(ICtrlWrk refCtrl) {
        this.refCtrl = refCtrl;
    }
}
