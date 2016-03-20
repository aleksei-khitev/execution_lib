package ru.akhitev.execution_lib;

import org.slf4j.Logger;

import java.util.*;

/**
 * Общий интерфейс для команд и исполнителей команд.
 * Сделан для реализации автологирования выполнений действий (аналог аоп).
 */
interface Executable {
    /**
     * Выполнение.
     * @return Список результатов выполнения.
     */
    Optional<Queue<Status>> execute();

    /**
     * Реализация аспекта журналирования.
     * @return Список результатов выполнения.
     */
    default Optional<Queue<Status>> loggableExecute() {
        final String errorKey = "Ошибка в ходе обработки";
        Logger logger = org.slf4j.LoggerFactory.getLogger(Executable.class);
        try {
            return execute();
        } catch (Exception e) {
            logger.error(errorKey, e);
            Status errorStatus = OperationStatus.newInstance(errorKey, e.getMessage());
            Queue<Status> errors = new LinkedList<>();
            errors.add(errorStatus);
            return Optional.of(errors);
        } catch (Throwable er) {
            logger.error(errorKey, er);
            return Optional.empty();
        }
    }
}
