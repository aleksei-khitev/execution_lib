package ru.akhitev.execution_lib;

import ru.akhitev.execution_lib.CommandOperation.OperationWithoutStatus;
import ru.akhitev.execution_lib.CommandOperation.OperationWithStatus;

import java.util.Optional;
import java.util.Queue;

/**
 * Фабрика для работы создания команд.
 */
public final class CommandFactory {
    /** Команда будет возвращать пустой статус.*/
    public static Command newInstanceWithoutStatus(final OperationWithoutStatus operation){
        return new CommandImpl(operation);
    }

    /** Команда будет возвращать наполненный статус.*/
    public static Command newInstanceWithStatus(final OperationWithStatus operation){
        return new CommandImpl(operation);
    }

    /** Реализация команды. В зависимости от интерфейса операции,
     * возвращает или нет результат.
     */
    private static class CommandImpl implements Command {
        private final String unKnownOperationMessageTemplate = "Неизвестный тип операции [%s]";
        /** Сама операция. */
        CommandOperation operation;

        /** Конструктор открыт фабрике./ */
        CommandImpl(final CommandOperation operation) {
            this.operation = operation;
        }

        /** {@inheritDoc}. */
        @Override
        public Optional<Queue<Status>> execute() {
            if (operation instanceof OperationWithoutStatus) {
                ((OperationWithoutStatus) operation).execute();
                return Optional.empty();
            } else if (operation instanceof OperationWithStatus) {
                return ((OperationWithStatus) operation).execute();
            }
            throw new IllegalArgumentException(String.format(unKnownOperationMessageTemplate, operation.toString()));
        }
    }
}
