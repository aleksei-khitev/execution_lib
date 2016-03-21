package ru.akhitev.execution_lib;

import java.util.Properties;
import java.util.Comparator;
import java.util.Queue;

/**
 * Данные, которые передаются на вход {@link ExecutorBuilder}, для выполнения таких действий,
 * как проверка входных данных, реагирование на ошибки выполнения (вызов закрытия окна, к примеру).
 */
public interface ExecBuilderStrategy {
    /** Выполнение перед первыми шагами заданными, в строителе. */
    public void executeBeforeBuild(final Object executorData);

    /** Проверка корректности входных данных. */
    public boolean isIncorrectInput(final Object executorData);

    /** Выполнение дополнительных шагов при ошибке (закрыть окно, например). */
    public void executeOnError(final Object executorData);

    /** Чтение excel и получение данных в виде очереди свойств. */
    public Queue<Properties> getDataFromExcel(final Object executorData, final Comparator comparator);

    /** Для сортировки данных в таблице. */
    public Comparator getExcelComparator(final Object executorData);
}
