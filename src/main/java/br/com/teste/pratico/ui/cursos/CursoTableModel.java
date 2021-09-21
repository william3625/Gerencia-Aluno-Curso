package br.com.teste.pratico.ui.cursos;

import br.com.teste.pratico.model.Curso;
import java.util.List;

import javax.swing.table.AbstractTableModel;



public class CursoTableModel extends AbstractTableModel {

	private List<Curso> alunos;
	
	private String[] colNomes = { "Código", "Descrição", "Ementa" };
	private Class<?>[] colTipos = { Integer.class, String.class, String.class };
	
	public CursoTableModel(){
	}
	
	public void reload(List<Curso> alunos) {
		this.alunos = alunos;
		//atualiza o componente na tela
		fireTableDataChanged();
	}

	@Override
	public Class<?> getColumnClass(int coluna) {
		return colTipos[coluna];
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int coluna) {
		return colNomes[coluna];
	}

	@Override
	public int getRowCount() {
		if (alunos == null){
			return 0;
		}
		return alunos.size();
	}

	@Override
	public Object getValueAt(int linha, int coluna) {
		Curso m = alunos.get(linha);
		switch (coluna) {
		case 0:
                    return m.getId();
                case 1:
                    return m.getDescricao();
                case 2:
                    return m.getEmenta();
		default:
			return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	public Curso getAlunoAt(int index) {
		return alunos.get(index);
	}
}
