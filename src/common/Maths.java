/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.Random;

/**
 *
 * @author Xavier TALANDIER <xavier@talandier.fr>
 */
public class Maths {
	public static int getRandom(){
		Random rand = new Random();
		return rand.nextInt(10) + 1;
	}
}
