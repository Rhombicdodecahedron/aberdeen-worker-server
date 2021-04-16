package helpers;

public class SystemLib {

    /**
     * Classe de méthodes statiques SystemLib
     *
     * @author Jean-Claude Stritt & Pierre-Alain Mettraux
     */
    public static String getCurrentMethod() {
        StackTraceElement e[] = Thread.currentThread().getStackTrace();
        StackTraceElement trace = e[3];
        return trace.getMethodName();
    }

    public static String getTestCurrentMethod() {
        return "*** " + getCurrentMethod() + "...";
    }

    public static String getFullMethodName() {
        StackTraceElement e[] = Thread.currentThread().getStackTrace();
        StackTraceElement trace = e[2];
        return trace.getClassName() + "." + trace.getMethodName() + "\n";
    }
}
