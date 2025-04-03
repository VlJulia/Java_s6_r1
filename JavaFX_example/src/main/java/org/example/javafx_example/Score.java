package org.example.javafx_example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Score {
    @FXML
    Label allscore;
    Label scores[] = null;
    protected int score_label_count;
    Score(int izn, int score_label_count_, Label allscore_, int x ,int y, int dx){
        allscore =allscore_;
        score_label_count =score_label_count_;
        allscore.setText(String.valueOf(izn));
        scores=new Label[score_label_count];
        for (int i=0;i<score_label_count;i++){
            scores[i]=new Label();
            scores[i].setText("0");
            scores[i].setLayoutY(y);
            scores[i].setFont(Font.font("Arial", FontWeight.BOLD, 24));
            scores[i].setLayoutX(x + dx*i);
        }
    }
    public void coinPlus(int coin, int num) {
        int a = (Integer.parseInt(scores[num].getText()) + 1);
        scores[num].setText(String.valueOf(a));
        a = (Integer.parseInt(allscore.getText()) + coin);
        allscore.setText(String.valueOf(a));
    }

    public Label[] getScores() {
        return scores;
    }

    public void restart(){
        for (Label a: scores) a.setText("0");
        allscore.setText("0");
    }
}
