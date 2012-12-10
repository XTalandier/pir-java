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
import sun.awt.windows.ThemeReader;
/**
 *
 * @author Xavier TALANDIER <xavier@talandier.fr>
 */
public class Program implements Runnable{
	
	private int portEcoute;
	private int portEnvoie;
	private int numProg;
	Thread leServeur;
	Thread leClient;
	Client client;
	public Program(int numeroProgramme , int lePortEcoute , int lePortEnvoit){
		portEcoute = lePortEcoute;
		portEnvoie = lePortEnvoit;
		numProg = numeroProgramme;
	}
	
	public void recoitMessage(Message msg){
		System.out.println("recoitMessage : " + msg.getData());
	}

	@Override
	public void run() {
		try {
			System.out.println("Je suis le #" + numProg);
			leServeur = new Thread(new Server(portEcoute, this));
			leServeur.start();
			client = new Client(portEnvoie);
			leClient = new Thread(client);
			leClient.start();
			if(numProg == 1){
				Thread.sleep(2000);
				client.envoyerMessage("Waza !!");
			}
		} catch (IOException ex) {
			Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
