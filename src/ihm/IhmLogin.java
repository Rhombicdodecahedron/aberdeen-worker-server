package ihm;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx_mvc2.BaseIhm;

import javax.swing.*;
import java.lang.reflect.Method;
import java.net.URL;

public class IhmLogin extends BaseIhm {

    public static final String LOGO_APPLICATION = "crusader-logo.png";
    public TextField txt_password;
    public TextField txt_login;


    @Override
    public URL getStageFXMLNameURL() {
        return getClass().getResource("IhmLogin.fxml");
    }

    @Override
    public String getStageTitle() {
        return "Login - © Alexis Stella 2020-2021";
    }

    // Appelée pour informer que l'ihm sera bientôt visible...
    // Permet d'initialiser les composants, ... de manière à ce que la fenêtre ait le contenu souhaité une fois visible.
    // Surcharger pour modifier ce comportement par défaut.
    @Override
    protected void etatIhm_1_ChargeeEtInvisible() {
        // On informe le contrôleur de l'application qui lui saura quoi faire

        // Changer l'icône de la fenêtre et de l'application avant que l'application soit visible

        getStage().initModality(Modality.APPLICATION_MODAL);

        getStage().getIcons().add(new Image(this.getClass().getResourceAsStream("resources/" + LOGO_APPLICATION)));


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
        getStage().close();
        getRefIhm().etatIhm_4_VaQuitter();

    }

    public void fermerIhmLogin(){
        getStage().hide();
    }

    public void actionAuthentification(ActionEvent actionEvent) {
        if (txt_login != null && txt_password != null) {
            getRefIhm().getRefCtrl().connexionUilisateur(txt_login.getText(), txt_password.getText());
        }
    }

}
