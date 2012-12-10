/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author Duss
 */
public class Horloge {
	private int timestamp = 0;

	/**
	 * Return the current time
	 * @return 
	 */
	public int getTime(){
		return timestamp;
	}
	
	/**
	 * +1 on timestamp
	 */
	public void tick(){
		timestamp++;
	}
	
	/**
	 * Synchronize the timestamp
	 * @param time 
	 */
	public void synchronize(int time){
		timestamp = time;
	}
}
