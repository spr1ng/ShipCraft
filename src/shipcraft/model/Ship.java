package shipcraft.model;

import shipcraft.ai.ship.DefaultShipAI;
import static shipcraft.core.Config.*;
import static shipcraft.utils.Utils.*;

/**
 * @version $Id: Ship.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class Ship extends Corpus{
   
    private String name;
    /** Команда корабля */
    private String teamName;
    /** Кол-во возможных апгрейдов */
    private int upgradePoints;
    /** Количество фотонных ракет */
    private int missileQuantity;
    /** Количество особо разрушительных ракет */
    private int heavyMissileQuantity;
    /** Количество парализующих ракет */
    private int stunningMissileQuantity;
    /** Количество зажигательных ракет */
    private int burningMissileQuantity;
    /** Количество ракет, выпущенных кораблем */
    private int missileIdx;
    /** Кол-во восстанавливаемых hp у корабля перед каждым ходом */
    private int regenHpLevel;
    /** Время парализованности корабля */
    private int stunTime;
    /** Возможность корабля стрелять критическими ракетами */
    private int criticalAttackLevel;
    /** Способность корабля уклоняться от ракет */
    private int evasionLevel;
    /** Время невидимости корабля */
    private int invisibleTime;
    /** Уровень способности корабля быть невидимым*/
    private int invisibleLevel;
    /** Урон получаемый от зажигательной ракеты */
    private int burningDamage;

    public Ship(String id) {
        setId(id);
        //Устанавливаем AI
        setAi(new DefaultShipAI());
        //Устанавливаем кол-во ракет
        setMissileQuantity(MISSILE_QUANTITY);
        //Устанавливаем кол-во ракет высокой разрушительной способности
        setHeavyMissileQuantity(HEAVY_MISSILE_QUANTITY);
        //Устанавливаем кол-во парализующих ракет
        setStunningMissileQuantity(STUNNING_MISSILE_QUANTITY);
        //Устанавливаем кол-во доступных апгрейдов для корабля
        setUpgradePoints(UPGRADABLE_POINTS);
        //Устанавливаем броню
        setArmor(ARMOR);
        //Устанавливаем регенерацию
        setRegenHpLevel(REGEN_HP_LEVEL);
        //Устанавливаем коэффициент урона при столкновении с др. объектом
        setDamage(1);
        //Устанавливаем скорость
        setMaxSpeed(5);
        //Устанавливаем уровень критических атак корабля
        setCriticalAttackLevel(CRITICAL_ATTACK_LEVEL);
        //Устанавливаем уровень способности корабля уклоняться от атак
        setEvasionLevel(EVASION_LEVEL);
    }

    public int getBurningMissileQuantity() {
        return burningMissileQuantity;
    }

    public void setBurningMissileQuantity(int burningMissileQuantity) {
        this.burningMissileQuantity = burningMissileQuantity;
    }

    public void increaseBurningMissileQuantity(){
        burningMissileQuantity++;
    }

    public void decreaseBurningMissileQuantity(){
        burningMissileQuantity--;
    }

    public int getBurningDamage() {
        return burningDamage;
    }

    public void setBurningDamage(int burningDamage) {
        this.burningDamage = burningDamage;
    }

    public void decreaseBurningDamage(){
        burningDamage -= 2;
        if (burningDamage < 0) burningDamage = 0;
    }
    /** Горит ли корабль? */
    public boolean isBurned(){
        if (burningDamage > 0)
            return true;
        else
            return false;
    }

    public int getInvisibleLevel() {
        return invisibleLevel;
    }

    public void setInvisibleLevel(int invisibleLevel) {
        this.invisibleLevel = invisibleLevel;
    }

    public void increaseInvisibleLevel(){
        invisibleLevel++;
    }

    public int getInvisibleTime() {
        return invisibleTime;
    }

    public void setInvisibleTime(int invisibleTime) {
        this.invisibleTime = invisibleTime;
    }
    
    public int increaseInvisibleTime() {
        return invisibleTime += invisibleLevel*2;
    }

    public boolean isVisible(){
        if (invisibleTime > 0)
            return false;
        else
            return true;
    }
    
    public int decreaseInvisibleTime() {
        return invisibleTime--;
    }

    /** Может ли корабль стать активировать режим невидимости */
    public boolean canHide(){
        if(invisibleLevel > 0 && upgradePoints > 0)
            return true;
        else
            return false;
    }

    public boolean canIncreaseInvisLevel(){
        if (invisibleLevel < INVISIBLE_LEVEL_MAX)
            return true;
        else
            return false;
    }

    /** Может ли корабль увеличить свою ширину */
    public boolean canIncreaseWidth(){
        if(getWidth() < SHIP_WIDTH_MAX-1)
            return true;
        else
            return false;
    }
    /** Может ли корабль уменьшить свою ширину */
    public boolean canDecreaseWidth(){
        if(getWidth() > SHIP_WIDTH_MIN+1)
            return true;
        else
            return false;
    }
    /** Может ли корабль увеличить свою высоту */
    public boolean canIncreaseHeight(){
        if(getHeight() < SHIP_HEIGTH_MAX-1)
            return true;
        else
            return false;
    }
    /** Может ли корабль уменьшить свою высоту */
    public boolean canDecreaseHeight(){
        if(getHeight() > SHIP_HEIGTH_MIN+1)
            return true;
        else
            return false;
    }
    /** Может ли корабль увеличить броню */
    public boolean canIncreaseArmor(){
        if (getArmor() < ARMOR_MAX)
            return true;
        else
            return false;
    }
    /** Может ли корабль увеличить регенерацию */
    public boolean canIncreaseRegen(){
        if (getRegenHpLevel() < REGEN_HP_LEVEL_MAX)
            return true;
        else
            return false;
    }
    /** Может ли корабль увеличить критическую атаку */
    public boolean canIncreaseCriticalAttack(){
        if (getCriticalAttackLevel() < CRITICAL_ATTACK_LEVEL_MAX)
            return true;
        else
            return false;
    }
    /** Может ли корабль увеличить свою способность уклоняться от ракет */
    public boolean canIncreaseEvasion(){
        if (getEvasionLevel() < EVASION_LEVEL_MAX)
            return true;
        else
            return false;
    }
    

    public int getEvasionLevel() {
        return evasionLevel;
    }

    public void setEvasionLevel(int evasionLevel) {
        this.evasionLevel = evasionLevel;
    }

    public void increaseEvasionLevel(){
        evasionLevel++;
    }

    /** Показывает, смог ли корабль увернуться */
    public boolean hasEvasion(){
        if (evasionLevel == 0) return false;
        if (getRanInt(5 - evasionLevel) == 0)
            return true;
        else
            return false;
    }

    public int getStunningMissileQuantity() {
        return stunningMissileQuantity;
    }

    public void setStunningMissileQuantity(int stunningMissileQuantity) {
        this.stunningMissileQuantity = stunningMissileQuantity;
    }

    public void increaseStunningMissileQuantity(){
        stunningMissileQuantity++;
    }

    /** Уменьшает кол-во парализующих ракет на 1 */
    public void decreaseStunningMissileQuantity(){
        stunningMissileQuantity--;
    }

    public int getCriticalAttackLevel() {
        return criticalAttackLevel;
    }

    public void setCriticalAttackLevel(int criticalAttackLevel) {
        this.criticalAttackLevel = criticalAttackLevel;
    }

    /** Увеличивает уровень критических атак корабля на 1 */
    public void increaseCriticalAttackLevel(){
        criticalAttackLevel ++;
    }

    public int getStunTime() {
        return stunTime;
    }
    /** Показывает обездвижен ли корабль */
    public boolean isStunned(){
        if (stunTime > 0)
            return true;
        else
            return false;
    }

    public void setStunTime(int stunTime) {
        this.stunTime = stunTime;
    }

    public void increaseStunTime(){
        stunTime += STUN_TIME;
    }

    public void decreaseStunTime(){
        stunTime--;
    }

    /** Регерация корабля на regenHp очков */
    public void regenerateHp(){
        setHp(getHp() + regenHpLevel * 4);
    }
    
    /**
     * Уменьшает здоровье корабля на указанное кол-во hp
     * @param hp
     */
    public void unregenerateHp(int hp){
        setHp(getHp() - (getRegenHpLevel() * 4) - hp);
    }

    /** Уменьшает здоровье корабля на 2 hp */
    public void unregenerateHp(){
        unregenerateHp(2);
    }

    /** Увеличивает число восстанавливаемых hp в начале каждого дейсвия корабля на 2 */
    public void increaseRegenHpLevel(){
        regenHpLevel ++;
    }

    public int getRegenHpLevel() {
        return regenHpLevel;
    }

    public void setRegenHpLevel(int regenHpLevel) {
        this.regenHpLevel = regenHpLevel;
    }

    public int getMissileQuantity() {
        return missileQuantity;
    }

    public void setMissileQuantity(int missileQuantity) {
        this.missileQuantity = missileQuantity;
    }

    /** Максимальное количество ракет на борту корабля (зависит от его размеров) */
    public int getMaxMissileQuantity(){
        return getMaxHp() / 10;
    }

    public void increaseMissileQuantity(){
        missileQuantity += MISSILE_QUANTITY;
        //Следим за тем, чтобы не перебрать ракет =)
        if (missileQuantity > getMaxMissileQuantity())
            missileQuantity = getMaxMissileQuantity();
    }
    
    /** Уменьшает кол-во ракет на 1 */
    public void decreaseMissileQuantity(){
        missileQuantity--;
    }

    public int getUpgradePoints() {
        return upgradePoints;
    }

    public void setUpgradePoints(int upgradePoints) {
        this.upgradePoints = upgradePoints;
    }
    /** Увеличивает кол-во возможных апгрейдов на 1 */
    public void increaseUpgradePoints(){
        upgradePoints++;
    }
    
    /** Увеличивает кол-во возможных апгрейдов на указанное кол-во */
    public void increaseUpgradePoints(int points){
        upgradePoints += points;
    }
    
    /** Уменьшает кол-во возможных апгрейдов на 1 */
    public void decreaseUpgradePoints(){
        upgradePoints--;
    }
    
    /** Уменьшает кол-во возможных апгрейдов на указанное кол-во */
    public void decreaseUpgradePoints(int points){
        upgradePoints -= points;
    }

    public int getMissileIdx() {
        return missileIdx;
    }

    /** Увеличивает индекс выпущенных ракет на 1 */
    public void increaseMissileIdx(){
        this.missileIdx++;
    }
    
    public void increaseWidth(){
        int width = getWidth();
        width += 2; //увеличиваем на 2 (для сохранения симметрии)
        setWidth(width);
    }

    public void decreaseWidth(){
        int width = getWidth();
        width -= 2; //уменьшаем на 2 (для сохранения симметрии)
        setWidth(width);
    }

    public void increaseHeight(){
        int height = getHeight();
        height += 2; //увеличиваем на 2 (для сохранения симметрии)
        setHeight(height);
    }

    public void decreaseHeight(){
        int height = getHeight();
        height -= 2; //уменьшаем на 2 (для сохранения симметрии)
        setHeight(height);
    }

    /** Применяется при увеличении кораблем своего размера (прирост hp: 20%) */
    public void increaseHp(){
        int hp = getHp();
        hp += hp * 20 / 100;//Добавляем 20%
        setHp(hp);
    }
    /** Применяется при уменьшении кораблем своего размера (убыль hp: 20%) */
    public void decreaseHp(){
        int hp = getHp();
        hp -= hp * 20 / 100;//Снимаем 20%
        setHp(hp);
    }

    public int getHeavyMissileQuantity() {
        return heavyMissileQuantity;
    }

    public void setHeavyMissileQuantity(int heavyMissileQuantity) {
        this.heavyMissileQuantity = heavyMissileQuantity;
    }
    
    public void increaseHeavyMissileQuantity(){
        heavyMissileQuantity++;
    }
    
    public void decreaseHeavyMissileQuantity(){
        heavyMissileQuantity--;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /** Координата по оси x середины ширины корабля  */
    public int getMiddleX(){
        return getPosX() + getWidth() / 2;
    }

    /** Координата по оси y середины высоты корабля  */
    public int getMiddleY(){
        return getPosY() + getHeight() / 2;
    }

    public void move(int moveX, int moveY) {
        int posX = getPosX();
        int posY = getPosY();
        //Собственно перемещение (смена координаты)
        posX += moveX;
        posY += moveY;

        //Находим середину длины корабля (нельзя смещать дальше неё)
        int middleX = getMiddleX() + moveX;
        //Находим середину высоты корабля (нельзя смещать дальше неё)
        int middleY = getMiddleY() + moveY;
        //Если новая координата по х не смещает корабль дальше середины корабля
        if (middleX > 0 && middleX < FIELD_WIDTH) setPosX(posX);
        //Если новая координата по н не смещает корабль дальше середины корабля
        if (middleY > 0 && middleY < FIELD_HEIGHT) setPosY(posY);
    }



}

