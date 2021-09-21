package br.com.teste.pratico.ui.cursos;

import br.com.teste.pratico.dao.CursosDAO;
import br.com.teste.pratico.model.Curso;
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



public class IncluirCursoFrame extends JFrame {

	private JTextField tfDescricao;
	private JTextField tfEmenta;
	private JFormattedTextField tfId;
	
	private JButton bSalvar;
	private JButton bCancelar;
	protected JButton bExcluir;
	
	private CursosFrame framePrincipal;

	public IncluirCursoFrame(CursosFrame framePrincipal) {
		this.framePrincipal = framePrincipal;
		setTitle("Incluir Aluno");
		setSize(300,250);
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
		
		
		tfDescricao = new JTextField();
                tfEmenta = new JTextField();
		tfId = new JFormattedTextField();
		tfId.setEnabled(false);

                painelEditarAluno.add(new JLabel("Id:"));
		painelEditarAluno.add(tfId);
		painelEditarAluno.add(new JLabel("Descrição:"));
		painelEditarAluno.add(tfDescricao);
		painelEditarAluno.add(new JLabel("Ementa:"));
		painelEditarAluno.add(tfEmenta);
		
		return painelEditarAluno;
	}
	
	private void resetForm() {
		tfId.setValue(null);
		tfDescricao.setText("");
		tfEmenta.setText("");
	}
	
	private void populaTextFields(Curso m){
		tfId.setValue(m.getId());
		tfDescricao.setText(m.getDescricao());
		tfEmenta.setText(m.getEmenta());

	}
	
	protected Integer getIdAluno(){
		try {
			return Integer.parseInt(tfId.getText());
		} catch (Exception nex) {
			return null;
		}
	}
	
	private String validador() {
		StringBuilder sb = new StringBuilder();
		sb.append(tfDescricao.getText() == null || "".equals(tfDescricao.getText().trim()) ? "Descricao, " : "");
		
		if (!sb.toString().isEmpty()) {
			sb.delete(sb.toString().length()-2, sb.toString().length());
		}
		return sb.toString();
	}
	
	protected Curso loadAlunoFromPanel() {
		String msg = validador();
		if (!msg.isEmpty()) {
			throw new RuntimeException("Informe o(s) campo(s): "+msg);
		}
		
		String descricao = tfDescricao.getText().trim();
		String ementa = tfEmenta.getText().trim();
		
		if (descricao.length() < 5) {
			throw new RuntimeException("A descricao deve conter no mínimo 5 caracteres!");
		}
		
		
		return new Curso(null, descricao, ementa);
	}
	
	public void setAluno(Curso m){
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
				Curso m = loadAlunoFromPanel();
				CursosDAO dao = new CursosDAO();
				dao.save(m);
				
				setVisible(false);
				resetForm();
				//SwingUtilities.invokeLater(framePrincipal.newAtualizaAlunosAction());
				
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(IncluirCursoFrame.this, 
						ex.getMessage(), "Erro ao incluir Aluno", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private class ExcluirAlunoListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Integer id = getIdAluno();
			if (id != null) {
				try {
					CursosDAO dao = new CursosDAO();
					Curso m = dao.findById(id);
					if (m != null) {
						dao.remove(m);
					}
					
					setVisible(false);
					resetForm();
					//SwingUtilities.invokeLater(framePrincipal.newAtualizaAlunosAction());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(IncluirCursoFrame.this,
							ex.getMessage(), "Erro ao excluir Aluno", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
