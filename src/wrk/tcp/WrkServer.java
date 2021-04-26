package wrk.tcp;

import app.Constante;
import beans.PacketTCP;
import ch.emf.info.robot.links.bean.RobotState;
import enumeration.Ordres;
import helpers.Utils;
import wrk.Wrk;
import enumeration.LogLevel;
import wrk.tcp.ordres.OrdresATraiter;
import wrk.tcp.producteur.WrkClient;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * Cette classe renferme le worker "WrkServer" de l'application.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public class WrkServer extends Thread implements Constante {

    /**
     * Instance de la classe ServerSocket
     */
    private volatile ServerSocket serverSocket;
    /**
     * Variable running de la classe WrkServeur. Permet de définir l'état du
     * thread. True -> allumé | False -> arrêté
     */
    private volatile boolean running;
    /**
     * Liste des clients connectés au serveur tcp.
     */
    private volatile ArrayList<WrkClient> clients;
    /**
     * Instance de la classe Wrk
     */
    private final Wrk refWrk;

    /**
     * Constructeur de la classe worker "WrkServer". Il défini l'instance
     * initialise du worker principal, crée la liste des clients et donne un nom
     * au thread de la classe "WrkRobot".
     *
     * @param refWrk représente l'instance du worker principal.
     */
    public WrkServer(Wrk refWrk) {
        this.refWrk = refWrk;
        clients = new ArrayList<>();
        setName("Thread Serveur TCP");
    }

    /**
     * Cette méthode permet de demarrer le serveur tcp sur le port donné en
     * paramètre. En cas de problème, elle throw une Exception
     *
     * @param port représente le port du serveur tcp.
     * @throws Exception représente une erreur que le serveur rencontre.
     */
    public void demarrerServeur(int port) throws Exception {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(TIMEOUT);
            refWrk.envoieLog(LogLevel.SUCCESS, "Le serveur TCP a correctement été lancé sur le port : " + port);
        } catch (Exception exc) {
            throw new Exception("Erreur dans le démarrage du serveur.");
        }

    }

    /**
     * Cette méthode permet d'arrêter le serveur tcp. En cas de problème, elle
     * throw une Exception.
     *
     * @throws Exception représente une erreur que le serveur rencontre.
     */
    public void arreterServeur() throws Exception {
        try {
            serverSocket.close();
            OrdresATraiter.getInstance().viderLaListeDesOrdres();
            refWrk.envoieLog(LogLevel.SUCCESS, "Le serveur TCP a correctement été arrêté");
        } catch (Exception ex) {
            throw new Exception("Erreur dans l'arrêt du serveur.");
        }

    }

    /**
     * Cette méthode permet d'inspecter le socket tcp. Lorsqu'elle détecte une
     * nouvelle connexion d'un client, elle va alors créer un thread avec les
     * informations de son socket tout en donnant l'instance du worker
     * principale et de WrkServeur. En cas d'erreur, une message est envoyé
     * sinon un message de connexion informant qu'un client est connecté est
     * alors envoyé. Bite
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                synchronized (serverSocket) {
                    Socket socketClient = serverSocket.accept();
                    WrkClient client = new WrkClient("Client (" + socketClient.getInetAddress().getHostAddress() + ")", socketClient, this, refWrk);
                    clients.add(client);
                    client.start();
                    refWrk.envoieLog(LogLevel.INFO, "Client connecté (" + socketClient.getInetAddress().getHostAddress() + ")");
                    attendre(SLEEP);
                }
            } catch (SocketTimeoutException ex) {
                // rien car timeout
            } catch (IOException exc) {
               // refWrk.envoieLog(LogLevel.ERROR, "Erreur dans la connexion d'un client.");
            }
        }
        if (!clients.isEmpty()) {
            for (WrkClient client : clients) {
                if (client != null) {
                    client.setRunning(false);
                    try {
                        client.join();
                        client = null;
                    } catch (InterruptedException ex) {

                    }
                }
            }
        }

        System.gc();
    }

    /**
     * Cette méthode permet de faire attendre le thread pendant le nombre de
     * milliseconde reçu en paramètre.
     *
     * @param ms représente le temps que doit attendre le thread.
     */
    public void attendre(long ms) {
        try {
            sleep(ms);
        } catch (InterruptedException ex) {
            //envoieErreur(ex.getMessage());
        }
    }

    /**
     * Cette méthode permet de supprimer l'objet client TCP de la liste de tous
     * les clients connectés. Pour ce faire, elle va tester si le client à
     * enlever n'est pas null et
     *
     * @param client représente l'objet client à supprimer de la liste des
     * clients connectés au serveur tcp.
     */
    public void removeClient(WrkClient client) {
        if (client != null) {
            if (clients != null) {
                clients.remove(client);
            }
        }
    }

    /**
     * Cette méthode permet d'envoyer un paquet tcp contenant un ordre et des
     * informations à tous les clients connectés au serveur tcp.
     *
     * @param pkt représente l'object PacketTCP contenant un ordre et les
     * informations à envoyer aux clients.
     */
    public void envoiePaquetTCP(PacketTCP pkt) {
        if (!clients.isEmpty()) {
            if (pkt != null) {
                for (WrkClient client : clients) {
                    if (client != null) {
                        client.envoiePaquet(pkt);
                    }
                }
            }
        }
    }

    /**
     * Cette méthode permet d'envoyer un message avec un ordre spécifique à tous
     * les clients connectés au serveur tcp.
     *
     * @param ordre représente l'ordre à envoyer aux clients.
     * @param msg représente le message à envoyer aux clients.
     */
    public void envoieMessageClient(Ordres ordre, String msg) {
        if (!clients.isEmpty()) {
            if (ordre != null && msg != null) {
                PacketTCP pkt = new PacketTCP();
                pkt.setOrdre(ordre);
                pkt.setTexte(msg);
                envoiePaquetTCP(pkt);
            }
        }
    }

    /**
     * Cette méthode permet d'envoyer l'état du robot à tous les utilisateurs
     * connectés au serveur tcp. Elle demande en paramètre l'objet de l'état du
     * robot.
     *
     * @param robotState représente l'objet de l'état du robot contenant l'id,
     * la vitesse des chenilles, la dernière image, etc...
     */
    public void envoieInfosRobot(RobotState robotState) {
        if (!clients.isEmpty()) {
            if (robotState != null) {
                PacketTCP pkt = new PacketTCP();
                byte[] newImage = Utils.compressByteArray(robotState.getImage());
                pkt.setOrdre(Ordres.INFOS_ROBOT);
                pkt.addParams("name", robotState.getName());
                pkt.addParams("id", robotState.getId());
                pkt.addParams("pw", robotState.getPw());
                pkt.addParams("battery", robotState.getBattery());
                pkt.addParams("audio", robotState.getAudio());
                pkt.addParams("image", newImage);
                pkt.addParams("left_speed", robotState.getLeftSpeed());
                pkt.addParams("right_speed", robotState.getRightSpeed());
                envoiePaquetTCP(pkt);
            }
        }
    }

    /**
     * Cette méthode permet de retourner l'adresse ip du serveur tcp. Si aucune
     * adresse ip n'est trouvée, la méthode renverra null.
     *
     * @return l'adresse ip du serveur tcp ou null si aucune n'est trouvée.
     */
    public String getIPServeur() {
        String ip = null;
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("google.com", 80));
            ip = socket.getLocalAddress().getHostAddress();
        } catch (IOException e) {
            refWrk.envoieLog(LogLevel.ERROR, e.getMessage());
        }
        return ip;
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
