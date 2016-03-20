package ru.akhitev.execution_lib;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Cтроитель исполнителя.
 */
public abstract class ExecutorBuilder {
    protected static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ExecutorBuilder.class);

    protected Regimes regime;

    protected Executor executor;

    protected OperationAppender beforeAppender;

    protected OperationAppender mainAppender;

    protected OperationAppender afterAppender;

    protected Object executorData;

    protected ExecutorBuilder(Object executorData) {
        executor = ExecutorImpl.newInstance();
        this.executorData = executorData;
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
            final List<Properties> xlsProperties = getDataFromExcel();
            if (xlsProperties.size() < 1) {
                executeOnBuildError();
                return Optional.empty();
            }
            beforeAppender.append(executor, null);
            xlsProperties.stream()
                    .sorted((p1, p2) -> {
                        final long n1 = convertStringToLong(p1.getProperty(getExcelColumnNameForSort().toUpperCase()));
                        final long n2 = convertStringToLong(p2.getProperty(getExcelColumnNameForSort().toUpperCase()));
                        return Long.compare(n1, n2);
                    })
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


    protected abstract void executeBeforeBuild();

    /** Вызывается при ошибке валидации и при ошибке данных в excel. */
    protected abstract void executeOnBuildError();

    /** Проверка входных. Данных, логин и пароль входа на сайт. */
    protected abstract boolean isWrongInput();

    /** Преобразование данных из excel.*/
    protected abstract List<Properties> getDataFromExcel() throws IOException;

    /** Поле, по которому будет построена сортировка из таблицы excel. */
    protected abstract String getExcelColumnNameForSort();

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
