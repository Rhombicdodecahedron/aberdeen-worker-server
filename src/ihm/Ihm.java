package ihm;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;

import ch.emf.info.robot.links.bean.RobotState;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx_mvc2.BaseIhmPrincipale;
import helpers.EasyPopups;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Ihm extends BaseIhmPrincipale implements IIhmCtrl {

    public static final String LOGO_APPLICATION = "crusader-logo.png";

    public ImageView fluxVideo;
    public Label disconnected_robot;
    public TextArea logs;
    public Button robot_on;
    public Button robot_off;
    public Button server_off;
    public Button server_on;
    public Label ip_serveur;
    public Label port_serveur;
    public Label txt_connected_user;

    private IhmLogin ihmLogin;
    private IhmUsers ihmUsers;


    @Override
    public URL getStageFXMLNameURL() {
        return getClass().getResource("Ihm.fxml");
    }

    @Override
    public String getStageTitle() {
        return "Interface serveur - © Alexis Stella 2020-2021";
    }

    // Appelée pour informer que l'ihm sera bientôt visible...
    // Permet d'initialiser les composants, ... de manière à ce que la fenêtre ait le contenu souhaité une fois visible.
    // Surcharger pour modifier ce comportement par défaut.
    @Override
    protected void etatIhm_1_ChargeeEtInvisible() {
        // On informe le contrôleur de l'application qui lui saura quoi faire
        getRefCtrl().etatIhmChargeeEtInvisible();


        ihmUsers = new IhmUsers();
        ihmLogin = new IhmLogin();
        getMainStage().setOpacity(0);
        // Changer l'icône de la fenêtre et de l'application avant que l'application soit visible
        getMainStage().getIcons().add(new Image(this.getClass().getResourceAsStream("resources/" + LOGO_APPLICATION)));


        // <editor-fold defaultstate="collapsed" desc="Code pour avoir un icône pour cette application sur le Dock pour OS/X">
        // Pour l'icône de l'application sur Mac OS/X, pas nécessairement le même que celui de la fenêtre principale.
        // Appel de setDockIconImage() de manière dynamique puisque cette classe n'est peut-être pas disponible (sur Windows, Linux, ..)
        {
            try {
                Class aClass = Class.forName("com.apple.eawt.Application");
                Method getApplication = aClass.getMethod("getApplication", (Class[]) null);
                Object application = getApplication.invoke(null);
                Method setDockIconImage = aClass.getMethod("setDockIconImage", java.awt.Image.class);
                URL iconURL = this.getClass().getResource("resources/" + LOGO_APPLICATION);
                java.awt.Image osxImage = new ImageIcon(iconURL).getImage();
                setDockIconImage.invoke(application, osxImage);
            } catch (Exception e) {
            }
        }
        // </editor-fold>


    }

    // Appelée pour informer que cet ihm va être refermée sous peu.
    // Permet de faire ce qui doit être fait juste avant que cette ihm ne soit refermée, par exemple sauvegarder des états si nécessaire
    @Override
    protected void etatIhm_4_VaQuitter() {
        // On informe le contrôleur de l'application qui lui saura quoi faire
        getRefCtrl().actionIhmVaQuitter();
        getMainStage().close();
    }

    @Override
    protected void etatIhm_2b_Visible() {
        refCtrl.etatIhmVisible();
        ihmLogin.createAndShowStage();
    }

    @Override
    public void afficheInformation(String msgInformation) {
        EasyPopups.afficheInformation(msgInformation);
    }

    @Override
    public void afficheErreur(String msgErreur) {
        EasyPopups.afficheErreur(msgErreur);
    }

    @Override
    public boolean afficheQuestion(String msgQuestion) {
        return EasyPopups.afficheQuestion(msgQuestion);
    }

    public void actionAllumerRobot(ActionEvent actionEvent) {
        refCtrl.actionAllumerRobot();
    }

    public void actionEteindreRobot(ActionEvent actionEvent) {
        refCtrl.actionEteindreRobot();
    }

    @Override
    public void robotOnOff(boolean onOff) {
        if (robot_off != null && robot_on != null) {
            robot_off.setDisable(!onOff);
            robot_on.setDisable(onOff);
            if (fluxVideo != null && disconnected_robot != null) {
                fluxVideo.setOpacity(!onOff ? 0.50 : 1);
                disconnected_robot.setVisible(!onOff);
            }
        }
    }

    @Override
    public void afficheLog(String msg) {
        if (logs != null) {
            if (msg != null) {
                try {                    
                    logs.appendText(msg + System.getProperty("line.separator"));
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public void afficheServeurOn(boolean isOn) {
        server_on.setDisable(isOn);
        server_off.setDisable(!isOn);
    }

    @Override
    public void afficheIpPortServeur(String ip, int port) {
        if (ip_serveur != null && port_serveur != null) {
            if (ip != null && !ip.isEmpty()) {
                ip_serveur.setText(ip);
            } else {
                ip_serveur.setText("0.0.0.0");
            }
            port_serveur.setText(Integer.toString(port));
        }
    }

    @Override
    public void afficherInfos(RobotState robotState) {
        if (robotState.getImage() != null && fluxVideo != null) {
            Platform.runLater(() -> {
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(robotState.getImage());
                    BufferedImage buff = ImageIO.read(bais);
                    WritableImage newImage = SwingFXUtils.toFXImage(buff, null);
                    fluxVideo.setImage(newImage);
                } catch (IOException e) {

                }
            });
        }
    }

    @FXML
    public void actionUsers(ActionEvent actionEvent) {
        ihmUsers.createAndShowStage();
    }

    @Override
    public void fermerIhmLogin() {
        getMainStage().setOpacity(1);
        ihmLogin.fermerIhmLogin();
    }

    @Override
    public void afficheNomUtilisateur(String user) {
        if (txt_connected_user != null) {
            if (user != null) {
                try {
                     txt_connected_user.setText(user);
                } catch (Exception e) {
                }
            }
        }
    }


    public void actionArretServeur(ActionEvent actionEvent) {
        refCtrl.arreterServeur();
    }

    public void actionDemarrageServeur(ActionEvent actionEvent) {
        refCtrl.demarrerServeur();
    }

    public void actionDeconnexionUtilisateur(ActionEvent actionEvent) {
        getMainStage().setOpacity(0);
        refCtrl.arreterServeur();
        refCtrl.actionEteindreRobot();
        ihmLogin.createAndShowStage();
    }

    public void actionTestConnexionDB(ActionEvent actionEvent) {
        refCtrl.testConnexionDB();
    }

}
