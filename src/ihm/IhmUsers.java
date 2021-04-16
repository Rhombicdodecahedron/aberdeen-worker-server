package ihm;

import beans.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx_mvc2.BaseIhm;

import javax.swing.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

public class IhmUsers extends BaseIhm {

    public static final String LOGO_APPLICATION = "crusader-logo.png";
    @FXML
    public TextField txt_password;
    @FXML
    public TextField txt_login;
    @FXML
    public ListView<Utilisateur> users_list;

    @Override
    public URL getStageFXMLNameURL() {
        return getClass().getResource("IhmUsers.fxml");
    }

    @Override
    public String getStageTitle() {
        return "Gestion utilisateurs - © Alexis Stella  2020-2021";
    }

    // Appelée pour informer que l'ihm sera bientôt visible...
    // Permet d'initialiser les composants, ... de manière à ce que la fenêtre ait le contenu souhaité une fois visible.
    // Surcharger pour modifier ce comportement par défaut.
    @Override
    protected void etatIhm_1_ChargeeEtInvisible() {
        // On informe le contrôleur de l'application qui lui saura quoi faire

        // Changer l'icône de la fenêtre et de l'application avant que l'application soit visible
        //afficheListeUtilisateurs(getRefIhm().getRefCtrl().);
        getStage().initModality(Modality.APPLICATION_MODAL);
        afficheListeUtilisateurs(getRefIhm().getRefCtrl().getAllUsers());
        getStage().getIcons().add(new Image(this.getClass().getResourceAsStream("resources/" + LOGO_APPLICATION)));

        users_list.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> afficheUtilisateur(newValue));

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

    }

    public void actionSupprimerUtilisateur(ActionEvent actionEvent) {
        if (txt_login != null && txt_password != null) {
            getRefIhm().getRefCtrl().supprimeUtilisateur(users_list.getSelectionModel().getSelectedItem());
            afficheListeUtilisateurs(getRefIhm().getRefCtrl().getAllUsers());
        }
    }

    public void actionModifierUtilisateur(ActionEvent actionEvent) {
        if (txt_login != null && txt_password != null) {
            getRefIhm().getRefCtrl().modifieUtilisateur(users_list.getSelectionModel().getSelectedItem(), txt_login.getText(), txt_password.getText());
            afficheListeUtilisateurs(getRefIhm().getRefCtrl().getAllUsers());
        }
    }

    public void actionAjouterutilisateur(ActionEvent actionEvent) {
        if (txt_login != null && txt_password != null) {
            getRefIhm().getRefCtrl().ajouteUtilisateur(txt_login.getText(), txt_password.getText());
            afficheListeUtilisateurs(getRefIhm().getRefCtrl().getAllUsers());
        }
    }

    public void afficheListeUtilisateurs(List<Utilisateur> list) {
        if (list != null) {
            try {
                users_list.getItems().setAll(list);
            } catch (Exception e) {

            }

        }
    }

    public void afficheUtilisateur(Utilisateur user) {
        if (txt_login != null && txt_password != null) {
            if (user != null) {
                try {
                    txt_login.setText(users_list.getSelectionModel().getSelectedItem().getLogin());
                    txt_password.setText(users_list.getSelectionModel().getSelectedItem().getPassword());
                } catch (Exception e) {
                }
            }
        }
    }

}
