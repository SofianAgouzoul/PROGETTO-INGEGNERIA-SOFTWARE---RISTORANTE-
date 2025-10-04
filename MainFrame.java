import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


class ImagePanel extends JPanel {
    private Color bgColor;

    // Costruttore che accetta BufferedImage (come l'originale) ma usa un colore di default
    public ImagePanel(BufferedImage image) {
        // Ignoro l'immagine, imposto un colore di sfondo predefinito o trasparente
        // Useremo il colore impostato esternamente tramite setBackground()
    }

    // Aggiungo un costruttore che accetta direttamente il colore per un uso più pulito
    public ImagePanel(Color bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Disegna lo sfondo con il colore definito
        if (bgColor != null) {
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        // Se volessimo usare il colore del container, useremmo: g.setColor(getBackground());
    }
}

// ====================================================================
// CLASSE PRINCIPALE MainFrame
// ====================================================================
public class MainFrame extends JFrame {
    private JButton btnSala, btnCliente, btnPizzayolo, btnCassiere, btnChef; 
    private JPanel panel, panelSala, panelPizzayolo, panelChef, panelCassiere, panelClienti;
    // Queste liste sono commentate per la compilazione, se le classi non esistono:
    private ArrayList<Admin> admList = new ArrayList<>();
    private ArrayList<Cuoco> cuochiList = new ArrayList<>();

    // Definisco i colori da usare
    private static final Color CLIENTE_COLOR = new Color(240, 248, 255); 
    private static final Color SALA_COLOR = new Color(255, 239, 213);   
    private static final Color CASSA_COLOR = new Color(224, 255, 255);  
    private static final Color CHEF_COLOR = new Color(245, 245, 220);   
    private static final Color PIZZAIOLO_COLOR = new Color(250, 235, 215); 


    public MainFrame() {
        // Carico immagini per i vari pannelli (originale: white.jpg, b-pizza.png)
        // Queste immagini vengono caricate MA NON USATE come sfondo grazie a ImagePanel modificato.
        BufferedImage imgCliente = loadImage("immagini/white.jpg");
        BufferedImage imgSala = loadImage("immagini/white.jpg");
        BufferedImage imgCassa = loadImage("immagini/white.jpg");
        BufferedImage imgChef = loadImage("immagini/white.jpg");
        BufferedImage imgPizzaiolo = loadImage("immagini/b-pizza.png");

        // Carico le icone per i bottoni
        BufferedImage iconCliente = loadImage("immagini/User.png");
        BufferedImage iconSala = loadImage("immagini/dining-table.png");
        BufferedImage iconCassa = loadImage("immagini/cash-register.png");
        BufferedImage iconChef = loadImage("immagini/chef.png");
        BufferedImage iconPizzaiolo = loadImage("immagini/pizza.png");

        // Layout principale: 5 righe in verticale
        panel = new JPanel(new GridLayout(5, 1));

        Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        // ---- Pannelli con immagine (per compatibilità) + bottoni ----
        JButton[] ref = new JButton[1];

        panelClienti = createImageButtonPanel(imgCliente, CLIENTE_COLOR, "Cliente", iconCliente, ref);
        btnCliente = ref[0];

        panelSala = createImageButtonPanel(imgSala, SALA_COLOR, "Sala", iconSala, ref);
        btnSala = ref[0];

        panelCassiere = createImageButtonPanel(imgCassa, CASSA_COLOR, "Cassa", iconCassa, ref);
        btnCassiere = ref[0];

        panelChef = createImageButtonPanel(imgChef, CHEF_COLOR, "Chef", iconChef, ref);
        btnChef = ref[0];

        panelPizzayolo = createImageButtonPanel(imgPizzaiolo, PIZZAIOLO_COLOR, "Pizzaiolo", iconPizzaiolo, ref);
        btnPizzayolo = ref[0];

        // Imposto colori di sfondo per i bottoni
        btnCliente.setBackground(new Color(200, 220, 240));
        btnSala.setBackground(new Color(200, 240, 200));
        btnCassiere.setBackground(new Color(240, 200, 200));
        btnChef.setBackground(new Color(240, 240, 200));
        btnPizzayolo.setBackground(new Color(200, 200, 240));

        // Bordi
        panelClienti.setBorder(BorderFactory.createCompoundBorder(raisedetched, loweredetched));
        panelSala.setBorder(BorderFactory.createCompoundBorder(raisedetched, loweredetched));
        panelChef.setBorder(BorderFactory.createCompoundBorder(raisedetched, loweredetched));
        panelPizzayolo.setBorder(BorderFactory.createCompoundBorder(raisedetched, loweredetched));
        panelCassiere.setBorder(BorderFactory.createCompoundBorder(raisedetched, loweredetched));

        // Aggiungo i pannelli al contenitore principale
        panel.add(panelClienti);
        panel.add(panelSala);
        panel.add(panelCassiere);
        panel.add(panelChef);
        panel.add(panelPizzayolo);

       
        // ----Azioni dei bottoni 
        btnSala.addActionListener(e -> admList.add(new Sala()));
        btnCliente.addActionListener(_ -> new ClienteFrame(admList, cuochiList));
        btnCassiere.addActionListener(e -> admList.add(new Cassiere()));
        btnPizzayolo.addActionListener(e -> cuochiList.add(new Pizzayolo()));
        btnChef.addActionListener(e -> cuochiList.add(new Chef()));

        // ---- Impostazioni JFrame ----
        setTitle("Gestionale Ristorante");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setResizable(false);
        add(panel);
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    // Funzione modificata per creare pannello con sfondo colorato
    private JPanel createImageButtonPanel(BufferedImage bgImg, Color bgColor, String text, BufferedImage iconImg, JButton[] outBtn) {
        // Uso ImagePanel passandogli il colore, ignorando bgImg (per coerenza strutturale)
        ImagePanel p = new ImagePanel(bgColor); 
        p.setLayout(new BorderLayout());

        // Etichetta con icona a sinistra
        JLabel lblIcon = new JLabel();
        if (iconImg != null) {
            Image scaledIcon = iconImg.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(scaledIcon));
        }
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcon.setPreferredSize(new Dimension(80, 80));

        // Bottone con solo testo a destra
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setFocusPainted(false);
        btn.setForeground(Color.BLACK); 

        // Ritorno il bottone tramite array
        outBtn[0] = btn;

        // Aggiungo al pannello
        p.add(lblIcon, BorderLayout.WEST);
        p.add(btn, BorderLayout.EAST);

        return p;
    }

    // Caricamento sicuro immagine
    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Immagine non trovata: " + path);
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}