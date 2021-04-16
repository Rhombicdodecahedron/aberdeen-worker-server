package wrk.tcp.consommateur;

import app.Constante;
import enumeration.LogLevel;
import wrk.Wrk;
import wrk.tcp.ordres.OrdresATraiter;

/**
 * Cette classe renferme le worker "WrkConsommateur" de l'application.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public class WrkConsommateur extends Thread implements Constante {

    /**
     * Instance de la classe Wrk
     */
    private final Wrk refWrk;
    /**
     * Variable running de la classe WrkConsommateur. Permet de définir l'état
     * du thread. True -> allumé | False -> arrêté
     */
    private volatile boolean running;

    /**
     * Constructeur de la classe worker "WrkConsommateur".Il initialise le
     * worker principal et donne un nom au thread de la classe
     * "WrkConsommateur".
     *
     * @param name représente le nom que doit prendre le thread.
     * @param refWrk représente l'instance du worker principal.
     */
    public WrkConsommateur(String name, Wrk refWrk) {
        super(name);
        this.refWrk = refWrk;
    }

    /**
     * Cette méthode permet de consommer tous les ordres à exécuter ce trouvant
     * de la liste d'ordres du singleton OrdresATraiter.
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            // Consommer tous les ordres de travail a exécuter
            String ordre = null;
            do {
                ordre = OrdresATraiter.getInstance().prochainOrdreATraiter();
                if (ordre != null) {
                    refWrk.ordreToExecutor(ordre);
                }
            } while (ordre != null);

            attendre(SLEEP);
        }
    }

    /**
     * Cette méthode permet de faire attendre le thread pendant le nombre de
     * milliseconde reçu en paramètre.
     *
     * @param millis représente le temps que doit attendre le thread.
     */
    public void attendre(int millis) {
        try {
            sleep(millis);
        } catch (InterruptedException ex) {
            refWrk.envoieLog(LogLevel.ERROR, ex.getMessage());
        }
    }

    /**
     * Setter de la variable running.
     *
     * @param running représente l'état du thread.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

}
