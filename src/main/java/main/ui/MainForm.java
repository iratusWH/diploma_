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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.StaticAnalyzer;
import support.classes.AnalyzeResultInfo;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class MainForm extends Application {

    private File projectPath;
    private AnalyzeResultInfo resultInfo;
    private static final String ACTIVE_ANALYZE_BUTTON_CSS = "-fx-background-color: #3c703c; -fx-text-fill: #fdfdfd";

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

    private FlowPane getFlowPane(Node... nodes) {
        FlowPane flowPane = new FlowPane(Orientation.VERTICAL, 10, 10, nodes);
        flowPane.setBackground(new Background(new BackgroundFill(Color.rgb(52, 22, 22), CornerRadii.EMPTY, Insets.EMPTY)));
        flowPane.setPadding(new Insets(15));
        flowPane.setAlignment(Pos.CENTER);
        return flowPane;
    }

    private Scene getScene(Pane root) {
        return new Scene(root, 250, 200);
    }

    private Node[] getNodes(Stage stage) {
        Label directoryLabel = new Label("Директория проекта");
        TextField directoryPath = new TextField();
        Button directoryChooserButton = new Button("Выбор рабочего пути проекта");
        Button startAnalyzerButton = new Button("Приступить к анализу проекта");
        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryLabel.setTextFill(Color.WHEAT);
        directoryPath.appendText("Введите директорию проекта");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        startAnalyzerButton.setDisable(true);
        
        directoryPath.setOnKeyTyped(
                actionEvent -> {
                    if (!Objects.equals(directoryPath.getText(), "Введите директорию проекта")) {
                        startAnalyzerButton.setDisable(false);
                        startAnalyzerButton.setStyle(ACTIVE_ANALYZE_BUTTON_CSS);
                    }
                }
        );

        directoryChooserButton.setOnAction(
                actionEvent -> {
                    File tempProjPath = projectPath;
                    projectPath = Optional.of(directoryChooser)
                            .map(chooser -> chooser.showDialog(stage))
                            .orElse(tempProjPath);

                    if (Optional.ofNullable(projectPath).map(File::getPath).isPresent()) {
                        directoryPath.setText(projectPath.getPath());
                        startAnalyzerButton.setDisable(false);
                        startAnalyzerButton.setStyle(ACTIVE_ANALYZE_BUTTON_CSS);
                    }
                }
        );

        startAnalyzerButton.setOnAction(
                actionEvent -> {
                    if (directoryPath.getText() != null) {
                        resultInfo = StaticAnalyzer.starter(directoryPath.getText());
                        Alert resultInfoMessageWindow = getAlertMessageWindow(resultInfo);
                        resultInfoMessageWindow.show();
                        startAnalyzerButton.setDisable(false);
                        startAnalyzerButton.setStyle(ACTIVE_ANALYZE_BUTTON_CSS);
                    }
                }
        );

        return new Node[]{
                directoryLabel,
                directoryPath,
                directoryChooserButton,
                startAnalyzerButton
        };
    }

    private Alert getAlertMessageWindow(AnalyzeResultInfo resultInfo) {
        Alert resultInfoMessageWindow = new Alert(resultInfo.getAlertType());
        resultInfoMessageWindow.setTitle(resultInfo.isResult() ? "Выполнено!" : "Ошибка");
        resultInfoMessageWindow.setContentText(resultInfo.isResult() ? "Отчет " + resultInfo.getFolder() + " сохранен!" : resultInfo.getErrorMessage());
        resultInfoMessageWindow.setHeaderText(null);

        return resultInfoMessageWindow;
    }
}
