package wrk.log;

import helpers.DateTimeLib;
import enumeration.LogLevel;

/**
 * Cette classe renferme le worker "WrkLog" de l'application.
 *
 * @author Stella Alexis
 * @version 1.0
 * @since 15.01.2021
 */
public class WrkLog {

    /**
     * Constructeur de la classe "WrkLog".
     */
    public WrkLog() {
    }

    /**
     * Cette méthode génère un log contenant un niveau, une date et un message
     * ou null si le message ou l'ordre passés en paramètre sont null.
     *
     * @param level représente le niveau du log défini dans l'énumération
     * "LogLevel" (WARNING, INFO, ERROR, SUCCESS)
     * @param message représente message du log. Cela peut représenter un
     * message d'erreur ou de succès.
     * @return un String contenant le message de log, la date et le niveau de
     * log. (ex.: [SUCCESS] 01.01.2021 : message ) ou null en cas d'erreur
     */
    public String log(LogLevel level, String message) {
        String result = null;
        if (message != null) {
            if (level != null) {
                String date = DateTimeLib.dateToString(DateTimeLib.getNow());
                switch (level) {
                    case ERROR:
                        result = "[ERROR] " + date + " : " + message;
                        break;
                    case WARNING:
                        result = "[WARNING] " + date + " : " + message;
                        break;
                    case INFO:
                        result = "[INFO] " + date + " : " + message;
                        break;
                    case SUCCESS:
                        result = "[SUCCESS] " + date + " : " + message;
                        break;
                }
            }
        }
        return result;
    }
}
