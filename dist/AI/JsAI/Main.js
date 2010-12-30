importPackage(Packages.shipcraft.utils);
importPackage(Packages.shipcraft.model);
importPackage(Packages.shipcraft.core);
importPackage(Packages.shipcraft.ai.ship);

v = new Packages.shipcraft.intrfc.ShipAI() {
    getShipName: function(){
        return "JsAI"
    },
    getAction: function(corpusId, field, isTeamMatch) {
        return new Action("move");
    },
    
    getTeamName: function(){
        return "JsAITeam"
    }
}

//var t = JavaAdapter(Packages.shipcraft.intrfc.ShipAI, v);
//println(t);