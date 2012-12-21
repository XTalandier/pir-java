/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetir;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import common.ColorSVG;
import common.Message;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cyril
 */
public class ServeurSVG extends Program implements Runnable{
	private String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
			"<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"1600\" width=\"1600\">\n" +
				"<defs>\n" +
					"<marker id=\"Triangle\" viewBox=\"0 0 40 40\" refX=\"16\" refY=\"16\"\n" +
						"markerWidth=\"10\" markerHeight=\"10\" orient=\"auto\">\n" +
						"<path d=\"M 0 0 L 16 16 L 0 32 L 40 16 Z\" />\n" +
					"</marker>\n" +
				"</defs>\n";
	private String body;
	private String footer = "\n</svg>";
	private String fileName="c:\\output.svg";
	/**
	 * Date = 0 (x)
	 */
	private int start = 80;
	/**
	 * Step between 2 dates
	 */
	private int step  = 50;
	
	/**
	 * Step between 2 process
	 */
	private int interline = 40;
	
	public ServeurSVG(int numProg, int portEcoute, int portEnvoi , int numCopain){
		super(numProg, portEcoute, portEnvoi, numCopain);
		for (int i = 1; i < 11; i++) {
				dessinerProcessus(i, "Process #" + i);
		}
	}
	
	@Override
	public void recoitMessage(Message msg){
		parseMsg(msg.getData());
	}
	
	public void parseMsg(String msg){
		String delims = "[,]";
		String[] tokens = msg.split(delims);
		
		if (tokens[0].equals("EXIT")){
			try {
				close();
			} catch (IOException ex) {
				Logger.getLogger(ServeurSVG.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		else if (tokens[0].equals("REAL")){
			dessinerREAL(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
		}
		else if(tokens[0].equals("REP")){
			dessinerREP(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
		}
		else if(tokens[0].equals("REQ")){
			dessinerREQ(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
		}
		else if(tokens[0].equals("SC")){
			dessinerSC(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
		}
		
	}
	
	private void appendToBody(String data){
		this.body += "\n" + data + "\n";
	}
	
	/**
	 * Write the SVG file or throw IOException
	 */
	public void close() throws IOException {
		try (FileWriter fw = new FileWriter(fileName)) {
			fw.write(header);
			fw.write(body);
			fw.write(footer);
		}
	}
	
	/**
	 * Permit to draw a line
	 * @param x1 start position (x)
	 * @param y1 start position (y)
	 * @param x2 final position (x)
	 * @param y2 final position (y)
	 * @param couleur color line
	 */
	public void tracerTrait (int x1, int y1, int x2, int y2, String couleur){
		this.appendToBody("<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" stroke=\"" + couleur + "\" />");
	}
	
	
	/**
	 * Permit to draw a rectangle
	 * @param x start position (x)
	 * @param y start position (y)
	 * @param largeur rect largeur
	 * @param longueur rect longueur
	 * @param couleur rectangle color
	 */
	public void tracerRectangle (int x, int y, int largeur, int longueur, String couleur){
		this.appendToBody("<rect width=\"" + largeur + "\" height=\"" + longueur + "\" x=\"" + x + "\" y=\"" + y + "\" fill=\"" + couleur + "\" />");
	}
	
	
	/**
	 * Draw arrow to canvas
	 * @param x1 Start position (x)
	 * @param y1 Start position (y)
	 * @param x2 End position (x)
	 * @param y2 End position (y)
	 * @param couleur Color
	 */
	public void tracerFleche(int x1 , int y1 , int x2 , int y2 , String couleur){
		this.appendToBody("<path d=\"M " + x1 + " " + y1 + " L " + x2 + " " + y2 + "\" style=\"fill:none;stroke:" + couleur + "; stroke-width:2; marker-end:url(#Triangle)\" />");
	}
	
	/**
	 * Write text to canvas
	 * @param x pos X
	 * @param y pos Y
	 * @param texte Text to write
	 * @param couleur Color
	 */
	public void ecrireText(int x, int y, String texte , String couleur){
		this.appendToBody("<text x=\"" + x + "\" y=\"" + y +"\"  fill=\"" + couleur + "\">" + texte + "</text>");
	}
	
	/**
	 * Draw process
	 * @param num PID
	 * @param nom Name
	 */
	public void dessinerProcessus(int num , String nom){
		int curY = num * interline ;
		int step2=10;
		ecrireText(step , curY , nom , ColorSVG.BLACK);
		tracerFleche(step, curY + step2, 1600 , curY + step2, ColorSVG.BLACK);
	}
	
	/**
	 * Draw red rectangle for critical section of process
	 * @param num
	 * @param dateDeb
	 * @param dateFin 
	 */
	public void dessinerSC (int num, int dateDeb, int dateFin){
		int startPosX = dateDeb * step;
		int width     = dateFin * step - startPosX;
		// 1 step is "step", so, start + dateDeb * step
		tracerRectangle(start + startPosX , interline * num , width , step , ColorSVG.RED);
	}

	/**
	 * Red arrow for REQUEST message
	 * @param numDep Start process number
	 * @param numArr Finish process number
	 * @param dateDeb Date 1
	 * @param dateFin Date 2
	 */
	public void dessinerREQ (int numDep, int numArr, int dateDeb, int dateFin){
		int startPosX = dateDeb * step;
		int stopPosX = dateFin * step;
		tracerFleche(start + startPosX, interline*numDep, start+stopPosX, interline*numArr, ColorSVG.RED);
		
		ecrireText(start + startPosX, interline*numDep, String.valueOf(dateDeb), ColorSVG.BLACK);
		ecrireText(start + stopPosX, interline*numArr, String.valueOf(dateFin), ColorSVG.BLACK);
	}
	
	/**
	 * Blue arrow for REQUEST message
	 * @param numDep Start process number
	 * @param numArr Stop process number
	 * @param dateDeb Date 1
	 * @param dateFin Date 2
	 */
	public void dessinerREP (int numDep, int numArr, int dateDeb, int dateFin){
		int startPosX = dateDeb * step;
		int stopPosX = dateFin * step;
		tracerFleche(start + startPosX, interline*numDep, start+stopPosX, interline*numArr, ColorSVG.BLUE);
		ecrireText(start + startPosX, interline*numDep, String.valueOf(dateDeb), ColorSVG.BLACK);
		ecrireText(start + stopPosX, interline*numArr, String.valueOf(dateFin), ColorSVG.BLACK);
	}
	
	/**
	 * Green arrow for RELEASE message
	 * @param numDep Start process number
	 * @param numArr Stop process number
	 * @param dateDeb Date 1
	 * @param dateFin Date 2
	 */
	public void dessinerREAL (int numDep, int numArr, int dateDeb, int dateFin){
		int startPosX = dateDeb * step;
		int stopPosX = dateFin * step;
		tracerFleche(start + startPosX, interline*numDep, start+stopPosX, interline*numArr, ColorSVG.GREEN);
			
		ecrireText(start + startPosX, interline*numDep, String.valueOf(dateDeb), ColorSVG.BLACK);
		ecrireText(start + stopPosX, interline*numArr, String.valueOf(dateFin), ColorSVG.BLACK);
	}
	/**
	 * Return the X position of an object
	 * @param proc # of processus
	 * @param date date
	 * @return position X
	 */
	public int getPositionX(int proc , int date){
		return start + date * step;
	}
}
