package support.classes;

import lombok.Builder;

@Builder

public class AnalyzeResultInfo {
    private final String errorMessage;
    private final boolean result;
}
