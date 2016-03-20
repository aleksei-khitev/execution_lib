package ru.akhitev.execution_lib;

import java.util.*;

/**
 * Реализация исполнителя очереди команд.
 */
final class ExecutorImpl implements Executor {
    /** Очередь команд. */
    private final Queue<Command> commandQueue;

    /** Статус, который будет выведен перед началом выполнения очереди. */
    private Optional<OperationStatus> statusesBefore;

    /** Статус, который будет выведен после окончания выполнения очереди. */
    private Optional<OperationStatus> statusesAfter;

    /** Создание экземпляра через {@link #newInstance()} */
    private ExecutorImpl() {
        commandQueue = new LinkedList<>();
        statusesBefore = Optional.empty();
        statusesAfter = Optional.empty();
    }

    /** Возвращает новый экземпляр. */
    public static Executor newInstance() {
        return new ExecutorImpl();
    }

    /** {@inheritDoc}. */
    public void addCommand(final Command command) {
        commandQueue.add(command);
    }

    /** {@inheritDoc}. */
    public Optional<Queue<Status>> execute() {
        final Queue<Status> statuses = new LinkedList<>();
        statusesBefore.ifPresent(statuses::add);
        commandQueue.forEach(updaterCommand -> updaterCommand.loggableExecute().ifPresent(statuses::addAll));
        statusesAfter.ifPresent(statuses::add);
        return Optional.of(statuses);
    }

    @Override
    public String toString() {
        return "ExecutorImpl{" +
                "commandQueue.size=" + commandQueue.size() +
                '}';
    }

    /** {@inheritDoc}. */
    public void setStatusesBefore(final Optional<OperationStatus> statusesBefore) {
        this.statusesBefore = statusesBefore;
    }

    /** {@inheritDoc}. */
    public void setStatusesAfter(final Optional<OperationStatus> statusesAfter) {
        this.statusesAfter = statusesAfter;
    }
}
