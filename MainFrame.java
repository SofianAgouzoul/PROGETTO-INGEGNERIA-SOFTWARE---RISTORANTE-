import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/* Pannello personalizzato con immagine ridimensionata */
class ImagePanel extends JPanel {
    private BufferedImage image;

    public ImagePanel(BufferedImage image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            g2.dispose();
        }
    }
}

public class MainFrame extends JFrame {
    private JButton btnSala, btnCliente, btnPizzayolo, btnCassiere, btnChef; 
    private JPanel panel, panelSala, panelPizzayolo, panelChef, panelCassiere, panelClienti;
    private ArrayList<Admin> admList = new ArrayList<>();
    private ArrayList<Cuoco> cuochiList = new ArrayList<>();

    public MainFrame() {
        // Carico immagini per i vari pannelli
        BufferedImage imgCliente   = loadImage("immagini/white.jpg");
        BufferedImage imgSala      = loadImage("immagini/white.jpg");
        BufferedImage imgCassa     = loadImage("immagini/white.jpg");
        BufferedImage imgChef      = loadImage("immagini/white.jpg");
        BufferedImage imgPizzaiolo = loadImage("immagini/white.jpg");

        // Carico le icone per i bottoni
        BufferedImage iconCliente   = loadImage("immagini/User.png");
        BufferedImage iconSala      = loadImage("immagini/dining-table.png");
        BufferedImage iconCassa     = loadImage("immagini/cash-register.png");
        BufferedImage iconChef      = loadImage("immagini/chef.png");
        BufferedImage iconPizzaiolo = loadImage("immagini/pizza.png");

        // Layout principale: 5 righe in verticale
        panel = new JPanel(new GridLayout(5, 1));

        Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        // ---- Pannelli con immagini + bottoni con icona ----
        panelClienti = createImageButtonPanel(imgCliente, "Cliente", iconCliente);
        panelSala    = createImageButtonPanel(imgSala, "Sala", iconSala);
        panelCassiere= createImageButtonPanel(imgCassa, "Cassa", iconCassa);
        panelChef    = createImageButtonPanel(imgChef, "Chef", iconChef);
        panelPizzayolo = createImageButtonPanel(imgPizzaiolo, "Pizzaiolo", iconPizzaiolo);

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

        // ---- Recupero i bottoni ----
        btnCliente   = (JButton) panelClienti.getComponent(0);
        btnSala      = (JButton) panelSala.getComponent(0);
        btnCassiere  = (JButton) panelCassiere.getComponent(0);
        btnChef      = (JButton) panelChef.getComponent(0);
        btnPizzayolo = (JButton) panelPizzayolo.getComponent(0);

        // ---- Azioni bottoni ----
        btnSala.addActionListener(e -> admList.add(new Sala()));
        btnCliente.addActionListener(e -> new ClienteFrame(admList, cuochiList));
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

    // Funzione per creare pannello con immagine + bottone con icona
    private JPanel createImageButtonPanel(BufferedImage bgImg, String text, BufferedImage iconImg) {
        ImagePanel p = new ImagePanel(bgImg);
        p.setLayout(new OverlayLayout(p));

        JButton btn = new JButton(text);
        
        // Se c'è un'icona, la impostiamo e la scaldiamo
        if (iconImg != null) {
            Image scaledIcon = iconImg.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaledIcon));
            btn.setHorizontalAlignment(SwingConstants.LEFT); // testo a destra dell'icona
        }

        btn.setAlignmentX(0.5f); 
        btn.setAlignmentY(0.5f);
        btn.setMaximumSize(new Dimension(150, 40)); // allargato se serve spazio icona

        p.add(btn);
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
