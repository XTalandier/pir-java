/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import projetir.*;

/**
 *
 * @author xtalandier
 */
public class Server implements Runnable {

	public static int nbClient = 0;

	private int port = 5234;
	private ServerSocket socketserver;
	private Socket socket;

	public Thread johnTheRunner;
	Program prgParent = null;

	public Server(int lePort , Program prg ) throws IOException {
		port = lePort;
		prgParent = prg;
		InetAddress LocaleAdresse;
		InetAddress ServeurAdresse;
		LocaleAdresse = InetAddress.getLocalHost();
		System.out.println("Listen to : " + LocaleAdresse + ":" + port);
		socketserver = new ServerSocket(port);
	}

	@Override
	public void run() {
		while (true) {
			try {
				socket = socketserver.accept(); // Un client se connecte on l'accepte
				System.out.println("SERVER : Oh! A new friend ! (#" + ++nbClient + ")");
				johnTheRunner = new Thread(new ServerReader(socket , prgParent));
				johnTheRunner.start();
			} catch (IOException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
