package br.com.teste.pratico.ui.cursos;

import br.com.teste.pratico.dao.CursosDAO;
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
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import br.com.teste.pratico.exception.PersistenceException;
import br.com.teste.pratico.model.Curso;


public class CursosFrame extends JFrame {

    private CursoTable tabela;
    private JScrollPane scrollPane;
    private JButton bNovaAluno;
    private JButton bBuscarAluno;
    private JButton bAtualizaLista;
    private JMenuBar menubar;

    private IncluirCursoFrame incluirFrame;
    private EditaCursoFrame editarFrame;
    private BuscaCursoFrame buscaFrame;

    public CursosFrame() {
        setTitle("Gerenciamento de Cursos");

        inicializaComponentes();
        adicionaComponentes();

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void inicializaComponentes() {
        tabela = new CursoTable();
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

        menubar = new JMenuBar();

        setJMenuBar(menubar);

        incluirFrame = new IncluirCursoFrame(this);
        editarFrame = new EditaCursoFrame(this);
        buscaFrame = new BuscaCursoFrame(this);

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
            new CursosDAO().init();
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
                    CursosDAO dao = new CursosDAO();
                    tabela.reload(dao.getAll());
                } catch (PersistenceException ex) {
                    JOptionPane.showMessageDialog(CursosFrame.this,
                            ex.getMessage(), "Erro ao consultar Aluno(s)", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }

    public void refreshTable(List<Curso> alunos) {
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
                Curso m = tabela.getAlunoSelected();
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

    private class SobreMenuListener extends AbstractAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //sobreFrame.setVisible(true);
        }
    }

}
