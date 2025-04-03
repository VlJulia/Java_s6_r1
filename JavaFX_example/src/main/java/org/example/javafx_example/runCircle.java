package org.example.javafx_example;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.Random;


public class runCircle extends  Thread{
    public double MinY;
    public double MaxY;
    public Score score;
    private int coin;
    protected boolean fpause;
    public double speed;
    public int sleepT;
    public  boolean up =true;
    private int numdr;
    private double def_speed;
    private int def_rad;
    private Color color;
    Circle circle;
    runCircle(int y, int x, Score score_, int r, int numbr_){
        setDaemon(true);
        speed= (y/40 - 0.0008*r*r)*1.5;
        if (speed>y/60) speed = y/60;
        if (speed<0) speed = 5;
        def_speed= speed;
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+speed+ " "+ y + "  " + y/40 +" " +  r);
        coin = numbr_ + 1;
        sleepT=10;
        score =score_;
        MinY=r;
        MaxY =y-r;
        numdr=numbr_;
        createCircle(r,x,y);
        def_rad =r;
    }
    @Override
    public void run(){
        fpause = false;
        while (true)
        {
            Platform.runLater(this::next);
            try {
                if(fpause)
                {
                    synchronized (this)
                    {
                        this.wait();
                    }
                    fpause = false;
                }
                sleep(sleepT);
            } catch (InterruptedException e) {
                fpause = true;
            }
        }
    }
    void next(){
        double y = circle.getLayoutY();
        if (up&&(y+speed>=MaxY)) {up=false;}
        if ((!up)&&(y-speed<=-MaxY)) {up=true;}
        if (up) y += speed;
        else y-=speed;
        circle.setLayoutY(y);
    }
    public void plusCoin(){
        score.coinPlus(coin, numdr);
    }
    public void setPause(boolean fpause_){
        fpause = fpause_;
    }
    private void createCircle(int r, double x, double y){
        circle = new Circle();
        circle.setRadius(r);
        circle.setCenterX(x);
        circle.setCenterY(y);
        changeCol();
        circle.setStyle("-fx-border-color: black; -fx-border-width: 5; -fx-padding: 10;");
    }
    public Circle getCircle(){return circle;}
    public void changeCol(){
        Random random = new Random();
        int r=0,g=0,b=0, maxCs = 700, minCs = 300;
        while (((r+g+b)<minCs)||((r+g+b)>maxCs)) {
            r = random.nextInt(255);
            g = random.nextInt(255);
            b =  random.nextInt(255);
        }
        color = Color.rgb(r,g,b);
        circle.setFill(color);
    }
    public void setCoin(int coin_) {
        coin = coin_;
    }
    public int getNumdr() {
        return numdr;
    }
    public Color getColor() {
        return color;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public void setDefSpeed() {
        this.speed = def_speed;
    }
    public void setDefRad() {
        circle.setRadius(def_rad);
    }
    public void setRad(int r) {
        circle.setRadius(r);
    }
    public double getRad() {
        return circle.getRadius();
    }

}
