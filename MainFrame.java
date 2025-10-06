import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// Classe per la gestione degli sfondi colorati
class ImagePanel extends JPanel {
    private Color bgColor;

    public ImagePanel(BufferedImage image) { /* Lasciato vuoto intenzionalmente */ }

    public ImagePanel(Color bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgColor != null) {
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

// ====================================================================
// CLASSE PRINCIPALE MainFrame
// ====================================================================
public class MainFrame extends FrameBase {
    private JButton btnSala, btnCliente, btnPizzayolo, btnCassiere, btnChef; 
    // Il campo 'panel' di FrameBase non è usato in questo contesto, ma usiamo un pannello locale:
    private JPanel panelSala, panelPizzayolo, panelChef, panelCassiere, panelClienti;
    private final ArrayList<Admin> admList = new ArrayList<>();
    private final ArrayList<Cuoco> cuochiList = new ArrayList<>();

    // Definisco i colori da usare
    private static final Color CLIENTE_COLOR = new Color(240, 248, 255); 
    private static final Color SALA_COLOR = new Color(255, 239, 213); 
    private static final Color CASSA_COLOR = new Color(224, 255, 255); 
    private static final Color CHEF_COLOR = new Color(245, 245, 220); 
    private static final Color PIZZAIOLO_COLOR = new Color(250, 235, 215); 


    public MainFrame() {
        // Chiama FrameBase con la dimensione fissa (850x600)
        super("Gestionale Ristorante", 850, 600); 

        // Carico immagini
        BufferedImage imgCliente = loadImage("immagini/white.jpg");
        BufferedImage imgSala = loadImage("immagini/white.jpg");
        BufferedImage imgCassa = loadImage("immagini/white.jpg");
        BufferedImage imgChef = loadImage("immagini/white.jpg");
        BufferedImage imgPizzayolo = loadImage("immagini/b-pizza.png");

        // Carico le icone
        BufferedImage iconCliente = loadImage("immagini/User.png");
        BufferedImage iconSala = loadImage("immagini/dining-table.png");
        BufferedImage iconCassa = loadImage("immagini/cash-register.png");
        BufferedImage iconChef = loadImage("immagini/chef.png");
        BufferedImage iconPizzayolo = loadImage("immagini/pizza.png");

        // Layout principale: 5 righe in verticale (usiamo un nuovo pannello per questo)
        JPanel mainContentPanel = new JPanel(new GridLayout(5, 1));

        Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        // ---- Pannelli con bottoni ----
        JButton[] ref = new JButton[1];

        panelClienti = createImageButtonPanel(imgCliente, CLIENTE_COLOR, "Cliente", iconCliente, ref);
        btnCliente = ref[0];

        panelSala = createImageButtonPanel(imgSala, SALA_COLOR, "Sala", iconSala, ref);
        btnSala = ref[0];

        panelCassiere = createImageButtonPanel(imgCassa, CASSA_COLOR, "Cassa", iconCassa, ref);
        btnCassiere = ref[0];

        panelChef = createImageButtonPanel(imgChef, CHEF_COLOR, "Chef", iconChef, ref);
        btnChef = ref[0];

        panelPizzayolo = createImageButtonPanel(imgPizzayolo, PIZZAIOLO_COLOR, "Pizzaiolo", iconPizzayolo, ref);
        btnPizzayolo = ref[0];

        // Imposto colori
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

        // Aggiungo i pannelli al contenitore GridLayout
        mainContentPanel.add(panelClienti);
        mainContentPanel.add(panelSala);
        mainContentPanel.add(panelCassiere);
        mainContentPanel.add(panelChef);
        mainContentPanel.add(panelPizzayolo);

        
        // ----Azioni dei bottoni 
        // Nota: Assicurati che Sala, Cassiere, Pizzayolo, Chef esistano e estendano FrameBase
        btnSala.addActionListener(e -> admList.add(new Sala()));
        btnCliente.addActionListener(e -> new ClienteFrame(admList, cuochiList));
        btnCassiere.addActionListener(e -> admList.add(new Cassiere()));
        btnPizzayolo.addActionListener(e -> cuochiList.add(new Pizzayolo()));
        btnChef.addActionListener(e -> cuochiList.add(new Chef()));

        // AGGIORNAMENTO: Aggiunge il pannello contente i bottoni al JFrame
        this.add(mainContentPanel); 
        this.revalidate();
        this.repaint();
    }

    private JPanel createImageButtonPanel(BufferedImage bgImg, Color bgColor, String text, BufferedImage iconImg, JButton[] outBtn) {
        ImagePanel p = new ImagePanel(bgColor); 
        p.setLayout(new BorderLayout());

        JLabel lblIcon = new JLabel();
        if (iconImg != null) {
            Image scaledIcon = iconImg.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(scaledIcon));
        }
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcon.setPreferredSize(new Dimension(80, 80));

        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setFocusPainted(false);
        btn.setForeground(Color.BLACK); 

        outBtn[0] = btn;

        p.add(lblIcon, BorderLayout.WEST);
        p.add(btn, BorderLayout.EAST);

        return p;
    }

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