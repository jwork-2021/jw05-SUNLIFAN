package cn.edu.nju.entity;

import java.util.List;

import cn.edu.nju.scene.Map;
import cn.edu.nju.scene.Tile;
import cn.edu.nju.utils.Direction;

public class Creature {
    public Map map;
    protected List<Bullet> bullets;

    protected String name;

    public Direction dir;
    public Direction facing;

    protected int maxHealth;
    protected int health;

    protected int strength;
    protected int defence;

    protected boolean alive;

    protected int xPos;
    protected int yPos;

    public int getXPos(){return xPos;}

    public int getYPos(){return yPos;}

    public Creature(String name, int x, int y, Map map, List<Bullet> bullets){
        this.name = name;
        this.xPos = x;
        this.yPos = y;
        this.maxHealth = 10;
        this.health = 10;
        this.strength = 3;
        this.defence = 2;
        this.facing = Direction.LEFT;
        this.dir = Direction.LEFT;
        this.alive = true;
        this.map = map;
        map.getTile(xPos, yPos).setCreature(this);
        this.bullets = bullets;
    }

    public String getName(){return this.name;}

    public synchronized void damage(int amount){
        this.health -= amount;
        if(this.health <= 0){
            health = 0;
            map.getTile(xPos, yPos).setCreature(null);
            this.alive = false;
        }
    }

    public void setDirection(Direction dir){
        this.dir = dir;
    }

    public int getHealth(){return health;}

    public int getStrength(){return strength;}

    public int getMaxHealth(){return maxHealth;}
    
    public synchronized void move(){
        Tile neighborTile = map.getNeighborTile(xPos, yPos, dir);
        int curX = xPos;
        int curY = yPos;
        switch(dir){
            case LEFT:   
                curY--;
                break;
            case RIGHT:   
                curY++;
                break;
            case UP:     
                curX--;
                break;
            case DOWN:    
                curX++;
                break;
        }

        if(neighborTile!=null && neighborTile.isAvailable()){
            Tile curTile = map.getTile(xPos, yPos);
            curTile.setCreature(null);
            xPos = curX;
            yPos = curY;
            neighborTile.setCreature(this);
        }
        
    }

    public boolean isAlive(){return this.alive;}

    public void fire(Direction dir){
        if(bullets.size() <= 100)bullets.add(new Bullet(strength,dir, xPos, yPos,name,map));
        else System.out.println("Bullet list is full !");
    }



}
