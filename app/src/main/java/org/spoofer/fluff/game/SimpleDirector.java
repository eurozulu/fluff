package org.spoofer.fluff.game;

import android.animation.Animator;
import android.util.Log;

import org.spoofer.fluff.Script;
import org.spoofer.fluff.TestScript;
import org.spoofer.fluff.game.actions.Action;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimpleDirector implements Director {

    private static final String LOGTAG = SimpleDirector.class.getName();

    private final Map<Bot, Animator> animators = new HashMap<>();
    private final Map<Integer, Bot> botIndex = new HashMap<>();

    private Scene currentScene;

    private Script script;
    

    @Override
    public void startScene(Scene scene) {
        endScene();
        currentScene = scene;
        indexBotIDs(scene.getBots());
        script = loadScript(scene);
    }

    @Override
    public void endScene() {
        stopAll();
        currentScene = null;
        botIndex.clear();
    }

    @Override
    public void startAction(int botId, String command) {
        stopAction(botId);

        Bot bot = botIndex.get(botId);
        if (null == bot) {
            Log.d(LOGTAG, String.format("ignoring command '%s' for bot id: %d as not in this scene", command, botId));
            return;
        }
        
        Action action = script.getAction(bot, command);
        if (null == action) {
            Log.d(LOGTAG, String.format("ignoring command '%s' for bot id: %d as no action found in the script", command, botId));
            return;
        }
        
        Animator animator = action.performAction(bot);
        if (null == animator) {
            Log.d(LOGTAG, String.format(
                    "ignoring command '%s' for bot id: %d as action %s did not perform an animation",
                    command, botId, action.getClass().getSimpleName()));
            return;
        }

        animators.put(bot, animator);
        animator.start();
    }

    @Override
    public void stopAction(int botId) {
        Bot bot = botIndex.get(botId);
        if (null == bot) {
            Log.d(LOGTAG, String.format("ignoring stop action as bot id %s not known in this scene", botId));
            return;
        }
        
        Animator animator = animators.remove(bot);
        if (null != animator)
            animator.cancel();
    }

    
    @Override
    public void stopAll() {
        for (Animator animator : animators.values())
            animator.cancel();
        animators.clear();
    }

    /**
     * Populate the bot index with all botclasses with a valid ID
     * @param bots
     */
    private void indexBotIDs(Set<Bot> bots) {
        botIndex.clear();
        for (Bot bot : bots) {
            if (bot.getView().getId() != 0)
                botIndex.put(bot.getView().getId(), bot);
        }
    }

    private Script loadScript(Scene scene) {
        return new TestScript(scene.getBots());
    }
}