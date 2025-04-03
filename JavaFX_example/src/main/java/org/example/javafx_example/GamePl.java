package org.example.javafx_example;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javafx.scene.shape.Polygon;

import java.io.IOException;

public class GamePl {
    @FXML
    ArrowView arrows[];
    @FXML
    AnchorPane mainPane;
    @FXML
    Label allscore;
    @FXML
    Label sloznost;
    Score score;
    boolean play = false;
    public boolean fpause = false;
    int arrow_count = 3;
    int circle_count = 3;
    Pane fakeArrow = newArrow(Color.PINK);
    double playerY;
    int speed = 5;
    ArrowView t[] = null;
    runCircle c[] =null;
    @FXML
    void start() throws InterruptedException, IOException {
//        circle.setLayoutX(0);
        if(t == null)
        {
            playerY =mainPane.getHeight()/2;
            fakeArrow.setLayoutX(20);
            fakeArrow.setLayoutY(playerY);
            mainPane.getChildren().add(fakeArrow);
            mainPane.setFocusTraversable(true);
            score = new Score(0,circle_count, allscore, 50, 50, 50);
            Label[] tmp = score.getScores();
            c =new runCircle[circle_count];
            int mr = (int)(mainPane.getWidth()*0.3/(circle_count));
            if (mr> 80) mr =80;
            for (int i =0 ;i <circle_count; i++) {
                c[i]= new runCircle(340, (int)(mainPane.getWidth()*0.7 + (mainPane.getWidth()*0.3/circle_count)*i), score, (int)(mr-i*10), i);
                tmp[i].setTextFill(c[i].getColor());
                mainPane.getChildren().add(c[i].getCircle());
                mainPane.getChildren().add(tmp[i]);
                c[i].start();
            }

            t= new ArrowView[arrow_count];
            for (int i =0; i<arrow_count;i++) {
                t[i] = new ArrowView(mainPane.getWidth(), mainPane.getHeight(),Color.BLACK);
                mainPane.getChildren().add(t[i].getArrow());
                t[i].setFlying(false);
                t[i].setCircles(c);
                t[i].start();
            }
        }
        else {
            if (fpause) return;
            spun(playerY);
        }
    }
    @FXML
    void stop()
    {
        play = false;
        for (int i =0; i<arrow_count;i++) {
            if(t[i] != null)
            {
                t[i].setFlying(play);
            }
        }
        if (c!=null)
        for (int i =0; i<circle_count;i++) {
            if(c[i] != null)
            {
                c[i].setPause(play);
            }
        }
    }
    @FXML
    void pause(){
        for (int i=0;i<arrow_count;i++) {
            t[i].setPause(fpause);
            if (!fpause)
            synchronized (t[i]) {
                t[i].notify();
            }
        }
        if (c!=null)
        for (int i=0;i<circle_count;i++) {
            c[i].setPause(fpause);
            if (!fpause)
                synchronized (c[i]) {
                    c[i].notify();
                }
        }
    }

    @FXML
    void click(MouseEvent evn){
        if (fpause) return;
        newPlayerY(evn.getY());
        spun(evn.getY());
    }

    @FXML
    public void keyPress(KeyEvent e){
        if (e.getCode() == KeyCode.P) {
            fpause=!fpause;
            play=!play;
            pause();
            return;
        }
        if (fpause) return;
        switch (e.getCode()){
            case KeyCode.W: {
                newPlayerY(playerY-speed);
                return;
            }
            case KeyCode.S: {
                newPlayerY(playerY+speed);
                return;
            }
            case KeyCode.A: {
                speed/=2;
                return;
            }
            case KeyCode.D: {
                speed*=2;
                if (speed>= mainPane.getHeight()/3) speed=(int)mainPane.getHeight()/3;
                return;
            }
            case KeyCode.EQUALS: {
                int s = Integer.parseInt(sloznost.getText()) +1;
                if (s<0) return;
                sloznost.setText(String.valueOf(s));
                for (runCircle a: c) {a.setSpeed(a.speed*1.5);a.setRad((int)(a.getRad()/1.2));}
                for (ArrowView a: t) {a.speed*=1.5;}
                return;
            }
            case KeyCode.MINUS: {
                int s = Integer.parseInt(sloznost.getText()) -1;
                if (s<0) return;
                sloznost.setText(String.valueOf(s));
                for (runCircle a: c) {a.setSpeed(a.speed/1.5);a.setRad((int)(a.getRad()*1.2));}
                for (ArrowView a: t) {a.speed/=1.5;}
                return;
            }
        }
    System.out.println(e.getCode());
    }
    @FXML
    public void restart(){
        score.restart();
        for (runCircle a: c) {a.setDefSpeed();a.setDefRad();}
        for (ArrowView a: t) {a.setDefSpeed();}
        sloznost.setText("5");
    }
    public static Pane newArrow(Color col) {
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
        return pane;
    }
    public void newPlayerY(double y){
        playerY = y;
        if (playerY<0) playerY=0;
        if(playerY> mainPane.getHeight()) playerY=mainPane.getHeight();
        fakeArrow.setLayoutY(playerY);
    }
    public void spun(double y){
        for (int i =0; i<arrow_count;i++) {
            if ( !t[i].getFlying()) {
                t[i].arrow.setLayoutY(playerY);
                t[i].cY=playerY;
                t[i].arrow.setLayoutX(-100);
                t[i].isFlying = true;
                t[i].fpause = false;
                synchronized (t[i]) {
                    t[i].notify();
                }
                break;
            }
        }
    }
}

