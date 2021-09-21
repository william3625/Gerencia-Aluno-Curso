package br.com.teste.pratico.ui.cursos;

import br.com.teste.pratico.model.Curso;

public class EditaCursoFrame extends IncluirCursoFrame {

	public EditaCursoFrame(CursosFrame framePrincipal) {
		super(framePrincipal);
		setTitle("Editar Aluno");
		bExcluir.setVisible(true);
	}
	
	protected Curso loadAlunoFromPanel() {
		Curso m = super.loadAlunoFromPanel();
		m.setId(getIdAluno());
		return m;
	}
	
}
