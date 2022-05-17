package metrics.classes.text.checks;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.*;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static support.classes.OperatorsConstClass.*;

@Slf4j
public class BracketsCheck extends MetricProcessingImpl {

    public static final String OK_MESSAGE = "OK";

    public BracketsCheck() {
        setMetricName(MetricNameEnum.BRACKET_CHECK);
    }

    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());

            List<Statement> allStatements = getAllStatementsWithFigureBrackets(compilationUnit);
            String missingFigureBracketString = OK_MESSAGE;
            if (!allStatements.isEmpty()) {
                List<Position> missingFigureBracketsList = allStatements.stream().map(this::findMissingFigureBracket).toList();
                if (!missingFigureBracketsList.isEmpty())
                    missingFigureBracketString = formatPositionToString(missingFigureBracketsList);
            }

            setMetric(missingFigureBracketString);
        } catch (Exception e) {
            log.error("BracketsCheck error - {}", e.getMessage());
        }
        ;
    }

    private List<Statement> getAllStatementsWithFigureBrackets(CompilationUnit compilationUnit) {
        List<Statement> statementsList = new ArrayList<>();

        statementsList.addAll(compilationUnit.findAll(IfStmt.class).stream().toList());
        statementsList.addAll(compilationUnit.findAll(ForStmt.class).stream().toList());
        statementsList.addAll(compilationUnit.findAll(ForEachStmt.class).stream().toList());
        statementsList.addAll(compilationUnit.findAll(WhileStmt.class).stream().toList());

        return statementsList;
    }

    private boolean isEqualHomePosition(Position position) {
        return Position.HOME.equals(position);
    }

    private Position findMissingFigureBracket(Statement startStatement) {
        Position statementPosition;

        Optional<JavaToken> nextToken = startStatement.getTokenRange()
                .map(TokenRange::getBegin);

        String currentToken;
        do {
            nextToken = nextToken.map(JavaToken::getNextToken)
                    .filter(Optional::isPresent)
                    .map(Optional::get);

            currentToken = nextToken
                    .map(JavaToken::getText)
                    .orElseThrow();

            statementPosition = getPosition(nextToken);
        } while (!currentToken.equals(CLOSE_ROUND_BRACKET));

        int tokenSpreadCount = 10;
        do {
            nextToken = nextToken.map(JavaToken::getNextToken)
                    .filter(Optional::isPresent)
                    .map(Optional::get);

            currentToken = nextToken
                    .map(JavaToken::getText)
                    .orElseThrow();

            --tokenSpreadCount;
        } while (!currentToken.equals(OPEN_FIGURE_BRACKET) || tokenSpreadCount > 0);

        return tokenSpreadCount == 0 ? statementPosition : Position.HOME;
    }


    private Position getPosition(Optional<JavaToken> token) {
        return token.map(JavaToken::getRange)
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow)
                .map(range -> range.begin)
                .orElseThrow();
    }

    private String formatPositionToString(List<Position> missingFigureBracketsList) {
        return String.join("\n",
                missingFigureBracketsList.stream()
                        .filter(this::isEqualHomePosition)
                        .map(Position::toString)
                        .toList()
        );
    }
}
