package ru.akhitev.execution_lib;

/**
 * Результат операции.
 */
public final class OperationStatus implements Status {

    /** Формат для формирования записи в таблицу html. */
    private static final String TO_HTML_TBL_FORMAT = "<tr><td>%s</td><td>%s</td></tr>";

    /** Название записи. */
    private final String name;

    /** Значение записи. */
    private final String value;

    /** Создание экземпляра через {@link #newInstance(String, String)} */
    private OperationStatus(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    /** Возвращает новый экземпляр. */
    public static OperationStatus newInstance(final String name, final String value) {
        return new OperationStatus(name, value);
    }

    /** Название записи. */
    public final String getName() {
        return name;
    }

    /** Значение записи. */
    public final String getValue() {
        return value;
    }

    /** Вывод в строку таблицы html. */
    @Override
    public String toString() {
        return String.format(TO_HTML_TBL_FORMAT, name, value);
    }
}
