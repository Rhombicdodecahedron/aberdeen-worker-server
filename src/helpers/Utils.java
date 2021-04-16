package helpers;

import beans.PacketTCP;

import java.io.*;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Classe de méthodes statiques permettant la sérialisation et désérialisation
 * d'objet, compression et dépression de tableau de byte.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public class Utils {

    /**
     * Constructeur de la class "Utils"
     */
    public Utils() {
    }

    /**
     * Cette méthode permet de sérialiser l'objet PacketTCP à envoyer au client.
     * Cela permet de faire passer le paquet tcp à travers le tunnel tcp tout en
     * le rendant plus léger.
     *
     * @param packetTCP représente l'objet PacketTCP à envoyer au client.
     * @return un String de la sérialisation de l'objet PacketTCP.
     */
    public static String packetTCPSerialisation(PacketTCP packetTCP) {
        String result = null;
        if (packetTCP != null) {
            ObjectOutputStream oos = null;
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(packetTCP);
                result = Base64.getEncoder().encodeToString(baos.toByteArray());
            } catch (IllegalArgumentException | IOException ignored) {
            } finally {
                try {
                    if (oos != null) {
                        oos.close();
                    }
                } catch (IOException e) {
                }
            }

        }
        return result;
    }

    /**
     * Cette méthode permet de désérialiser en objet PacketTCP la sérialisation
     * envoyé par le client tcp.
     *
     * @param serialisation représente la sérialisation envoyé par le client.
     * @return un objet PacketTCP représentant la désérialisation du String.
     */
    public static PacketTCP stringDeserialisation(String serialisation) {
        PacketTCP packetTCP = null;
        if (serialisation != null) {
            ObjectInputStream ois = null;
            try {
                byte[] data = Base64.getDecoder().decode(serialisation);
                ois = new ObjectInputStream(new ByteArrayInputStream(data));
                packetTCP = (PacketTCP) ois.readObject();
            } catch (IllegalArgumentException | IOException | ClassNotFoundException ignored) {

            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                }
            }
        }
        return packetTCP;
    }

    /**
     * Cette méthode permet de connaître l'adresse ip d'un host en fonction de
     * son adresse mac.
     *
     * @param mac représente l'adresse mac de l'host que nous souhaitons
     * connaître l'adresse ip.
     * @return un String de l'adresse ip de l'host.
     * @throws IOException représente une erreur que la méthode rencontre.
     */
    public static String rechercheIPViaMac(String mac) throws IOException {
        String result = null;
        Process proc = Runtime.getRuntime().exec("arp -a ");
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String s = null;
        String ip = null;
        if (mac != null) {
            while ((s = stdInput.readLine()) != null) {
                if (s.contains(mac)) {
                    ip = s.trim().replaceAll(" +", " ").split("\\s+")[0];
                    break;
                }
            }
            if (ip != null) {
                result = ip;
            } else {
                throw new IOException("Aucun robot n'a été détecté sur le réseau");
            }
        } else {
            throw new IOException("Veuillez mettre une adresse mac non-null");
        }

        return result;
    }

    /**
     * Cette méthode permet de décompresser un tableau de byte qui était
     * préalablement compressé pour prendre moins de place dans le tunnel tcp.
     *
     * @param bytes représente un tableau de byte compressé.
     * @return un tableau de byte decompressé.
     */
    public static byte[] decompressByteArray(byte[] bytes) {
        byte[] result = null;
        ByteArrayOutputStream baos = null;
        if (bytes != null) {
            Inflater iflr = new Inflater();
            iflr.setInput(bytes);
            baos = new ByteArrayOutputStream();
            byte[] tmp = new byte[32];
            try {
                while (!iflr.finished()) {
                    int size = iflr.inflate(tmp);
                    baos.write(tmp, 0, size);
                    result = baos.toByteArray();
                }
            } catch (DataFormatException e) {

            } finally {
                try {
                    if (baos != null) {
                        baos.close();
                    }
                } catch (IOException e) {

                }
            }
        }
        return result;
    }

    /**
     * Cette méthode permet de compresser un tableau de byte pour réduire sa
     * taille dans le tunnel tcp.
     *
     * @param bytes représente un tableau de byte décompressé.
     * @return un tableau de byte compréssé.
     */
    public static byte[] compressByteArray(byte[] bytes) {
        byte[] result = null;
        ByteArrayOutputStream baos = null;
        if (bytes != null) {
            Deflater dfl = new Deflater();
            dfl.setLevel(Deflater.BEST_COMPRESSION);
            dfl.setInput(bytes);
            dfl.finish();
            baos = new ByteArrayOutputStream();
            byte[] tmp = new byte[32];
            try {
                while (!dfl.finished()) {
                    int size = dfl.deflate(tmp);
                    baos.write(tmp, 0, size);
                    result = baos.toByteArray();
                }
            } catch (Exception e) {
            } finally {
                try {
                    if (baos != null) {
                        baos.close();
                    }
                } catch (IOException e) {
                }
            }
        }

        return result;
    }

}
