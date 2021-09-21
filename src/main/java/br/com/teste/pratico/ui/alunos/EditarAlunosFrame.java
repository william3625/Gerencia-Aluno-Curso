package br.com.teste.pratico.ui.alunos;

import br.com.teste.pratico.model.Aluno;

public class EditarAlunosFrame extends IncluirAlunosFrame {

	public EditarAlunosFrame(AlunosFrame framePrincipal) {
		super(framePrincipal);
		setTitle("Editar Aluno");
		bExcluir.setVisible(true);
	}
	
	protected Aluno loadAlunoFromPanel() {
		Aluno m = super.loadAlunoFromPanel();
		m.setId(getIdAluno());
		return m;
	}
	
}
