package br.com.teste.pratico.model;

public class Curso {

	private Integer id;
	private String descricao;
        private String ementa;
		
	public Curso(){
	}
	
	public Curso(Integer id, String descricao, String ementa) {
		this.id = id;
		this.descricao = descricao;
                this.ementa = ementa;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
        
        public String getEmenta() {
		return ementa;
	}
	
	public void setEmenta(String ementa) {
		this.ementa = ementa;
	}
	
	public String toString() {
		return "[ " + ementa + " - " + " ]";
	}
	
}
