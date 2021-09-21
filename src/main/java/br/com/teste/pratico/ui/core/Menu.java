package br.com.teste.pratico.ui.core;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.KeyStroke;


public class Menu extends JMenu {

	public Menu(String title) {
		super(title);
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"),"click");
	}
	

	public void addListener(final AbstractAction action) {
		this.getActionMap().put("click", action);
	}

}
