package enumeration;

/**
 *
 */
public enum Ordres {
    BOUGER_ROBOT,               // [VITESSE DROITE, VITESSE GAUCHE]
    AJOUT_PHOTO,                // [PHOTO]
    PHOTOS,
    ARRET_ROBOT,
    DEMARRAGE_ROBOT,
    CONNEXION_UTILISATEUR,      // [LOGIN, PASSWORD]
    AJOUT_UTILISATEUR,          // [LOGIN, PASSWORD]
    ERROR,                      // [MSG]
    SUCCESS,                    // [MSG]
    INFORMATION,                // [MSG]
    UNDOCK,
    DOCK,
    INFOS_ROBOT                 // [INFORMATIONS ROBOT]
}
