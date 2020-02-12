package com.github.martinfrank.simpleciv.swt.gui;

import com.github.martinfrank.drawlib.Point;
import com.github.martinfrank.drawlib.Shape;
import com.github.martinfrank.simpleciv.game.Player;
import com.github.martinfrank.simpleciv.game.Settlement;
import com.github.martinfrank.simpleciv.gui.MouseSelection;
import com.github.martinfrank.simpleciv.map.CivMap;
import com.github.martinfrank.simpleciv.map.CivMapEdge;
import com.github.martinfrank.simpleciv.map.CivMapField;
import com.github.martinfrank.simpleciv.map.CivMapNode;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CivMapCanvas extends Canvas {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(CivMapCanvas.class);

    private CivMap map;

    public void setMap(CivMap map) {
        this.map = map;
        int h = (int) map.getTransformed().getHeight();
        int w = (int) map.getTransformed().getWidth();
        setHeight(h);
        setWidth(w);
//        Shape aField = map.getFields().get(0).getShape();
//        double fieldHeight = aField.getTransformed().getHeight();
//        setWidth(map.getTransformed().getWidth());
//        if (map.getRows() % 2 == 1) {
//            setHeight(map.getTransformed().getHeight() - fieldHeight * 1.5);
//        } else {
//            setHeight(map.getTransformed().getHeight());
//        }
        drawMap();
    }

    void drawMap() {
        if (map != null) {
            map.getFields().forEach(this::drawField);
        }
    }

    private void drawNode(CivMapNode node) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        Point point = node.getPoint().getTransformed();
        gc.strokeLine(point.getX(), point.getY(), point.getX(), point.getY());
    }

    private void drawEdge(CivMapEdge edge) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);

        Point a = edge.getLine().getTransformed().getA();
        Point b = edge.getLine().getTransformed().getB();
        gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
    }

    private void drawField(CivMapField field) {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.GRAY);
        Player owner = field.getData().getOwner();
        if (owner != null) {
            gc.setFill(fromArgb(owner.getColor()));
        }
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(1);

        Shape shape = field.getShape().getTransformed();
        double[] xs = shape.getPoints().stream().mapToDouble(Point::getX).toArray();
        double[] ys = shape.getPoints().stream().mapToDouble(Point::getY).toArray();
        int amount = xs.length;
        gc.fillPolygon(xs, ys, amount);

        field.getEdges().forEach(this::drawEdge);
        field.getNodes().forEach(this::drawNode);

        Settlement settlement = field.getData().getSettlement();
        if (settlement != null) {
            Point point = shape.getCenter();
            double minW = shape.getPoints().stream().mapToDouble(Point::getX).min().orElse(0);
            double maxW = shape.getPoints().stream().mapToDouble(Point::getX).max().orElse(0);

            double minH = shape.getPoints().stream().mapToDouble(Point::getY).min().orElse(0);
            double maxH = shape.getPoints().stream().mapToDouble(Point::getY).max().orElse(0);
            double w = (maxW - minW) / 4d;
            double h = (maxH - minH) / 4d;
            gc.setFill(Color.BLACK);
            gc.fillOval(minW + w, minH + h, 2d * w, 2d * h);
        }

        if (!field.getData().getUnits().isEmpty()) {
            //FIXME draw unit
            Point point = shape.getCenter();
            double minW = shape.getPoints().stream().mapToDouble(Point::getX).min().orElse(0);
            double maxW = shape.getPoints().stream().mapToDouble(Point::getX).max().orElse(0);

            double minH = shape.getPoints().stream().mapToDouble(Point::getY).min().orElse(0);
            double maxH = shape.getPoints().stream().mapToDouble(Point::getY).max().orElse(0);

            double w = (maxW - minW) / 8d;
            double h = (maxH - minH) / 8d;
            gc.setFill(Color.YELLOW);
            gc.fillOval(minW + w, minH + h, 2d * w, 2d * h);
        }
    }


    public MouseSelection getSelectionAt(int x, int y) {
        MouseSelection selection = new MouseSelection(x, y);
        if (map != null) {
            selection.setNode(map.getNodeAt(x, y));
            selection.setEdge(map.getEdgeAt(x, y));
            selection.setField(map.getFieldAt(x, y));
        }
        return selection;
    }

    private static Color fromArgb(int argb) {
        LOGGER.debug("argb:{}, hex:{}", argb, Integer.toHexString(argb));
        double a = Math.abs((argb & 0xFF000000) / 4278190080d); //FIXME unprecise
        double r = (argb & 0x00FF0000) / 16711680d;
        double g = (argb & 0x0000FF00) / 65280d;
        double b = (argb & 0x000000FF) / 255d;
        LOGGER.debug("a {}", a);
        LOGGER.debug("r {}", r);
        LOGGER.debug("g {}", g);
        LOGGER.debug("b {}", b);
        return new Color(r, g, b, a);
    }

}
