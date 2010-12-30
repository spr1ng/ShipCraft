importPackage(Packages.shipcraft.utils);
importPackage(Packages.shipcraft.model);
importPackage(Packages.shipcraft.core);
importPackage(Packages.shipcraft.ai.ship);

v = new Packages.shipcraft.intrfc.ShipAI() {
    getShipName: function(){
        return "JsAI"
    },
    getAction: function(myShipId, field, isTeamMatch) {
        return new Action("missile", 1,-1);
    },
    
    getTeamName: function(){
        return "JsAITeam"
    }
}

//var t = JavaAdapter(Packages.shipcraft.intrfc.ShipAI, v);