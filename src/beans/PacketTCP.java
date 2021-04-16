package beans;

import enumeration.Ordres;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class PacketTCP implements Serializable {

    private Ordres ordre;
    private Object obj;
    private String texte;
    private final HashMap<String, Object> params;
    private final String packetUUID = UUID.randomUUID().toString();

    public PacketTCP() {
        params = new HashMap<>();
    }

    public PacketTCP(Ordres ordre, Object obj, String texte, HashMap<String, Object> params) {
        this.ordre = ordre;
        this.obj = obj;
        this.texte = texte;
        this.params = params;
    }

    public Ordres getOrdre() {
        return ordre;
    }

    public void setOrdre(Ordres ordre) {
        this.ordre = ordre;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public void addParams(String key, Object param) {
        if (param != null) {
            params.put(key, param);
        }
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public String getPacketUUID() {
        return packetUUID;
    }
}
