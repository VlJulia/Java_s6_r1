package org.example.javafx_example;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class ArrowView extends Thread{
    public double MX;
    public double MY;
    public double cY = 0;
    public double cX = 0;
    protected runCircle circles[]=null;
    Pane arrow;
    Polygon plg;
    protected boolean isFlying = true;
    protected boolean fpause;
    public int speed =20;
    public int def_speed =10;
    public double pover = 1.25;//1.5 - good , 5 -bad
    ArrowView(double x, double y, Color c){
        setDaemon(true);
        createArrow(c);
        MX=x;
        MY=y;
        arrow.setLayoutY(-100);
        arrow.setLayoutX(-100);
        cY = arrow.getLayoutY();
        cX = arrow.getLayoutX();
    }
    public void setCircles(runCircle circles_[]){
        circles=circles_;
    }
    @Override
    public void run(){
        fpause = false;
        while (true)
        {
            try {
                if(fpause || (isFlying==false))
                {
                    synchronized (this)
                    {
                        this.wait();
                    }
                    fpause = false;
                }
                sleep(15);
                Platform.runLater(this::next);
            } catch (InterruptedException e) {
                isFlying =false;
                Throwable cause = e.getCause();
                if (cause != null) {
                    cause.printStackTrace(); // Печатает стек вызовов, если cause не null
                } else {

                    e.printStackTrace(); // Печатает стек вызовов для InvocationTargetException
                }

            }
        }

    }
    public void setFlying(boolean flying) {
        isFlying = flying;
    }
    public boolean getFlying() {
        return isFlying;
    }
    public void setPause(boolean p) {
        fpause = p;
    }
    boolean checkCollision(){
        for (runCircle a: circles){
            if (arrow.getBoundsInParent().intersects(a.circle.getBoundsInParent())) {
                //System.out.println(arrow.getChildren().get(1).isPickOnBounds(a.circle.getBoundsInParent()));
                a.plusCoin();
                return true;
            }
        }
        return false;
    }
    void next(){
        double x = arrow.getLayoutX();
        double y = arrow.getLayoutY();
        x += speed;
        double nY= Math.pow(pover, (x - cX)/50 - pover*2 );
        y = nY+cY;
        double rot =Math.min(Math.atan((y-arrow.getLayoutY())/(x-arrow.getLayoutX()))*90, 90);
        arrow.setLayoutX(x);
        arrow.setLayoutY(y);
        arrow.setRotate(rot);
        boolean b = checkCollision();
        if (b||(x>= MX)||(y>=MY)) {
            arrow.setLayoutX(-200);
            arrow.setRotate(-200);
            isFlying =false;
        };
    }
    private void createArrow(Color col){
        Pane pane = new Pane();
        Rectangle base = new Rectangle(100, 3 );
        base.setFill(col);
        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(
                100.0, -7.0,   // Левый верхний
                120.0, 1.5,  // Кончик стрелки
                100.0, 10.0   // Правый верхний
        );
        arrowHead.setFill(col);
        pane.getChildren().addAll(base, arrowHead);
        pane.setPrefSize(120, 20);
        arrow = pane;
    }

    public Pane getArrow() {
        return arrow;
    }

    public void setDefSpeed(){speed=def_speed;}
}
