/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetir;


/**
 *
 * @author Xavier TALANDIER <xavier@talandier.fr>
 */
public class ProjetIR {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		int port1 = 3000;
		int port2 = 3001;
		Thread p1;
		Thread p2;
	
		p1 = new Thread(new Program(1 , port1 , port2));
		p2 = new Thread(new Program(2 , port2 , port1));
		
		p1.start();
		p2.start();
	}
}
