package app;

import ctrl.Ctrl;
import ihm.Ihm;
import wrk.Wrk;

/**
 * @author Stella Alexis
 */
public class Application {

    public static void main(String[] args) {
        Ctrl ctrl = new Ctrl();
        Ihm ihm = new Ihm();
        Wrk wrk = new Wrk();
        ctrl.setRefIhm(ihm);
        ctrl.setRefWrk(wrk);
        wrk.setRefCtrl(ctrl);
        ihm.setRefCtrl(ctrl);
        ctrl.start();
    }
}
