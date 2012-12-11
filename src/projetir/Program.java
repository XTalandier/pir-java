/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetir;

import common.FileDeRequetes;
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
	private FileDeRequetes fdr = new FileDeRequetes();
	
	// Jalon 17: Veut entrer en section critique
	private boolean jeVeuxEtreEnCritique = false;

	// Jalon 19: Nb de reply
	private int nbReply = 0;
	
	public Program(int numeroProgramme , int lePortEcoute , int lePortEnvoit , int numeroCopain){
		portEcoute = lePortEcoute;
		portEnvoie = lePortEnvoit;
		numProg    = numeroProgramme;
		numProgAEnvoyer = numeroCopain;
		
	}
	
	public void recoitMessage(Message msg){
		// Traite simplement les messages
		synchronized(this){
			fdr.addRequest(msg);
		}
		//diffuserMessageTelQuel(msg);
		/*
		// Synchronise le lamport
		lamport.synchronize(Math.max(msg.getEstampille(), lamport.getTime()) + 1);
		// Affiche le message recu
		
		System.out.println(numProg + " recoit : " + msg.getData());
		
		/*
		if(!envoye){
			envoyerAuxClients(numProg + "");
			envoye = true;
		}*/
		
		int Action = Maths.getRandom() % 2;
		// Si en section critique, ne rien faire
		if(!jeVeuxEtreEnCritique){
			if (Action == 0){
				//Action message
				int numDestinataire = Maths.getRandom();
				envoyerAUnClient(numDestinataire, msg.getData());
			}
			else {
				int choix = Maths.getRandom() % 2;
				// Veut entrer en section critique
				if(choix == 1){
					System.out.println(numProg + " entre en section critique");
					jeVeuxEtreEnCritique = true;
					// Envoie du message REQUEST
					envoyerAuxClients(numProg + ",REQUEST");
					fdr.addRequest(new Message(numProg + ",REQUEST", lamport.getTime()));
					nbReply = 0;
				}else{
					// Envoie d'un message normal
					envoyerAuxClients(numProg + ",NORMAL");
				}
				// Diffuse message
				//envoyerAuxClients(numProg + "");
			}
		}else{
			// Traitement des messages
			String delimiter = "[,]";
			String[] tokens = msg.getData().split(delimiter);
			int idMachine = Integer.parseInt(tokens[0]);
			String commande = tokens[1];
			// On envoie un REPLY à l'émétteur
			if(commande.equals("REQUEST")){
				envoyerAUnClient(idMachine , numProg + ",REPLY");
			}else if(commande.equals("REPLY")){
				nbReply++;
				if(nbReply == ProjetIR.grandN){
					// Il faut checker si la demande est dans la file
					boolean estDansLaFile = true;
					if(estDansLaFile){
						envoyerAuxClients(numProg + ",RELEASE");
						System.out.println(numProg + " sort de section critique");
					}
				}
			}else{
				// Message normal
			}
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
		if(lesClients[idClient] != null){
			String toSVG = "REQ," + numProg + "," + (idClient + 1) + "," + lamport.getTime() + "," + (lamport.getTime() + 1);
			clientSVG.envoyerMessage(new Message( toSVG  , 0), new Horloge());
			lesClients[idClient].envoyerMessage(new Message(msg, lamport.getTime()), lamport);
		}
	}
	
	public void diffuserMessageTelQuel(Message msg){
		for(int i = 0 ; i < ProjetIR.grandN ; i++){
			if(lesClients[i] != null){
				lesClients[i].envoyerMessage(msg, new Horloge());
			}
		}
	}
}
