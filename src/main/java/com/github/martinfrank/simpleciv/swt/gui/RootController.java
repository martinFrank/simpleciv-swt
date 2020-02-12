package com.github.martinfrank.simpleciv.swt.gui;

import com.github.martinfrank.simpleciv.game.Game;
import com.github.martinfrank.simpleciv.gui.GuiEventListener;
import com.github.martinfrank.simpleciv.gui.MouseSelection;
import com.github.martinfrank.simpleciv.map.CivMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RootController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootController.class);
    private GuiEventListener eventListener;
    private final Game civGame;

    @FXML
    private CivMapCanvas mapCanvas;

    @FXML
    private TextArea console;


    public RootController(Game civGame) {
        this.civGame = civGame;
        setGuiEventListener(civGame);
    }


    public void init() {
        LOGGER.debug("mapCanvas: {}", mapCanvas);
        mapCanvas.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            int x = (int) mouseEvent.getX();
            int y = (int) mouseEvent.getY();
            MouseSelection selection = mapCanvas.getSelectionAt(x, y);
            eventListener.mouseSelect(selection);
        });
        setMap(civGame.getMap());
    }

    public void setMap(CivMap map) {
        mapCanvas.setMap(map);
        redrawMap();
    }

    public void setGuiEventListener(GuiEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void redrawMap() {
        mapCanvas.drawMap();
    }

    public void clearConsole() {
        console.clear();
    }

    public void endTurnButton(ActionEvent actionEvent) {
        civGame.endTurn();
        redrawMap();
    }
}
