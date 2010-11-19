$:.unshift(File.dirname(__FILE__))
require 'java'

import 'shipcraft.core.Constants'
import 'shipcraft.utils.Utils'
import 'shipcraft.model.Action'
import 'shipcraft.ai.ship.AttackerShipAI'


class RubyAI < AttackerShipAI
 
  def initialize()
    @move = 0;
    @attack = 0;
    @upgrade = 0;
    @mem_hp = 0;
  end
  
  def getUpgrade()
    my_ship = getMyShip;
    @move = 0
    @attack = 0
    @upgrade = 1
    #Если есть возможность для апгрейда.
    if my_ship.getUpgradePoints > 0
      #Если маловато ракет..
      if my_ship.getMissileQuantity < 10
        p "AI_UPGRADE_MISSLE"
        return Action.new("missile++");
      else
        p "AI_UPGRADE"
        if my_ship.canIncreaseRegen
          return Action.new("regen++");
        end
        if my_ship.canIncreaseArmor
          return Action.new("armor++");
        end
        p "DEFAULT_UPGRADE"
        return super
      end
    end
    super
  end

  def getAction(my_ship_id, field, is_team_match)
    initAI(my_ship_id, field, is_team_match)
    my_ship = getMyShip
    my_ship.regenerateHp();
    if hp_min?
       if @upgrade == 0
         return getUpgrade
       end
      return secured_attack_action;
    end
    if @move == 0
      move_action, x_a, y_a = getWiseRubyMoveAction(my_ship)
      setAction(move_action);
      if is_team_match && !isAllyDamaged(move_action) || !isMyShipDamaged
        return move_action;
      else
        if @attack == 0
          if my_ship.getMissileQuantity > 0 
            return get_ruby_attack_action(x_a, y_a);
          end
        end
      end
    end
    return secured_attack_action;
  end

  # Стреляем в ближайшего.
  def get_ruby_attack_action(x_a, y_a)
    p "AI_Ruby_ATTACK"
    @attack = 1
    @move = 0
    @upgrade = 0
    Action.new("missile", x_a, y_a);
  end

  def hp_min?
    getMyShip.getHp < @mem_hp;
  end

  def secured_attack_action
    action = getAttackAction
    if action != nil
      setAction(action);
      if !isAllyDamaged(action)
        @attack = 1;
        @move   = 0;
        @upgrade = 0;
        @mem_hp = getMyShip.getHp
        p "AI_Ruby_SECURED_ATTACK"
        return action;
      end
    end
    return getWiseRubyMoveAction(getMyShip)[0];
  end

  def getWiseRubyMoveAction(my_ship)
    ships = getEnemies
    @mem_hp = getMyShip.getHp
    #Если все погибли - стоять и наслаждаться победой! ;)
    if ships == nil
      return Action.new("move")
    elsif ships.size == 0
      return Action.new("move")
    end
    
    d_hash = Hash.new()
    my_middle_x = my_ship.getMiddleX
    my_middle_y = my_ship.getMiddleY
    for enemy in ships
      #if enemy.getTeamName != my_ship.getTeamName
        enemy_middle_x = enemy.getMiddleX
        enemy_middle_y = enemy.getMiddleY
        enemy_hp = enemy.getHp
        d = d(enemy_middle_x, enemy_middle_y, my_middle_x, my_middle_y)
        d_hash[enemy.getId] = [d, enemy_hp, enemy_middle_x, enemy_middle_y];
      #end
    end

    max_data = d_hash.max_by{|key, value| value};
    min_data = d_hash.min_by{|key, value| value};

    enemy_hp    = min_data[1][1];
    d_min       = min_data[1][0];
    d_max       = max_data[1][0];
    enemy_min_x = min_data[1][2];
    enemy_min_y = min_data[1][3];

    x_move = 0;
    y_move = 0;
    x_missle = 0;
    y_missle = 0;

    attantion_coefficient = (d_max/d_min).round;
    speed = my_ship.getMaxSpeed;
    
    if enemy_hp < my_ship.getHp
      speed = my_ship.getMaxSpeed/attantion_coefficient;
      #Если враг правее и близко.
      if enemy_min_x >= my_middle_x
        x_move = Constants::MOVE_RIGHT#идем вправо
        x_missle = x_move;
        #Если враг левее и близко.
      else
        x_move = Constants::MOVE_LEFT#идем влево
        x_missle =  x_move;
      end

      #Если враг ниже и близко.
      if enemy_min_y >= my_middle_y
        y_move = Constants::MOVE_DOWN#идем вниз
        y_missle = y_move;
        #Если враг выше и близко.
      else
        y_move = Constants::MOVE_UP#идем вверх
        y_missle = y_move;
      end
    else
      #Если враг правее и близко и hp у него больше чем у нас.
      if enemy_min_x >= my_middle_x
        x_move = Constants::MOVE_LEFT#уходим влево.
        x_missle = Constants::MOVE_RIGHT | Constants::MOVE_LEFT;
        #Если враг левее и близко и hp у него больше чем у нас.
      else
        x_move = Constants::MOVE_RIGHT#уходим вправо.
        x_missle = Constants::MOVE_LEFT | Constants::MOVE_RIGHT;
      end

      #Если враг ниже и близко и hp у него больше чем у нас.
      if enemy_min_y >= my_middle_y
        y_move = Constants::MOVE_UP#уходим вверх.
        y_missle = Constants::MOVE_DOWN | Constants::MOVE_UP;
        #Если враг выше и близко и hp у него больше чем у нас.
      else
        y_move = Constants::MOVE_DOWN#уходим вниз.
        y_missle = Constants::MOVE_UP | Constants::MOVE_DOWN;
      end
    end
    @attack = 0;
    @move = 1;
    @upgrade = 0;
    p max_data;
    p min_data;
    p "AI_MOVE (RubyWiseMove)"
    p "####"
    p "Speed = "+speed.to_s;
    p "####"
    action = Action.new("move", speed, x_move, y_move) == nil ? Action.new("move") : Action.new("move", speed, x_move, y_move)
    [action, x_missle, y_missle]
  end

  def d(enemy_middle_x, enemy_middle_y, my_middle_x, my_middle_y)
    d = Math.sqrt((my_middle_x - enemy_middle_x)**2 + (my_middle_y - enemy_middle_y)**2);
    d
  end

  #Должен возвращать имя корабля
  def getShipName()
    "RubyShip"
  end

  #Должен возвращать название команды корабля
  def getTeamName()
    "RubyShipTeam"
  end
  
end

@ruby_ai = RubyAI.new()