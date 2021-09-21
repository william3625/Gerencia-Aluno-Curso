package br.com.teste.pratico.ui.cursos;

import br.com.teste.pratico.model.Curso;
import java.util.List;

import javax.swing.JTable;



public class CursoTable extends JTable {

	private CursoTableModel modelo;
	
	public CursoTable() {
		modelo = new CursoTableModel();
		setModel(modelo);
	}
	
	public Curso getAlunoSelected() {
		int i = getSelectedRow();
		if (i < 0) {
			return null;
		}
		return modelo.getAlunoAt(i);
	}
	
	public void reload(List<Curso> alunos) {
		modelo.reload(alunos);
	}
}
