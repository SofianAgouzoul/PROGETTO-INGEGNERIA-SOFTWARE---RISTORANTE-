import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

// 🟢 CORREZIONE 1: Estende la classe base JFrame-oriented
public class Chef extends FrameBase implements Cuoco {

	
	private final ArrayList<Tavolo> reference = new ArrayList<>();
	private final ArrayList<String> listaPrimiPiatti = new ArrayList<>();
	private final ArrayList<Ordine> listaOrdini = new ArrayList<>();
    
    // 🟢 CORREZIONE 2: Rinominato il panel principale per chiarezza e per evitare conflitti
    private final JPanel panelListaOrdini = new JPanel(); 
    
	private final JTextArea textArea = new JTextArea(50,40);
	private final JPanel textPanel = new JPanel();
	private final JButton nextOrd = new JButton("Ordine concluso");
	private Integer ordineSelezionato;
	private final JPanel botPanel = new JPanel(new FlowLayout());
	private final JButton btnStorico = new JButton("Storico ordini");
	
	
	public Chef() {
		// 🟢 Chiama il costruttore della superclasse
		super("Gestionale Ristorante - Chef"); 
        this.setLayout(new BorderLayout()); // Imposta il layout principale come BorderLayout

		Border raisedbevel, loweredbevel;
	    
		raisedbevel = BorderFactory.createRaisedBevelBorder();
		loweredbevel = BorderFactory.createLoweredBevelBorder();
        
        // Configurazione Panel Lista Ordini (sinistra)
		panelListaOrdini.setLayout(new BoxLayout(panelListaOrdini, BoxLayout.Y_AXIS));
        panelListaOrdini.setBackground(new Color(210, 210, 210));

        JScrollPane scrollPanel = new JScrollPane(panelListaOrdini);
        scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Configurazione Text Area (centro)
		textArea.setFont(new Font("Courier", Font.BOLD, 15));
		textArea.setEditable(false);
		textPanel.add(textArea);
		textPanel.setBackground(new Color(255, 253, 208));
		textPanel.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));

        JScrollPane textScrollPanel = new JScrollPane(textPanel);
        textScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        // Configurazione Bottom Panel (sud)
		botPanel.setBackground(new Color(210, 210, 210));
		botPanel.add(btnStorico);


        // Listener per 'Ordine concluso'
		nextOrd.addActionListener(e -> {
			cucina(this.ordineSelezionato);
			nextOrd.setEnabled(false);
			this.revalidate(); // 🟢 Usa 'this'
		});
		
		// 🟢 CORREZIONE 4: Aggiunge tutti i componenti al JFrame (this)
        this.add(scrollPanel, BorderLayout.WEST);     // Lista Ordini (Sinistra)
        this.add(textScrollPanel, BorderLayout.CENTER); // Dettagli Ordine (Centro)
        this.add(nextOrd, BorderLayout.EAST);         // Bottone Concluso (Destra)
        this.add(botPanel, BorderLayout.SOUTH);       // Bottoni extra (Sotto)
        
        // Inizializzazione Lista Primi Piatti
		Menu menu = new Menu(); // Assumiamo Menu.java sia disponibile
		for(Pietanze element : menu.getPrimiPiatti()) {
			listaPrimiPiatti.add(element.getNome());
		}
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	@Override
	public void updateTODO(Tavolo tav, Ordine ordine) {
		
		Ordine ordTmp = new Ordine(ordine.getNumTavolo()); 
		
		// Logica di riferimento tavolo (invariata)
		boolean trovato = false;
		for(Tavolo t : this.reference) {
			if(t.getNumTav() == tav.getNumTav()) {
				trovato = true;
				break;
			}
		}
		if(!trovato) {
			reference.add(tav);
		}
        
        // 🟢 CORREZIONE 5: Usa .equals() per confrontare le stringhe
		for(Pietanze elem : ordine.getPietanze()) {
			for(String nomePietanza : listaPrimiPiatti) {
				if(nomePietanza.equals(elem.getNome())) { // ✅ CORREZIONE CRITICA: Usa .equals()
					ordTmp.aggiungiPietanza(nomePietanza, elem.getQnt());
					break;
				}
			}
		}
        
		if(!ordTmp.getPietanze().isEmpty()) {
			listaOrdini.add(ordTmp);
			JButton btnTmp = new JButton("Ordine numero: "+ordine.getNumOrd());
			btnTmp.addActionListener(e -> {
				textArea.setText("Ordine numero: "+ordine.getNumOrd()+"\n");
				for(Pietanze p : ordTmp.getPietanze()) {
					textArea.append(p.getNome()+" x"+p.getQnt()+"\n");
				}
				this.ordineSelezionato = ordine.getNumTavolo();
				if(ordTmp.getState() instanceof OrdineConsegnato)
					nextOrd.setEnabled(true);
			});
			
            // 🟢 Aggiunge al panel rinominato
			panelListaOrdini.add(btnTmp); 
			
			this.revalidate(); // 🟢 Usa 'this'
			// ⚠️ Logica da spostare: questa modifica di stato DEVE essere gestita dal GestoreRistorante
			// tav.setStatusOrdine(new OrdineRicevuto()); 
		}
		
	}
	
	// ... (Metodo cucina invariato nella sua logica)
	public void cucina(Integer numTavolo) {
		System.out.println("Lo chef sta cucinando");
		
		// ... Logica di aggiornamento dello stato e rimozione dal reference ...
		
		// Logica di aggiornamento stato (invariata)
        for(Ordine ord : this.listaOrdini) {
             if(ord.getNumTavolo()==numTavolo) {
                ord.setState(new OrdineConsegnato());
             }
        }
        
        // Logica di rimozione dal reference (invariata)
        Integer delete=0;
        Boolean changed=false;
        for(Integer i=0;i<reference.size();i++) {
            if(reference.get(i).getNumTav()==numTavolo) {
                reference.get(i).setStatusOrdine(new OrdineConsegnato());
                changed=true;
                delete=i;
            }
        }
        if(changed) {
            reference.remove(delete);
        }
	}
}