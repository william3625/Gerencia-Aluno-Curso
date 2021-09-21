package br.com.teste.pratico.ui.alunos;

import br.com.teste.pratico.dao.AlunosDAO;
import br.com.teste.pratico.exception.PersistenceException;
import br.com.teste.pratico.model.Aluno;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class AlunosFrame extends JFrame {

    private AlunosTable tabela;
    private JScrollPane scrollPane;
    private JButton bNovaAluno;
    private JButton bBuscarAluno;
    private JButton bAtualizaLista;

    private IncluirAlunosFrame incluirFrame;
    private EditarAlunosFrame editarFrame;
    private BuscaAlunosFrame buscaFrame;

    public AlunosFrame() {
        setTitle("Gerencia de aluno");
        inicializaComponentes();
        adicionaComponentes();
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void inicializaComponentes() {
        tabela = new AlunosTable();
        tabela.addMouseListener(new EditarAlunoListener());
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(tabela);

        bNovaAluno = new JButton();
        bNovaAluno.setText("Nova");
        bNovaAluno.setMnemonic(KeyEvent.VK_N);
        bNovaAluno.addActionListener(new IncluirAlunoListener());

        bBuscarAluno = new JButton();
        bBuscarAluno.setText("Buscar");
        bBuscarAluno.setMnemonic(KeyEvent.VK_B);
        bBuscarAluno.addActionListener(new BuscarAlunoListener());

        bAtualizaLista = new JButton();
        bAtualizaLista.setText("Atualizar");
        bAtualizaLista.setMnemonic(KeyEvent.VK_A);
        bAtualizaLista.addActionListener(new AtualizarListaListener());

        incluirFrame = new IncluirAlunosFrame(this);
        editarFrame = new EditarAlunosFrame(this);
        buscaFrame = new BuscaAlunosFrame(this);

        inicializaDB();
    }

    private void adicionaComponentes() {
        add(scrollPane);
        JPanel panel = new JPanel();
        panel.add(bNovaAluno);
        panel.add(bBuscarAluno);
        panel.add(bAtualizaLista);
        add(panel, BorderLayout.SOUTH);
    }

    private void inicializaDB() {
        try {
            new AlunosDAO().init();
            SwingUtilities.invokeLater(newAtualizaAlunosAction());
        } catch (PersistenceException ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível inicializar o Banco de dados: "
                    + ex.getMessage() + "\nVerifique a dependência do driver ou configurações do banco!", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public Runnable newAtualizaAlunosAction() {
        return new Runnable() {
            public void run() {
                try {
                    AlunosDAO dao = new AlunosDAO();
                    tabela.reload(dao.getAll());
                } catch (PersistenceException ex) {
                    JOptionPane.showMessageDialog(AlunosFrame.this,
                            ex.getMessage(), "Erro ao consultar Aluno(s)", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }

    public void refreshTable(List<Aluno> alunos) {
        tabela.reload(alunos);
    }

    private class AtualizarListaListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(newAtualizaAlunosAction());
        }
    }

    private class IncluirAlunoListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            incluirFrame.setVisible(true);
        }
    }

    private class EditarAlunoListener extends MouseAdapter {

        public void mouseClicked(MouseEvent event) {
            if (event.getClickCount() == 2) {
                Aluno m = tabela.getAlunoSelected();
                if (m != null) {
                    editarFrame.setAluno(m);
                    editarFrame.setVisible(true);
                }
            }
        }
    }

    private class BuscarAlunoListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            buscaFrame.setVisible(true);
        }
    }

}
