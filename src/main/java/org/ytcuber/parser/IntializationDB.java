package org.ytcuber.parser;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.model.Lesson;
import org.ytcuber.repository.LessonRepository;
import org.ytcuber.types.DayOfWeek;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class IntializationDB {

    @Autowired
    private LessonRepository lessonRepository;

    @PostConstruct
    public void dataBase() {

        // Нечётная неделя
        // Понедельник
        Lesson lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(ТО) МДК.11.01 Технол.разработки и защиты баз данных");
        lesson.setSubgroup(0);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А02");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(Лаб) МДК.11.01 Технол.разработки и защиты баз данных");
        lesson.setSubgroup(1);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А403");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(Лаб) Системное программирование ");
        lesson.setSubgroup(2);
        lesson.setTeacher("Тутарова В.Д.");
        lesson.setLocation("А207");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setOrdinal(6);
        lesson.setSubject("(КП) МДК.11.01 Технолог.разр.и защиты баз данных ");
        lesson.setSubgroup(2);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А405");

        lessonRepository.save(lesson);

        // Вторник
        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(1);
        lesson.setSubject("(Лаб) МДК.11.01 Технол.разработки и защиты баз данных");
        lesson.setSubgroup(2);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А311");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(2);
        lesson.setSubject("(Лаб) Прогр.в среде 1С Предприятие");
        lesson.setSubgroup(2);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Лаб) МДК.11.01 Технол.разработки и защиты баз данных");
        lesson.setSubgroup(1);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А405");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Лаб) МДК.02.01 Технол.разработки ПО");
        lesson.setSubgroup(2);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(Лаб) МДК.02.01 Технол.разработки ПО");
        lesson.setSubgroup(1);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(Лаб) МДК.11.01 Технол.разработки и защиты баз данных");
        lesson.setSubgroup(2);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А405");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(КП) МДК.11.01 Технолог.разр.и защиты баз данных");
        lesson.setSubgroup(1);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А403");

        lessonRepository.save(lesson);

        // Среда
        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.WEDNESDAY);
        lesson.setOrdinal(2);
        lesson.setSubject("(Лаб) МДК.02.02 Инстр.средст.разраб.ПО");
        lesson.setSubgroup(1);
        lesson.setTeacher("Аитбаев Д.А.");
        lesson.setLocation("А407");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.WEDNESDAY);
        lesson.setOrdinal(2);
        lesson.setSubject("(Лаб) МДК.02.03 Матем.моделирование");
        lesson.setSubgroup(2);
        lesson.setTeacher("Смоленцов В.В.");
        lesson.setLocation("А105");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.WEDNESDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Лаб) Прогр.в среде 1С Предприятие");
        lesson.setSubgroup(1);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.WEDNESDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Лаб) МДК.02.02 Инстр.средст.разраб.ПО");
        lesson.setSubgroup(2);
        lesson.setTeacher("Аитбаев Д.А.");
        lesson.setLocation("А407");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.WEDNESDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(ТО) Прогр.в среде 1С Предприятие");
        lesson.setSubgroup(0);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.WEDNESDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(Лаб) Системное программирование ");
        lesson.setSubgroup(1);
        lesson.setTeacher("Тутарова В.Д.");
        lesson.setLocation("А207");

        lessonRepository.save(lesson);

        // Четверг
        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.THURSDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Лаб) Системное программирование ");
        lesson.setSubgroup(1);
        lesson.setTeacher("Тутарова В.Д.");
        lesson.setLocation("А207");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.THURSDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Лаб) МДК.01.03 Разработка мобил.прил.");
        lesson.setSubgroup(2);
        lesson.setTeacher("Чернега В.А.");
        lesson.setLocation("А305");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.THURSDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(ТО) Системное программирование ");
        lesson.setSubgroup(0);
        lesson.setTeacher("Тутарова В.Д.");
        lesson.setLocation("У402");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.THURSDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(Лаб) МДК.01.03 Разработка мобил.прил.");
        lesson.setSubgroup(1);
        lesson.setTeacher("Чернега В.А.");
        lesson.setLocation("А305");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.THURSDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(Лаб) Системное программирование ");
        lesson.setSubgroup(1);
        lesson.setTeacher("Тутарова В.Д.");
        lesson.setLocation("А207");

        lessonRepository.save(lesson);

        // Пятница
        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.FRIDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("Ин.яз (проф.)");
        lesson.setSubgroup(1);
        lesson.setTeacher("Шеметова М.С. ");
        lesson.setLocation("М210");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.FRIDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(Конс) МДК.02.01 Технология разработки ПО");
        lesson.setSubgroup(0);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.FRIDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("Россия - мои горизонты");
        lesson.setSubgroup(0);
        lesson.setTeacher("Утралинова С.М.");
        lesson.setLocation("А112");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.FRIDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(ТО) МДК.02.03 Матем.моделирование");
        lesson.setSubgroup(0);
        lesson.setTeacher("Смоленцов В.В.");
        lesson.setLocation("А112");

        lessonRepository.save(lesson);

        // Суббота
        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.SATURDAY);
        lesson.setOrdinal(2);
        lesson.setSubject("Физкультура");
        lesson.setSubgroup(0);
        lesson.setTeacher("Шунин В.С.");
        lesson.setLocation("С05");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.SATURDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(ТО) МДК.01.03 Разработка мобил.прил.");
        lesson.setSubgroup(0);
        lesson.setTeacher("Чернега В.А.");
        lesson.setLocation("А02");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.SATURDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(Лаб) МДК.02.03 Матем.моделирование");
        lesson.setSubgroup(1);
        lesson.setTeacher("Смоленцов В.В.");
        lesson.setLocation("А408");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(1);
        lesson.setDayOfWeek(DayOfWeek.SATURDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(Лаб) МДК.01.03 Разработка мобил.прил.");
        lesson.setSubgroup(2);
        lesson.setTeacher("Чернега В.А.");
        lesson.setLocation("А305");

        lessonRepository.save(lesson);

        // Чётная неделя
        // Понедельник
        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setOrdinal(2);
        lesson.setSubject("(Лаб) МДК.01.03 Разработка мобил.прил.");
        lesson.setSubgroup(2);
        lesson.setTeacher("Чернега В.А.");
        lesson.setLocation("А305");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Конс) МДК.11.01 Технол. разработки и защиты баз данных");
        lesson.setSubgroup(0);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А02");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(Лаб) МДК.11.01 Технол.разработки и защиты баз данных");
        lesson.setSubgroup(1);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А405");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("Ин.яз (проф.)");
        lesson.setSubgroup(2);
        lesson.setTeacher("Ческидова О.А.");
        lesson.setLocation("У208");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(Лаб) МДК.01.03 Разработка мобил.прил.");
        lesson.setSubgroup(1);
        lesson.setTeacher("Чернега В.А.");
        lesson.setLocation("А305");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(Лаб) МДК.11.01 Технол.разработки и защиты баз данных");
        lesson.setSubgroup(2);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А405");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.MONDAY);
        lesson.setOrdinal(6);
        lesson.setSubject("(КП) МДК.11.01 Технолог.разр.и защиты баз данных ");
        lesson.setSubgroup(1);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А405");

        lessonRepository.save(lesson);

        // Вторник
        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(1);
        lesson.setSubject("(Лаб) Системное программирование ");
        lesson.setSubgroup(1);
        lesson.setTeacher("Тутарова В.Д.");
        lesson.setLocation("А207");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(1);
        lesson.setSubject("(Лаб) МДК.11.01 Технол.разработки и защиты баз данных");
        lesson.setSubgroup(2);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А407");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(2);
        lesson.setSubject("(Лаб) МДК.02.01 Технол.разработки ПО");
        lesson.setSubgroup(1);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(2);
        lesson.setSubject("(Лаб) МДК.11.01 Технол.разработки и защиты баз данных");
        lesson.setSubgroup(2);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А407");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Лаб) МДК.11.01 Технол.разработки и защиты баз данных");
        lesson.setSubgroup(1);
        lesson.setTeacher("Мазнина Ю.А.");
        lesson.setLocation("А309");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Лаб) МДК.02.01 Технол.разработки ПО");
        lesson.setSubgroup(2);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.TUESDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("Физкультура");
        lesson.setSubgroup(0);
        lesson.setTeacher("Шунин В.С.");
        lesson.setLocation("С04");

        lessonRepository.save(lesson);

        // Среда
        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.WEDNESDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Лаб) МДК.01.03 Разработка мобил.прил.");
        lesson.setSubgroup(1);
        lesson.setTeacher("Чернега В.А.");
        lesson.setLocation("А305");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.WEDNESDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(Конс) МДК.01.03 Разработка мобильных приложений");
        lesson.setSubgroup(0);
        lesson.setTeacher("Чернега В.А.");
        lesson.setLocation("А305");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.WEDNESDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("Россия - мои горизонты");
        lesson.setSubgroup(0);
        lesson.setTeacher("Утралинова С.М.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        // Четверг
        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.THURSDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(ТО) МДК.02.02 Инстр.средст.разраб.ПО");
        lesson.setSubgroup(0);
        lesson.setTeacher("Аитбаев Д.А.");
        lesson.setLocation("А02");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.THURSDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(ТО) МДК.02.01 Технол.разработки ПО");
        lesson.setSubgroup(0);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.THURSDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(Конс) Системное программирование ");
        lesson.setSubgroup(0);
        lesson.setTeacher("Тутарова В.Д.");
        lesson.setLocation("А207");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.THURSDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(Лаб) Прогр.в среде 1С Предприятие");
        lesson.setSubgroup(1);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.THURSDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(Лаб) Системное программирование ");
        lesson.setSubgroup(2);
        lesson.setTeacher("Тутарова В.Д.");
        lesson.setLocation("А207");

        lessonRepository.save(lesson);

        // Пятница
        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.FRIDAY);
        lesson.setOrdinal(2);
        lesson.setSubject("Ин.яз (проф.)");
        lesson.setSubgroup(1);
        lesson.setTeacher("Шеметова М.С.");
        lesson.setLocation("А012");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.FRIDAY);
        lesson.setOrdinal(2);
        lesson.setSubject("(Лаб) Прогр.в среде 1С Предприятие");
        lesson.setSubgroup(2);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.FRIDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Конс) Прогр.в среде 1С Предприятие");
        lesson.setSubgroup(0);
        lesson.setTeacher("Фетисова Л.А.");
        lesson.setLocation("А205");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.FRIDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("Час общения");
        lesson.setSubgroup(0);
        lesson.setTeacher("Утралинова С.М.");
        lesson.setLocation("");

        lessonRepository.save(lesson);

        // Суббота
        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.SATURDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("(Лаб) МДК.02.03 Матем.моделирование");
        lesson.setSubgroup(1);
        lesson.setTeacher("Смоленцов В.В.");
        lesson.setLocation("А406");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.SATURDAY);
        lesson.setOrdinal(3);
        lesson.setSubject("Ин.яз (проф.)");
        lesson.setSubgroup(2);
        lesson.setTeacher("Ческидова О.А.");
        lesson.setLocation("У208");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.SATURDAY);
        lesson.setOrdinal(4);
        lesson.setSubject("(Конс) МДК.02.03 Матем.моделирование");
        lesson.setSubgroup(0);
        lesson.setTeacher("Смоленцов В.В.");
        lesson.setLocation("А406");

        lessonRepository.save(lesson);

        lesson = new Lesson();

        lesson.setOdd(2);
        lesson.setDayOfWeek(DayOfWeek.SATURDAY);
        lesson.setOrdinal(5);
        lesson.setSubject("(Лаб) МДК.02.03 Матем.моделирование");
        lesson.setSubgroup(2);
        lesson.setTeacher("Смоленцов В.В.");
        lesson.setLocation("А406");

        lessonRepository.save(lesson);
    }
}
