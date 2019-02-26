package hudson.plugins.clover.targets;

import hudson.plugins.clover.results.AbstractCloverMetrics;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Holds the target coverage for a specific condition;
 *
 * @author Stephen Connolly
 * @since 1.1
 */
public class CoverageTarget implements Serializable {

    private float methodCoverage;

    private float conditionalCoverage;

    private float statementCoverage;

    private float elementCoverage;

    public CoverageTarget() {
    }

    @DataBoundConstructor
    public CoverageTarget(float methodCoverage, float conditionalCoverage, float statementCoverage) {
        this.methodCoverage = methodCoverage;
        this.conditionalCoverage = conditionalCoverage;
        this.statementCoverage = statementCoverage;
        this.elementCoverage = null;
    }

    public boolean isAlwaysMet() {
        return (methodCoverage == null || methodCoverage < 0) &&
                (conditionalCoverage == null || conditionalCoverage < 0) &&
                (statementCoverage == null || statementCoverage < 0) &&
                (elementCoverage == null || elementCoverage < 0);
    }

    public boolean isEmpty() {
        return methodCoverage == null &&
                conditionalCoverage == null &&
                statementCoverage == null &&
                elementCoverage == null;
    }

    public Set<CoverageMetric> getFailingMetrics(AbstractCloverMetrics coverage) {
        final Set<CoverageMetric> result = new HashSet<CoverageMetric>();

        if (methodCoverage != null && coverage.getMethodCoverage().getPercentageFloat() < methodCoverage) {
            result.add(CoverageMetric.METHOD);
        }

        if (conditionalCoverage != null && coverage.getConditionalCoverage().getPercentage() < conditionalCoverage) {
            result.add(CoverageMetric.CONDITIONAL);
        }

        if (statementCoverage != null && coverage.getStatementCoverage().getPercentage() < statementCoverage) {
            result.add(CoverageMetric.STATEMENT);
        }

        if (elementCoverage != null && coverage.getElementCoverage().getPercentage() < elementCoverage) {
            result.add(CoverageMetric.ELEMENT);
        }

        return result;
    }

    public Map<CoverageMetric, Integer> getRangeScores(CoverageTarget min, AbstractCloverMetrics coverage) {
        final Map<CoverageMetric, Integer> result = new HashMap<CoverageMetric, Integer>();

        result.put(CoverageMetric.METHOD,
                calcRangeScore(methodCoverage, min.methodCoverage, coverage.getMethodCoverage().getPercentage()));

        result.put(CoverageMetric.CONDITIONAL,
                calcRangeScore(conditionalCoverage, min.conditionalCoverage, coverage.getConditionalCoverage().getPercentage()));

        result.put(CoverageMetric.STATEMENT,
                calcRangeScore(statementCoverage, min.statementCoverage, coverage.getStatementCoverage().getPercentage()));

        result.put(CoverageMetric.ELEMENT,
                calcRangeScore(elementCoverage, min.elementCoverage, coverage.getElementCoverage().getPercentage()));

        return result;
    }

    private static int calcRangeScore(Integer max, Integer min, int value) {
        if (min == null || min < 0) min = 0;
        if (max == null || max > 100) max = 100;
        if (min > max) min = max - 1;
        int result = (int)(100f * (value - min.floatValue()) / (max.floatValue() - min.floatValue()));
        if (result < 0) return 0;
        if (result > 100) return 100;
        return result;
    }

    /**
     * Getter for property 'methodCoverage'.
     *
     * @return Value for property 'methodCoverage'.
     */
    public float getMethodCoverage() {
        return methodCoverage;
    }

    /**
     * Setter for property 'methodCoverage'.
     *
     * @param methodCoverage Value to set for property 'methodCoverage'.
     */
    public void setMethodCoverage(float methodCoverage) {
        this.methodCoverage = methodCoverage;
    }

    /**
     * Getter for property 'conditionalCoverage'.
     *
     * @return Value for property 'conditionalCoverage'.
     */
    public Integer getConditionalCoverage() {
        return conditionalCoverage;
    }

    /**
     * Setter for property 'conditionalCoverage'.
     *
     * @param conditionalCoverage Value to set for property 'conditionalCoverage'.
     */
    public void setConditionalCoverage(Integer conditionalCoverage) {
        this.conditionalCoverage = conditionalCoverage;
    }

    /**
     * Getter for property 'statementCoverage'.
     *
     * @return Value for property 'statementCoverage'.
     */
    public Integer getStatementCoverage() {
        return statementCoverage;
    }

    /**
     * Setter for property 'statementCoverage'.
     *
     * @param statementCoverage Value to set for property 'statementCoverage'.
     */
    public void setStatementCoverage(Integer statementCoverage) {
        this.statementCoverage = statementCoverage;
    }

    /**
     * Getter for property 'elementCoverage'.
     *
     * @return Value for property 'elementCoverage'.
     */
    public Integer getElementCoverage() {
        return elementCoverage;
    }

    /**
     * Setter for property 'elementCoverage'.
     *
     * @param elementCoverage Value to set for property 'elementCoverage'.
     */
    public void setElementCoverage(Integer elementCoverage) {
        this.elementCoverage = elementCoverage;
    }
}
