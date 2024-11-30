package org.ytcuber.database.types;

public enum DayOfWeek {
    MONDAY ("Понедельник"),
    TUESDAY("Вторник"),
    WEDNESDAY("Среда"),
    THURSDAY("Четверг"),
    FRIDAY("Пятница"),
    SATURDAY("Суббота");

    private final String label;

    DayOfWeek(String label) {
        this.label = label;
    }

    public static DayOfWeek valueOfLabel(String label) {
        for (DayOfWeek dayOfWeek : values()) {
            if (dayOfWeek.label.equals(label))
                return dayOfWeek;
        }
        return null;
    }

}
