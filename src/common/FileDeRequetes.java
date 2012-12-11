/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Cyril
 */
public class FileDeRequetes {
	public ArrayList<Message> listRequest = new ArrayList<>();
	
	public void addRequest(Message msg){
		listRequest.add(msg);
		if(listRequest != null){
			Collections.sort(listRequest, new Comparator<Message>(){
				@Override
				public int compare(Message m1, Message m2){
					int est = m1.getEstampille() - m2.getEstampille();
					String delimiter = "[,]";
					String[] tokens1 = m1.getData().split(delimiter);
					String[] tokens2 = m2.getData().split(delimiter);
					return est == 0 ? Integer.parseInt(tokens1[0]) - Integer.parseInt(tokens2[0]) : est;
				}
			});
		}
	}
	
}
