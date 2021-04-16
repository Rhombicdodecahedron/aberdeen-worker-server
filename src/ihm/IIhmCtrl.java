package ihm;

import ch.emf.info.robot.links.bean.RobotState;

/**
 * Application "JavaFXMVC2Base".<br>
 * <p>
 * Interface MVC2 "Ihm pour le contrôleur".
 *
 * @author <a href="mailto:friedlip@edufr.ch">Paul Friedli</a>
 * @version 1.02
 * @since 9 mai 2020
 */
public interface IIhmCtrl {

    void demarrerIhm();

    void afficheInformation(String msgInformation);

    void afficheErreur(String msgErreur);

    boolean afficheQuestion(String msgQuestion);

    void robotOnOff(boolean onOff);

    void afficheLog(String msg);

    void afficheServeurOn(boolean isOn);

    void afficheIpPortServeur(String ip, int port);

    void afficherInfos(RobotState robotState);

    void fermerIhmLogin();

    void afficheNomUtilisateur(String user);
}
