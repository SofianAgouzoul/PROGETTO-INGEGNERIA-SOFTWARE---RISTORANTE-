import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.util.Formatter;

public class CassaFrame extends Frame{
    JButton btnHome = new JButton("Home");
    Vector<JButton> btnTav = new Vector<JButton>();
    Integer txtCount=0;
    JTextArea textArea;
    JPanel panel;
    JButton btnLiberaT = new JButton("Libera");
    
    
    public CassaFrame() {
    	
    	Border raisedetched, loweredetched;
		   
		raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    	
    	
    	btnLiberaT = new JButton("Libera");
    	textArea = new JTextArea(30,30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> {textArea.replaceRange("", 0, this.txtCount);});
        btnHome.addActionListener(e -> {new MainFrame();});
        btnLiberaT.addActionListener(e -> {liberaTav();});
        
        
        panel = new JPanel();
    	panel.setBackground(new Color(152,255,152));
        
        
        
        for(int i=1;i<=20;i++) {
        	btnTav.add(new JButton("Tavolo: "+i));
        	//btnTav.get(i-1).setForeground(Color.red);
        	this.panel.add(btnTav.lastElement());
        }
        //INSERISCI METODO DI PAGAMENTO
        aggiornaTavoli();
        
        
    	
        frame.setTitle("Cassa");
      
        panel.add(btnClear);
        panel.add(btnHome);
        panel.add(btnLiberaT);
       
        frame.add(panel);
        frame.add(scrollPane, BorderLayout.LINE_END);
    }

    
    
    public void aggiornaTavoli() {
    	
        
        for(int i=1;i<=20;i++) {
        	File myObj = new File("tav"+i+".txt");
        	if(myObj.exists()) {
				System.out.println("Dentro aggiornaTavoli"+i);
				final Integer ii = new Integer(i);
				btnTav.get(i-1).addActionListener(e -> showOrder(ii, myObj, textArea));
				btnTav.get(i-1).setEnabled(true);
				//btnTav.get(i-1).setForeground(Color.green);
			} else { 
				btnTav.get(i-1).setEnabled(false);
			}
        }
    }
    
    private void showOrder(Integer i, File file, JTextArea txt){
    	//Aggiungiamo l'ordine al textArea...
    	Scanner myReader;
    	String tmpCounter;
		try {
			myReader = new Scanner(file);
			String temp, temptxt;
	    	temptxt = txt.getText();
	    	if(!temptxt.isEmpty()) {
	    		txt.replaceRange("", 0, temptxt.length());
	    	}
	    	txt.append("Il tavolo "+i+" ha ordinato:\n");
	    	while(myReader.hasNextLine()) {
	    		temp = myReader.nextLine();
	    		txt.append(temp+"\n");
	    	}
	    	tmpCounter=txt.getText();
	    	this.txtCount = tmpCounter.length();
	    	myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Scanner non trovato");
		}
    }
    
    public void liberaTav() {
    	String txt, numTav;
    	int n;
    	txt = this.textArea.getText();
    	numTav = txt.substring(10,12);
    	if(numTav.contains(" ")) numTav = txt.substring(10,11);
    	n = Integer.parseInt(numTav);
    	File tavolo = new File("tav"+n+".txt");
    	tavolo.delete();
    	aggiornaTavoli();
    }
    
}

