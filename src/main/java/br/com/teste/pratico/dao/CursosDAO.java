package br.com.teste.pratico.dao;


import br.com.teste.pratico.exception.PersistenceException;
import br.com.teste.pratico.model.Curso;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class CursosDAO {

	//comandos SQL utilizados pelo DAO.
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS curso(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, descricao VARCHAR(50), ementa TEXT)";
	private final static String INSERT_ALUNO = "INSERT INTO curso (descricao, ementa) VALUES (?,?)";
	private final static String UPDATE_ALUNO = "UPDATE curso SET descricao = ?, ementa = ? WHERE id = ?";
	private final static String DELETE_ALUNO = "DELETE FROM curso WHERE id = ?";
	private final static String GET_ALL_ALUNOS = "SELECT * FROM curso";
	private final static String GET_ALUNOS_BY_NOME = "SELECT * FROM curso WHERE descricao like ?";
	private final static String GET_ALUNO_BY_ID = "SELECT * FROM curso WHERE id = ?";
	
	private static Logger log = Logger.getLogger(CursosDAO.class);
        
        
        
        public void init() throws PersistenceException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.createStatement();
			int r = stmt.executeUpdate(CREATE_TABLE);
			
			if (r > 0) {
				log.info("Criou a tabela 'aluno'");
			}
		} catch (SQLException e) {
			log.error(e);
			throw new PersistenceException("Não foi possivel inicializar o banco de dados: " + CREATE_TABLE, e);
		} finally {
			ConnectionManager.closeAll(conn, stmt);
		}
	}
	
	
	public void save(Curso aluno) throws PersistenceException {
		if (aluno == null) {
			throw new PersistenceException("Informe a Aluno para salvar!");
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionManager.getConnection();
			if (aluno.getId() == null) {
				stmt = getStatementInsert(conn, aluno);
			} else {
				stmt = getStatementUpdate(conn, aluno);
			}
			stmt.executeUpdate();
			conn.commit();
			log.debug("Aluno foi salva");
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception sx) {}
			String errorMsg = "Erro ao salvar Aluno!";
			log.error(errorMsg, e);
			throw new PersistenceException(errorMsg, e);
		} finally {
			ConnectionManager.closeAll(conn, stmt);
		}
	}
	
	private PreparedStatement getStatementInsert(Connection conn, Curso m) throws SQLException {
		PreparedStatement stmt = createStatementWithLog(conn, INSERT_ALUNO);
		stmt.setString(1, m.getDescricao());
                stmt.setString(2, m.getEmenta());
		return stmt;
	}
	
	private PreparedStatement getStatementUpdate(Connection conn, Curso m) throws SQLException {
		PreparedStatement stmt = createStatementWithLog(conn, UPDATE_ALUNO);
		stmt.setString(1, m.getDescricao());
                stmt.setString(2, m.getEmenta());
		stmt.setInt(3, m.getId());
		return stmt;
	}

	
	public void remove(Curso m) throws PersistenceException {
		if (m == null || m.getId() == null) {
			throw new PersistenceException("Informe a aluno para exclusao!");
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionManager.getConnection();
			stmt = createStatementWithLog(conn, DELETE_ALUNO);
			stmt.setInt(1, m.getId());
			stmt.executeUpdate();
			conn.commit();
			log.debug("Aluno foi excluida");
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception sx) {}
			String errorMsg = "Erro ao excluir Aluno!";
			log.error(errorMsg, e);
			throw new PersistenceException(errorMsg, e);
		}finally{
			ConnectionManager.closeAll(conn, stmt);
		}
	}
	
	
	public Curso findById(Integer id) throws PersistenceException {
		if (id == null || id.intValue() <= 0) {
			throw new PersistenceException("Informe o id válido para fazer a busca!");
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Curso m = null;
		
		try {
			conn = ConnectionManager.getConnection();
			stmt = createStatementWithLog(conn, GET_ALUNO_BY_ID);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				String descricao = rs.getString("descricao");
				String ementa = rs.getString("ementa");
				
				m = new Curso(id, descricao, ementa);
			}
			return m;
		} catch (SQLException e) {
			String errorMsg = "Erro ao consultar aluno por id!";
			log.error(errorMsg, e);
			throw new PersistenceException(errorMsg, e);
		} finally {
			ConnectionManager.closeAll(conn, stmt, rs);
		}
	}
	
	
	public List<Curso> getAll() throws PersistenceException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			stmt = createStatementWithLog(conn, GET_ALL_ALUNOS);
			rs = stmt.executeQuery();
			
			return toAlunos(rs);
		} catch (SQLException e) {
			String errorMsg = "Erro ao consultar todas as alunos!";
			log.error(errorMsg, e);
			throw new PersistenceException(errorMsg, e);
		} finally {
			ConnectionManager.closeAll(conn, stmt, rs);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Curso> getAlunosByNome(String descricao) throws PersistenceException {
		if (descricao == null || descricao.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			stmt = createStatementWithLog(conn, GET_ALUNOS_BY_NOME);
			stmt.setString(1, "%" + descricao + "%");
			rs = stmt.executeQuery();
			
			return toAlunos(rs);
		} catch (SQLException e) {
			String errorMsg = "Erro ao consultar alunos por nome!";
			log.error(errorMsg, e);
			throw new PersistenceException(errorMsg, e);
		} finally {
			ConnectionManager.closeAll(conn, stmt, rs);
		}
	}
	
	private List<Curso> toAlunos(ResultSet rs) throws SQLException {
		List<Curso> lista = new ArrayList<Curso>();
		while (rs.next()) {
			int id = rs.getInt("id");
			String descricao = rs.getString("descricao");
                        String ementa = rs.getString("ementa");
			lista.add(new Curso(id, descricao, ementa));
		}
		return lista;
	}

	private static PreparedStatement createStatementWithLog(Connection conn, String sql) throws SQLException{
		if (conn == null)
			return null;
		
		log.debug("SQL: "+sql);
		return conn.prepareStatement(sql);
	}
	
}