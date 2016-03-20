package ru.akhitev.execution_lib;

/**
 * Статус операции, команды и т.д.
 */
public interface Status {
    /** Название (ключ) записи. */
    String getName();

    /** Значение записи. */
    String getValue();
}
