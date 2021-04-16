package javafx_mvc2;

////////////////////////////////////////////////////////////////////////////////
//       _                  _______  __          __  ____     ______ ____     //
//      | | __ ___   ____ _|  ___\ \/ /         |  \/  \ \   / / ___|___ \    //
//   _  | |/ _` \ \ / / _` | |_   \  /   _____  | |\/| |\ \ / / |     __) |   //
//  | |_| | (_| |\ V / (_| |  _|  /  \  |_____| | |  | | \ V /| |___ / __/    //
//   \___/ \__,_| \_/ \__,_|_|   /_/\_\    _   _|_|_ |_| _\_/  \____|_____|   //
//     _ __   __ _ _ __  |  _ \ __ _ _   _| | |  ___| __(_) ___  __| | (_)    //
//    | '_ \ / _` | '__| | |_) / _` | | | | | | |_ | '__| |/ _ \/ _` | | |    //
//    | |_) | (_| | |    |  __/ (_| | |_| | | |  _|| |  | |  __/ (_| | | |    //
//    | .__/ \__,_|_|    |_|   \__,_|\__,_|_| |_|  |_|  |_|\___|\__,_|_|_|    //
//    |_|                            v1.02                                    //
//                                                                            //
// -------------------------------------------------------------------------- //
//                                                                            //
// Classes pour facilement réaliser un projet MVC2 avec des Ihm en JavaFX.    //
//                                                                            //
// ========================================================================== //
// HISTORIQUE                                                                 //
// ========================================================================== //
//                                                          v1.0 / 12.04.2020 //
// Création initiale.                                                         //
// -------------------------------------------------------------------------- //
//                                                         v1.01 / 20.04.2020 //
// Correction de bugs. Ajout de EasyPopups.                                   //
// -------------------------------------------------------------------------- //
//                                                         v1.02 / 09.05.2020 //
// Séparation de l'Ihm principale (contrôleur + application au sens JavaFX),  //
// de l'Ihm secondaire (seulement contrôleur au sens JavaFX) à l'aide de 2    //
// classes distinctes BaseIhmPrincipale et BaseIhm.                           //
//                                                                            //
// Ajout de notifications très pratiques et surchargeables pour facilement    //
// connaître l'état d'une fenêtre/stage :                                     //
// - etatIhm_1_ChargeeEtInvisible() => créée mais juste pas encore visible    //
//                                     le bon moment pour la configurer...    //
// - etatIhm_4_VaQuitter()          => l'ihm va quitter sous peu...           //
// - etatIhm_2a_BientotVisible()    => ihm va sous peu apparaître             //
// - etatIhm_2b_Visible()           => ihm est visible                        //
// - etatIhm_3a_BientotInvisible()  => ihm va sous peu disparaître            //
// - etatIhm_3b_Invisible()         => ihm a disparu                          //
//                                                                            //
// Créer et afficher une ihm secondaire devient aussi simple que :            //
//   new IhmSecondaire().createAndShowStage();                                //
//                                                                            //
// ATTENTION : Ne pas tenter de communiquer "directement" avec l'objet créé ! //
//             C'est lui qui le fera facilement avec son Ihm principale,      //
//             via les méthodes prévues pour être surchargées, et dans        //
//             lesquelles il pourra facilement retrouver son Ihm principale   //
//             pour lui parler à l'aide de getRefIhm().                       //
//             En effet, c'est à nouveau JavaFX qui va créer une instance du  //
//             contrôleur (même classe) et donc nous parlerions avec la       //
//             mauvaise instance/thread !                                     //
//                                                                            //
////////////////////////////////////////////////////////////////////////////////
import ihm.Ihm;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class BaseIhm {

    // <editor-fold defaultstate="collapsed" desc="Ne pas toucher ! Ce code permet de facilement structurer un projet JavaFX en MVC2. Votre Ihm secondaire doit juste hériter de cette classe et implémenter les méthodes requises.">
    private static Stage stage;     // Pour stocker le stage courant. Nécessairement static car il ne peut y en avoir qu'un, malgré les 2 instances créées par JavaFX

    protected BaseIhm() {   // Protected de manière à ce que seulement JavaFX pourra l'utiliser, pas de création manuelle directe possible
//         System.out.println( "Objet BaseIhm() créé : " + this );
    }

    // Pour créer et afficher le contenu de cette ihm, une fois l'objet créé
    public void createAndShowStage() {
        // Créer le nouveau stage JavaFX
        stage = new Stage();

        try {
            // Création de l'Ihm à l'aide de FXML et son éditeur
            createStage( stage );
        }
        catch ( IOException ex ) {
            Logger.getLogger( BaseIhm.class.getName() ).log( Level.SEVERE, null, ex );
        }

        // Afficher notre scène JavaFX
        stage.show();
    }

    // Prévue pour créer le stage et le configurer.
    // Surcharger si un autre comportement est souhaité.
    protected void createStage( Stage stage ) throws IOException {

        FXMLLoader loader = new FXMLLoader( getStageFXMLNameURL() );

        // Indiquer que le contrôleur JavaFX de cette Ihm c'est ce même objet (ATTENTION : il est impératif que l'attribut XML fx:controller="xxx.xxx" ait été supprimé du fichier FXML, sinon boom, exception !)
        // Si on est perdu avec le fichier FXML et qu'on ne sait plus quels composants et méthodes @FXML doivent être importés dans le contrôleur, 
        // il suffit de faire clic droit sur le fichier FXML->"Make Controller" et récupérer les signatures dans le contrôleur créé. Mais dans 
        // ce cas il faudra ensuite supprimer l'attribut fx:controller="xxx.xxx" automatiquement rajouté dans le FXML, ainsi que la classe contrôleur temporairement créée.
        //loader.setController( this );
        // Charger la scène javaFX
        stage.setScene( new Scene( loader.load() ) );

        // Configurer la fenêtre/stage
        stage.setTitle( getStageTitle() );
    }

    @FXML
    // Ne pas surcharger pour modifier ce comportement par défaut.
    // Plutôt que d'utiliser initialize() pour préparer l'ihm, utiliser etatIhm_1_ChargeeEtInvisible() appelée et prévue pour faire la même chose.
    protected void initialize() {

        // JavaFX va créer une seconde instance de cette classe pour créer le contrôleur lui-même (à cause de l'attribut fx:controller).
        //
        // N.B. Cela pourrait être évité en supprimant cet attribut et en faisant un loader.setController(this); juste avant le chargement de la scène, mais avec l'inconvénient majeur
        // qu'il faudra entretenir manuellement les attributs @FXML dans le contrôleur (donc dans la classe fille) suite aux changements apportés dans le fichier FXML avec l'éditeur
        // intégré. Pas très pratique...
        //
        // La conséquence c'est que, pour être sûr d'être sur le bon objet/thread c'est ici que ça se passe, JavaFX appelle cette méthode du contrôleur pour l'initialiser.
        // C'est donc d'ici qu'on va appeler ces notifications fort utiles.
        //
        // Action lors de la fermeture de ce stage JavaFx
        stage.setOnCloseRequest( (WindowEvent event) -> {
            // Informer que notre ihm va sous peu disparaître
            etatIhm_4_VaQuitter();
        } );
        // Autres actions lors du changement d'état de la fenêtre/stage JavaFx
        stage.setOnShowing( (WindowEvent event) -> {
            // Informer que notre ihm va sous peu apparaître
            etatIhm_2a_BientotVisible();
        } );
        stage.setOnShown( (WindowEvent event) -> {
            // Informer que notre ihm est visible
            etatIhm_2b_Visible();
        } );
        stage.setOnHiding( (WindowEvent event) -> {
            // Informer que notre ihm va sous peu disparaître
            etatIhm_3a_BientotInvisible();
        } );
        stage.setOnHidden( (WindowEvent event) -> {
            // Informer que notre ihm a disparu
            etatIhm_3b_Invisible();
        } );

        // Actuellement elle est créée mais tout juste pas encore visible...
        etatIhm_1_ChargeeEtInvisible();
    }

    // Appelée pour informer que l'ihm sera bientôt visible...
    // Permet d'initialiser les composants, ... de manière à ce que la fenêtre ait le contenu souhaité une fois visible.
    protected abstract void etatIhm_1_ChargeeEtInvisible();

    // Appelée pour informer que notre ihm va sous peu être visible.
    // Surcharger pour en tirer profit.
    protected void etatIhm_2a_BientotVisible() {
    }

    // Appelée pour informer que notre ihm est désormais visible.
    // Surcharger pour en tirer profit.
    protected void etatIhm_2b_Visible() {
    }

    // Appelée pour informer que notre ihm va sous peu disparaître.
    // Surcharger pour en tirer profit.
    protected void etatIhm_3a_BientotInvisible() {
    }

    // Appelée pour informer que notre ihm a désormais disparu.
    // Surcharger pour en tirer profit.
    protected void etatIhm_3b_Invisible() {
    }

    // Appelée pour informer que cet ihm va être refermée sous peu.
    // Permet de faire ce qui doit être fait juste avant que cette ihm ne soit refermée, par exemple sauvegarder des états si nécessaire
    protected abstract void etatIhm_4_VaQuitter();

    // Prévue pour obtenir le fichier FXML a utiliser pour ce stage
    // Doit être surchargée.
    protected abstract URL getStageFXMLNameURL();

    // Prévue pour obtenir le titre de la fenêtre principale
    // Doit être surchargée.
    protected abstract String getStageTitle();

    public Ihm getRefIhm() {
        return ( Ihm ) BaseIhmPrincipale.getMainIhmInstance();
    }

    protected Stage getStage() {
        return stage;
    }

    // </editor-fold>
}
