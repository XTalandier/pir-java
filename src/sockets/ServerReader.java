/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import common.*;
import java.util.Arrays;
import projetir.*;
/**
 *
 * @author xtalandier
 */
public class ServerReader implements Runnable {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out = null;
	Program prgParent = null;

	private Message[] msgs      = new Message[1000];
	private int       msgOffset = 0;
	public ServerReader(BufferedReader in, String login){
		this.in = in;
	}
	
	
	public ServerReader(Socket s , Program prg){
		prgParent = prg;
		socket = s;
	}


	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			boolean dontStop = true;
			while(dontStop){
				String speech = in.readLine();
				System.out.println("SERVER : He said '" + speech + "'");
				Message msg = Message.fromString(speech);
				prgParent.recoitMessage(msg);
				//parse(msg.getData());
				//if(msg.getData().equals("EXIT")){
				//	dontStop = false;
					//analyze();
				//}
			}
		} catch (IOException ex) {
			Logger.getLogger(ServerReader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * Sort & parses messages
	 */
	private void analyze(){
		// Sort objects
		for(int i = 0 ; i < msgOffset ; i++){
			Arrays.sort(msgs);
		}
		// Parse strings
		for(int i = 0 ; i < msgOffset ; i++){
			
			parse(msgs[i].getData());
		}
	}
	
	/**
	 * Parse and execute a line
	 * @param line Line to parse & execute
	 * @return If the message is exit
	 */
	private boolean parse(String line){
		boolean retVal = true;
		/*
		StringTokenizer st = new StringTokenizer(line , ",");
		String command = st.nextToken();
		System.out.println("SERVER : The command is : " + command);
		if(command.equals("REQ")){
			int numDep  = Integer.parseInt(st.nextToken());
			int numArr  = Integer.parseInt(st.nextToken());
			int dateDeb = Integer.parseInt(st.nextToken());
			int dateFin = Integer.parseInt(st.nextToken());
			svg.dessinerREQ(numDep, numArr, dateDeb, dateFin);
		}else if(command.equals("SC")){
			int num     = Integer.parseInt(st.nextToken());
			int dateDeb = Integer.parseInt(st.nextToken());
			int dateFin = Integer.parseInt(st.nextToken());
			svg.dessinerSC(num, dateDeb, dateFin);
		}
		else if(command.equals("EXIT")){
			retVal = false;
			try {
				svg.close();
			} catch (IOException ex) {
				Logger.getLogger(ServerReader.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		else if (command.equals("REP")){
			int numDep  = Integer.parseInt(st.nextToken());
			int numArr  = Integer.parseInt(st.nextToken());
			int dateDeb = Integer.parseInt(st.nextToken());
			int dateFin = Integer.parseInt(st.nextToken());
			svg.dessinerREP(numDep, numArr, dateDeb, dateFin);
		}
		else if (command.equals("REAL")){
			int numDep  = Integer.parseInt(st.nextToken());
			int numArr  = Integer.parseInt(st.nextToken());
			int dateDeb = Integer.parseInt(st.nextToken());
			int dateFin = Integer.parseInt(st.nextToken());
			svg.dessinerREAL(numDep, numArr, dateDeb, dateFin);
		}
		*/
		return retVal;
	}
	
}
