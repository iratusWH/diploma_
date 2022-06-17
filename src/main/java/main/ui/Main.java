package main.ui;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
       MainForm.startMainForm(getSetUp());
    }

    private static MainForm.FormSetUp getSetUp() {
        return MainForm
                .FormSetUp
                .builder()
                .visible(true)
                .rectangle(new Rectangle())
                .size(MainForm.Size
                        .builder()
                        .height(300)
                        .width(300)
                        .build())
                .build();
    }
}
