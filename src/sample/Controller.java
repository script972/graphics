package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.cos;

public class Controller implements Initializable {
    double minX=-10;
    double maxX=10;
    double minY=-10;
    double maxY=10;

    @FXML
    private Pane centerPane;
    @FXML
    private Canvas canvas;
    @FXML
    private TextField min1X;
    @FXML
    private TextField max1X;
    @FXML
    private TextField min1Y;
    @FXML
    private TextField max1Y;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvas.widthProperty().bind(centerPane.widthProperty());
        canvas.heightProperty().bind(centerPane.heightProperty());
        canvas.widthProperty().addListener(e->draw());
        canvas.heightProperty().addListener(e->draw());
    }






    public void draw() {
        if(!min1X.getText().isEmpty()) {
            this.minX = Double.parseDouble(min1X.getText());
            this.maxX = Double.parseDouble(max1X.getText());
            this.minY = Double.parseDouble(min1Y.getText());
            this.maxY = Double.parseDouble(max1Y.getText());
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.AQUA);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());



        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(3);
        gc.strokeLine(toScreenX(minX), toScreenY(0), toScreenX(maxX), toScreenY(0));
        if (toScreenX(0)<0)
        gc.strokeLine(Math.abs(toScreenX(0))/15,toScreenY(maxY),Math.abs(toScreenX(0))/15,toScreenY(minY));
        else
        if (toScreenX(0)>canvas.getWidth())
            gc.strokeLine(canvas.getWidth()-Math.abs(toScreenX(0))/15,toScreenY(maxY),canvas.getWidth()-Math.abs(toScreenX(0))/15,toScreenY(minY));
            else
            gc.strokeLine(toScreenX(0),toScreenY(maxY),toScreenX(0),toScreenY(minY));
        List<Point> points = tabulation();
        gc.beginPath();
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.moveTo(toScreenX(points.get(0).getX()), toScreenY(points.get(0).getY()));
        for (int i=1; i<points.size(); i++) {
            gc.lineTo(toScreenX(points.get(i).getX()), toScreenY(points.get(i).getY()));
        }
       // System.out.println(toScreenX(0));




        gc.stroke();
    }

    private List<Point> tabulation() {
        return IntStream
                .range(0, (int) canvas.getWidth())
                .mapToDouble(this::toWorldX)
                .mapToObj(x->new Point(x, f(x)))
                .collect(Collectors.toList());
    }




    private int toScreenX(double x) {
        return (int) (canvas.getWidth() * (x - minX) / (maxX - minX));
    }

    private int toScreenY(double y) {
        return (int) (canvas.getHeight() * (1 - (y - minY) / (maxY - minY)));
    }

    private double toWorldX(int xs) {
        return 1.0 * xs / canvas.getWidth() * (maxX - minX) + minX;
    }

    private double toWorldY(int ys) {
        return (1.0 * ys - canvas.getHeight()) / (-canvas.getHeight()) * (maxY - minY) + minY;
    }

    private double f(double x) {
        return cos(x+0.1)/(x*x+1);
    }
}
