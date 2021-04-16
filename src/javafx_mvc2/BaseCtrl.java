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

import ctrl.Ctrl;

public class BaseCtrl {

    // <editor-fold defaultstate="collapsed" desc="Ne pas toucher ! Ce code permet de facilement structurer un projet JavaFX en MVC2. Votre Ctrl doit juste hériter de cette classe.">
    private static Ctrl uniqueInstance;

    protected BaseCtrl() {  // protected pour interdire la création d'une instance mais permettre l'héritage
        if ( uniqueInstance != null ) {
            throw new AssertionError( "Seulement une seule instance de Ctrl/BaseCtrl autorisée !" );
        } else {
            uniqueInstance = (Ctrl)this;
        }
    }

    public static Ctrl getInstance() {
        return uniqueInstance;
    }
    // </editor-fold>

}
