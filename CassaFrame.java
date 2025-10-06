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

// 🟢 Cambiamento 1: Estende FrameBase (il wrapper di JFrame)
public class CassaFrame extends FrameBase {
    
    private final JButton btnHome = new JButton("Home");
    private final Vector<JButton> btnTav = new Vector<>();
    private final JTextArea textArea;
    private final JPanel panel;
    private final JButton btnLiberaT;
    
    // 🟢 Cambiamento 2: Memorizza il tavolo attualmente selezionato
    private Integer tavoloSelezionato = null; 

    public CassaFrame() {
        // 🟢 Chiama il costruttore della superclasse e imposta il titolo
        super("Cassa"); 
        this.setLayout(new BorderLayout()); 

        Border raisedetched, loweredetched;
        raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        
        
        btnLiberaT = new JButton("Libera");
        textArea = new JTextArea(30, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        JButton btnClear = new JButton("Clear");
        // 🟢 Pulizia: Pulisce la TextArea
        btnClear.addActionListener(e -> {textArea.setText(""); this.tavoloSelezionato = null;}); 
        
        // 🟢 Gestione Home (assumendo che MainFrame esista e si voglia chiudere questa finestra)
        btnHome.addActionListener(e -> {new MainFrame().setVisible(true); this.dispose();}); 
        
        btnLiberaT.addActionListener(e -> {liberaTav();});
        
        
        panel = new JPanel();
        panel.setBackground(new Color(152, 255, 152));
        
        
        // Setup bottoni tavolo
        for(int i = 1; i <= 20; i++) {
            btnTav.add(new JButton("Tavolo: " + i));
            this.panel.add(btnTav.get(i-1));
        }
        
        aggiornaTavoli();
        
        
        // 🟢 Aggiunge i componenti al JFrame (this)
        this.add(panel, BorderLayout.CENTER);
        this.add(scrollPane, BorderLayout.LINE_END);
        
        panel.add(btnClear);
        panel.add(btnHome);
        panel.add(btnLiberaT);
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        this.setVisible(true);
    }

    
    public void aggiornaTavoli() {
        
        for(int i = 1; i <= 20; i++) {
            final int tavoloNum = i;
            File myObj = new File("tav" + i + ".txt");
            JButton currentBtn = btnTav.get(i - 1);
            
            // 🟢 CORREZIONE 3: Rimuove tutti i listener esistenti per evitare duplicazioni
            for (ActionListener al : currentBtn.getActionListeners()) {
                currentBtn.removeActionListener(al);
            }

            if (myObj.exists()) {
                System.out.println("Dentro aggiornaTavoli" + i);
                
                // Aggiunge un nuovo listener all'ordine
                currentBtn.addActionListener(e -> showOrder(tavoloNum, myObj));
                currentBtn.setEnabled(true);
            } else { 
                currentBtn.setEnabled(false);
            }
        }
    }
    
    // 🟢 Riscritto per usare i campi d'istanza e try-with-resources
    private void showOrder(int i, File file){
        
        // 🟢 Memorizza il tavolo selezionato
        this.tavoloSelezionato = i; 

        // Usiamo try-with-resources per garantire che lo Scanner venga chiuso
        try (Scanner myReader = new Scanner(file)) {
            
            // Pulisce e inizia a popolare la TextArea
            textArea.setText("Il tavolo " + i + " ha ordinato:\n");
            
            while(myReader.hasNextLine()) {
                textArea.append(myReader.nextLine() + "\n");
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Scanner non trovato per tav" + i + ".txt");
        }
    }
    
    public void liberaTav() {
        // 🟢 CORREZIONE 4: Usa il tavolo memorizzato, molto più robusto
        if (this.tavoloSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Nessun tavolo selezionato per la liberazione.", "Errore", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int n = this.tavoloSelezionato;
        
        File tavolo = new File("tav" + n + ".txt");
        
        if (tavolo.exists()) {
             if (tavolo.delete()) {
                 JOptionPane.showMessageDialog(this, "Tavolo " + n + " liberato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
             } else {
                 JOptionPane.showMessageDialog(this, "Errore durante la liberazione del tavolo " + n + ".", "Errore File", JOptionPane.ERROR_MESSAGE);
             }
        } else {
             JOptionPane.showMessageDialog(this, "File ordine per tavolo " + n + " non trovato.", "Errore", JOptionPane.WARNING_MESSAGE);
        }
        
        // Pulisce la visualizzazione e aggiorna lo stato
        textArea.setText("");
        this.tavoloSelezionato = null;
        aggiornaTavoli();
    }
}