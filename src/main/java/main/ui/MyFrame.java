//package main.ui;
//
//import support.classes.AnalyzeResultInfo;
//
//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
//import static java.lang.Math.*;
//
//
//class ResultDialog extends Dialog {
//    ResultDialog(Frame parent, String title) {
//        super(parent, title, true);
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
//            }
//        });
//
//        setLayout(new FlowLayout(FlowLayout.CENTER));
//        setSize(300, 300);
//        JButton btnOk;
//        JLabel lb;
//        lb = new JLabel("Все хорошо");
//        btnOk = new JButton("Ок");
//        add(lb);
//        add(btnOk);
//        btnOk.addActionListener(new OkListener(this));
//    }
//
//    static class OkListener implements ActionListener{
//        ResultDialog rd;
//        OkListener(ResultDialog resultDialog){
//            rd = resultDialog;
//        }
//        public void actionPerformed(ActionEvent e){
//            rd.dispose();
//            rd.setVisible(false);
//        }
//    }
//}
//
//class FileChooserDialog extends Dialog {
//
//    JFileChooser jf;
//
//    FileChooserDialog(Frame parent, Info inf, String title) {
//        super(parent, title, true);
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
//            }
//        });
//
//        setLayout(new FlowLayout(FlowLayout.CENTER));
//        setSize(500, 500);
//        jf = new JFileChooser(System.getProperty("user.home"));
//        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        JButton btnOk;
//        btnOk = new JButton("Ок");
//        add(jf);
//        add(btnOk);
//        btnOk.addActionListener(new FileChooserDialog.OkListener(this, inf));
//    }
//
//    class OkListener implements ActionListener{
//        FileChooserDialog rd;
//        Info inf;
//        OkListener(FileChooserDialog fileChooserDialog, Info info){
//            rd = fileChooserDialog;
//            inf = info;
//        }
//        public void actionPerformed(ActionEvent e){
//            inf.filePath = jf.getSelectedFile().getPath();
//            rd.dispose();
//            rd.setVisible(false);
//        }
//    }
//}
//
//public class MyFrame extends JFrame{
//    JButton btnFile;
//    JButton btnStart;
//    AnalyzeResultInfo inf = new AnalyzeResultInfo();
//    public MyFrame (){
//        super("TitleScreen");
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        btnFile = new JButton("Обзор...");
//        btnStart = new JButton("Запуск");
//
//        JPanel contents = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        contents.add(btnFile);
//        contents.add(btnStart);
//
//        setContentPane(contents);
//
//        btnFile.addActionListener(new FileListener(inf, this));
//        btnStart.addActionListener(new AnalysisListener(this));
//    }
//    static class FileListener implements ActionListener {
//        MyFrame mf;
//
//        FileListener(AnalyzeResultInfo info, MyFrame myframe) {
//            inf = info;
//            mf = myframe;
//        }
//
//        public void actionPerformed(ActionEvent e) {
//            FileChooserDialog fcd = new FileChooserDialog(mf, inf, "Выберите файл!");
//            fcd.setVisible(true);
//        }
//    }
//    class AnalysisListener implements ActionListener {
//        MyFrame mf;
//
//        AnalysisListener(MyFrame myframe) {
//            mf = myframe;
//        }
//
//        public void actionPerformed(ActionEvent e) {
//
//            int a = 1 + 8; //сюда пойдет анализатор
//
//            if (a > 5) {
//                JOptionPane.showMessageDialog(mf,
//                        inf.filePath,
//                        "Кря",
//                        JOptionPane.WARNING_MESSAGE);
//                ResultDialog rd = new ResultDialog(mf, "Замечатльно!");
//                rd.setVisible(true);
//            } else {
//                JOptionPane.showMessageDialog(mf,
//                        "Упс",
//                        "Ошибка",
//                        JOptionPane.WARNING_MESSAGE);
//            }
//        }
//    }
//}
