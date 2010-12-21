local luaAI = luajava.createProxy("shipcraft.intrfc.ShipAI",
{
    getAction = function(corpusId, field, isTeamMatch)
        return luajava.newInstance("shipcraft.model.Action","move");
    end,
    
    getShipName = function()
        return "LuaShip";
    end,

    getTeamName = function()
        return "LuaAI";
    end
});