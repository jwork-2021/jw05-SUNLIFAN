package cn.edu.nju.gui;

import javax.swing.JPanel;

import cn.edu.nju.GameLogic.GameControl;

import java.awt.Graphics;
import java.awt.Color;

public class GameScreen extends JPanel{
    
    private Renderer renderer;
    
    public GameScreen(){
        super();
		
		this.renderer = new Renderer();
    }

    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        try {
			graphics.setColor(Color.BLACK);
			graphics.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);

			renderer.renderMap(GameControl.getMap(), GameControl.getPlayer(),graphics);
			renderer.renderPlayer(GameControl.getPlayer(), graphics);
			renderer.renderMonsters(GameControl.getMonsters(),GameControl.getPlayer(), graphics);
			renderer.renderBullets(GameControl.getBullets(), GameControl.getPlayer(), graphics);
		} catch (Exception e) {
			System.err.println("\n[Logic][GameLoop]: Uncaught exception in render system!\n");
			e.printStackTrace();
			System.exit(-1);
		}
		
		repaint();
    }
}