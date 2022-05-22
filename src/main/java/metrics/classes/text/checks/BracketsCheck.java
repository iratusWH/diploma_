package metrics.classes.text.checks;

import com.github.javaparser.JavaToken;
import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.*;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import org.apache.commons.lang3.StringUtils;
import support.classes.MetricNameEnum;

import java.util.*;

import static support.classes.OperatorsConstClass.*;

@Slf4j
public class BracketsCheck extends MetricProcessingImpl {

    private static final String OK_MESSAGE = "OK";

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
                if (!missingFigureBracketsList.isEmpty()) {
                    missingFigureBracketString = formatPositionToString(missingFigureBracketsList);
                }
            }

            setMetric(
                    "".equals(missingFigureBracketString.trim()) ? OK_MESSAGE : missingFigureBracketString
            );
        } catch (Exception e) {
            log.error("BracketsCheck error - {}", e.getMessage());
        }
    }

    private List<Statement> getAllStatementsWithFigureBrackets(CompilationUnit compilationUnit) {
        List<Statement> statementsList = new ArrayList<>();

        statementsList.addAll(compilationUnit.findAll(IfStmt.class).stream().toList());
        statementsList.addAll(compilationUnit.findAll(ForStmt.class).stream().toList());
        statementsList.addAll(compilationUnit.findAll(ForEachStmt.class).stream().toList());
        statementsList.addAll(compilationUnit.findAll(WhileStmt.class).stream().toList());

        return statementsList;
    }

    private boolean isNotEqualHomePosition(Position otherPosition) {
        return !Position.HOME.equals(otherPosition);
    }

    private Position findMissingFigureBracket(Statement startStatement) {

        Optional<JavaToken> nextToken = startStatement.getTokenRange()
                .map(TokenRange::getBegin);

        String currentToken;
        Deque<Integer> bracketsStack = new ArrayDeque<>();
        do {
            nextToken = nextToken.map(JavaToken::getNextToken)
                    .filter(Optional::isPresent)
                    .map(Optional::get);

            currentToken = nextToken
                    .map(JavaToken::getText)
                    .orElseThrow();

        } while (!currentToken.equals(OPEN_ROUND_BRACKET));

        do {
            if (OPEN_BRACKETS_LIST.contains(currentToken)) {
                bracketsStack.push(OPEN_BRACKETS_LIST.indexOf(currentToken));
            }
            if (CLOSE_BRACKETS_LIST.contains(currentToken)
                    && (Objects.nonNull(bracketsStack.peek()))
                    && (bracketsStack.peek() == CLOSE_BRACKETS_LIST.indexOf(currentToken))) {
                bracketsStack.pop();
            }

            nextToken = nextToken.map(JavaToken::getNextToken)
                    .filter(Optional::isPresent)
                    .map(Optional::get);

            currentToken = nextToken
                    .map(JavaToken::getText)
                    .orElseThrow();

        } while (!bracketsStack.isEmpty());

        Position statementPosition = getPosition(nextToken);
        int statementCount = 7;
        do {
            nextToken = getNextNotEmptyToken(nextToken);

            currentToken = nextToken
                    .map(JavaToken::getText)
                    .orElseThrow();

            --statementCount;
        } while (!currentToken.equals(OPEN_FIGURE_BRACKET) && statementCount > 0);

        return currentToken.equals(OPEN_FIGURE_BRACKET) ? Position.HOME : statementPosition;
    }


    private Position getPosition(Optional<JavaToken> token) {
        return token.map(JavaToken::getRange)
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow)
                .map(range -> range.begin)
                .orElseThrow();
    }

    private Optional<JavaToken> getNextNotEmptyToken(Optional<JavaToken> token) {
        String tokenText;
        do {
            token = token.map(JavaToken::getNextToken)
                    .filter(Optional::isPresent)
                    .map(Optional::get);

            tokenText = token.map(JavaToken::getText).orElseThrow();
        } while (tokenText.equals(StringUtils.EMPTY));
        return token;
    }

    private String formatPositionToString(List<Position> missingFigureBracketsList) {
        return String.join("\n",
                missingFigureBracketsList.stream()
                        .filter(this::isNotEqualHomePosition)
                        .map(Position::toString)
                        .filter(pos -> !StringUtils.EMPTY.equals(pos))
                        .toList()
        );
    }
}
