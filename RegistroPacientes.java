import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class RegistroPacientes extends JFrame {

    static final Color NAVY = new Color(0x13306B), BLUE = new Color(0x1F62C9),
            SOFT = new Color(0xE8F0FC), BG = new Color(0xF4F7FC),
            LINE = new Color(0xD6DFEE), TEXT = new Color(0x16233F),
            MUTED = new Color(0x6B7A99), RED = new Color(0xD93636);
    static final Font BASE = new Font("Segoe UI", Font.PLAIN, 14);
    static final DateTimeFormatter FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    JComboBox<String> tipoDoc = combo("Cédula de ciudadanía", "Tarjeta de identidad", "Cédula de extranjería", "Pasaporte", "Registro civil");
    JComboBox<String> genero = combo("Seleccione...", "Masculino", "Femenino", "Otro");
    JComboBox<String> eps = combo("Seleccione...", "Nueva EPS", "Sanitas", "Sura", "Compensar", "Famisanar", "Salud Total");
    JComboBox<String> regimen = combo("Seleccione...", "Contributivo", "Subsidiado", "Especial");
    JComboBox<String> civil = combo("Seleccione...", "Soltero(a)", "Casado(a)", "Unión libre", "Divorciado(a)", "Viudo(a)");
    JComboBox<String> condicion = combo("No", "Sí");

    HintField numDoc = new HintField("1234567890"), nombres = new HintField("Juan Diego"),
            apellidos = new HintField("Lopez Arevalo"), fnac = new HintField("dd/mm/aaaa"),
            fijo = new HintField("601 1234567"), cel = new HintField("300 123 4567"),
            correo = new HintField("juan.lopez@example.com"),
            dir = new HintField("Carrera 10 # 20 - 30"), ocupacion = new HintField("Abogado");
    JTextField edad = new JTextField();
    JTextArea obs = new JTextArea(2, 10);

    //resumen
    JLabel rNombre = new JLabel("Nombre del paciente"), rDoc = new JLabel(" "),
            rFnac = dato(), rCel = dato(), rCorreo = dato(), rDir = dato(), rEps = dato(), rReg = dato();

    public RegistroPacientes() {
        super("Laboratorio Clínico - Registrar paciente");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        edad.setEditable(false);
        edad.setBackground(new Color(0xE9EEF7));
        obs.setLineWrap(true);
        obs.setWrapStyleWord(true);
        obs.setFont(BASE);

        JPanel root = new JPanel(new BorderLayout());
        root.add(crearHeader(), BorderLayout.NORTH);
        root.add(crearMenu(), BorderLayout.WEST);
        root.add(crearContenido(), BorderLayout.CENTER);
        setContentPane(root);

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizar(); }
            public void removeUpdate(DocumentEvent e) { actualizar(); }
            public void changedUpdate(DocumentEvent e) { actualizar(); }
        };
        for (JTextField f : new JTextField[]{numDoc, nombres, apellidos, fnac, cel, correo, dir})
            f.getDocument().addDocumentListener(dl);
        for (JComboBox<?> c : new JComboBox[]{tipoDoc, eps, regimen})
            c.addActionListener(e -> actualizar());
        actualizar();
    }

    JComponent crearHeader() {
        JPanel h = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                ((Graphics2D) g).setPaint(new GradientPaint(getWidth() * .35f, 0,
                        new Color(0x0F2A5C), getWidth(), 0, new Color(0xB9C7E0)));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        h.setPreferredSize(new Dimension(0, 72));
        h.setBorder(new EmptyBorder(0, 28, 0, 28));

        JLabel marca = new JLabel("<html><span style='font-size:17px'><b>Laboratorio Clínico</b></span><br>" 
                                + "<span style='font-size:11px'>Resultados que generan confianza</span></html>");
        marca.setForeground(Color.WHITE);
        JLabel usuario = new JLabel("<html><b>Mateo</b><br><span style='font-size:11px'>Administrador</span></html>");
        usuario.setForeground(NAVY);
        h.add(marca, BorderLayout.WEST);
        h.add(usuario, BorderLayout.EAST);
        return h;
    }

    JComponent crearMenu() {
        JPanel m = new JPanel();
        m.setLayout(new BoxLayout(m, BoxLayout.Y_AXIS));
        m.setBackground(NAVY);
        m.setPreferredSize(new Dimension(210, 0));
        m.setBorder(new EmptyBorder(22, 0, 0, 0));
        String[] items = {"Inicio", "Pacientes", "Toma de muestras", "Resultados", "Reportes", "Configuración"};
        for (String it : items) {
            JButton l = new JButton(it);
            l.setFont(BASE.deriveFont(15f));
            l.setForeground(Color.WHITE);
            l.setOpaque(true);
            l.setContentAreaFilled(true);
            l.setBackground(it.equals("Pacientes") ? BLUE : NAVY);
            l.setBorder(new EmptyBorder(14, 24, 14, 10));
            l.setFocusPainted(false);
            l.setHorizontalAlignment(SwingConstants.LEFT);
            l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            l.setAlignmentX(0f);
            m.add(l);
        }
        return m;
    }

    JComponent crearContenido() {
        JPanel c = new JPanel(new BorderLayout(0, 18));
        c.setBackground(BG);
        c.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Banner
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(SOFT);
        banner.setBorder(new CompoundBorder(new LineBorder(LINE), new EmptyBorder(18, 24, 18, 24)));
        banner.add(new JLabel("<html><span style='font-size:20px;color:#13306B'><b>Registrar paciente</b></span><br>" 
                            + "<span style='color:#3D5486'>Complete la información del paciente para continuar con el proceso de atención.</span></html>"));
        c.add(banner, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(18, 0));
        centro.setOpaque(false);
        centro.add(crearFormulario(), BorderLayout.CENTER);
        centro.add(crearResumen(), BorderLayout.EAST);

        JScrollPane sp = new JScrollPane(centro);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        c.add(sp, BorderLayout.CENTER);
        return c;
    }

    JComponent crearFormulario() {
        JPanel f = tarjeta();
        f.setLayout(new BoxLayout(f, BoxLayout.Y_AXIS));

        f.add(titulo("Datos personales"));
        f.add(fila(campo("Tipo de documento", true, tipoDoc), campo("Número de documento", true, numDoc)));
        f.add(fila(campo("Nombres", true, nombres), campo("Apellidos", true, apellidos)));

        JPanel fn = new JPanel(new GridLayout(1, 2, 10, 0));
        fn.setOpaque(false);
        fn.add(campo("Fecha de nacimiento", true, fnac));
        fn.add(campo("Edad", false, edad));
        f.add(fila(fn, campo("Género", true, genero)));

        f.add(separador());
        f.add(titulo("Información de contacto"));
        f.add(fila(campo("Teléfono fijo", false, fijo), campo("Teléfono celular", true, cel)));
        f.add(fila(campo("Correo electrónico", false, correo), campo("Dirección de residencia", true, dir)));

        f.add(separador());
        f.add(titulo("Información adicional"));
        f.add(fila(campo("EPS", true, eps), campo("Régimen", false, regimen)));
        f.add(fila(campo("Ocupación", false, ocupacion), campo("Estado civil", false, civil)));
        JScrollPane spObs = new JScrollPane(obs);
        spObs.setBorder(new LineBorder(LINE));
        spObs.setPreferredSize(new Dimension(0, 56));
        f.add(fila(campo("¿Tiene alguna condición médica relevante?", false, condicion),
                campo("Observaciones", false, spObs)));


        JButton cancelar = boton("Cancelar", Color.WHITE, BLUE);
        JButton guardar = boton("Guardar paciente", BLUE, Color.WHITE);
        cancelar.addActionListener(e -> limpiar());


        JPanel b = new JPanel(new BorderLayout());
        b.setOpaque(false);
        b.setBorder(new EmptyBorder(8, 0, 0, 0));
        b.add(cancelar, BorderLayout.WEST);
        b.add(guardar, BorderLayout.EAST);
        f.add(b);
        return f;
    }

    JComponent crearResumen() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);
        col.setPreferredSize(new Dimension(310, 0));

        JPanel r = tarjeta();
        r.setLayout(new BoxLayout(r, BoxLayout.Y_AXIS));
        r.add(titulo("Resumen"));
        rNombre.setFont(BASE.deriveFont(Font.BOLD, 15f));
        rNombre.setForeground(NAVY);
        rDoc.setForeground(MUTED);
        r.add(rNombre);
        r.add(rDoc);
        r.add(Box.createVerticalStrut(14));
        String[] t = {"Fecha de nacimiento", "Teléfono celular", "Correo electrónico", "Dirección", "EPS", "Régimen"};
        JLabel[] v = {rFnac, rCel, rCorreo, rDir, rEps, rReg};
        for (int i = 0; i < t.length; i++) {
            JLabel k = new JLabel(t[i]);
            k.setForeground(MUTED);
            k.setFont(BASE.deriveFont(12f));
            r.add(k);
            r.add(v[i]);
            r.add(Box.createVerticalStrut(12));
        }

        JPanel nota = tarjeta();
        nota.setBackground(SOFT);
        nota.setLayout(new BorderLayout());
        nota.add(new JLabel("<html><div style='width:230px'><b style='color:#13306B'>Tenga en cuenta</b><br><br>" 
                            + "<span style='color:#3D5486'>Los datos del paciente son necesarios para la generación de la " 
                            + "orden médica, la trazabilidad de las muestras y la entrega de resultados.</span></div></html>"));

        col.add(r);
        col.add(Box.createVerticalStrut(16));
        col.add(nota);
        return col;
    }

    LocalDate parseFecha() {
        try {
            LocalDate d = LocalDate.parse(fnac.getText().trim(), FECHA);
            return d.isAfter(LocalDate.now()) ? null : d;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    void actualizar() {
        LocalDate d = parseFecha();
        int anios = d == null ? -1 : Period.between(d, LocalDate.now()).getYears();
        edad.setText(d == null ? "" : anios + " años");

        String nom = (nombres.getText() + " " + apellidos.getText()).trim();
        rNombre.setText(nom.isEmpty() ? "Nombre del paciente" : nom);
        String num = numDoc.getText().trim();
        rDoc.setText(num.isEmpty() ? " " : abreviatura() + " " + num);
        rFnac.setText(d == null ? "—" : fnac.getText().trim() + " (" + anios + " años)");
        rCel.setText(vacio(cel.getText()));
        rCorreo.setText(vacio(correo.getText()));
        rDir.setText(vacio(dir.getText()));
        rEps.setText(eps.getSelectedIndex() <= 0 ? "—" : (String) eps.getSelectedItem());
        rReg.setText(regimen.getSelectedIndex() <= 0 ? "—" : (String) regimen.getSelectedItem());
    }

    String abreviatura() {
        return new String[]{"CC", "TI", "CE", "PA", "RC"}[tipoDoc.getSelectedIndex()];
    }

    void limpiar() {
        for (JTextField f : new JTextField[]{numDoc, nombres, apellidos, fnac, fijo, cel, correo, dir, ocupacion, edad})
            f.setText("");
        obs.setText("");
        for (JComboBox<?> c : new JComboBox[]{tipoDoc, genero, eps, regimen, civil, condicion})
            c.setSelectedIndex(0);
        actualizar();
    }

    static JComboBox<String> combo(String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(BASE);
        c.setBackground(Color.WHITE);
        return c;
    }

    static JLabel dato() {
        JLabel l = new JLabel("—");
        l.setForeground(TEXT);
        l.setFont(BASE);
        return l;
    }

    static String vacio(String s) { return s == null || s.isBlank() ? "—" : s.trim(); }

    static JPanel tarjeta() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(LINE), new EmptyBorder(20, 22, 20, 22)));
        return p;
    }

    static JComponent titulo(String s) {
        JLabel l = new JLabel(s);
        l.setFont(BASE.deriveFont(Font.BOLD, 17f));
        l.setForeground(NAVY);
        l.setBorder(new EmptyBorder(0, 0, 12, 0));
        l.setAlignmentX(0f);
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(l, BorderLayout.WEST);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return p;
    }

    static JComponent separador() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(6, 0, 14, 0));
        p.add(new JSeparator());
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return p;
    }

    static JPanel fila(JComponent a, JComponent b) {
        JPanel p = new JPanel(new GridLayout(1, 2, 22, 0));
        p.setOpaque(false);
        p.add(a);
        p.add(b);
        p.setBorder(new EmptyBorder(0, 0, 14, 0));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        return p;
    }

    static JPanel campo(String texto, boolean requerido, JComponent c) {
        JLabel l = new JLabel(requerido
                ? "<html>" + texto + " <font color='#D93636'>*</font></html>" : texto);
        l.setFont(BASE);
        l.setForeground(TEXT);
        l.setBorder(new EmptyBorder(0, 0, 5, 0));
        if (c instanceof JTextField || c instanceof JComboBox) {
            c.setPreferredSize(new Dimension(0, 36));
            c.setFont(BASE);
            if (c instanceof JTextField) c.setBorder(new CompoundBorder(new LineBorder(LINE), new EmptyBorder(0, 10, 0, 10)));
        }
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(l, BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    static JButton boton(String t, Color fondo, Color texto) {
        JButton b = new JButton(t);
        b.setFont(BASE.deriveFont(Font.BOLD, 14f));
        b.setBackground(fondo);
        b.setForeground(texto);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBorder(new CompoundBorder(new LineBorder(BLUE), new EmptyBorder(9, 26, 9, 26)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    static class HintField extends JTextField {
        final String hint;
        HintField(String hint) { this.hint = hint; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(new Color(0x9AA7C0));
                g2.setFont(getFont());
                Insets in = getInsets();
                g2.drawString(hint, in.left, (getHeight() + g2.getFontMetrics().getAscent()
                        - g2.getFontMetrics().getDescent()) / 2);
                g2.dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistroPacientes().setVisible(true));
    }
}