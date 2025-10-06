import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.imageio.ImageIO;


public class ClienteFrame extends FrameBase  {
    
    // --- Nuova classe interna per lo sfondo ---
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        
        public BackgroundPanel(String path) {
            // Carica l'immagine
            try {
                // Assicurati che il percorso dell'immagine sia corretto
                backgroundImage = ImageIO.read(new File("immagini/white.jpg")); 
            } catch (IOException e) {
                System.err.println("ERRORE: Immagine di sfondo non trovata o non caricabile: " + path);
                // Fallback: usa un colore scuro e opaco come sfondo
                setBackground(new Color(40, 40, 40)); 
            }
            // 🏆 FIX: Il pannello di sfondo deve essere OPACO per disegnare l'immagine
            setOpaque(true); 
            // Imposta il Layout per contenere gli altri componenti
            setLayout(new BorderLayout(10, 10)); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Disegna l'immagine per coprire l'intero pannello
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                 // Disegna il colore di fallback
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
    // ------------------------------------------
    
    // Inizializza il pannello di sfondo
    private final BackgroundPanel backgroundPanel = new BackgroundPanel("white.jpg");
    
    // Variabili e componenti esistenti
    private int risposta; 
    private final Vector<String> vectorS = new Vector<>();
    private final Vector<Integer> vectorQ = new Vector<>();
    private final JCheckBox cBox = new JCheckBox();
    private Tavolo tav[];
    private final JTextArea textArea = new JTextArea(30,30); 
    private final JTextArea textAreaTavolo = new JTextArea(30,30); 
    private final MandaComande gestoreTavoli = new MandaComande();
    private final Serviamo avviso = new Serviamo();
    private final ArrayList<Admin> aList = new ArrayList<>();
    private final ArrayList<Cuoco> cList = new ArrayList<>();
    private final JComboBox<String> pizze = new JComboBox<>();
    private final JComboBox<String> primiPiatti = new JComboBox<>();
    private final JComboBox<String> bevande = new JComboBox<>();
    private final JComboBox<String> tavoli = new JComboBox<>();
    private final JLabel labelTav = new JLabel("Ordinazione per il tavolo n:");
    private final JButton btnOrdina = new JButton("Ordina");
    private final JButton btnServi = new JButton("Permetti chiusura");
    
    private final JButton btnAdd = new JButton("+");
    private final JButton btnDec = new JButton("-");        
    private final JButton btnAdd1 = new JButton("+");
    private final JButton btnDec1 = new JButton("-");        
    private final JButton btnAdd2 = new JButton("+");
    private final JButton btnDec2 = new JButton("-");        
    
    private final JButton btnStato = new JButton("   ");
    private Object selected;
    private final JPanel panPietanze = new JPanel(new GridBagLayout()); 
    private final Menu menu = new Menu();
    
    protected JButton btnHome = new JButton("Home"); 

    public ClienteFrame(ArrayList<Admin> admList, ArrayList<Cuoco> cuochiList) {
        super("Ordine Cliente", 1200, 600); 

        // Omissis: Logica di inizializzazione aList/cList
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
            if(!boolPizzaiolo) cList.add(new Pizzayolo());
            if(!boolChef) cList.add(new Chef());
        }
        start();
    }


    public void start(){
        
    	if(this.textArea.getText().isEmpty()) this.btnOrdina.setEnabled(false);
        else this.btnOrdina.setEnabled(true);
    	
	    Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	    Border loweredbevel = BorderFactory.createLoweredBevelBorder();
	    
        for(int i=0;i<cList.size();i++) {
            avviso.add(cList.get(i));
        }
        for(int i=0;i<aList.size();i++) {
            gestoreTavoli.add(aList.get(i));
        }
    	
        // 🏆 FIX: Rendi il panPietanze NON opaco per far trasparire l'immagine
		panPietanze.setOpaque(false); 
		panPietanze.setBorder(BorderFactory.createCompoundBorder(raisedbevel,loweredbevel));
		
        JLabel label = new JLabel("Ristorante Parthenope");
        label.setFont(new Font(null, Font.PLAIN,25));
        
        // 🏆 FIX: Rendi la JLabel NON opaca
        label.setOpaque(false);
        
        // 🏆 FIX: Imposta il layout del JFrame e aggiungi il pannello di sfondo
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(backgroundPanel, BorderLayout.CENTER);
        
        // Popolamento ComboBox
        for (int i=0;i<menu.getPizze().size();i++){
            pizze.addItem(menu.getPizze().get(i).getNome());
        }
        for(int j=0;j<menu.getBibite().size();j++) {
        	bevande.addItem(menu.getBibite().get(j).getNome());
        }
        for(int j=0;j<menu.getPrimiPiatti().size();j++) {
        	primiPiatti.addItem(menu.getPrimiPiatti().get(j).getNome());
        }
        
        // CREAZIONE DEI TAVOLI
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
        
        // JTextArea Ordine Corrente (Destra)
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createCompoundBorder(raisedbevel,loweredbevel));
        // 🏆 FIX: Rendi la textArea NON opaca
        textArea.setOpaque(false); 
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // 🏆 FIX: Rendi scrollpane e il suo viewport NON opachi
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false); 
        
        // JTextArea Stato Tavolo (Centro, in basso)
        textAreaTavolo.setBorder(BorderFactory.createCompoundBorder(raisedbevel,loweredbevel));
        textAreaTavolo.setEditable(false);
        // 🏆 FIX: Rendi la textArea NON opaca
        textAreaTavolo.setOpaque(false); 
        JScrollPane scrollTATav = new JScrollPane(textAreaTavolo);
         // 🏆 FIX: Rendi scrollpane e il suo viewport NON opachi
        scrollTATav.setOpaque(false);
        scrollTATav.getViewport().setOpaque(false);
        
        JButton btnClear = new JButton("Clear");
      	btnStato.setBackground(Color.white);
        
        // Listener per Clear
        btnClear.addActionListener(e -> {
        								this.vectorQ.clear();
        								this.vectorS.clear();
        								this.textArea.setText("");
        								this.textAreaTavolo.setText("");
        								selected = tavoli.getSelectedItem();
        				                Object copy = selected;
        				                Integer n = Integer.parseInt((String)copy);
        				                rinnovaTavolo(tav[n-1]); 
        								});
 
        // Listener JComboBox Tavoli
        tavoli.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                selected = tavoli.getSelectedItem();
                Integer n = Integer.parseInt((String)selected);
                
                if(tav[n-1].hasOrders()) {
                	btnServi.setEnabled(true);
                } else{
                	btnServi.setEnabled(false);
                }
                
                btnStato.doClick(); 
            }
        });
     	
        // BOTTONE ORDINAZIONE
        btnOrdina.addActionListener(e -> ordiniamo(vectorS, vectorQ, tav[Integer.parseInt((String) tavoli.getSelectedItem())-1]));
        btnOrdina.setEnabled(false);
        
        btnServi.setEnabled(false);
        // BOTTONE SERVI
        btnServi.addActionListener(e -> {
            aggiornaTav(tav[Integer.parseInt((String) tavoli.getSelectedItem())-1], textAreaTavolo);
            btnStato.doClick(); 
        	btnServi.setEnabled(false);
        	this.btnOrdina.setEnabled(false);
        });

        
        // Listener Tasti Quantità collegati ai ComboBox corretti
        // Pizze
        btnAdd.addActionListener(e -> addOrdine((String) pizze.getSelectedItem(), vectorS, vectorQ));
        btnDec.addActionListener(e -> decOrdine((String) pizze.getSelectedItem(), vectorS, vectorQ));
        // Bevande
        btnAdd1.addActionListener(e -> addOrdine((String) bevande.getSelectedItem(), vectorS, vectorQ)); 
        btnDec1.addActionListener(e -> decOrdine((String) bevande.getSelectedItem(), vectorS, vectorQ)); 
        // Primi piatti
        btnAdd2.addActionListener(e -> addOrdine((String) primiPiatti.getSelectedItem(), vectorS, vectorQ)); 
        btnDec2.addActionListener(e -> decOrdine((String) primiPiatti.getSelectedItem(), vectorS, vectorQ)); 
        
        // btn stato ordine
        btnStato.setFocusable(false);
        btnStato.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (selected==null && tavoli.getItemCount() > 0) {
					tavoli.setSelectedIndex(0);
                    selected = tavoli.getSelectedItem();
				} else if (selected == null && tavoli.getItemCount() == 0) {
                    return;
                }
				
					
				selected = tavoli.getSelectedItem();
				Integer i=Integer.parseInt(selected.toString());
				Tavolo currentTav = tav[i-1];
				
				showOrder(currentTav, textAreaTavolo);
					
				if(currentTav.getStatusOrdine() instanceof OrdineRicevuto) {
					btnStato.setBackground(Color.orange);
					btnOrdina.setEnabled(true);
					btnServi.setEnabled(true);
					
				}
				else if(currentTav.getStatusOrdine() instanceof OrdineConsegnato) {
					btnStato.setBackground(Color.green);
					btnOrdina.setEnabled(false);
					btnServi.setEnabled(false); 
					
				} else if(currentTav.getStatusOrdine() instanceof NoState) {
					btnStato.setBackground(Color.white);
					btnOrdina.setEnabled(true);
					btnServi.setEnabled(false);
					
				} 
			}
        	
        });
        
        // 🏆 MODIFICA: Aggiungi i pannelli e la label AL BACKGROUND PANEL
        backgroundPanel.add(label, BorderLayout.NORTH); // Aggiungi la label in alto
        backgroundPanel.add(scrollPane, BorderLayout.EAST); // Ordine corrente (Destra)
        backgroundPanel.add(panPietanze, BorderLayout.CENTER); // Controlli (Centro)
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        /*Menu pizze*/
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.01; gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        pizze.setBorder(raisedbevel);
        panPietanze.add(pizze,gbc);    
        
        /*Bottoni Pizza*/
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.01; gbc.weighty = 0.01;
        panPietanze.add(btnAdd,gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.01; gbc.weighty = 0.01;
        panPietanze.add(btnDec,gbc);
        
        /*Menu Bevande*/
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.01; gbc.weighty = 0.01;
        bevande.setBorder(raisedbevel);
        panPietanze.add(bevande,gbc);
        
        /*Bottoni bevande*/
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.01; gbc.weighty = 0.01;
        panPietanze.add(btnAdd1,gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.01; gbc.weighty = 0.01;
        panPietanze.add(btnDec1,gbc);
        
        /*Menu primi piatti*/
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.01; gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        primiPiatti.setBorder(raisedbevel);
        panPietanze.add(primiPiatti,gbc);    
        
        /*Bottoni Primi piatti*/
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.01; gbc.weighty = 0.01;
        panPietanze.add(btnAdd2,gbc);
        
        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0.01; gbc.weighty = 0.01;
        panPietanze.add(btnDec2,gbc);
        
        /*Label scegli tavolo*/
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.01; gbc.weighty = 0.01;
        panPietanze.add(labelTav,gbc);
        
        /*Jcombobox tavoli*/
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.01; gbc.weighty = 0.01;
        tavoli.setBorder(raisedbevel);
        panPietanze.add(tavoli,gbc);
        
        /*Stato del tavolo*/
        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0.01; gbc.weighty = 0.01;
        panPietanze.add(btnStato,gbc);
        
        /*Bottoni ordina e servi*/
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.02; gbc.weighty = 0.02;
        panPietanze.add(btnOrdina,gbc);
        
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.02; gbc.weighty = 0.02;
        panPietanze.add(btnServi,gbc);
        
        gbc.gridx = 2; gbc.gridy = 4; gbc.weightx = 0.02; gbc.weighty = 0.02;
        panPietanze.add(btnClear,gbc);
        
        /*TextArea tavolo*/
        gbc.gridx = 0; gbc.gridy = 5; 
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 3;
        panPietanze.add(scrollTATav, gbc);
        
        this.revalidate();
    }
    
    // ----------------------------------------------------
    // METODI LOGICI (Invariati)
    // ----------------------------------------------------

	public void addOrdine(String scelta, Vector<String> listaS, Vector<Integer> listaQ){
        int index = listaS.indexOf(scelta);
        
        if (index == -1) {
            listaS.add(scelta);
            listaQ.add(1);
        } else {
            int currentQ = listaQ.get(index);
            listaQ.set(index, currentQ + 1);
        }
        
        updateOrdineListTextArea();
    }

    public void decOrdine(String scelta, Vector<String> listaS, Vector<Integer> listaQ){
        int index = listaS.indexOf(scelta);

        if (index != -1) {
            int currentQ = listaQ.get(index);
            
            if (currentQ > 1) {
                listaQ.set(index, currentQ - 1);
            } else if (currentQ == 1) {
                listaS.remove(index);
                listaQ.remove(index);
            } 
        }
        
        updateOrdineListTextArea();
    }

    private void updateOrdineListTextArea() {
        textArea.setText("");
        for (int i = 0; i < vectorS.size(); i++) {
            int q = vectorQ.get(i);
            if (q != 0) { 
                textArea.append(vectorS.get(i) + " Qt: " + q + "\n");
            }
        }
        
        boolean isListEmpty = vectorS.isEmpty();
        this.btnOrdina.setEnabled(!isListEmpty && selected != null);
    }

   public void ordiniamo(Vector<String> scelte, Vector<Integer> qnt, Tavolo numTav){
    	
       if(!scelte.isEmpty()){
    	   
	    	Integer numeroT = numTav.getNumTav();
	    	
	    	Ordine tmpOrd = new Ordine(numeroT);
	    	for(int i=0;i<scelte.size();i++) {
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
   
   // VISUALIZZAZIONE STATO TAVOLO (CORRETTA)
   private void showOrder(Tavolo tav, JTextArea txt){
   	txt.setText("");
   	
   	txt.append("========================================\n");
   	txt.append("  STATO TAVOLO: " + tav.getNumTav() + "\n");
   	txt.append("  Stato Corrente: " + tav.getStatusOrdine().toString() + "\n");
   	txt.append("========================================\n");
   	
   	for(Ordine ord : tav.getOrdine()) {
   		txt.append("\n[ Ordine n. " + ord.getNumOrd() + " ]\n");
   		txt.append("----------------------------------------\n");
   		
   		for(Pietanze p : ord.getPietanze()) {
   			txt.append("  - " + p.getNome() + " x" + p.getQnt() + "\n");
       	}
   		txt.append("----------------------------------------\n");
   	}
   	
   }
   
   public void rinnovaTavolo(Tavolo tavolo) {
	   tav[tavolo.getNumTav()-1]=new Tavolo();
       tav[tavolo.getNumTav()-1].setNumTav(tavolo.getNumTav());
       this.revalidate(); 
       btnStato.doClick();
       gestoreTavoli.allertaComanda(tav[tavolo.getNumTav()-1]);
   }
   
}