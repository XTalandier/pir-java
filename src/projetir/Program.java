/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetir;

import common.Message;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sockets.*;
/**
 *
 * @author Xavier TALANDIER <xavier@talandier.fr>
 */
public class Program implements Runnable{
	
	private int portEcoute;
	private int portEnvoie;
	private int numProg;
	Server leServeur;
	Client leClient;
	public Program(int numeroProgramme , int lePortEcoute , int lePortEnvoit){
		portEcoute = lePortEcoute;
		portEnvoie = lePortEnvoit;
		numProg = numeroProgramme;
	}
	
	public void recoitMessage(Message msg){
		System.out.println(msg.getData());
	}

	@Override
	public void run() {
		try {
			System.out.println("Je suis le #" + numProg);
			leServeur = new Server(portEcoute, this);
			leServeur.run();
		} catch (IOException ex) {
			Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
