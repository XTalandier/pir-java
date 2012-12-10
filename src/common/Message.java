package common;

import java.util.*;
import java.io.*;

/**
 *
 * @author Xavier TALANDIER
 */
public class Message implements Serializable , Comparable<Message> {

	private int estampille;
	private String data;

	/**
	 * Create message from string
	 * @param string serialized message
	 * @return Message
	 */
	public static Message fromString(String string){
		StringTokenizer tokens = new StringTokenizer(string , "|");
		int time   = Integer.parseInt(tokens.nextToken());
		String msg = tokens.nextToken();
		return new Message(msg , time);
	}
	
	/**
	 * Constructor
	 * @param msg
	 * @param time 
	 */
	public Message(String msg, int time) {
		this.data = msg;
		this.estampille = time;
	}

	/**
	 * Get text from message
	 * @return 
	 */
	public String getData() {
		return data;
	}

	/**
	 * Get stamp from message
	 * @return 
	 */
	public int getEstampille() {
		return estampille;
	}
	
	/**
	 * Serializer
	 * @return 
	 */
	@Override
	public String toString(){
		return this.estampille + "|" + data;
	}

	
	/**
	 * 
	 * Comparable - method compareTo()
	 * @param t Message
	 * @return 
	 */
	@Override
	public int compareTo(Message t) {
		if(this.getEstampille() > t.getEstampille()){
			return 1;
		}
		else if (this.getEstampille() == t.getEstampille()){
			return 0;
		}
		else{
			return -1;
		}	
	}
	
}
