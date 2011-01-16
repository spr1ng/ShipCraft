import static shipcraft.core.Constants.*;
import static shipcraft.utils.Utils.*;
import shipcraft.model.Field;
import shipcraft.model.Ship;
import shipcraft.model.Action;
import shipcraft.ai.ship.DefaultShipAI; 

public class GroovyAI extends DefaultShipAI {
    private boolean move = false;
    private boolean upgrade = false;
    private boolean attack = false;
    private int memHp = 0;
     
    public Action getAction(String myShipId, Field field, boolean isTeamMatch) {
        initAI(myShipId, field, isTeamMatch);
        if (hpMin()){
            if (upgrade) {
                return getUpgrade();
            }
            setState(move: true, attack:false, upgrade: false);
            return getMoveAction();
        }
        if (move) {
            Action moveAction = getMoveAction();
            setAction(moveAction);
            if (isTeamMatch && !isAllyDamaged(moveAction) || !isMyShipDamaged()) {
                setState(move: true, attack:false,  upgrade: false);
                return moveAction;
            } else {
                if (!attack){
                    if (getMyShip().getMissileQuantity() > 0){
                        return securedAttackAction();
                    }
                }
            }
        }
        return securedAttackAction();
    }

    public Action securedAttackAction(){
        Action action = getAttackAction();
        if (!isAllyDamaged(action)){
            setState(move:false, attack:true, upgrade:false);
            memHp = getMyShip().getHp();
            println("AI_Groovy_SECURED_ATTACK");
            return action;
        }
        return getMoveAction();
    }

    boolean hpMin(){
        return getMyShip().getHp() < memHp ? true : false;
    }

    @Override
    Action getUpgrade(){
        setState(move:false, attack:false, upgrade:true);
        int decision = getRanInt(5);
        switch (decision){
            default : return new Action("missile++");
            case 0 : return new Action("critical++");
            case 1 : return new Action("evasion++");
        }
    }

    void setState(Map map){
        this.move = map.move;
        this.attack = map.attack;
        this.upgrade = map.upgrade;
    }
    
    @Override
    public String getShipName(){
        return "GroovyShip"
    }

    @Override
    public String getTeamName(){
        return "GroovyShipTeam"
    }
}

GroovyAI groovyAi = new GroovyAI();
