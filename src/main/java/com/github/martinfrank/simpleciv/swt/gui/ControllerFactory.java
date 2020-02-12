package com.github.martinfrank.simpleciv.swt.gui;

import com.github.martinfrank.simpleciv.game.Game;
import javafx.util.Callback;

public class ControllerFactory implements Callback<Class<?>, Object> {

    private final Game game;

    private RootController rootController;

    public ControllerFactory(Game game) {
        this.game = game;
    }

    @Override
    public Object call(Class<?> type) {
        if (type == RootController.class) {
            rootController = new RootController(game);
            return rootController;
        } else {
            // default behavior for controllerFactory:
            try {
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception exc) {
                exc.printStackTrace();
                throw new RuntimeException(exc); // fatal, just bail...
            }
        }
    }

    public RootController getRootController() {
        return rootController;
    }


}
