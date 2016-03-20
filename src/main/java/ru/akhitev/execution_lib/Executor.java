package ru.akhitev.execution_lib;

import java.util.Optional;

/**
 * Исполнитель команд.
 */
public interface Executor extends Executable {
    /** Добавляет команду в очередь команд. */
    void addCommand(final Command command);

    /** Добавляет статус, который будет выведен перед началом выполнения очереди. */
    void setStatusesBefore(final Optional<OperationStatus> statusesBefore);

    /** Добавляет статус, который будет выведен после окончания выполнения очереди. */
    void setStatusesAfter(final Optional<OperationStatus> statusesAfter);
}
