importPackage(Packages.shipcraft.utils);
importPackage(Packages.shipcraft.model);
importPackage(Packages.shipcraft.core);
importPackage(Packages.shipcraft.ai.ship);
importPackage(Packages.shipcraft.intrfc);

//function JsAI(){
//    getAction: function(shiId, field){
//        return Constants.MU;
//    }
//}
//
//JsAI.prototype = new DefaultShipAI();
//
//var jsAi = new JsAI();
//
////println(jsAi);

var v = {
    getAction: function(shiId, field, isTeamMatch) {
        return Constants.MU;
    },

    getShipName: function() {
        return "JsShip";
    },
    
    getTeamName: function() {
        return "JsShipTeam";
    }
}


var jsAI = new JavaAdapter(DefaultShipAI, v);