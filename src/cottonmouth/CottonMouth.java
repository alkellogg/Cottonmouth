/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cottonmouth;

import environment.ApplicationStarter;

/**
 *
 * @author audreykellogg
 */
public class CottonMouth {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ApplicationStarter.run("CottonMouth Rules!", new CottonMouthEnvironment());
    }
    
}
