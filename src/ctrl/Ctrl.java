package ctrl;

import app.Constante;
import beans.Utilisateur;
import ch.emf.info.robot.links.bean.RobotState;
import enumeration.LogLevel;
import enumeration.Ordres;
import exceptions.MyDBException;
import exceptions.MyRobotException;
import ihm.IIhmCtrl;
import javafx_mvc2.BaseCtrl;
import wrk.IWrkCtrl;

import java.util.List;

/**
 * Cette classe renferme le conrôleur de l'application.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public class Ctrl extends BaseCtrl implements ICtrlWrk, ICtrlIhm, Constante {

    private IIhmCtrl refIhm;
    private IWrkCtrl refWrk;

    public Ctrl() {
        this.refIhm = null;
        this.refWrk = null;
    }

    public void start() {
        getRefIhm().demarrerIhm();
    }

    @Override
    public void etatIhmChargeeEtInvisible() {
        connexionDB();
    }

    @Override
    public void etatIhmVisible() {

    }

    @Override
    public void actionIhmVaQuitter() {
        try {
            arreterServeur();
            actionEteindreRobot();
            getRefWrk().stopThreads();
            deconnexionDB();
        } catch (Exception e) {
            getRefIhm().afficheErreur(e.getMessage());
        }
    }

    private void deconnexionDB() {
        try {
            boolean ok = getRefWrk().deconnexionDB();
            if (!ok) {
                getRefIhm().afficheErreur("Erreur lors de la deconnexion de la base de données");
            }
        } catch (MyDBException ex) {
            getRefIhm().afficheErreur(ex.getMessage());
        }

    }

    @Override
    public void actionAllumerRobot() {
        try {
            //String ip = Utils.rechercheIPViaMac(MAC_ROBOT);  
            //getRefWrk().connexionRobot(ip, ID, PW);
            getRefWrk().connexionRobot(IP_ROBOT, ID, PW);
        } catch (MyRobotException/* | IOException*/ e) {
            getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.ERROR, e.getMessage()));
        }
    }

    @Override
    public void actionRecoieStateRobot(RobotState robotState) {
        if (robotState != null) {
            getRefIhm().afficherInfos(robotState);
        }
    }

    @Override
    public void afficheLog(String log) {
        getRefIhm().afficheLog(log);
    }

    @Override
    public void actionEteindreRobot() {
        try {
            getRefWrk().deconnexionRobot();
        } catch (MyRobotException e) {
            getRefIhm().afficheLog(refWrk.recoisLog(LogLevel.ERROR, e.getMessage()));
            getRefWrk().envoieMessageClient(Ordres.ERROR, e.getMessage());
        }
    }

    @Override
    public void onConnectionStateReceived(boolean connected) {
        getRefIhm().robotOnOff(connected);
    }

    @Override
    public void demarrerServeur() {
        try {
            getRefWrk().demarrerServeur(PORT);
            String ipServeur = refWrk.getIpServeur();
            getRefIhm().afficheServeurOn(true);
            if (ipServeur != null) {
                getRefIhm().afficheIpPortServeur(getRefWrk().getIpServeur(), PORT);
            }
        } catch (Exception e) {
            getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.ERROR, e.getMessage()));
        }
    }

    @Override
    public void arreterServeur() {
        try {
            refWrk.arreterServeur();
            getRefIhm().afficheIpPortServeur("", 0);
            getRefIhm().afficheServeurOn(false);
        } catch (Exception e) {
            getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.ERROR, e.getMessage()));
        }
    }

    public void connexionDB() {
        try {
            if (getRefWrk().connexionDB(JPA_PU)) {
                getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.SUCCESS, "La connexion à la base de données a correctement été effectué"));
            } else {
                getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.ERROR, "Erreur lors de la connexion à la base de données"));
            }
        } catch (MyDBException e) {
            getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.ERROR, e.getMessage()));
            getRefIhm().afficheErreur(e.getMessage());
        }
    }

    @Override
    public void supprimeUtilisateur(Utilisateur user) {
        if (user != null) {
            try {
                getRefWrk().supprimeUtilisateur(user);
                String msg = "L'utilisateur " + user.getLogin() + " a correctement été supprimé";
                getRefIhm().afficheInformation(msg);
                getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.SUCCESS, msg));
            } catch (MyDBException e) {
                getRefIhm().afficheErreur(e.getMessage());
                getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.ERROR, e.getMessage()));
            }
        } else {
            getRefIhm().afficheErreur("Veuillez selectionner un utilisateur à supprimer");
        }
    }

    @Override
    public void ajouteUtilisateur(String login, String password) {
        if (!login.isEmpty() && !password.isEmpty()) {
            try {
                getRefWrk().ajouteUtilisateur(login, password);
                String msg = "L'utilisateur " + login + " a correctement été ajouté";
                getRefIhm().afficheInformation(msg);
                getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.SUCCESS, msg));
            } catch (MyDBException e) {
                getRefIhm().afficheErreur(e.getMessage());
                getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.ERROR, e.getMessage()));
            }
        } else {
            getRefIhm().afficheErreur("Veuillez remplir tous les champs");
        }
    }

    @Override
    public void modifieUtilisateur(Utilisateur user, String login, String password) {
        if (user != null) {
            if (!login.isEmpty() && !password.isEmpty()) {
                try {
                    getRefWrk().modifieUtilisateur(user, login, password);
                    String msg = "L'utilisateur " + user.getLogin() + " a correctement été modifié";
                    getRefIhm().afficheInformation(msg);
                    getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.SUCCESS, msg));
                } catch (MyDBException e) {
                    getRefIhm().afficheErreur(e.getMessage());
                    getRefIhm().afficheLog(getRefWrk().recoisLog(LogLevel.ERROR, e.getMessage()));
                }
            } else {
                getRefIhm().afficheErreur("Veuillez remplir tous les champs");
            }
        } else {
            getRefIhm().afficheErreur("Veuillez selectionner un utilisateur à modifier");
        }
    }

    @Override
    public List<Utilisateur> getAllUsers() {
        List<Utilisateur> result = null;
        try {
            result = refWrk.getAllUser();
        } catch (MyDBException ex) {
            getRefIhm().afficheErreur(ex.getMessage());
        }
        return result;
    }

    @Override
    public void testConnexionDB() {
        boolean ok = getRefWrk().testConnexionDB();
        if (ok) {
            getRefIhm().afficheInformation("La connexion à la base de données fonctionne parfaitement");
        } else {
            getRefIhm().afficheInformation("La connexion à la base de données ne fonctionne pas");
        }
    }

    @Override
    public void connexionUilisateur(String user, String password) {
        if (user != null && password != null) {
            try {
                if (getRefWrk().testConnexionDB()) {
                    boolean ok = getRefWrk().connexionUilisateur(user, password);
                    if (ok) {
                        getRefIhm().fermerIhmLogin();
                        getRefIhm().afficheInformation("Connexion effectué");
                        getRefIhm().afficheNomUtilisateur(user.toLowerCase());
                        demarrerServeur();
                        actionAllumerRobot();
                    } else {
                        getRefIhm().afficheErreur("Le login ou le mot de passe est incorrect");
                    }
                } else {
                    getRefIhm().afficheErreur("Erreur de connexion à la base de données");
                }
            } catch (MyDBException e) {
                getRefIhm().afficheErreur(e.getMessage());
            }
        }
    }

    public IIhmCtrl getRefIhm() {
        return refIhm;
    }

    public void setRefIhm(IIhmCtrl refIhm) {
        this.refIhm = refIhm;
    }

    public IWrkCtrl getRefWrk() {
        return refWrk;
    }

    public void setRefWrk(IWrkCtrl refWrk) {
        this.refWrk = refWrk;
    }

}
