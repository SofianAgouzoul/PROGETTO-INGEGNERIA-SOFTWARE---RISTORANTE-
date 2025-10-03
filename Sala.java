import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class Sala extends Frame implements Admin{
	private JButton btnHome = new JButton("Home");
	private Vector<JButton> btnTav = new Vector<JButton>();
	private JTextArea textArea;
	private JPanel panel;
	private JButton btnLiberaT = new JButton("Libera");
    public Sala() {    	
    	Border blackline, raisedetched, loweredetched,raisedbevel, loweredbevel, empty;
	    
    	blackline = BorderFactory.createLineBorder(Color.black);
    	raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    	loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    	raisedbevel = BorderFactory.createRaisedBevelBorder();
    	loweredbevel = BorderFactory.createLoweredBevelBorder();
    	empty = BorderFactory.createEmptyBorder();
    	
    	btnLiberaT = new JButton("Libera");
    	textArea = new JTextArea(30,30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> {textArea.setText("");});
        btnLiberaT.addActionListener(e -> {liberaTav();});
        
        
        panel = new JPanel();
        
        panel.setBackground(new Color(172,255,175));
		panel.setBorder(BorderFactory.createCompoundBorder(raisedbevel,loweredbevel));


		textArea.setBorder(BorderFactory.createCompoundBorder(raisedbevel,loweredbevel));
		
		for(Integer i=1;i<=20;i++) {
			btnTav.add(new JButton("Tavolo: "+i));
			btnTav.get(i-1).setEnabled(false);
			this.panel.add(btnTav.get(i-1));
		}       
        
    	
        frame.setTitle("Sala");
      
        panel.add(btnClear);
        panel.add(btnHome);
        panel.add(btnLiberaT);
       
        frame.add(panel);
        frame.add(scrollPane, BorderLayout.LINE_END);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


	public void liberaTav() {
		System.out.println("<SYSTEM>:: Sto per liberare un tavolo!");
		String txt, numTav;
		int n;
		txt = this.textArea.getText();
		System.out.println("<SYSTEM>:: txt contiene: " + txt);				//WARINING: NON CONTIENE LA SOTTORSTRIGNA CORRETTA
		numTav = txt.substring(8,10);	//prendiamo non dall carattere di indice 8 a quello di indice 9 ma da 8 a 10 per coprire il caso in cui si recuperi il numero di un tavolo a due cifre
		System.out.println("<SYSTEM>:: numTav (substring [8...10]) contiene: " + numTav);
		if(numTav.contains(":"))
			numTav = txt.substring(8,9);
			System.out.println("<SYSTEM>:: numTav (substring [8...9]) contiene: " + numTav);
		//if(numTav.contains(" ")) 
			//numTav = txt.substring(10,11);
		//System.out.println("<SYSTEM>:: numTav (substring [10...11]) contiene: " + numTav);
		n = Integer.parseInt(numTav);
		System.out.println("<SYSTEM>:: Liberazione tavolo numero " + n);
		File tavolo = new File("tav"+n+".txt");
		//tavolo.delete();
	}



	@Override
	public void aggiungiComanda(Tavolo tavolo) {
		for(ActionListener al : btnTav.get(tavolo.getNumTav()-1).getActionListeners()) {
			btnTav.get(tavolo.getNumTav()-1).removeActionListener(al);
			System.out.println("\nrimozione Al");
		}
		if(!tavolo.getOrdine().isEmpty()) {
			this.btnTav.get(tavolo.getNumTav()-1).setEnabled(true);
			this.btnTav.get(tavolo.getNumTav()-1).addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					getInfo(tavolo);
					
					}
				}
			);
		} else {
			this.btnTav.get(tavolo.getNumTav()-1).setEnabled(false);
			this.textArea.setText("");
		}
			
		

		frame.validate();

	}

	public void getInfo(Tavolo tavolo) {
		Double totParziale=0.0;
		this.textArea.setText("");
		this.textArea.append("Tavolo n"+tavolo.getNumTav()+":\n");
		for(Ordine ord : tavolo.getOrdine()) {

			for(Pietanze p : ord.getPietanze()) {
				textArea.append(p.getNome()+" x"+p.getQnt()+" ("+p.getPrezzo()+" e)\n");
				totParziale += p.getPrezzo()*p.getQnt();
			}
			textArea.append("Il totale relativo a quest'ordine e': "+totParziale+"\n");
			totParziale=0.0;
			textArea.append("\n\t|----------------------| \n");
		}


	}
    
}

