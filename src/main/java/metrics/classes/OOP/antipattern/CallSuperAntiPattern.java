package metrics.classes.OOP.antipattern;

import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

public class CallSuperAntiPattern extends MetricProcessingImpl {

    public CallSuperAntiPattern(){
        setMetricName(MetricNameEnum.CALL_SUPER_ANTI_PATTERN);
    }

    @Override
    public void processMetric() {

    }
}
