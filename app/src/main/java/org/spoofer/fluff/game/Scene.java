package org.spoofer.fluff.game;

import android.graphics.Rect;
import android.support.annotation.IdRes;
import android.view.ViewGroup;

import java.util.Set;

/**
 * The Scene defines the current playing level with all the Bots of Actors and Scenery for that level.
 */
public interface Scene {


    /**
     * Initiates the Bots from the sceneView.
     */
    //void setSceneView(ViewGroup sceneView);

    /**
     * Gets the parent {@link ViewGroup} containing this scene.
     * The main container View.
     *
     * @return
     */
    ViewGroup getSceneView();

    /**
     * Gets all the {@link Bot}s in this scene.
     *
     * @return
     */
    Set<Bot> getBots();


    /**
     * Gets the Scenery botclasses in this scene.
     * Scenery are the static Bots.
     *
     * @return
     * @See #getBots
     */
    Set<Scenery> getScenery();

    /**
     * Finds a Bot in this scene with the given view Id.
     *
     * @param id the view id
     * @return the Bot with the id or null if no bot with given ID is in this scene.
     */
    Bot findBot(@IdRes int id);

    /**
     * Finds the scenery bot in the given searchArea.
     * Specifically, finds the Scenery Bot that intersects with the location of the
     * given area.  If more than one bot interects with the area, the Bot closest to the center
     * of the search area is returned.
     *
     * @param searchArea
     * @return The Scenery Bot closest to the center of the given searchArea or null if
     * no Scenery if found within the givne area.
     */
    Scenery findScenery(Rect searchArea);

}
