package br.com.teste.pratico.ui.alunos;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.teste.pratico.model.Aluno;

public class AlunosTableModel extends AbstractTableModel {

    private List<Aluno> alunos;

    private String[] colNomes = {"Código", "Nome", "Curso"};
    private Class<?>[] colTipos = {Integer.class, String.class, String.class};

    public AlunosTableModel() {
    }

    public void reload(List<Aluno> alunos) {
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
        if (alunos == null) {
            return 0;
        }
        return alunos.size();
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Aluno m = alunos.get(linha);
        switch (coluna) {
            case 0:
                return m.getId();
            case 1:
                return m.getNome();
            case 2:
                return m.getCurso().getDescricao();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Aluno getAlunoAt(int index) {
        return alunos.get(index);
    }
}
