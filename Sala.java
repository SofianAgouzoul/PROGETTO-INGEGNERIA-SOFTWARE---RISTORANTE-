import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

// 🟢 Cambiamento 1: Estende la classe base FrameBase (che estende JFrame)
public class Sala extends FrameBase implements Admin {
    
    // Dichiarazioni: Uso final dove possibile per indicare che non cambieranno riferimento
    private final JButton btnHome = new JButton("Home");
    private final Vector<JButton> btnTav = new Vector<>();
    private final JTextArea textArea;
    
    // Il campo 'panel' è dichiarato qui, sovrascrivendo potenzialmente quello ereditato.
    // Lo manteniamo, ma lo gestiamo correttamente.
    private JPanel panel; 
    
    private JButton btnLiberaT; // Viene riassegnato nel costruttore

    public Sala() { 
        // 🟢 Cambiamento 2: Chiama il costruttore della superclasse per impostare il titolo e la configurazione base (dimensioni, icona, ecc.)
        super("Sala"); 
        
        // Definisce un layout generale per il JFrame (BorderLayout è standard)
        this.setLayout(new BorderLayout()); 

        Border raisedbevel, loweredbevel;
        
        raisedbevel = BorderFactory.createRaisedBevelBorder();
        loweredbevel = BorderFactory.createLoweredBevelBorder();
        
        // ⚠️ Nota: Stai riassegnando l'oggetto 'btnLiberaT', usa una singola inizializzazione
        btnLiberaT = new JButton("Libera");
        
        textArea = new JTextArea(30, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> {textArea.setText("");});
        
        // La logica di liberaTav() è molto fragile (vedi nota sotto)
        btnLiberaT.addActionListener(e -> {liberaTav();});
        
        
        // 🟢 Configurazione del JPanel che contiene i bottoni dei tavoli
        panel = new JPanel();
        panel.setBackground(new Color(172, 255, 175));
        panel.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));

        textArea.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));
        
        // Creazione e aggiunta dei bottoni Tavolo al panel
        for (int i = 1; i <= 20; i++) {
            btnTav.add(new JButton("Tavolo: " + i));
            btnTav.get(i - 1).setEnabled(false);
            this.panel.add(btnTav.get(i - 1));
        }
        
        // Aggiunta di Clear, Home e LiberaT al panel (Layout non definito: FlowLayout implicito)
        panel.add(btnClear);
        panel.add(btnHome);
        panel.add(btnLiberaT);
       
        // 🟢 Cambiamento 3: Aggiunge i componenti al JFrame (this)
        this.add(panel, BorderLayout.CENTER); // Pannello principale al centro
        this.add(scrollPane, BorderLayout.LINE_END); // Log del tavolo a destra
        
        // 🟢 Cambiamento 4: Usa 'this'
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    public void liberaTav() {
        // ⚠️ NOTA CRITICA: La logica qui è estremamente fragile! 
        // Dipende dall'esatta formattazione del testo in textArea: "Tavolo n<num>:\n"
        // Se il testo è diverso, la sottostringa fallisce.
        System.out.println("<SYSTEM>:: Sto per liberare un tavolo!");
        String txt, numTav;
        int n;
        txt = this.textArea.getText();
        System.out.println("<SYSTEM>:: txt contiene: " + txt);
        
        // La logica di substring è corretta per lo scopo che si prefigge, ma molto fragile.
        // Se il tavolo è 1, il numero è 9. Se è 10, il numero è 9 e 10.
        numTav = txt.substring(8, 10).trim(); // Prende i caratteri che potrebbero essere il numero

        if(numTav.contains(":"))
            numTav = numTav.substring(0, numTav.indexOf(":"));
        
        try {
            n = Integer.parseInt(numTav);
            System.out.println("<SYSTEM>:: Liberazione tavolo numero " + n);
            File tavolo = new File("tav" + n + ".txt");
            // tavolo.delete(); // Logica di business da spostare nel GestoreRistorante
        } catch (NumberFormatException e) {
            System.err.println("<SYSTEM>:: Errore: Impossibile estrarre il numero del tavolo dalla TextArea.");
            JOptionPane.showMessageDialog(this, "Errore nella lettura del numero del tavolo.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public void aggiungiComanda(Tavolo tavolo) {
        // ... (Logica di rimozione/aggiunta ActionListener invariata) ...
        
        // Nota: Assicurati che Tavolo.getNumTav() sia 1-based (da 1 a 20)
        int index = tavolo.getNumTav() - 1;
        
        for(ActionListener al : btnTav.get(index).getActionListeners()) {
             btnTav.get(index).removeActionListener(al);
             System.out.println("\nrimozione Al");
        }
        
        if (!tavolo.getOrdine().isEmpty()) {
            this.btnTav.get(index).setEnabled(true);
            this.btnTav.get(index).addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getInfo(tavolo);
                }
            });
        } else {
            this.btnTav.get(index).setEnabled(false);
            this.textArea.setText("");
        }
        
        // 🟢 Cambiamento 5: Usa 'this'
        this.validate(); 
    }

    public void getInfo(Tavolo tavolo) {
        // ... (Logica di visualizzazione invariata) ...
        this.textArea.setText("");
        this.textArea.append("Tavolo n" + tavolo.getNumTav() + ":\n"); // Questa riga crea il testo che liberaTav() tenta di leggere!

        Double totParziale = 0.0;
        for(Ordine ord : tavolo.getOrdine()) {
            for(Pietanze p : ord.getPietanze()) {
                textArea.append(p.getNome() + " x" + p.getQnt() + " (" + p.getPrezzo() + " e)\n");
                totParziale += p.getPrezzo() * p.getQnt();
            }
            textArea.append("Il totale relativo a quest'ordine e': " + totParziale + "\n");
            totParziale = 0.0;
            textArea.append("\n\t|----------------------| \n");
        }
    }
}