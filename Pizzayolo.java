import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

// Assumiamo che FrameBase esista e estenda JFrame
public class Pizzayolo extends FrameBase implements Cuoco { 
    
    private ArrayList<Tavolo> reference = new ArrayList<Tavolo>();
    private ArrayList<String> listaPizze = new ArrayList<String>();
    private ArrayList<Ordine> listaOrdini = new ArrayList<Ordine>();
    private JTextArea textArea = new JTextArea(50,40);
    private JPanel textPanel = new JPanel();
    private JButton nextOrd = new JButton("Ordine concluso");
    private Integer ordineSelezionato;
    private JButton selectedButton = null; 
    private JPanel botPanel = new JPanel(new FlowLayout());
    private JButton btnStorico = new JButton("Storico ordini");
    private JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
    private Menu menu = new Menu(); 

    // Campi ereditati per evitare errori di compilazione
    protected JButton btnSala = new JButton();
    protected JButton btnCliente = new JButton();
    protected JButton btnChef = new JButton();
    protected JButton btnPizzayolo = new JButton();
    protected JButton btnCassiere = new JButton(); 
    protected JLabel label = new JLabel();
    protected JMenuBar menuBar = new JMenuBar();
    
    public Pizzayolo() {
        super("Gestionale Ristorante - Pizzaiolo"); 
        
        // Popola lista pizze
        for (Pietanze p : menu.getPizze()) {
            listaPizze.add(p.getNome());
        }
        
        Border raisedetched, loweredetched,raisedbevel, loweredbevel;
        
        raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        raisedbevel = BorderFactory.createRaisedBevelBorder();
        loweredbevel = BorderFactory.createLoweredBevelBorder();
        
        // Configurazione pannelli e layout
        panel.setBorder(BorderFactory.createTitledBorder("Ordini da preparare"));
        panel.setBackground(new Color(255, 239, 213)); 
        textPanel.setBackground(new Color(255, 239, 213));

        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));
        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel.add(scrollPane);

        botPanel.add(nextOrd);
        botPanel.add(btnStorico);
        
        // Listener per il bottone di completamento ordine
        nextOrd.setEnabled(false);
        nextOrd.addActionListener(e -> {
            if (ordineSelezionato != null) {
                infornaPizze(ordineSelezionato);
            }
        });
        
        // Aggiungi i pannelli al JFrame (this)
        this.getContentPane().setLayout(new BorderLayout(10, 10));
        this.getContentPane().add(panel, BorderLayout.WEST);
        this.getContentPane().add(textPanel, BorderLayout.CENTER);
        this.getContentPane().add(botPanel, BorderLayout.SOUTH);
        
        this.revalidate();
    }
    
    @Override
    public void updateTODO(Tavolo tav, Ordine ordine) {
        Boolean trovato = false;
        // 🏆 FIX RIGA 81 (e 141): Rende la variabile 'final' per l'uso nelle lambda
        final Ordine ordTmp = new Ordine(tav.getNumTav()); 
        
        for(Tavolo t : reference) {
            if(t.getNumTav() == tav.getNumTav()) {
                trovato = true;
                break;
            }
        }
        if(!trovato) {
            reference.add(tav);
        }
        
        // FILTRO: Aggiunge solo le pizze
        for(Pietanze elem : ordine.getPietanze()) {
            for(String nomePietanza : listaPizze) {
                if(nomePietanza.equals(elem.getNome())) { 
                    ordTmp.aggiungiPietanza(nomePietanza, elem.getQnt());
                    break;
                }
            }
        }
        
        // Se ci sono pizze nell'ordine
        if(!ordTmp.getPietanze().isEmpty()) {
            listaOrdini.add(ordTmp);
            JButton btnTmp = new JButton("Tavolo: "+tav.getNumTav()+" Ordine: "+ordine.getNumOrd());
            
            btnTmp.addActionListener(e -> {
                // Aggiorna la vista principale
                textArea.setText("Ordine numero: "+ordine.getNumOrd()+"\n");
                textArea.append("Tavolo: "+tav.getNumTav()+"\n");
                textArea.append("----------------------------\n");
                // Questa lambda usa ordTmp, che ora è final.
                for(Pietanze p : ordTmp.getPietanze()) { 
                    textArea.append(p.getNome()+" x"+p.getQnt()+"\n");
                }
                // Imposta la selezione corrente
                this.ordineSelezionato = tav.getNumTav(); 
                this.selectedButton = btnTmp; 
                
                nextOrd.setEnabled(true);
            });
            panel.add(btnTmp);
            this.revalidate(); 
            tav.setStatusOrdine(new OrdineRicevuto());
        }
    }
    
    /**
     * Logica per completare l'ordine della pizza.
     */
    public void infornaPizze(Integer numTavolo) {
        if (selectedButton == null) return;
        
        // 1. Trova e rimuovi l'Ordine da listaOrdini
        Integer numOrd = extractNumOrdFromButton(selectedButton);
        
        Ordine orderToRemove = null;
        // Bisogna cercare l'ordine completo usando sia tavolo che numero d'ordine
        // Nota: Assicurati che ogni Ordine in listaOrdini abbia anche il campo numTavolo impostato.
        for(Ordine ord : listaOrdini) {
            if (ord.getNumTavolo() == numTavolo && ord.getNumOrd() == numOrd) {
                orderToRemove = ord;
                break;
            }
        }
        
        if (orderToRemove != null) {
            listaOrdini.remove(orderToRemove);
        }

        // 2. Rimuovi il pulsante dal pannello
        panel.remove(selectedButton);
        
        // 3. Pulisci l'area di testo e resetta la selezione
        textArea.setText("");
        selectedButton = null;
        ordineSelezionato = null;
        nextOrd.setEnabled(false);

        // 4. Ridisegna il pannello
        panel.revalidate();
        panel.repaint();
        
        System.out.println("Pizze per il tavolo " + numTavolo + " completate.");
    }
    
    /** * Metodo di supporto per estrarre l'ID Ordine dal testo del bottone. */
    private Integer extractNumOrdFromButton(JButton btn) {
        String text = btn.getText();
        try {
            int indexOrd = text.indexOf("Ordine: ") + 8;
            String numOrdStr = text.substring(indexOrd).trim();
            return Integer.parseInt(numOrdStr);
        } catch (Exception e) {
            // Se fallisce l'estrazione, torna un valore non valido
            return -1; 
        }
    }
}