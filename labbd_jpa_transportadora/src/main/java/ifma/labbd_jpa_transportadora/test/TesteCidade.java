package ifma.labbd_jpa_transportadora.test;

import ifma.labbd_jpa_transportadora.modelo.Cidade;
import ifma.labbd_jpa_transportadora.repositorio.CidadeRepository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TesteCidade extends JFrame {
    private JTextField nomeField;
    private JTextField ufField;
    private JTextField taxaField;
    private JButton salvarButton;
    
    private CidadeRepository repo;

    public TesteCidade() {
        repo = new CidadeRepository();
        
        setTitle("Cadastro de Cidade");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        nomeField = new JTextField(20);
        ufField = new JTextField(2);
        taxaField = new JTextField(10);
        salvarButton = new JButton("Salvar");
        
        add(new JLabel("Nome:"));
        add(nomeField);
        add(new JLabel("UF:"));
        add(ufField);
        add(new JLabel("Taxa (R$):"));
        add(taxaField);
        add(salvarButton);
        
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarCidade();
            }
        });
    }
    
    private void salvarCidade() {
        try {
            String nome = nomeField.getText();
            String uf = ufField.getText().toUpperCase();
            double taxa = Double.parseDouble(taxaField.getText());
            
            Cidade cidade = new Cidade(nome, uf, taxa);
            repo.salvar(cidade);
            
            JOptionPane.showMessageDialog(this, 
                "Cidade salva com ID: " + cidade.getId(),
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Taxa deve ser um número válido",
                "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar cidade: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparCampos() {
        nomeField.setText("");
        ufField.setText("");
        taxaField.setText("");
    }
    
    public static void main(String[] args) {
        System.out.println("Iniciando aplicação...");
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Erro ao definir look-and-feel: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            System.out.println("Criando frame...");
            TesteCidade frame = new TesteCidade();
            frame.setVisible(true);
            System.out.println("Frame visível");
        });
    }
}