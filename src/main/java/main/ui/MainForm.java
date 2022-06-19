package main.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.StaticAnalyzer;
import support.classes.AnalyzeResultInfo;

import java.awt.*;
import java.io.File;

public class MainForm extends Application {

    private File projectPath;
    private AnalyzeResultInfo resultInfo;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Node[] elements = getNodes(stage);
        FlowPane root = getFlowPane(elements);

        Scene scene = getScene(root);
        stage.setTitle("Static Analyzer");
        stage.setScene(scene);
        stage.show();
    }

    FlowPane getFlowPane(Node... nodes) {
        FlowPane flowPane = new FlowPane(Orientation.VERTICAL, 10, 10, nodes);
        flowPane.setBackground(new Background(new BackgroundFill(Color.rgb(52,22,22), CornerRadii.EMPTY, Insets.EMPTY)));
        flowPane.setPadding(new Insets(15));
        flowPane.setAlignment(Pos.CENTER);
        return flowPane;
    }

    Scene getScene(Pane root) {
        return new Scene(root, 250, 200);
    }

    Node[] getNodes(Stage stage) {
        Label directoryLabel = new Label("Директория проекта");
        TextField directoryPath = new TextField();
        Button directoryChooserButton = new Button("Выбор директории");
        Button startAnalyzerButton = new Button("Приступить к анализу");
        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryLabel.setTextFill(Color.WHEAT);
        directoryPath.appendText("Введите директорию проекта");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        startAnalyzerButton.setDisable(true);

        directoryChooserButton.setOnAction(
                actionEvent -> {
                    projectPath = directoryChooser.showDialog(stage);
                    directoryPath.setText(projectPath.getPath());
                    startAnalyzerButton.setDisable(false);
                }
        );

        startAnalyzerButton.setOnAction(
                actionEvent -> {
                    resultInfo = StaticAnalyzer.starter(projectPath.getPath());
                    Alert resultInfoMessageWindow = getAlertMessageWindow(resultInfo);
                    resultInfoMessageWindow.show();

                    if (resultInfo.isResult()) {
                        openFile(resultInfo.getFolder());
                    }
                }
        );

        return new Node[] {
                directoryLabel,
                directoryPath,
                directoryChooserButton,
                startAnalyzerButton
        };
    }

    Alert getAlertMessageWindow(AnalyzeResultInfo resultInfo) {
        Alert resultInfoMessageWindow = new Alert(resultInfo.getAlertType());
        resultInfoMessageWindow.setTitle("Static Analyzer");
        resultInfoMessageWindow.setContentText(resultInfo.isResult() ? "Отчет " + resultInfo.getFolder() + " сохранен!"  : resultInfo.getErrorMessage());
        resultInfoMessageWindow.setHeaderText(resultInfo.isResult() ? "Выполнено!" : "Ошибка");

        return resultInfoMessageWindow;
    }

    void openFile(String filePath) {
        try {
            Desktop.getDesktop().open(new File(filePath));

        } catch (Exception e) {
            Alert errorOpeningResultMessageWindow = new Alert(Alert.AlertType.ERROR);
            errorOpeningResultMessageWindow.setTitle("Static Analyzer");
            errorOpeningResultMessageWindow.setHeaderText("Ошибка открытия отчета!");
            errorOpeningResultMessageWindow.show();
        }
    }
}
