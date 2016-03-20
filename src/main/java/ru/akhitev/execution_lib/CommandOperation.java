package ru.akhitev.execution_lib;

import java.util.Optional;
import java.util.Queue;

/**
 * Не всем командам нужно выдавать какой то результат. Для разграничения заведены операции, которые упрощают
 * логку возврата пустого значения.
 *
 */
interface CommandOperation {

    /** Возвращает статус. */
    @FunctionalInterface
    interface OperationWithStatus extends CommandOperation {
        Optional<Queue<Status>> execute();
    }

    /** Не возвращает статус. */
    @FunctionalInterface
    interface OperationWithoutStatus extends CommandOperation {
        void execute();
    }
}
