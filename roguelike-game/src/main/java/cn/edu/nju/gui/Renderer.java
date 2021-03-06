package cn.edu.nju.gui;

import cn.edu.nju.GameLogic.GameControl;
import cn.edu.nju.entity.Bullet;
import cn.edu.nju.entity.Monster;
import cn.edu.nju.entity.Player;
import cn.edu.nju.resources.Textures;
import cn.edu.nju.scene.Map;
import cn.edu.nju.scene.Tile;
import cn.edu.nju.utils.Direction;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Vector;
import java.awt.Font;
import java.awt.Color;

public class Renderer {
    private int zoomLevel;

    public Renderer(){
        this.zoomLevel = 2;
    }

    /**
     * render player image at correct place
     * @param player
     * @param graphics
     */
    public void renderPlayer(Player player, Graphics graphics){
        BufferedImage sprite = Textures.getSprite("player");

        if(player.dir == Direction.RIGHT)sprite = mirrorImage(Textures.getSprite("player"));

        int drawPosX = (Window.WIDTH/2)-(sprite.getWidth()/2)*zoomLevel;
		int drawPosY = (Window.HEIGHT/2)-(sprite.getHeight()/2)*zoomLevel;

        graphics.drawImage(sprite, drawPosX, drawPosY, sprite.getWidth()*zoomLevel,
        sprite.getHeight()*zoomLevel,null);

    }

    /**
     * render tiles at correct position
     * @param mapData
     * @param graphics
     */
    public void renderMap(Map mapData, Player player,Graphics graphics){
        int width = mapData.getWidth();
        int height = mapData.getHeight();

        for(int i = 0; i < height;i++)
            for(int j = 0; j < width;j ++){
                Tile tile = mapData.getTile(i, j);
                BufferedImage sprite = Textures.getSprite(tile.getName());
                int drawPosX = calculateWidthOffset(sprite, tile, player);
                int drawPosY = calculateHeightOffset(sprite, tile, player);
                graphics.drawImage(sprite, drawPosX, drawPosY, 
                sprite.getWidth()*zoomLevel,sprite.getHeight()*zoomLevel, null);
            }
    }

    /**
     * render current alive monsters
     * @param monsters
     * @param graphics
     */
    public void renderMonsters(Collection<Monster> monsters, Player player, Graphics graphics){
        if(monsters == null || monsters.isEmpty())return;

        for(Monster m : monsters){
            if(!m.isAlive())continue;
            BufferedImage sprite = Textures.getSprite(m.getName());
            int xPos = calculateWidthOffset(sprite, m, player);
            int yPos = calculateHeightOffset(sprite, m, player);
            graphics.drawImage(sprite, xPos, yPos, sprite.getWidth()*zoomLevel,
            sprite.getHeight()*zoomLevel, null);
        }
    }

    /**
     * render bullets at correct places
     * @param bullets
     * @param player
     * @param graphics
     */
    public void renderBullets(Vector<Bullet> bullets, Player player, Graphics graphics){
        if(bullets == null || bullets.isEmpty())return;
        
        for(int i = 0 ; i < bullets.size();i++){
                Bullet b = bullets.get(i);
                if(!b.isActive()){
                    bullets.remove(b);
                    continue;
                }
            BufferedImage sprite = Textures.getSprite("bullet");
            int drawPosX = calculateWidthOffset(sprite, b, player);
            int drawPosY = calculateHeightOffset(sprite, b, player);
            graphics.drawImage(sprite, drawPosX, drawPosY, 
            sprite.getWidth(), sprite.getHeight(), null);
        }
    }

    /**
     * render start screen
     * @param graphics
     */
    public void renderTitleScreen(Graphics graphics) {
		graphics.setColor(Color.WHITE);
		graphics.drawRoundRect(50, 50, Window.WIDTH-150, Window.HEIGHT-150, 10, 10);
		graphics.setFont(new Font("Dialog", Font.PLAIN, 40));
		graphics.drawString("Roguelike Game", 100, 100);
		graphics.setFont(new Font("Dialog", Font.PLAIN, 20));
		graphics.drawString("Greetings chosen,", 100, 150);
		graphics.drawString("I've been waiting here for you since the beginning of this universe...", 100, 180);
		graphics.drawString("You know the world is fading...", 100, 210);
		graphics.drawString("Can you escape here?", 100, 240);
		graphics.drawString("Press enter to start.....", 200, 350);
	}


    /**
     * render the end screen when game ends
     * @param graphics
     */
    public void renderEndScreen(Graphics graphics){
        graphics.setColor(Color.WHITE);
		graphics.drawRoundRect(50, 50, Window.WIDTH-150, Window.HEIGHT-150, 10, 10);
		graphics.setFont(new Font("Dialog", Font.PLAIN, 40));
		graphics.drawString("Roguelike Game", 100, 100);
        if(GameControl.playerWin){
            graphics.drawString("Marvelous! You have escaped....", 100, 150);
        }else{
            graphics.drawString("You died. What a pity ....", 100, 150);
        }
    }


    public void renderUI(Graphics2D graphics, Player player){
        graphics.setColor(Color.BLACK);
		graphics.fillRoundRect(5, 5, 100, 150, 10, 10);
		graphics.setColor(Color.WHITE);
		graphics.drawRoundRect(5, 5, 100, 150, 10, 10);


        graphics.setFont(new Font("Dialog", Font.PLAIN, 20));
		graphics.drawString("- Player -", 10, 25);
		graphics.setFont(new Font("Dialog", Font.PLAIN, 12));
		graphics.drawString("HP: "+player.getHealth()+"/"+player.getMaxHealth(), 10, 45);
		graphics.drawString("STR: "+player.getStrength(), 10, 65);
    }

    /**
     * mirror the image based on its facing
     * @param image
     * @return
     */
    private BufferedImage mirrorImage(BufferedImage image) {
		int h = image.getHeight();
		int w = image.getWidth();
		BufferedImage rotated = new BufferedImage(h, w, image.getType());
		
		for(int x=0;x<w;x++) {
			for(int y=0;y<h;y++) {
				rotated.setRGB(x, y, image.getRGB(w-1-x, y));
			}
		}
		return rotated;
	}


    private int calculateWidthOffset(BufferedImage sprite, Tile tile, Player player){
        int offsetOnScreen = (tile.getYPos() - player.getYPos())*sprite.getWidth()*zoomLevel + (Window.WIDTH/2)-(sprite.getWidth()/2)*zoomLevel;;

        return offsetOnScreen;
    }

    private int calculateHeightOffset(BufferedImage sprite, Bullet bullet, Player player){
        int offsetOnScreen = (bullet.getXPos() - player.getXPos())*sprite.getHeight()*zoomLevel + (Window.HEIGHT/2)-(sprite.getHeight()/2)*zoomLevel;

        return offsetOnScreen;
    }

    private int calculateWidthOffset(BufferedImage sprite, Bullet bullet, Player player){
        int offsetOnScreen = (bullet.getYPos() - player.getYPos())*sprite.getWidth()*zoomLevel + (Window.WIDTH/2)-(sprite.getWidth()/2)*zoomLevel;;

        return offsetOnScreen;
    }

    private int calculateHeightOffset(BufferedImage sprite, Tile tile, Player player){
        int offsetOnScreen = (tile.getXPos() - player.getXPos())*sprite.getHeight()*zoomLevel + (Window.HEIGHT/2)-(sprite.getHeight()/2)*zoomLevel;

        return offsetOnScreen;
    }

    private int calculateWidthOffset(BufferedImage sprite, Monster monster, Player player){
        int offsetOnScreen = (monster.getYPos() - player.getYPos())*sprite.getWidth()*zoomLevel + (Window.WIDTH/2)-(sprite.getWidth()/2)*zoomLevel;

        return offsetOnScreen;
    }

    private int calculateHeightOffset(BufferedImage sprite, Monster monster, Player player){
        int offsetOnScreen = (monster.getXPos() - player.getXPos())*sprite.getHeight()*zoomLevel + (Window.HEIGHT/2)-(sprite.getHeight()/2)*zoomLevel;

        return offsetOnScreen;
    }

}
