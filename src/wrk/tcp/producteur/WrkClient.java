package wrk.tcp.producteur;

import app.Constante;
import beans.PacketTCP;
import enumeration.LogLevel;
import helpers.Utils;
import wrk.Wrk;
import wrk.tcp.WrkServer;
import wrk.tcp.consommateur.WrkConsommateur;
import wrk.tcp.ordres.OrdresATraiter;

import java.io.*;
import java.net.Socket;

/**
 * Cette classe renferme le worker "WrkClient" de l'application.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public class WrkClient extends Thread implements Constante {

    /**
     * Instance de la classe Socket.
     */
    private volatile Socket socket;
    /**
     * Variable running de la classe WrkClient. Permet de définir l'état du
     * thread. True -> allumé | False -> arrêté
     */
    private volatile boolean running;
    /**
     * Instance de la classe BufferedReader
     */
    private volatile BufferedReader in;
    /**
     * Instance de la classe BufferedWriter
     */
    private volatile BufferedWriter out;
    /**
     * Instance de la classe Wrk
     */
    private final Wrk refWrk;
    /**
     * Instance de la classe WrkServer
     */
    private volatile WrkServer wrkServer;
    /**
     * Instance de la classe WrkConsommateur
     */
    private WrkConsommateur wrkConsommateur;

    /**
     * Constructeur de la classe WrkClient. Il défini les instance des classes
     * Socket, WrkServeur, Wrk et WrkConsommateur.
     *
     * @param name représente le nom que doit avoir le thread de la classe
     * WrkClient.
     * @param socket représente le socket serveur de l'application.
     * @param wrkServer représente l'instance du worker WrkServeur.
     * @param refWrk représente l'instance du worker principal.
     */
    public WrkClient(String name, Socket socket, WrkServer wrkServer, Wrk refWrk) {
        super(name);
        this.refWrk = refWrk;
        this.socket = socket;
        this.wrkServer = wrkServer;
        wrkConsommateur = new WrkConsommateur("Consommateur d'ordres client  (" + socket.getInetAddress().getHostAddress() + ")", refWrk);

    }

    /**
     * Cette méthode permet d'inspecter le tunnel entre le serveur et le client.
     * Lorsqu'elle reçoit des paquets du client, elle demande au singleton
     * OrdresATraiter d'ajouter un nouvel ordre. Si un problème survient lors de
     * l'exécution de la méthode, elle enverra un message d'erreur
     */
    @Override
    public void run() {
        running = true;
        wrkConsommateur.start();
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (running) {
                String ordre = in.readLine();
                if (ordre != null) {
                    OrdresATraiter.getInstance().ajouterOrdre(ordre);
                } else {
                    running = false;
                    refWrk.envoieLog(LogLevel.INFO, "Déconnexion du client (" + socket.getInetAddress().getHostAddress() + ")");
                }
                attendre(SLEEP);
            }

            wrkConsommateur.setRunning(false);
            try {
                wrkConsommateur.join();
                wrkConsommateur = null;
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            wrkServer.removeClient(this);
            // wrkServer.removeClient(this);
            socket.close();
            System.gc();
        } catch (IOException ex) {
            //System.out.println("ptdr");
            // wrkServer.envoieErreur(ex.getMessage());
        }

    }

    /**
     * Cette méthode permet de faire attendre le thread pendant un certain
     * temps. Ce temps est un paramètre qui est défini en milliseconde.
     *
     * @param ms représente le nombre de milliseconde à faire attendre le
     * thread.
     */
    public void attendre(long ms) {
        try {
            sleep(ms);
        } catch (InterruptedException ex) {
            refWrk.envoieLog(LogLevel.ERROR, ex.getMessage());
        }
    }

    /**
     * Cette méthode permet d'envoyer un paquet au client. Elle demande en
     * paramètre l'objet PacketTCP.
     *
     * @param pkt représente l'objet PacketTCP contenant un ordre et les
     * informations à faire transmettre au client.
     */
    public void envoiePaquet(PacketTCP pkt) {
        try {
            if (pkt != null) {
                String packetString = Utils.packetTCPSerialisation(pkt);
                out.write(packetString + System.getProperty("line.separator"));
                out.flush();
            }
        } catch (Exception ex) {
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
