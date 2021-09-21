package br.com.teste.pratico.ui.alunos;

import java.util.List;

import javax.swing.JTable;

import br.com.teste.pratico.model.Aluno;

public class AlunosTable extends JTable {

	private AlunosTableModel modelo;
	
	public AlunosTable() {
		modelo = new AlunosTableModel();
		setModel(modelo);
	}
	
	public Aluno getAlunoSelected() {
		int i = getSelectedRow();
		if (i < 0) {
			return null;
		}
		return modelo.getAlunoAt(i);
	}
	
	public void reload(List<Aluno> alunos) {
		modelo.reload(alunos);
	}
}
