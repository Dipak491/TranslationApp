import java.awt.*;
import java.util.*;
import javax.swing.*;

public class TranslationApp extends JFrame {
    private JTextField inputField;
    private JList<String> outputList;
    private DefaultListModel<String> listModel;
    private final Map<String, String> characterMap;

    public TranslationApp() {
        characterMap = new HashMap<>();
        initializeCharacterMap();  // Initialize the character mappings

        setTitle("Advanced Marathi Transliterator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(600, 400);

        // Setup font
        Font devanagariFont = new Font("Nirmala UI", Font.PLAIN, 16);

        // Create panels
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));

        // Create components
        inputField = new JTextField();
        listModel = new DefaultListModel<>();
        outputList = new JList<>(listModel);
        outputList.setFont(devanagariFont);

        // Add scroll pane for output list
        JScrollPane scrollPane = new JScrollPane(outputList);
        scrollPane.setPreferredSize(new Dimension(580, 300));

        // Add real-time translation listener
        inputField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { generateVariations(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { generateVariations(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { generateVariations(); }
        });

        // Add labels
        JLabel inputLabel = new JLabel("Type English text:");
        JLabel outputLabel = new JLabel("Possible variations:");
        outputLabel.setFont(devanagariFont);

        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(inputField, BorderLayout.CENTER);
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(outputPanel, BorderLayout.CENTER);

        // Add padding
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void initializeCharacterMap() {
        // Basic vowels
        characterMap.put("a", "अ");
        characterMap.put("aa", "आ");
        characterMap.put("i", "इ");
        characterMap.put("ee", "ई");
        characterMap.put("u", "उ");
        characterMap.put("oo", "ऊ");
        characterMap.put("e", "ए");
        characterMap.put("ai", "ऐ");
        characterMap.put("o", "ओ");
        characterMap.put("au", "औ");
        characterMap.put("am", "ं");
        characterMap.put("aha", "ः");

        // Consonants
        characterMap.put("k", "क्");
        characterMap.put("kh", "ख्");
        characterMap.put("g", "ग्");
        characterMap.put("gh", "घ्");
        characterMap.put("ng", "ङ्");
        characterMap.put("ch", "च्");
        characterMap.put("chh", "छ्");
        characterMap.put("j", "ज्");
        characterMap.put("jh", "झ्");
        characterMap.put("t", "त्");
        characterMap.put("th", "थ्");
        characterMap.put("d", "द्");
        characterMap.put("dh", "ध्");
        characterMap.put("n", "न्");
        characterMap.put("p", "प्");
        characterMap.put("ph", "फ्");
        characterMap.put("f", "फ्");
        characterMap.put("b", "ब्");
        characterMap.put("bh", "भ्");
        characterMap.put("m", "म्");
        characterMap.put("y", "य्");
        characterMap.put("r", "र्");
        characterMap.put("l", "ल्");
        characterMap.put("v", "व्");
        characterMap.put("w", "व्");
        characterMap.put("sh", "श्");
        characterMap.put("s", "स्");
        characterMap.put("h", "ह्");

        // Special characters
        characterMap.put(".", "।");
        characterMap.put(" ", " ");
    }

    private void generateVariations() {
        String input = inputField.getText().toLowerCase();
        if (input.isEmpty()) {
            listModel.clear();
            return;
        }

        Set<String> variations = new HashSet<>();
        
        // Generate different variations
        variations.add(getBasicTransliteration(input));  // Basic transliteration
        variations.add(getVariationWithLongVowels(input));  // Long vowel variation
        variations.add(getVariationWithShortVowels(input));  // Short vowel variation
        variations.add(getVariationWithNasalization(input));  // With anusvara
        variations.add(input);  // Original English input

        // Update the list model
        listModel.clear();
        variations.stream()
                 .filter(s -> s != null && !s.isEmpty())
                 .forEach(listModel::addElement);
    }

    private String getBasicTransliteration(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            String twoChars = (i < input.length() - 1) ? input.substring(i, i + 2) : "";
            String oneChar = input.substring(i, i + 1);
            
            if (!twoChars.isEmpty() && characterMap.containsKey(twoChars)) {
                output.append(characterMap.get(twoChars));
                i++;
            } else if (characterMap.containsKey(oneChar)) {
                output.append(characterMap.get(oneChar));
            } else {
                output.append(oneChar);
            }
        }
        return applyVowelMarks(output.toString());
    }

    private String getVariationWithLongVowels(String input) {
        return getBasicTransliteration(input.replace("i", "ee")
                                          .replace("u", "oo")
                                          .replace("a", "aa"));
    }

    private String getVariationWithShortVowels(String input) {
        return getBasicTransliteration(input.replace("ee", "i")
                                          .replace("oo", "u")
                                          .replace("aa", "a"));
    }

    private String getVariationWithNasalization(String input) {
        String transliterated = getBasicTransliteration(input);
        if (transliterated.matches(".*[कखगघचछजझटठडढतथदधपफबभमयरलवशषसह].*")) {
            return transliterated + "ं";
        }
        return transliterated;
    }

    private String applyVowelMarks(String text) {
        return text.replace("्अ", "")
                  .replace("्आ", "ा")
                  .replace("्इ", "ि")
                  .replace("्ई", "ी")
                  .replace("्उ", "ु")
                  .replace("्ऊ", "ू")
                  .replace("्ए", "े")
                  .replace("्ऐ", "ै")
                  .replace("्ओ", "ो")
                  .replace("्औ", "ौ");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            TranslationApp app = new TranslationApp();
            app.setLocationRelativeTo(null);
            app.setVisible(true);
        });
    }
}