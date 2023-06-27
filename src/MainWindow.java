import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindow extends JFrame {
    private JLabel hangedManLabel;
    private JLabel wordLabel;
    private JTextField inputField;
    private JButton guessButton;

    private List<Character> playerGuesses;
    private int wrongCount;
    private String word;

    public MainWindow() throws FileNotFoundException {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Juego del Ahorcado");
        setResizable(false);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(800, 600));

        // Panel superior para mostrar el dibujo del ahorcado
        JPanel hangedManPanel = new JPanel();
        hangedManPanel.setLayout(new BoxLayout(hangedManPanel, BoxLayout.Y_AXIS));
        hangedManPanel.setPreferredSize(new Dimension(400, 300));

        hangedManLabel = new JLabel();
        hangedManLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hangedManLabel.setFont(new Font("Arial", Font.BOLD, 20));

        hangedManPanel.add(Box.createVerticalGlue()); // Espacio vertical flexible
        hangedManPanel.add(hangedManLabel);
        hangedManPanel.add(Box.createVerticalGlue()); // Espacio vertical flexible

        mainPanel.add(hangedManPanel, BorderLayout.NORTH);

        // Panel central para mostrar la palabra a adivinar
        JPanel wordPanel = new JPanel();
        wordPanel.setPreferredSize(new Dimension(400, 100));

        wordLabel = new JLabel();
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wordLabel.setFont(new Font("Arial", Font.BOLD, 30));

        wordPanel.add(wordLabel);

        mainPanel.add(wordPanel, BorderLayout.CENTER);

        // Panel inferior para ingresar las adivinanzas del jugador
        JPanel inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(400, 100));

        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(200, 30));

        guessButton = new JButton("Adivinar");
        guessButton.setEnabled(false);

        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(inputField);
        inputPanel.add(guessButton);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);

            

        updateHangedManLabel();

        playerGuesses = new ArrayList<>();
        wrongCount = 0;
        initializeGame();

        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processGuess();
            }
        });

        guessButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processGuess();
            }
        });
    }

    private void initializeGame() throws FileNotFoundException {
        int players = JOptionPane.showOptionDialog(null, "¿1 o 2 jugadores?", "Modo de Juego",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"1 Jugador", "2 Jugadores"}, "1 Jugador");

        if (players == JOptionPane.YES_OPTION) {
            // Archivo con las palabras
            Scanner scanner = new Scanner(new File("C:/Users/Fran/OneDrive/Documentos/words.txt"));
            List<String> words = new ArrayList<>();
            while (scanner.hasNext()) {
                words.add(scanner.nextLine());
            }
            Random rand = new Random();
            MainWindow.this.word = words.get(rand.nextInt(words.size()));
        } else {
            word = JOptionPane.showInputDialog(null, "Jugador 1, escribe una palabra:");
            JOptionPane.showMessageDialog(null, "Listo Jugador 2, ¡Buena suerte!");
        }

        updateWordLabel();
        inputField.setEnabled(true);
        guessButton.setEnabled(true);
        inputField.requestFocusInWindow();
    }

    private void processGuess() {
        String guess = inputField.getText().trim().toLowerCase();
        inputField.setText("");

        if (guess.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa una letra o palabra.");
            return;
        }

        boolean isCorrectGuess = wordContainsGuess(guess);
        if (!isCorrectGuess) {
            wrongCount++;

            updateHangedManLabel();
        }

        updateWordLabel();

        if (isWordGuessed()) {
            JOptionPane.showMessageDialog(null, "¡Ganaste!");
            try {
                resetGame();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (wrongCount >= 6) {
            JOptionPane.showMessageDialog(null, "¡Perdiste!\nLa palabra era: " + word);
            try {
                resetGame();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean wordContainsGuess(String guess) {
        boolean isCorrectGuess = false;

        if (guess.length() == 1) {
            char letter = guess.charAt(0);
            playerGuesses.add(letter);

            if (word.contains(guess)) {
                isCorrectGuess = true;
            }
        } else if (guess.length() > 1) {
            if (word.equals(guess)) {
                isCorrectGuess = true;
            }
        }

        return isCorrectGuess;
    }

    private boolean isWordGuessed() {
        for (char letter : word.toCharArray()) {
            if (!playerGuesses.contains(letter)) {
                return false;
            }
        }
        return true;
    }

    private void resetGame() throws FileNotFoundException {
        int choice = JOptionPane.showConfirmDialog(null, "¿Deseas jugar de nuevo?", "Reiniciar Juego",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            playerGuesses.clear();
            wrongCount = 0;
            initializeGame();
        } else {
            System.exit(0);
        }
    }

    private void updateWordLabel() {
        StringBuilder sb = new StringBuilder();
        for (char letter : word.toCharArray()) {
            if (playerGuesses.contains(letter)) {
                sb.append(letter).append(" ");
            } else {
                sb.append("_ ");
            }
        }
        wordLabel.setText(sb.toString());
    }

    private void updateHangedManLabel() {
        StringBuilder sb = new StringBuilder();
        
        if (wrongCount >= 1) {
            sb.append("X");
        }
        if (wrongCount >= 2) {
            sb.append("X");
        if (wrongCount >= 3) {
            sb.append("X");
            } else {
                sb.append("\n");
            }
        }
        if (wrongCount >= 4) {
            sb.append("X");
        }
        if (wrongCount >= 5) {
            sb.append("X");
        if (wrongCount >= 6) {
        sb.append("X");
            } else {
                sb.append("\n");
            }
        }
        sb.append("\n\n\n");
        hangedManLabel.setText(sb.toString());
    }

    public static void main(String[] args) throws FileNotFoundException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainWindow().setVisible(true);
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "Error: Archivo no encontrado.");
                }
            }
        });
    }
}