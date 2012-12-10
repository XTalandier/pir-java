/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 *
 * @author Xavier TALANDIER <xavier@talandier.fr>
 */
public class Client implements Runnable{
	private int portEcoute; 
	
	public Client(int leportEcoute){
		portEcoute = leportEcoute;
	}
			
	@Override
	public void run() {
		
	}

}
