/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import common.Horloge;
import common.Message;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xavier TALANDIER <xavier@talandier.fr>
 */
public class Client implements Runnable{
	private int portEcoute; 
	private Socket socket = null;
	private PrintWriter out;
	
	public Client(int leportEcoute){
		portEcoute = leportEcoute;
	}
	
	public void envoyerMessage(Message msg , Horloge timer){
		timer.tick();
		out.println(msg);
		out.flush();		
	}
			
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			System.out.println("Connect to " + portEcoute);
			socket = new Socket("127.0.0.1", portEcoute);
			out = new PrintWriter(socket.getOutputStream());
		} catch (UnknownHostException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
