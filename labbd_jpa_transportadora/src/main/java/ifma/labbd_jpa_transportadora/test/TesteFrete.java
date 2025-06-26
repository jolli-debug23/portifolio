package ifma.labbd_jpa_transportadora.test;

import ifma.labbd_jpa_transportadora.modelo.*;
import ifma.labbd_jpa_transportadora.repositorio.*;
import jakarta.persistence.EntityManager;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.util.List;

public class TesteFrete extends JFrame {
    // Componentes existentes
    private JComboBox<Cliente> clienteCombo;
    private JComboBox<Cidade> cidadeOrigemCombo;
    private JComboBox<Cidade> cidadeDestinoCombo;
    private JTextField descricaoField;
    private JTextField pesoField;
    private JButton salvarButton;
    private EntityManager em;
    
    // Novos componentes para listagem
    private JButton listarFretesButton;
    private JTable fretesTable;
    private DefaultTableModel tableModel;
    
    private FreteRepository freteRepo;
    private ClienteRepository clienteRepo;
    private CidadeRepository cidadeRepo;

    public TesteFrete() {
        try {
            freteRepo = new FreteRepository();
            clienteRepo = new ClienteRepository();
            cidadeRepo = new CidadeRepository();

            initUI();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao inicializar: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initUI() {
        setTitle("Cadastro e Consulta de Fretes");
        setSize(600, 600); // Aumentado para acomodar a tabela
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Painel de cadastro
        JPanel cadastroPanel = new JPanel();
        cadastroPanel.setLayout(new BoxLayout(cadastroPanel, BoxLayout.Y_AXIS));
        cadastroPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Frete"));
        
        // Componentes de cadastro
        clienteCombo = new JComboBox<>();
        cidadeOrigemCombo = new JComboBox<>();
        cidadeDestinoCombo = new JComboBox<>();
        descricaoField = new JTextField();
        pesoField = new JTextField();
        salvarButton = new JButton("Salvar Frete");
        
        // Configuração de tamanhos
        descricaoField.setMaximumSize(new Dimension(Integer.MAX_VALUE, descricaoField.getPreferredSize().height));
        pesoField.setMaximumSize(new Dimension(100, pesoField.getPreferredSize().height));
        
        // Layout do painel de cadastro
        cadastroPanel.add(new JLabel("Cliente:"));
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cadastroPanel.add(clienteCombo);
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cadastroPanel.add(new JLabel("Cidade Origem:"));
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cadastroPanel.add(cidadeOrigemCombo);
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cadastroPanel.add(new JLabel("Cidade Destino:"));
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cadastroPanel.add(cidadeDestinoCombo);
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cadastroPanel.add(new JLabel("Descrição:"));
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cadastroPanel.add(descricaoField);
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cadastroPanel.add(new JLabel("Peso (kg):"));
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cadastroPanel.add(pesoField);
        cadastroPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cadastroPanel.add(salvarButton);
        
        // Painel de consulta
        JPanel consultaPanel = new JPanel();
        consultaPanel.setLayout(new BoxLayout(consultaPanel, BoxLayout.Y_AXIS));
        consultaPanel.setBorder(BorderFactory.createTitledBorder("Consulta de Fretes por Cliente"));
        
        listarFretesButton = new JButton("Listar Fretes do Cliente Selecionado");
        listarFretesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Configuração da tabela
        String[] colunas = {"ID", "Descrição", "Peso", "Valor", "Origem", "Destino"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela não editável
            }
        };
        
        fretesTable = new JTable(tableModel);
        fretesTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(fretesTable);
        scrollPane.setPreferredSize(new Dimension(550, 200));
        
        // Centraliza o conteúdo das células
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < fretesTable.getColumnCount(); i++) {
            fretesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Layout do painel de consulta
        consultaPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        consultaPanel.add(listarFretesButton);
        consultaPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        consultaPanel.add(scrollPane);
        
        // Adiciona os painéis ao painel principal
        panel.add(cadastroPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(consultaPanel);
        
        add(panel);
        
        // Eventos
        salvarButton.addActionListener(this::salvarFrete);
        listarFretesButton.addActionListener(this::listarFretesCliente);
        clienteCombo.addActionListener(e -> listarFretesCliente(null));
        
        // Carrega dados
        try {
            carregarCombos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar dados: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCombos() {
        try {
            List<Cliente> clientes = clienteRepo.buscarTodos();
            List<Cidade> cidades = cidadeRepo.buscarTodos();

            clienteCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Cliente) {
                        setText(((Cliente) value).getNome());
                    }
                    return this;
                }
            });

            DefaultListCellRenderer cidadeRenderer = new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Cidade) {
                        setText(((Cidade) value).getNome());
                    }
                    return this;
                }
            };

            cidadeOrigemCombo.setRenderer(cidadeRenderer);
            cidadeDestinoCombo.setRenderer(cidadeRenderer);

            clientes.forEach(clienteCombo::addItem);
            cidades.forEach(cidadeOrigemCombo::addItem);
            cidades.forEach(cidadeDestinoCombo::addItem);

            // Seleciona o primeiro cliente e carrega seus fretes
            if (clienteCombo.getItemCount() > 0) {
                clienteCombo.setSelectedIndex(0);
                listarFretesCliente(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar dados: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void salvarFrete(ActionEvent e) {
        try {
            Cliente cliente = (Cliente) clienteCombo.getSelectedItem();
            Cidade cidadeOrigem = (Cidade) cidadeOrigemCombo.getSelectedItem();
            Cidade cidadeDestino = (Cidade) cidadeDestinoCombo.getSelectedItem();
            String descricao = descricaoField.getText();
            double peso = Double.parseDouble(pesoField.getText());
            
            if (cidadeOrigem.equals(cidadeDestino)) {
                JOptionPane.showMessageDialog(this, 
                    "Cidade de origem e destino devem ser diferentes",
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Frete frete = new Frete(descricao, peso, cliente, cidadeOrigem, cidadeDestino);
            freteRepo.salvar(frete);
            
            double valor = frete.calcularValorFrete();
            JOptionPane.showMessageDialog(this, 
                "Frete salvo com ID: " + frete.getId() + 
                "\nValor: R$ " + String.format("%.2f", valor),
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            limparCampos();
            listarFretesCliente(null); // Atualiza a lista após salvar
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Peso deve ser um número válido",
                "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar frete: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void listarFretesCliente(ActionEvent e) {
        Cliente clienteSelecionado = (Cliente) clienteCombo.getSelectedItem();
        
        if (clienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um cliente primeiro",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Frete> fretes = freteRepo.buscarFretesPorCliente(clienteSelecionado);
            tableModel.setRowCount(0); // Limpa a tabela
            
            for (Frete frete : fretes) {
                Object[] row = {
                    frete.getId(),
                    frete.getDescricao(),
                    String.format("%.2f kg", frete.getPeso()),
                    String.format("R$ %.2f", frete.calcularValorFrete()),
                    frete.getCidadeOrigem().getNome(),
                    frete.getCidadeDestino().getNome()
                };
                tableModel.addRow(row);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar fretes: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparCampos() {
        descricaoField.setText("");
        pesoField.setText("");
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
            TesteFrete frame = new TesteFrete();
            frame.setVisible(true);
            System.out.println("Frame visível");
        });
    }
}