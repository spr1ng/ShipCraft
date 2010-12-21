from shipcraft.ai.ship import DefaultShipAI
from shipcraft.model import Action

class PythonAI(DefaultShipAI):
    def __init__(self):
        pass

    def getUpgrade(myShipId):
        return Action(u'width++')

# Должен возвращать имя корабля
    def getShipName():
        return u'PythonShip'

    # Должен возвращать название команды корабля
    def getTeamName():
        return u'PythonShipTeam'

pythonAi = PythonAI()
print pythonAi
    
