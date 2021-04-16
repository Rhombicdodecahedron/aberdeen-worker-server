package wrk.robot;

import app.Constante;
import ch.emf.info.robot.links.Robot;
import ch.emf.info.robot.links.exception.UnreachableRobotException;
import enumeration.LogLevel;
import enumeration.Ordres;
import exceptions.MyRobotException;
import wrk.Wrk;

/**
 * Cette classe renferme le worker "WrkRobot" de l'application.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public class WrkRobot extends Thread implements Constante {

    /**
     * Instance de la classe Robot
     */
    private final Robot robot;
    /**
     * Instance de la classe Wrk
     */
    private final Wrk refWrk;
    /**
     * Variable running de la classe WrkRobot. Permet de définir l'état du
     * thread. True -> allumé | False -> arrêté
     */
    private volatile boolean running;

    /**
     * Constructeur de la classe WrkRobot de l'application. Ce constructeur
     * ajoute un nom à ce worker qui extends la classe Thread. Il défini aussi
     * les instances de WrkRobot et Wrk.
     *
     * @param refWrk représente l'instance du worker principal.
     */
    public WrkRobot(Wrk refWrk) {
        robot = new Robot();
        this.refWrk = refWrk;
        setName("Thread Robot");
    }

    /**
     * Cette méthode renvoie l'état du robot connecté. L'état du robot est
     * constitué de de son id, nom, pw, la vitesse de la chenille droite et
     * gauche, la dernière image, etc...
     */
    @Override
    public void run() {
        running = true;

        while (running) {
            _sleep(SLEEP);
            try {
                if (robot.isConnected()) {
                    refWrk.actionRecoieStateRobot(robot.getRobotState());

                }
                refWrk.actionRecoisConnexion(robot.isConnected());
            } catch (Exception e) {
                // refWrk.envoieLog(LogLevel.ERROR, e.getMessage());
            }

        }
    }

    /**
     * Cette méthode permet de faire attendre le thread pendant un certain
     * temps. Ce temps est un paramètre qui est défini en milliseconde.
     *
     * @param millis représente le temps que doit attendre le thread.
     */
    private void _sleep(int millis) {
        try {
            sleep(millis);
        } catch (InterruptedException ex) {
            refWrk.envoieLog(LogLevel.ERROR, ex.getMessage());
        }
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
    public void connectRobot(String ip, int id, int pw) throws MyRobotException {
        if (ip != null) {
            if (robot != null && !robot.isConnected()) {
                try {
                    new Thread(() -> {
                        try {
                            refWrk.envoieLog(LogLevel.INFO, "Tentative de connexion au robot via l'adresse ip : " + ip);
                            robot.connect(ip, id, pw);
                            refWrk.envoieLog(LogLevel.SUCCESS, "La connexion au robot à correctement été effectué");
                            refWrk.envoieMessageClient(Ordres.SUCCESS, "Connexion au robot effectué");
                        } catch (UnreachableRobotException e) {
                            refWrk.envoieLog(LogLevel.ERROR, e.getMessage());
                            refWrk.envoieMessageClient(Ordres.ERROR, e.getMessage());
                        }
                    }).start();
                } catch (Exception ex) {
                    throw new MyRobotException("Erreur lors de la connexion au robot");
                }
            }
        }
    }

    /**
     * Cette méthode permet de déconnecter le robot de l'application. En cas
     * d'erreur, elle throw l'Exception MyRobotException avec le message
     * d'erreur.
     *
     * @throws MyRobotException représente une erreur que le robot rencontre.
     */
    public void disconnectRobot() throws MyRobotException {
        try {
            if (robot != null && robot.isConnected()) {
                refWrk.envoieLog(LogLevel.INFO, "Tentative de déconnexion du robot");
                new Thread(robot::disconnect).start();
                String msg = "Le robot a correctement été déconnecté";
                refWrk.envoieLog(LogLevel.SUCCESS, msg);
                refWrk.envoieMessageClient(Ordres.SUCCESS, msg);
            }
        } catch (Exception ex) {
            throw new MyRobotException("Erreur lors de la déconnexion du robot");
        }
    }

    /**
     * Cette méthode permet d'enlever le robot de sa station de chargement.
     */
    public void undockRobot() {
        if (robot != null && robot.isConnected()) {
            robot.undock();
        }
    }

    /**
     * Cette méthode permet de mettre le robot sur sa station de chargement.
     */
    public void dockRobot() {
        if (robot != null && robot.isConnected()) {
            robot.dock();
        }
    }

    /**
     * Cette méthode permet de faire bouger le robot connecté avec les valeurs
     * reçues en paramètre.
     *
     * @param vitesseGauche représente la vitesse de la chenille gauche.
     * @param vitesseDroite représente la vitesse de la chenille droite.
     */
    public void bougetRobot(int vitesseGauche, int vitesseDroite) {
        if (robot != null && robot.isConnected()) {
            robot.setLeftSpeed((short) vitesseGauche);
            robot.setRightSpeed((short) vitesseDroite);
        }
    }

    /**
     * Setter de la variable running.
     *
     * @param running représente l'état du thread.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}
