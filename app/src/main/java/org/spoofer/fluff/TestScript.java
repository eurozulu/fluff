package org.spoofer.fluff;

import android.util.Log;

import org.spoofer.fluff.game.Bot;
import org.spoofer.fluff.game.actions.Action;
import org.spoofer.fluff.game.actions.DigAction;
import org.spoofer.fluff.game.actions.FillAction;
import org.spoofer.fluff.game.actions.MoveAction;
import org.spoofer.fluff.game.misc.Movement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestScript implements Script {

    private static final String LOGTAG = TestScript.class.getName();


    private final Map<Bot, Map<String, Action>> botActions = new HashMap<>();


    public TestScript(Set<Bot> bots) {
        loadTestScript(bots);
    }

    @Override
    public Action getAction(Bot bot, String command) {

        Map<String, Action> actionMap = botActions.get(bot);
        if (null == actionMap) {
            Log.d(LOGTAG, String.format("Ignoring script command %s as bot %s is not in this script", command, bot));
            return null;
        }
        Action action = actionMap.get(command);
        if (null == action) {
            Log.d(LOGTAG, String.format("Ignoring script command %s it is not a known action for bot %s", command, bot));
            return null;
        }

        return action;
    }

    public void loadTestScript(Set<Bot> bots) {
        botActions.clear();
        for (Bot bot : bots) {
            Map<String, Action> actions = makeMovementActions();
            if (bot.getView().getId() == R.id.bot_player) {
                actions.put(DigAction.ACTION_DIG, new DigAction());
                actions.put(FillAction.ACTION_FILL, new FillAction());
            }
        }

    }

    private Map<String, Action> makeMovementActions() {
        Map<String, Action> actions = new HashMap<>();
        actions.put(Movement.Direction.Left.toString(), new MoveAction(Movement.Direction.Left));
        actions.put(Movement.Direction.Right.toString(), new MoveAction(Movement.Direction.Right));
        actions.put(Movement.Direction.Up.toString(), new MoveAction(Movement.Direction.Up));
        actions.put(Movement.Direction.Down.toString(), new MoveAction(Movement.Direction.Down));

        return actions;
    }


    private void blabla() {
        private void loadBots(ViewGroup parent) {
            try {
                BotConfigReader botConfigReader = new BotConfigReader();
                botConfigReader.loadConfig(parent.getResources(), R.xml.botclasses);
                loadBots(sceneView, botConfigReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
