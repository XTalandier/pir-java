/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetir;

import common.Horloge;
import common.Message;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.PortableInterceptor.HOLDING;
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
	private int numProgAEnvoyer;
	Thread leServeur;
	Thread leClient;
	Client client;
	Horloge lamport = new Horloge();

	public Program(int numeroProgramme , int lePortEcoute , int lePortEnvoit){
		portEcoute = lePortEcoute;
		portEnvoie = lePortEnvoit;
		numProg    = numeroProgramme;
	}
	
	public void recoitMessage(Message msg){
		// On prend le plus grand et on lui ajoute 1
		lamport.synchronize(Math.max(msg.getEstampille(), lamport.getTime()) + 1);
		// On étudie le message
		System.out.println(numProg + ": recoitMessage : " + msg.getData());
		client.envoyerMessage(new Message(msg.getData(), lamport.getTime()), lamport);
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
				client.envoyerMessage(new Message("Hello world !" , lamport.getTime()) , lamport);
			}
		} catch (IOException ex) {
			Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
