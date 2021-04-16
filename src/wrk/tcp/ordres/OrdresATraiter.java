package wrk.tcp.ordres;

import java.util.ArrayList;

/**
 * Cette classe renferme la classe singleton OrdresATraiter de l'application.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public class OrdresATraiter {

    /**
     * Liste des ordres à traiter
     */
    private final ArrayList<String> ordres;

    /**
     * Constructeur du singleton OrdresATraiter. Il initialise la liste d'ordres
     * à traiter.
     */
    private OrdresATraiter() {
        ordres = new ArrayList<>();
    }

    /**
     * Cette méthode permet d'ajouter un nouvel ordre dans la liste d'ordres à
     * ex�cuter.
     *
     * @param ordre repr�sente l'ordre sérialisé envoyé par le client TCP.
     */
    public void ajouterOrdre(String ordre) {
        synchronized (ordres) {
            ordres.add(ordre);
        }
    }

    /**
     * Cette méthode permet de recevoir le prochain ordre de la liste d'ordres à
     * traiter.
     *
     * @return le prochain ordre de la liste d'ordres à traitre.
     */
    public String prochainOrdreATraiter() {
        String ordre = null;
        synchronized (ordres) {
            if (!ordres.isEmpty()) {
                ordre = ordres.remove(0);
            }
        }
        return ordre;
    }

    /**
     * Cette méthode permet de vider la liste d'ordres à traiter.
     */
    public void viderLaListeDesOrdres() {
        synchronized (ordres) {
            ordres.clear();
        }
    }

    /**
     * Getter de l'instance de la classe OrdresATraiter.
     *
     * @return l'instance de la classe OrdresATraiter.
     */
    public static OrdresATraiter getInstance() {
        return OrdresATraiterHolder.INSTANCE;
    }

    /**
     *
     */
    private static class OrdresATraiterHolder {

        private static final OrdresATraiter INSTANCE = new OrdresATraiter();
    }
}
