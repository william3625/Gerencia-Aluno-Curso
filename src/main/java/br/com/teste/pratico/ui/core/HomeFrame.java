package br.com.teste.pratico.ui.core;

import br.com.teste.pratico.ui.cursos.CursosFrame;
import br.com.teste.pratico.ui.alunos.AlunosFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

public class HomeFrame extends JFrame {

    private JMenuBar menubar;

    private AlunosFrame alunosFrame;
    private CursosFrame cursosFrame;

    public HomeFrame() {
        inicializaComponentes();
        setSize(400, 400);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) ((dimension.getWidth() - getWidth()) / 2), (int) ((dimension.getHeight() - getHeight()) / 2));
        setVisible(true);
    }

    private void inicializaComponentes() {

        menubar = new JMenuBar();
        Menu menuAcoes = new Menu("Ações");
        JMenuItem menuAlunos = new JMenuItem("Alunos");
        menuAlunos.addActionListener(new AlunosMenuListener());
        menuAcoes.add(menuAlunos);
        JMenuItem menuCursos = new JMenuItem("Cursos");
        menuCursos.addActionListener(new CursosMenuListener());
        menuAcoes.add(menuCursos);
        JLabel bottomLabel = new JLabel("<html>Teste prático VR Software <BR> Gerenciamento de Cursos e Alunos <BR> William Nascimento</html>", SwingConstants.CENTER);
        menubar.add(menuAcoes);
        setJMenuBar(menubar);
        alunosFrame = new AlunosFrame();
        alunosFrame.setVisible(false);
        cursosFrame = new CursosFrame();
        add(bottomLabel);
        cursosFrame.setVisible(false);
        //inicializaDB();
    }

    private class AlunosMenuListener extends AbstractAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            alunosFrame.setVisible(true);
        }
    }
    
    private class CursosMenuListener extends AbstractAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            cursosFrame.setVisible(true);
        }
    }

}
