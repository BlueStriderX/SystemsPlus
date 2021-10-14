package thederpgamer.systemsplus.gui;

import org.schema.schine.graphicsengine.forms.gui.GUICallback;
import org.schema.schine.graphicsengine.forms.gui.GUIElement;
import org.schema.schine.graphicsengine.forms.gui.GUIOverlay;
import org.schema.schine.graphicsengine.forms.gui.GUIScrollablePanel;
import org.schema.schine.graphicsengine.forms.gui.newgui.FilterController;
import org.schema.schine.graphicsengine.forms.gui.newgui.GUIInnerTextbox;
import org.schema.schine.input.InputState;
import thederpgamer.systemsplus.utils.ResourceManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observer;

/**
 * Working replacement for ScrollableTilePane.
 *
 * @author TheDerpGamer
 * @since 07/09/2021
 */
public abstract class GUIScrollableTilePane<E> extends GUIElement implements Observer {

    private float tileWidth;
    private float tileHeight;
    private float spacing;
    private boolean sorted;

    private GUIElement dependent;
    private final FilterController<E> filterController;
    private GUIScrollablePanel scrollPanel;
    private ArrayList<GUIScrollableTile<E>> tiles;

    public GUIScrollableTilePane(InputState state, GUIElement dependent, float tileWidth, float tileHeight, float spacing, boolean sorted) {
        super(state);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.spacing = spacing;
        this.sorted = sorted;
        this.dependent = dependent;
        this.filterController = new FilterController<>(this);
        this.scrollPanel = new GUIScrollablePanel(100f, 100f, dependent, state) {
            @Override
            public boolean isActive() {
                return GUIScrollableTilePane.this.isActive();
            }
        };
        this.tiles = new ArrayList<>();
    }

    public void sortTiles() {
        Collections.sort(tiles, new Comparator<GUIScrollableTile<E>>() {
            @Override
            public int compare(GUIScrollableTile<E> o1, GUIScrollableTile<E> o2) {
                return ResourceManager.getName(o1.overlay.getSprite()).compareTo(ResourceManager.getName(o2.overlay.getSprite()));
            }
        });
    }

    public GUIScrollableTile<E> addTile(GUIOverlay overlay, GUICallback callback) {
        GUIScrollableTile<E> tile = new GUIScrollableTile<>(getState(), overlay, callback);
        tiles.add(tile);
        if(sorted) sortTiles();
        return tile;
    }

    public static class GUIScrollableTile<E> extends GUIInnerTextbox {

        private GUIOverlay overlay;

        public GUIScrollableTile(InputState state, GUIOverlay overlay, GUICallback callback) {
            super(state);
            this.overlay = overlay;
            this.overlay.setCallback(callback);
        }

        public GUIOverlay getOverlay() {
            return overlay;
        }
    }
}