/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetir;

import common.Maths;


/**
 *
 * @author Xavier TALANDIER <xavier@talandier.fr>
 */
public class ProjetIR {
	
	public static int grandN = 4;
	public static int portStart = 3000;
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// Création du serveur SVG
		new Thread(new ServeurSVG(0 , 2999 , 2999 , 0)).start();
		
		// Création des instances
		int maxInstance = grandN;
		for(int i = 1 ; i <= maxInstance ; i++){
			int portEmission  = getPortEmission(i, maxInstance);
			int portReception = getPortReception(i, maxInstance);
			int numCopain     = portEmission - portStart + 1;
			Program prg = new Program(i , portReception , portEmission , numCopain);
			prg.nbInstances = maxInstance;
			new Thread(prg).start();
		}
	}
	
	/**
	 * Renvoie le numero du port pour envoyer
	 * @param numero numero de l'instance
	 * @param max Nombre max d'instance à créer
	 * @return int
	 */
	public static int getPortEmission(int numero , int max){
		return numero == max ? portStart : portStart + numero;
	}
	/**
	 * Renvoie le numero du port à écouter
	 * @param numero numero de l'instance
	 * @param max Nombre max d'instance à créer
	 * @return int
	 */
	public static int getPortReception(int numero , int max){
		return (portStart + numero - 1) < 3000 ? portStart : portStart + numero - 1;
	}
}
