import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// 🏆 CORREZIONE: Estende direttamente JFrame per una corretta ereditarietà
public abstract class FrameBase extends JFrame {
    
    // Campi protetti ereditati (come nel tuo Frame.java originale)
    protected JPanel panel = new JPanel(new BorderLayout());
    protected JButton btnSala = new JButton();
    protected JButton btnCliente = new JButton();
    protected JButton btnChef = new JButton();
    protected JButton btnPizzayolo = new JButton();
    protected JButton btnCassiere = new JButton(); 
    protected ImageIcon img = new ImageIcon("icon.png");
    protected JLabel label = new JLabel();
    protected JMenuBar menuBar = new JMenuBar();
    
    // Costruttore che imposta la dimensione predefinita (800x600)
    public FrameBase(String title) {
        // Chiama il costruttore esteso con le dimensioni standard
        this(title, 800, 600); 
    }
    
    // NUOVO COSTRUTTORE: Permette di specificare Larghezza e Altezza
    public FrameBase(String title, int width, int height) {
        super(title);
        
        this.setSize(width, height);
        this.setResizable(false); // Impedisce il ridimensionamento
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null); // Centra la finestra
        
        if (img.getImage() != null) {
            this.setIconImage(img.getImage());
        }
        
        this.setVisible(true);
    }
    
    // Costruttore di fallback
    public FrameBase() {
        this("Applicazione Ristorante");
    }
}