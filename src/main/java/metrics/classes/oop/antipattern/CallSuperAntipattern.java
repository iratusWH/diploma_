package metrics.classes.oop.antipattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.SimpleMetricProcessingImpl;

import java.util.List;

@Slf4j
public class CallSuperAntipattern extends SimpleMetricProcessingImpl {
    @Override
    public void processMetric() {
        List<ExplicitConstructorInvocationStmt> list = getFile().findAll(ExplicitConstructorInvocationStmt.class);
        log.info("Added {}", list);
    }
}
