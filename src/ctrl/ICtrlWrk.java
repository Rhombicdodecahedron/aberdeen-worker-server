package ctrl;

import ch.emf.info.robot.links.bean.RobotState;

/**
 * Application "JavaFXMVC2Base".<br>
 * <p>
 * Interface MVC2 "contrôleur pour le worker principal".<br>
 * <br>
 * Cette interface est le plus souvent vide, sauf lorsqu'il y a de bonnes raisons pour un worker de notifier son contrôleur.<br>
 * Cela peut par exemple être le cas lorsqu'il détecte que les données ont été modifiées, ...).<br>
 *
 * @author <a href="mailto:friedlip@edufr.ch">Paul Friedli</a>
 * @version 1.02
 * @since 9 mai 2020
 */
public interface ICtrlWrk {

    void onConnectionStateReceived(boolean connected);

    void actionAllumerRobot();

    void actionRecoieStateRobot(RobotState robotState);

    void afficheLog(String log);


}
