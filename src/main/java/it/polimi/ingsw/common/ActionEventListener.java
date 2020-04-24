package it.polimi.ingsw.common;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;

import java.util.List;

public interface ActionEventListener {

    ActionEventResponse onSelectGods(Player player, List<God> gods);

    ActionEventResponse onChooseGod(Player player, God god);

    ActionEventResponse onSpawnWorker(Player player, Worker worker);

    ActionEventResponse onMoveWorker(Player player, Worker worker, Cell destination);

    ActionEventResponse onBuildBlock(Player player, Worker worker, Cell destination);

    ActionEventResponse onBuildDome(Player player, Worker worker, Cell destination);

    ActionEventResponse onForceWorker(Player player, Worker worker, Worker target, Cell destination);

    ActionEventResponse onEndTurn(Player player);

}
