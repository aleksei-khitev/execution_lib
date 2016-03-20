package ru.akhitev.execution_lib;

import java.util.Optional;

/**
 * Фабрика исполнителей.
 */
public interface ExecutorFactory {
    /** Возвращает новый исполнитель команд. */
    Optional<Executor> newExecutorInstance(final Object executorData);
}
