package br.com.teste.pratico.model;

public class Aluno {

	private Integer id;
	private String nome;
        private Curso curso;
		
	public Aluno(){
	}
	
	public Aluno(Integer id, String nome, Curso curso) {
		this.id = id;
		this.nome = nome;
                this.curso = curso;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
        
        public Curso getCurso() {
		return curso;
	}
	
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	public String toString() {
		return "[ " + nome + " - " + " ]";
	}
	
}
