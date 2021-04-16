package wrk.tcp.dispatcher;

import beans.PacketTCP;
import enumeration.LogLevel;
import exceptions.MyDBException;
import helpers.Utils;
import wrk.Wrk;
import enumeration.Ordres;

/**
 * Cette classe renferme le worker "WrkDispatcher" de l'application.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public class WrkDispatcher {

    /**
     * Instance de la classe Wrk
     */
    private final Wrk refWrk;

    /**
     * Constructeur de la classe WrkRobot de l'application. Ce constructeur
     * défini l'instance de Wrk.
     *
     * @param refWrk représente l'instance du worker principal.
     */
    public WrkDispatcher(Wrk refWrk) {
        this.refWrk = refWrk;
    }

    /**
     * Cette méthode permet de traiter un ordre sérialisé reçu par un client
     * tcp. Elle va de se fait désérialiser cet ordre pour correctement le
     * répartir dans l'application.
     *
     * @param ordreClient représente l'ordre sérialisé envoyé par le client TCP.
     */
    public void dispatchOrdre(String ordreClient) {
        if (ordreClient != null) {
            PacketTCP packetTCP = Utils.stringDeserialisation(ordreClient);
            if (packetTCP != null) {
                Ordres ordre = packetTCP.getOrdre();
                if (ordre != null) {
                    try {
                        PacketTCP pkt = new PacketTCP();
                        switch (ordre) {
                            case ARRET_ROBOT:
                                refWrk.deconnexionRobot();
                                break;
                            case DEMARRAGE_ROBOT:
                                refWrk.actionConnexionRobot();
                                break;
                            case DOCK:
                                refWrk.dockRobot();
                                break;
                            case UNDOCK:
                                refWrk.undockRobot();
                                break;
                            case AJOUT_UTILISATEUR:
                                pkt.setOrdre(Ordres.AJOUT_UTILISATEUR);
                                try {
                                    refWrk.ajouteUtilisateur((String) packetTCP.getParams().get("login"), (String) packetTCP.getParams().get("password"));
                                    pkt.addParams("ajoutOK", true);
                                } catch (MyDBException e) {
                                    pkt.addParams("ajoutOK", false);
                                }
                                refWrk.envoiePaquetClient(pkt);
                                break;
                            case BOUGER_ROBOT:
                                refWrk.bougerRobot((int) packetTCP.getParams().get("left_speed"), (int) packetTCP.getParams().get("right_speed"));
                                break;
                            case CONNEXION_UTILISATEUR:
                                try {
                                boolean ok = refWrk.connexionUilisateur((String) packetTCP.getParams().get("login"), (String) packetTCP.getParams().get("password"));
                                pkt.setOrdre(Ordres.CONNEXION_UTILISATEUR);
                                if (ok) {
                                    pkt.addParams("connexionOK", true);
                                } else {
                                    pkt.addParams("connexionOK", false);
                                }
                                refWrk.envoiePaquetClient(pkt);
                            } catch (MyDBException e) {
                                refWrk.envoieMessageClient(Ordres.ERROR, e.getMessage());
                            }
                            break;
                            case AJOUT_PHOTO:
                                try {
                                refWrk.ajouteImage((byte[]) packetTCP.getParams().get("image"));
                                refWrk.envoieMessageClient(Ordres.SUCCESS, "La photo a correctement été ajouté");
                            } catch (MyDBException e) {
                                refWrk.envoieMessageClient(Ordres.ERROR, e.getMessage());
                            }
                            break;
                            case PHOTOS:
                                try {
                                pkt.setOrdre(Ordres.PHOTOS);
                                pkt.addParams("photos", refWrk.listePhotos());
                                refWrk.envoiePaquetClient(pkt);
                            } catch (MyDBException e) {
                                refWrk.envoieMessageClient(Ordres.ERROR, e.getMessage());
                            }
                            break;
                            case INFORMATION:
                                refWrk.envoieLog(LogLevel.INFO, pkt.getTexte());
                                break;
                        }
                    } catch (Exception ex) {
                        refWrk.envoieLog(LogLevel.ERROR, ex.getMessage());
                        refWrk.envoieMessageClient(Ordres.ERROR, ex.getMessage());
                    }
                }
            }
        }

    }
}
