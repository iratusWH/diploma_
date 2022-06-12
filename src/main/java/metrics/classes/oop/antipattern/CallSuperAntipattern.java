package metrics.classes.oop.antipattern;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
public class CallSuperAntipattern extends SimpleMetricProcessingImpl {

    private final Predicate<TokenRange> isCallSuper = stmt -> stmt.toString().contains("super");
    private final Predicate<ExplicitConstructorInvocationStmt> isMethodWithOnlyInstruction = invocation -> invocation
            .getParentNode()
            .map(Node::getTokenRange)
            .map(Optional::orElseThrow)
            .map(TokenRange::toRange)
            .map(Optional::orElseThrow)
            .map(Range::getLineCount)
            .orElseThrow()
            .equals(3);

    public CallSuperAntipattern(){
        setMetricName(MetricNameEnum.CALL_SUPER_ANTIPATTERN);
    }

    @Override
    public void processMetric() {
        Optional<TokenRange> callSuperInvocation = getFile()
                .findAll(ExplicitConstructorInvocationStmt.class)
                .stream()
                .filter(isMethodWithOnlyInstruction)
                .map(ExplicitConstructorInvocationStmt::getTokenRange)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(isCallSuper)
                .findFirst();

        setMetric(callSuperInvocation.isPresent() ? getResultMessageByTokenRange(callSuperInvocation.get()) : "OK");
    }

    public String getResultMessageByTokenRange(TokenRange foundCallSuper){
        return foundCallSuper
                .getBegin()
                .getRange()
                .map(range -> range.begin)
                .map(Position::toString)
                .orElseThrow();
    }

}
