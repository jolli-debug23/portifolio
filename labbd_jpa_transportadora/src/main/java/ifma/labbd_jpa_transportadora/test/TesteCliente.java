package ifma.labbd_jpa_transportadora.test;

import ifma.labbd_jpa_transportadora.modelo.Cliente;
import ifma.labbd_jpa_transportadora.repositorio.ClienteRepository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TesteCliente extends JFrame {
    private JTextField nomeField;
    private JTextField telefoneField;
    private JTextField enderecoField;
    private JButton salvarButton;
    
    private ClienteRepository repo;

    public TesteCliente() {
        repo = new ClienteRepository();
        
        setTitle("Cadastro de Cliente");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        nomeField = new JTextField(20);
        telefoneField = new JTextField(20);
        enderecoField = new JTextField(20);
        salvarButton = new JButton("Salvar");
        
        add(new JLabel("Nome:"));
        add(nomeField);
        add(new JLabel("Telefone:"));
        add(telefoneField);
        add(new JLabel("Endereço:"));
        add(enderecoField);
        add(salvarButton);
        
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarCliente();
            }
        });
    }
    
    private void salvarCliente() {
        try {
            String nome = nomeField.getText();
            String telefone = telefoneField.getText();
            String endereco = enderecoField.getText();
            
            Cliente cliente = new Cliente(nome, telefone, endereco);
            repo.salvar(cliente);
            
            JOptionPane.showMessageDialog(this, 
                "Cliente salvo com ID: " + cliente.getId(),
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar cliente: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparCampos() {
        nomeField.setText("");
        telefoneField.setText("");
        enderecoField.setText("");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TesteCliente().setVisible(true);
            }
        });
    }
}
