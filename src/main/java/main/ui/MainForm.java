package main.ui;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {

    JButton btnFile;
    JButton btnStart;

    MainForm () {
        super("TitleScreen");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnFile = new JButton("Обзор...");
        btnStart = new JButton("Запуск");

        JPanel contents = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contents.add(btnFile);
        contents.add(btnStart);

        setContentPane(contents);

        btnFile.addActionListener(new FileListener(inf, this));
        btnStart.addActionListener(new AnalysisListener(this));
    }

    static MainForm getInstance(){
        return new MainForm();
    }

    public static void startMainForm(FormSetUp setUp){
        MainForm form = getInstance();
        form.setSize(setUp.size.width, setUp.size.height);
        form.setBounds(setUp.rectangle);
    }

    @Builder
    public static class FormSetUp {
        private boolean visible;
        private Size size;
        private Rectangle rectangle;
    }

    @Builder
    public static class Size {
        private int width;
        private int height;
    }
}
