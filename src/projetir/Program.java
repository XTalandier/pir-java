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
	// Jalon 13: Nombre d'instances
	public int nbInstances = 0;
	// Jalon 13: Tableau de clients
	public Client[] lesClients;
	public boolean envoye = false;
	public Program(int numeroProgramme , int lePortEcoute , int lePortEnvoit , int numeroCopain){
		portEcoute = lePortEcoute;
		portEnvoie = lePortEnvoit;
		numProg    = numeroProgramme;
		numProgAEnvoyer = numeroCopain;
		
	}
	
	public void recoitMessage(Message msg){
		// Synchronise le lamport
		lamport.synchronize(Math.max(msg.getEstampille(), lamport.getTime()) + 1);
		// Affiche le message recu
		
		System.out.println(numProg + " recoit : " + msg.getData());
		
		/*
		if(!envoye){
			envoyerAuxClients(numProg + "");
			envoye = true;
		}*/
		
		int Action = Maths.getAction();
		
		if (Action == 0){
			//Action message
			int numDestinataire = Maths.getRandom();
			envoyerAUnClient(numDestinataire, msg.getData());
		}
		else {
			// Diffuse message
			envoyerAuxClients(numProg + "");
		}
	}

	@Override
	public void run() {
		try {
			// Clients SVG
			clientSVG = new Client(2999);
			new Thread(clientSVG).start();
			// Serveur
			System.out.println("Je suis le #" + numProg);
			leServeur = new Thread(new Server(portEcoute, this));
			leServeur.start();
			// Clients
			lesClients = new Client[nbInstances];
			int nbFoo = 0;
			for(int i = 1 ; i <= nbInstances ; i++){
				if(i != numProg){
					lesClients[i - 1] = new Client(3000 + i - 1);
					new Thread(lesClients[i - 1]).start();
				}
			}
			if(numProg == 1){
				Thread.sleep(2000);
				envoyerAuxClients(numProg + "");
				Thread.sleep(1000);
				clientSVG.envoyerMessage(new Message("EXIT", 0), lamport);
			}
		} catch (IOException ex) {
			Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void envoyerAuxClients(String msg){
		for(int i = 0 ; i < ProjetIR.grandN ; i++){
			if(lesClients[i] != null){
				String toSVG = "REQ," + numProg + "," + (i + 1) + "," + lamport.getTime() + "," + (lamport.getTime() + 1);
				clientSVG.envoyerMessage(new Message( toSVG  , 0), new Horloge());
				lamport.tick();
				lesClients[i].envoyerMessage(new Message(msg, lamport.getTime()), lamport);
			}
		}
	}
	
	public void envoyerAUnClient(int idClient, String msg){
		lamport.tick();
		if(lesClients[idClient]!=null){
			String toSVG = "REQ," + numProg + "," + (idClient + 1) + "," + lamport.getTime() + "," + (lamport.getTime() + 1);
			clientSVG.envoyerMessage(new Message( toSVG  , 0), new Horloge());
			lesClients[idClient].envoyerMessage(new Message(msg, lamport.getTime()), lamport);
		}
	}
}
