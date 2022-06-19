package support.classes;

import javafx.scene.control.Alert;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AnalyzeResultInfo {
    private final Alert.AlertType alertType;
    private final String folder;
    private final String errorMessage;
    private final boolean result;
}
