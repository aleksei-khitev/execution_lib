package ru.akhitev.execution_lib;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.Queue;
import java.util.Optional;
import java.util.Properties;
import java.util.Comparator;

/**
 * Cтроитель исполнителя.
 */
public final class ExecutorBuilder {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ExecutorBuilder.class);

    private final ExecBuilderStrategy strategy;

    private Regimes regime;

    private final Executor executor;

    private OperationAppender beforeAppender;

    private OperationAppender mainAppender;

    private OperationAppender afterAppender;

    private final Object executorData;

    private ExecutorBuilder(final Object executorData, final ExecBuilderStrategy strategy) {
        executor = ExecutorImpl.newInstance();
        this.executorData = executorData;
	this.strategy = strategy;
    }

    public static final ExecutorBuilder newInstance(Object executorData, final ExecBuilderStrategy strategy) {
        return new ExecutorBuilder(executorData, strategy);
    }

    public ExecutorBuilder before(OperationAppender beforeAppender){
        this.beforeAppender = beforeAppender;
        return this;
    }

    public ExecutorBuilder main(OperationAppender mainAppender){
        this.mainAppender = mainAppender;
        return this;
    }

    public ExecutorBuilder after(OperationAppender afterAppender){
        this.afterAppender = afterAppender;
        return this;
    }

    public Optional<Executor> build() {
        switch (regime) {
            case WITH_XLSX: return buildWithXls();
            case WITHOUT_OUTSOURCE: return buildWithoutXls();
            default: return buildWithXls();
        }
    }

    public Optional<Executor> buildWithXls() {
        try {
            executeBeforeBuild();
            if (isWrongInput()) {
                executeOnBuildError();
                return Optional.empty();
            }
            final Queue<Properties> xlsProperties = getDataFromExcel();
            if (xlsProperties.size() < 1) {
                executeOnBuildError();
                return Optional.empty();
            }
            beforeAppender.append(executor, null);
            xlsProperties.stream()
                    .forEach(properties -> {
                        mainAppender.append(executor, properties);
                    });
            afterAppender.append(executor, null);
            return Optional.of(executor);
        } catch (Exception e) {
            LOGGER.error("Ошибка чтения.", e);
            return Optional.empty();
        }
    }

    public Optional<Executor> buildWithoutXls() {
        try {
            executeBeforeBuild();
            if (isWrongInput()) {
                executeOnBuildError();
                return Optional.empty();
            }
            beforeAppender.append(executor, null);
            mainAppender.append(executor, null);
            afterAppender.append(executor, null);
            return Optional.of(executor);
        } catch (Exception e) {
            LOGGER.error("Ошибка чтения.", e);
            return Optional.empty();
        }
    }

    public final ExecutorBuilder regime(Regimes regime) {
        this.regime = regime;
        return this;
    }

    /** Вызывается в самом начале выполнения {@link #before} */
    private void executeBeforeBuild() {
        strategy.executeBeforeBuild(executorData);
    }

    /** Вызывается при ошибке валидации и при ошибке данных в excel. */
    private void executeOnBuildError() {
        strategy.executeOnError(executorData);
    }

    /** Проверка входных. Данных, логин и пароль входа на сайт. */
    private boolean isWrongInput() {
        return strategy.isIncorrectInput(executorData);
    }

    /** Преобразование данных из excel.*/
    private Queue<Properties> getDataFromExcel() throws IOException {
        return strategy.getDataFromExcel(executorData, getExcelComparator());
    }

    /** Поле, по которому будет построена сортировка из таблицы excel. */
    private Comparator getExcelComparator() {
         return strategy.getExcelComparator(executorData);
    }

    public enum Regimes {
        WITHOUT_OUTSOURCE,
        WITH_XLSX;
    }

    @FunctionalInterface
    public interface OperationAppender {
        void append(Executor executor, Properties xlsProperties);
    }

    protected static Long convertStringToLong(final String input) {
        return Double.valueOf(input).longValue();
    }
}
