package br.com.teste.pratico.dao;

import br.com.teste.pratico.exception.PersistenceException;
import br.com.teste.pratico.model.Aluno;
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

public class AlunosDAO {

    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS aluno(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(50)); ";
    private final static String INSERT_ALUNO = "INSERT INTO aluno (nome) VALUES (?)";
    private final static String UPDATE_ALUNO = "UPDATE aluno SET nome = ? WHERE id = ?";
    private final static String DELETE_ALUNO = "DELETE FROM aluno WHERE id = ?";
    private final static String GET_ALL_ALUNOS = "SELECT a.id, a.nome, c.descricao FROM aluno a LEFT JOIN aluno_curso ac ON(ac.id_aluno = a.id) LEFT JOIN curso c ON(ac.id_curso=c.id)";
    private final static String GET_ALUNOS_BY_NOME = "SELECT * FROM aluno WHERE nome like ?";
    private final static String GET_ALUNO_BY_ID = "SELECT * FROM aluno WHERE id = ?";

    private static Logger log = Logger.getLogger(AlunosDAO.class);

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

    public void save(Aluno aluno) throws PersistenceException {
        if (aluno == null) {
            throw new PersistenceException("Informe o aluno para salvar!");
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
            log.debug("Aluno foi salvo");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception sx) {
            }
            String errorMsg = "Erro ao salvar o aluno!";
            log.error(errorMsg, e);
            throw new PersistenceException(errorMsg, e);
        } finally {
            ConnectionManager.closeAll(conn, stmt);
        }
    }

    private PreparedStatement getStatementInsert(Connection conn, Aluno m) throws SQLException {
        PreparedStatement stmt = createStatementWithLog(conn, INSERT_ALUNO);
        stmt.setString(1, m.getNome());
        return stmt;
    }

    private PreparedStatement getStatementUpdate(Connection conn, Aluno m) throws SQLException {
        PreparedStatement stmt = createStatementWithLog(conn, UPDATE_ALUNO);
        stmt.setString(1, m.getNome());
        stmt.setInt(5, m.getId());
        return stmt;
    }

    public void remove(Aluno m) throws PersistenceException {
        if (m == null || m.getId() == null) {
            throw new PersistenceException("Informe O ALUNO para exclusao!");
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionManager.getConnection();
            stmt = createStatementWithLog(conn, DELETE_ALUNO);
            stmt.setInt(1, m.getId());
            stmt.executeUpdate();
            conn.commit();
            log.debug("O ALUNO foi excluido");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception sx) {
            }
            String errorMsg = "Erro ao excluir o ALUNO!";
            log.error(errorMsg, e);
            throw new PersistenceException(errorMsg, e);
        } finally {
            ConnectionManager.closeAll(conn, stmt);
        }
    }

    public Aluno findById(Integer id) throws PersistenceException {
        if (id == null || id.intValue() <= 0) {
            throw new PersistenceException("Informe o id válido para fazer a busca!");
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Aluno m = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = createStatementWithLog(conn, GET_ALUNO_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                int qtde = rs.getInt("quantidade");
                double preco = rs.getDouble("preco");

                m = new Aluno(id, nome, null);
            }
            return m;
        } catch (SQLException e) {
            String errorMsg = "Erro ao consultar ALUNO por id!";
            log.error(errorMsg, e);
            throw new PersistenceException(errorMsg, e);
        } finally {
            ConnectionManager.closeAll(conn, stmt, rs);
        }
    }

    public List<Aluno> getAll() throws PersistenceException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = createStatementWithLog(conn, GET_ALL_ALUNOS);
            rs = stmt.executeQuery();

            return toAlunos(rs);
        } catch (SQLException e) {
            String errorMsg = "Erro ao consultar todOs Os ALUNOS!";
            log.error(errorMsg, e);
            throw new PersistenceException(errorMsg, e);
        } finally {
            ConnectionManager.closeAll(conn, stmt, rs);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Aluno> getAlunosByNome(String nome) throws PersistenceException {
        if (nome == null || nome.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = createStatementWithLog(conn, GET_ALUNOS_BY_NOME);
            stmt.setString(1, nome + "%");
            rs = stmt.executeQuery();

            return toAlunos(rs);
        } catch (SQLException e) {
            String errorMsg = "Erro ao consultar ALUNOS por nome!";
            log.error(errorMsg, e);
            throw new PersistenceException(errorMsg, e);
        } finally {
            ConnectionManager.closeAll(conn, stmt, rs);
        }
    }

    private List<Aluno> toAlunos(ResultSet rs) throws SQLException {
        List<Aluno> lista = new ArrayList<Aluno>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String nome = rs.getString("nome");
            String descricao = rs.getString("descricao");

            Curso c = new Curso();

            c.setDescricao(descricao);

            lista.add(new Aluno(id, nome, c));
        }
        return lista;
    }

    private static PreparedStatement createStatementWithLog(Connection conn, String sql) throws SQLException {
        if (conn == null) {
            return null;
        }

        log.debug("SQL: " + sql);
        return conn.prepareStatement(sql);
    }

}
