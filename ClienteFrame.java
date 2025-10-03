import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class ClienteFrame extends Frame  {
    int risposta;
    private Vector<String> vectorS = new Vector<String>();
    private Vector<Integer> vectorQ = new Vector<Integer>();
    private JCheckBox cBox = new JCheckBox();
    private Tavolo tav[];
    private JTextArea textArea = new JTextArea(30,30);
    private JTextArea textAreaTavolo = new JTextArea(30,30);
    private MandaComande gestoreTavoli = new MandaComande();
    private Serviamo avviso = new Serviamo();
    private ArrayList<Admin> aList = new ArrayList<Admin>();
    private ArrayList<Cuoco> cList = new ArrayList<Cuoco>();
    private JComboBox<String> pizze = new JComboBox<>();
    private JComboBox<String> primiPiatti = new JComboBox<>();
    private JComboBox<String> bevande = new JComboBox<>();
    private JComboBox<String> tavoli = new JComboBox<>();
    private JLabel labelTav = new JLabel("Ordinazione per il tavolo n:");
    private JButton btnOrdina = new JButton("Ordina");
    private JButton btnServi = new JButton("Permetti chiusura");
    private JButton btnAdd = new JButton("+");
    private JButton btnDec = new JButton("-");        
    private JButton btnAdd1 = new JButton("+");
    private JButton btnDec1 = new JButton("-");        
    private JButton btnAdd2 = new JButton("+");
    private JButton btnDec2 = new JButton("-");        
    private JButton btnStato = new JButton("   ");
    private Object selected;
    private JPanel panel = new JPanel(new BorderLayout());
    private JPanel panPietanze = new JPanel(new GridBagLayout());
    private Menu menu = new Menu();


    public ClienteFrame(ArrayList<Admin> admList, ArrayList<Cuoco> cuochiList) {
        Boolean boolCassa=false, boolSala=false;
        if(admList.isEmpty()) {
            this.aList.add(new Cassiere());
            this.aList.add(new Sala());
        } else {
            for(int i=0;i<admList.size();i++) {
                if(admList.get(i) instanceof Cassiere) {
                    boolCassa=true;
                } else if(admList.get(i) instanceof Sala) {
                    boolSala=true;
                }
            }
            if(!boolCassa)	aList.add(new Cassiere());
            if(!boolSala)	aList.add(new Sala());
        }
        Boolean boolPizzaiolo=false, boolChef=false;
        if(cuochiList.isEmpty()) {
            this.cList.add(new Pizzayolo());
            this.cList.add(new Chef());
        } else {
            for(int i=0;i<cuochiList.size();i++) {
                if(cuochiList.get(i) instanceof Pizzayolo) {
                    boolPizzaiolo=true;
                } else if(cuochiList.get(i) instanceof Chef) {
                    boolChef=true;
                }
            }
            if(!boolCassa)	cList.add(new Pizzayolo());
            if(!boolSala)	cList.add(new Chef());
        }
        start();
    }


    public void start(){
    	String tmpTextArea = this.textArea.toString();
        if(tmpTextArea.isEmpty()) this.btnOrdina.setEnabled(false);
        else this.btnOrdina.setEnabled(true);
    	
	    System.out.println("INIZIO...");
	    	
	    Border blackline, raisedetched, loweredetched,
	    raisedbevel, loweredbevel, empty;
    
	    blackline = BorderFactory.createLineBorder(Color.black);
	    raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
	    loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	    raisedbevel = BorderFactory.createRaisedBevelBorder();
	    loweredbevel = BorderFactory.createLoweredBevelBorder();
	    empty = BorderFactory.createEmptyBorder();

        for(int i=0;i<cList.size();i++) {
            avviso.add(cList.get(i));
        }
        for(int i=0;i<aList.size();i++) {
            gestoreTavoli.add(aList.get(i));
        }
    	
		panPietanze.setBackground(new Color(255,191,0));
		panPietanze.setBorder(BorderFactory.createCompoundBorder(raisedbevel,loweredbevel));
		
    	
        frame.setTitle("Gestionale Ristorante-Cliente");
        label.setText("Ristorante Parthenope");
        label.setFont(new Font(null, Font.PLAIN,25));
        frame.getContentPane().add(label, BorderLayout.NORTH);

        cBox.setFocusable(false);
        
        
        
        
        for (int i=0;i<menu.getPizze().size();i++){
            pizze.addItem(menu.getPizze().get(i).getNome());
        }
       
        
        for(int j=0;j<menu.getBibite().size();j++) {
        	bevande.addItem(menu.getBibite().get(j).getNome());
        }
        
        for(int j=0;j<menu.getPrimiPiatti().size();j++) {
        	primiPiatti.addItem(menu.getPrimiPiatti().get(j).getNome());
        }
        
        //CREAZIONE DEI TAVOLI
       
    	tav = new Tavolo[20];
    
        for(int i=0;i<20;i++) {
        	tav[i]=new Tavolo();
        }
    
        Integer tempI;
        String tempS;
        for(int i=0;i<20;i++) {
        		tav[i].setNumTav(i+1);
        		tempI = tav[i].getNumTav();
        		tempS = tempI.toString();
        		tavoli.addItem(tempS);
        }
        
        
        
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createCompoundBorder(raisedbevel,loweredbevel));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        
        textAreaTavolo.setBorder(BorderFactory.createCompoundBorder(raisedbevel,loweredbevel));
        textAreaTavolo.setEditable(false);
        JScrollPane scrollTATav = new JScrollPane(textAreaTavolo);
        //scrollTATav
        JButton btnClear = new JButton("Clear");
      	btnStato.setBackground(Color.white);
        btnClear.addActionListener(e -> {
        								this.vectorQ.clear();
        								this.vectorS.clear();
        								this.textArea.setText("");
        								this.textAreaTavolo.setText("");
        								selected = tavoli.getSelectedItem();
        				                Object copy = selected;
        				                Integer n = Integer.parseInt((String)copy);
        				                tav[n-1]=new Tavolo();
        				                tav[n-1].setNumTav(n);
        				                gestoreTavoli.allertaComanda(tav[n-1]);
        								frame.revalidate();
        				                btnStato.doClick();
        								});
 
        tavoli.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                /* QUANDO VIENE CLICKATO IL JCOMBOBOX DEL TAVOLO MOSTRAMI IL TAVOLO */
                
                selected = tavoli.getSelectedItem();
                Object copy = selected;
                Integer n = Integer.parseInt((String)copy);
                
                if(tav[n-1].hasOrders()) {
                	btnServi.setEnabled(true);
                } else{
                	btnServi.setEnabled(false);
                }
                
                btnStato.doClick();
                
                System.out.println("E' stato selezionato -> "+(String) selected);
          
                showOrder(tav[Integer.parseInt((String) selected)-1], textAreaTavolo);
                
            }
        });
     	//BOTTONE ORDINAZIONE
        btnOrdina.addActionListener(e -> ordiniamo(vectorS, vectorQ, tav[Integer.parseInt((String) tavoli.getSelectedItem())-1]));
        btnOrdina.setEnabled(false);
        
        btnServi.setEnabled(false);
        //BOTTONE SERVI
        btnServi.addActionListener(e -> {aggiornaTav(tav[Integer.parseInt((String) tavoli.getSelectedItem())-1], textAreaTavolo);  btnStato.doClick(); 
        								btnServi.setEnabled(false);
        								this.btnOrdina.setEnabled(false);
        								});

        
        //Pizze
        btnAdd.addActionListener(e -> addOrdine((String) pizze.getSelectedItem(), textArea, vectorS, vectorQ));
        btnDec.addActionListener(e -> decOrdine((String) pizze.getSelectedItem(), textArea, vectorS, vectorQ));
        //Bevande
        btnAdd1.addActionListener(e -> addOrdine((String) bevande.getSelectedItem(), textArea, vectorS, vectorQ));
        btnDec1.addActionListener(e -> decOrdine((String) bevande.getSelectedItem(), textArea, vectorS, vectorQ));
        //Primi piatti
        btnAdd2.addActionListener(e -> addOrdine((String) primiPiatti.getSelectedItem(), textArea, vectorS, vectorQ));
        btnDec2.addActionListener(e -> decOrdine((String) primiPiatti.getSelectedItem(), textArea, vectorS, vectorQ));
        //Home
       
        //btn stato ordine
        btnStato.setFocusable(false);
        btnStato.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (selected==null) {
					tavoli.setSelectedIndex(0);
				}
				
					
					selected = tavoli.getSelectedItem();
					Integer i=Integer.parseInt(selected.toString());
					
					
					if(tav[i-1].getStatusOrdine() instanceof OrdineRicevuto) {
						btnStato.setBackground(Color.orange);
						btnOrdina.setEnabled(true);
						btnServi.setEnabled(true);
						
					}
					else if(tav[i-1].getStatusOrdine() instanceof OrdineConsegnato) {
						btnStato.setBackground(Color.green);
						
					} else if(tav[i-1].getStatusOrdine() instanceof NoState) {
						btnStato.setBackground(Color.white);
						btnOrdina.setEnabled(true);
						btnServi.setEnabled(false);
						
					} 
					
				
				
			}
        	
        });
        
        frame.add(scrollPane, BorderLayout.LINE_END);
        
        //LAYOUT PANNELLO PIETANZE
        panel.add(panPietanze,BorderLayout.PAGE_START);
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        /*Menu pizze*/
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        pizze.setBorder(raisedbevel);
        
        panPietanze.add(pizze,gbc);    
        
        /*Bottoni Pizza*/
        gbc.gridx = 1;
        gbc.gridy = 0;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        
        panPietanze.add(btnAdd,gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        
        panPietanze.add(btnDec,gbc);
        
        /*Menu Bevande*/
        gbc.gridx = 0;
        gbc.gridy = 1;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
       
        bevande.setBorder(raisedbevel);
        
        panPietanze.add(bevande,gbc);
        
        /*Bottoni bevande*/
        gbc.gridx = 1;
        gbc.gridy = 1;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        
        panPietanze.add(btnAdd1,gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 1;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        
        panPietanze.add(btnDec1,gbc);
        
        /*Menu primi piatti*/
        gbc.gridx = 0;
        gbc.gridy = 2;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        primiPiatti.setBorder(raisedbevel);
        
        panPietanze.add(primiPiatti,gbc);    
        
        /*Bottoni Primi piatti*/
        gbc.gridx = 1;
        gbc.gridy = 2;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
           
        panPietanze.add(btnAdd2,gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 2;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;

        panPietanze.add(btnDec2,gbc);
        
        /*Label scegli tavolo*/
        gbc.gridx = 0;
        gbc.gridy = 3;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        
        panPietanze.add(labelTav,gbc);
        
        /*Jcombobox tavoli*/
        gbc.gridx = 1;
        gbc.gridy = 3;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        
        tavoli.setBorder(raisedbevel);
        
        panPietanze.add(tavoli,gbc);
        
        /*Stato del tavolo*/
        gbc.gridx = 2;
        gbc.gridy = 3;
        
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        
        panPietanze.add(btnStato,gbc);
        
        /*Bottoni ordina e servi*/
        gbc.gridx = 0;
        gbc.gridy = 4;
        
        gbc.weightx = 0.02;
        gbc.weighty = 0.02;
        
        panPietanze.add(btnOrdina,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        
        gbc.weightx = 0.02;
        gbc.weighty = 0.02;
        
        panPietanze.add(btnServi,gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 4;
        
        gbc.weightx = 0.02;
        gbc.weighty = 0.02;
        
        panPietanze.add(btnClear,gbc);
        
        /*TextArea tavolo*/
        gbc.gridx = 0;
        gbc.gridy = 5;
        
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 3;
        
        panPietanze.add(scrollTATav, gbc);
       
        frame.getContentPane().add(panPietanze);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

 
	public void addOrdine(String scelta, JTextArea textArea, Vector<String> listaS, Vector<Integer> listaQ){
		
    	boolean bool;
        int i, n, lenScelta, indice;
        String scontrino, temp;
        scontrino = textArea.getText();
        indice = scontrino.indexOf(scelta);
        bool = listaS.contains(scelta);
        if (!bool){
            listaS.add(scelta);
            listaQ.add(1);
            textArea.append(scelta+" Qt: 1  \n");
        } else if(indice>=0) {
            for(i=0;i<listaS.size();i++){
                    n=listaQ.get(i)+1;
                    listaQ.set(i, n);
                    if(listaQ.get(i)>9){
                        temp = scelta+" Qt: "+listaQ.get(i)+" \n";
                        lenScelta = temp.length() + indice;
                    } else {
                        temp = scelta+" Qt: "+listaQ.get(i)+"  \n";
                        lenScelta = temp.length() + indice;
                    }
                    textArea.replaceRange(temp, indice, lenScelta);          
                }
                
            }
        String tmpTextArea = this.textArea.toString();
        if(tmpTextArea.isEmpty()) this.btnOrdina.setEnabled(false);
        else this.btnOrdina.setEnabled(true);
           
        }


    public void decOrdine(String scelta, JTextArea textArea, Vector<String> listaS, Vector<Integer> listaQ){
    	 int indice, lenScelta, i, n=0;
         String scontrino, temp;
         Boolean bool;
         scontrino = textArea.getText();
         indice = scontrino.indexOf(scelta);
         bool = listaS.contains(scelta);
         if (!bool){
             listaS.add(scelta);
             listaQ.add(-1);
             textArea.append(scelta+" Qt: -1 \n");
             
         } else
         if(indice>=0){

             for(i=0;i<listaS.size();i++){
                 if(scelta==listaS.get(i)&&(listaQ.get(i)>1||listaQ.get(i)<=-1)){
                     n=listaQ.get(i)-1;
                     listaQ.set(i, n);
                     temp = scelta+" Qt: "+listaQ.get(i)+"\n";
                     lenScelta = temp.length()+indice;
                     textArea.replaceRange(temp, indice, lenScelta);
                 
         	    	
                 } else if(scelta==listaS.get(i)&&listaQ.get(i)==1){
                     temp = scelta+" Qt: n  \n";
                     lenScelta = temp.length()+indice;
                     textArea.replaceRange("", indice, lenScelta);
                     listaQ.remove(i);
                     listaS.remove(i);
                     
                 }
             }
        }
         String tmpTextArea = this.textArea.toString();
         if(tmpTextArea.isEmpty()) this.btnOrdina.setEnabled(false);
         else this.btnOrdina.setEnabled(true);

    }
    

    
   public void ordiniamo(Vector<String> scelte, Vector<Integer> qnt, Tavolo numTav){
    	
       if(!scelte.isEmpty()){
    	   
	    	Integer numeroT = numTav.getNumTav();
	    	
	    	Ordine tmpOrd = new Ordine(numeroT);
	    	for(int i=0;i<scelte.size();i++) {
	    		System.out.println("scelta: "+scelte.get(i)+" qnt: "+qnt.get(i));
	    		tmpOrd.aggiungiPietanza(scelte.get(i), qnt.get(i));
	    		
	    	}
	    	numTav.addOrdine(tmpOrd);
	    	avviso.aggiungiOrdine(scelte, qnt, numTav, numTav.lastOrder());
	    	this.gestoreTavoli.allertaComanda(numTav);
	    	btnStato.doClick();
	    	showOrder(numTav,this.textAreaTavolo);
       }
    	
    }
    
    
   public void aggiornaTav(Tavolo tav, JTextArea txt) {
   		tav.setStatusOrdine(new OrdineConsegnato());
   		tav.setChiusura();
		gestoreTavoli.allertaComanda(tav);
		
		
   }
   
   public void aggiornaTextA(JTextArea textArea, String stringa, int indice, Vector<Integer> vInt){
       String temp;
       int lenScelta;
       temp = stringa+" Qt: "+vInt.get(indice)+"\n";
       lenScelta = temp.length();
       textArea.replaceRange(temp, indice, lenScelta);
   }
   
   private void showOrder(Tavolo tav, JTextArea txt){
   	txt.setText("");
   	for(Ordine ord : tav.getOrdine()) {
   		txt.append("Nell'ordine numero "+ord.getNumOrd()+" il tavolo selezionato ha ordinato:");
   		txt.append("\n");
   		for(Pietanze p : ord.getPietanze()) {
   			
   			txt.append("-"+p.getNome()+" x"+p.getQnt()+"\n");
       	}
   		txt.append("\t\t|-----------|\n");
   	}
   	
   }
   
   public void rinnovaTavolo(Tavolo tavolo) {
	   tav[tavolo.getNumTav()-1]=new Tavolo();
       tav[tavolo.getNumTav()-1].setNumTav(tavolo.getNumTav());
       frame.revalidate();
       btnStato.doClick();
       gestoreTavoli.allertaComanda(tav[tavolo.getNumTav()-1]);
   }
   
}
    



