/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.model.missile;

import shipcraft.ai.missile.DefaultMissileAI;
import shipcraft.model.Corpus;
import static shipcraft.core.Config.*;
import static shipcraft.utils.Utils.*;

/**
 * @version $Id: Missile.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class Missile extends Corpus{
//    private double angle = -1.0;

    public Missile(int dx, int dy) {
        setHp(1);
        setWidth(1);
        setHeight(1);
        setDamage(getRanInt(3, 4)); // *10
        setMaxSpeed(getRanInt(22, 30));
        setAi(new DefaultMissileAI(dx, dy));
    }

    public Missile(String id, int posX, int posY, int dx, int dy){
        this(dx, dy);
        setId(id);
        setPosX(posX);
        setPosY(posY);
    }

    public void move(int moveX, int moveY) {
        /*int oldPosX = getPosX();
        int oldPosY = getPosY();*/
        int posX = getPosX();
        int posY = getPosY();
        //Собственно перемещение (смена координаты)
        posX += moveX;
        posY += moveY;
        //Если новая координата по х больше ширины поля
        if (posX > FIELD_WIDTH-1 || posY > FIELD_HEIGHT-1 ||
            posX < 0             || posY < 0 ) {
            //Самоуничтожение ракеты
            setHp(0);
            log.debug("Missile " + getId() + " exceeded field. Destructed.");//PENDING
        } else {
            setPosX(posX);
            setPosY(posY);
        /*
            //Special cases
            if(oldPosY == posY) angle = posX > oldPosX ? 90.0 : 270.0; //<- Left or right ->
            else if(oldPosX == posX) angle = posY > oldPosY ? 180.0 : 0.0; //^ Up or down V
            else {
                angle = Math.toDegrees(Math.atan(Math.abs((double)(posX - oldPosX) / (double)(posY - oldPosY))));
                if((posY > oldPosY) && (posX > oldPosX)) angle += 90.0; //II quarter
                if((posY > oldPosY) && (posX < oldPosX)) angle = 270.0 - angle; //III quarter
                if((posY < oldPosY) && (posX < oldPosX)) angle += 270.0; //IV quarter
            }*/
        }
    }

    /*public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }*/
    

}
