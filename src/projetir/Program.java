/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetir;

import common.Horloge;
import common.Maths;
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
	private int numProgAEnvoyer;
	Thread leServeur;
	Thread leClient;
	Client client;
	Horloge lamport = new Horloge();
	Client clientSVG;
	// Jalon 12: mon numero initialisé à -1
	public int monNumero = -1;
	
	public Program(int numeroProgramme , int lePortEcoute , int lePortEnvoit , int numeroCopain){
		portEcoute = lePortEcoute;
		portEnvoie = lePortEnvoit;
		numProg    = numeroProgramme;
		numProgAEnvoyer = numeroCopain;
	}
	
	public void recoitMessage(Message msg){
		// Synchronise le lamport
		lamport.synchronize(Math.max(msg.getEstampille(), lamport.getTime()) + 1);
		// Genere un numero si pas encore tiré
		String delims = "[,]";
		String[] tokens = msg.getData().split(delims);
		int idMachine   = Integer.parseInt(tokens[0]);
		String numElection = tokens[1];
		// Si gagnant, fait passer le message
		if(numElection.equals("win")){
			client.envoyerMessage(new Message(msg.getData(), lamport.getTime()), lamport);
		// Envoie qu'il a gagné
		}else if(idMachine == numProg){
			client.envoyerMessage(new Message(numProg + ",win", lamport.getTime()), lamport);
		}else{
			monNumero = Maths.getRandom();
			if(monNumero > Integer.parseInt(numElection)){
				client.envoyerMessage(new Message(numProg + "," + monNumero, lamport.getTime()), lamport);
			}else{
				client.envoyerMessage(new Message(msg.getData(), lamport.getTime()), lamport);
			}
		}
		/*
		if(msg.getEstampille() > 10){
			clientSVG.envoyerMessage(new Message("EXIT", lamport.getTime()) , lamport);
		}else {
			clientSVG.envoyerMessage(new Message("REQ," + numProg + "," + numProgAEnvoyer + "," + lamport.getTime() + "," + (lamport.getTime() + 1), lamport.getTime()) , lamport);
			// On prend le plus grand et on lui ajoute 1
			// On étudie le message
			System.out.println(numProg + ": recoitMessage : " + msg.getData());
			client.envoyerMessage(new Message(msg.getData().equals("ping") ? "pong" : "ping" , lamport.getTime()), lamport);
		}
		*/
	}

	@Override
	public void run() {
		try {
			// Client SVG
			clientSVG = new Client(2999);
			new Thread(clientSVG).start();
			// Serveur
			System.out.println("Je suis le #" + numProg);
			leServeur = new Thread(new Server(portEcoute, this));
			leServeur.start();
			client = new Client(portEnvoie);
			leClient = new Thread(client);
			leClient.start();
			if(numProg == 1){
				Thread.sleep(2000);
				client.envoyerMessage(new Message(numProg + "," + Maths.getRandom() , lamport.getTime()) , lamport);
			}
		} catch (IOException ex) {
			Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
