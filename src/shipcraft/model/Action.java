/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.model;
import static shipcraft.core.Constants.*;

/**
 * @version $Id: Action.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class Action{
    private int dx;
    private int dy;
    private int speed;
    private String type;
    /** comment from AI (if necessary) */
    private String aiComment;
    /** Рэйтинг текущего действия (необязательный параметр)*/
    private int rating;

    public Action(String type){
        this.type = type;
    }

    public Action(String type, int dx, int dy) {
        this.type = type;
        this.dx = dx;
        this.dy = dy;
    }
    
    public Action(String type, int speed, int dx, int dy) {
        this.type = type;
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;
    }

    public Action() {
        this("move", MOVE_0, MOVE_0);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    /** Увеличивает рэйтинг действия на единичку*/
    public void increaseRating(){
        rating++;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getAiComment() {
        return aiComment;
    }

    public void setAiComment(String aiComment) {
        this.aiComment = aiComment;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public String getType() {
        return type;
    }

    public void setType(String actionType) {
        this.type = actionType;
    }

}
