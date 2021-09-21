package br.com.teste.pratico.ui.alunos;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.teste.pratico.dao.AlunosDAO;
import br.com.teste.pratico.model.Aluno;
import br.com.teste.pratico.model.Curso;
import javax.swing.JComboBox;

public class IncluirAlunosFrame extends JFrame {

    private JFormattedTextField tfId;
    private JTextField tfNome;
    private JComboBox tfCurso;

    private JButton bSalvar;
    private JButton bCancelar;
    protected JButton bExcluir;

    private AlunosFrame framePrincipal;

    public IncluirAlunosFrame(AlunosFrame framePrincipal) {
        this.framePrincipal = framePrincipal;
        setTitle("Incluir Aluno");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        inicializaComponentes();
        resetForm();
    }

    private void inicializaComponentes() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(montaPanelEditarAluno(), BorderLayout.CENTER);
        panel.add(montaPanelBotoesEditar(), BorderLayout.SOUTH);
        add(panel);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }

    private JPanel montaPanelBotoesEditar() {
        JPanel panel = new JPanel();

        bSalvar = new JButton("Salvar");
        bSalvar.setMnemonic(KeyEvent.VK_S);
        bSalvar.addActionListener(new SalvarAlunoListener());
        panel.add(bSalvar);

        bCancelar = new JButton("Cancelar");
        bCancelar.setMnemonic(KeyEvent.VK_C);
        bCancelar.addActionListener(new CancelarListener());
        panel.add(bCancelar);

        bExcluir = new JButton();
        bExcluir.setText("Excluir");
        bExcluir.setMnemonic(KeyEvent.VK_E);
        bExcluir.addActionListener(new ExcluirAlunoListener());
        bExcluir.setVisible(false);
        panel.add(bExcluir);

        return panel;
    }

    private JPanel montaPanelEditarAluno() {
        JPanel painelEditarAluno = new JPanel();
        painelEditarAluno.setLayout(new GridLayout(8, 1));

        tfId = new JFormattedTextField();
        tfId.setEnabled(false);
        tfNome = new JTextField();
        String[] petStrings = { "Curso 1", "Curso 2", "Curso 3", "Curso 4", "Curso 5" };
        tfCurso = new JComboBox(petStrings);

        painelEditarAluno.add(new JLabel("Id:"));
        painelEditarAluno.add(tfId);
        painelEditarAluno.add(new JLabel("Nome:"));
        painelEditarAluno.add(tfNome);
        
        
        tfCurso.setSelectedIndex(4);       
        
        painelEditarAluno.add(new JLabel("Curso:"));
        painelEditarAluno.add(tfCurso);
        

        return painelEditarAluno;
    }
    


    private void resetForm() {
        tfId.setValue(null);
        tfNome.setText("");
        tfCurso.setSelectedIndex(0);
    }

    private void populaTextFields(Aluno m) {
        tfId.setValue(m.getId());
        tfNome.setText(m.getNome());
    }

    protected Integer getIdAluno() {
        try {
            return Integer.parseInt(tfId.getText());
        } catch (Exception nex) {
            return null;
        }
    }

    private String validador() {
        StringBuilder sb = new StringBuilder();
        sb.append(tfNome.getText() == null || "".equals(tfNome.getText().trim()) ? "Nome, " : "");

        if (!sb.toString().isEmpty()) {
            sb.delete(sb.toString().length() - 2, sb.toString().length());
        }
        return sb.toString();
    }

    protected Aluno loadAlunoFromPanel() {
        String msg = validador();
        if (!msg.isEmpty()) {
            throw new RuntimeException("Informe o(s) campo(s): " + msg);
        }

        String nome = tfNome.getText().trim();
        String curso = tfCurso.getSelectedItem().getClass().toString();

        if (nome.length() < 5) {
            throw new RuntimeException("O nome deve conter no mínimo 5 caracteres!");
        }
        
        Curso c = new Curso();
        c.setDescricao(curso);

        return new Aluno(null, nome, c);
    }

    public void setAluno(Aluno m) {
        resetForm();
        if (m != null) {
            populaTextFields(m);
        }
    }

    private class CancelarListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            resetForm();
        }
    }

    private class SalvarAlunoListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                Aluno m = loadAlunoFromPanel();
                AlunosDAO dao = new AlunosDAO();
                dao.save(m);

                setVisible(false);
                resetForm();
                //SwingUtilities.invokeLater(framePrincipal.newAtualizaAlunosAction());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(IncluirAlunosFrame.this,
                        ex.getMessage(), "Erro ao incluir Aluno", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ExcluirAlunoListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Integer id = getIdAluno();
            if (id != null) {
                try {
                    AlunosDAO dao = new AlunosDAO();
                    Aluno m = dao.findById(id);
                    if (m != null) {
                        dao.remove(m);
                    }

                    setVisible(false);
                    resetForm();
                    //SwingUtilities.invokeLater(framePrincipal.newAtualizaAlunosAction());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(IncluirAlunosFrame.this,
                            ex.getMessage(), "Erro ao excluir Aluno", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
