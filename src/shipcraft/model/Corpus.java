/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.model;

import java.awt.Color;
import shipcraft.model.missile.Missile;
import shipcraft.intrfc.CorpusAI;
import shipcraft.intrfc.Movable;
import static shipcraft.utils.Utils.*;


/**
 * @version $Id: Corpus.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public abstract class Corpus implements Movable, Cloneable{
    private String id;
    private int height;
    private int width;
    private int armor;//броня
    private int maxSpeed;
    private int posX;
    private int posY;
    private int hp;
    private int damage;//коэффициент урона при столкновении с др. объектом //TODO: может обозвать поудачнее
    private CorpusAI ai;
    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /** Возвращает максимальное возможное кол-во хп для данного тела (зависит от размеров)*/
    public int getMaxHp(){
        return width * height * 10;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }
    /** Увеличивает броню тела на 1 */
    public void increaseArmor(){
        armor++;
    }

    public CorpusAI getAi() {
        return ai;
    }

    public void setAi(CorpusAI ai) {
        this.ai = ai;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    /** Увеличивает максимальную скорость на 1 */
    public void increaseMaxSpeed(){
        maxSpeed ++;
    }
    /** Уменьшает максимальную скорость на 1 */
    public void decreaseMaxSpeed(){
        maxSpeed --;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    //Клонирование корпуса
    @Override
    protected Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {return null;}
    }

    public void takeDamageFrom(Corpus corpus){
        int corpusWidth = corpus.getWidth();
        int corpusHeight = corpus.getHeight();
        int corpusDamage = corpus.getDamage();
        int damageFromCorpus = corpusWidth * corpusHeight * corpusDamage * 10;
        //С учетом брони тела
        int totalDamage = damageFromCorpus - (damageFromCorpus * armor / 10);
        if (this instanceof Ship)
            log.info(id + " [hp: " + hp + "; arm: " + armor + "] is taking damage [" + totalDamage + "]");
        if (this instanceof Missile)
            log.debug(id + " [hp: " + hp + "; arm: " + armor + "] is taking damage [" + totalDamage + "]");
            
        hp -= totalDamage;
    }

    

}
