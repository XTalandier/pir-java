/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.Random;
import projetir.ProjetIR;
/**
 *
 * @author Xavier TALANDIER <xavier@talandier.fr>
 */
public class Maths {
	public static Random rand = new Random();
	
	public static int getRandom(){
		return rand.nextInt(ProjetIR.grandN - 1) + 0;
	}
	
	public static int getAction(){
		return rand.nextInt(1) + 0;	
	}
}
