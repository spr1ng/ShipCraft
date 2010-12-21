from shipcraft.ai.ship import DefaultShipAI
from shipcraft.model import Action

class PythonAI(DefaultShipAI):

    def getUpgrade(myShipId):
        return Action("width++")

# Должен возвращать имя корабля
    def getShipName():
        return "PythonShip"

    # Должен возвращать название команды корабля
    def getTeamName():
        return "PythonShipTeam"

    
pythonAi = PythonAI()
#print pythonAi
    
