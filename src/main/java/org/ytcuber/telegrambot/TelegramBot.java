package org.ytcuber.telegrambot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.ytcuber.database.dto.LessonDTO;
import org.ytcuber.database.dto.ReplacementDTO;
import org.ytcuber.database.repository.GroupRepository;
import org.ytcuber.database.types.DayOfWeek;
import org.ytcuber.handler.GroupSchedule;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private GroupRepository groupRepository;
    private GroupSchedule groupSchedule;
    @Autowired
    public void ApplicationInitializer(GroupSchedule groupSchedule, GroupRepository groupRepository) {
        this.groupSchedule = groupSchedule;
        this.groupRepository = groupRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final Map<String, String> userSelections = new HashMap<>(); // Хранение состояния пользователей

    @Value("${telegrambots.token}")
    private String botToken;

    @Value("${telegrambots.username}")
    private String botUsername;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private enum UserState {
        NONE, // Состояние по умолчанию
        WAITING_FOR_GROUP, // Ожидание ввода группы
        WAITING_FOR_SUBGROUP // Ожидание ввода подгруппы
    }

    private static class UserSession {
        String groupName;
        Integer subgroup;
        UserState state;

        public UserSession() {
            this.state = UserState.NONE;
        }
    }

    private final Map<String, UserSession> userSessions = new HashMap<>(); // Сессии пользователей

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String userMessage = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            UserSession userSession = userSessions.computeIfAbsent(chatId, k -> new UserSession()); // Получаем или создаём новую сессию
            SendMessage message = new SendMessage();
            message.setChatId(chatId);

            logger.info("Received message: {}", userMessage);

            // Глобальная обработка команд
            switch (userMessage.toLowerCase()) {
                case "/start" -> {
                    message.setText("Добро пожаловать в бота MpK_Mgn_Bot! Выберите одну из команд ниже.");
                    message.setReplyMarkup(createMainKeyboard()); // Устанавливаем основную клавиатуру
                    userSession.state = UserState.NONE;
                }
                case "расписание" -> {
                    message.setText("Введите название группы.");
                    userSession.state = UserState.WAITING_FOR_GROUP; // Состояние ожидания группы
                }
                case "преподаватель" -> {
                    message.setText("Введите фамилию преподавателя или выберите другую команду.");
                    message.setReplyMarkup(createMainKeyboard()); // Подставляем клавиатуру
                }
                case "кабинет" -> {
                    message.setText("Укажите номер кабинета для получения информации.");
                    message.setReplyMarkup(createMainKeyboard());
                }
                case "уведомления" -> {
                    message.setText("Здесь вы можете управлять уведомлениями.");
                    message.setReplyMarkup(createMainKeyboard());
                }
                case "справка" -> {
                    message.setText("Это справочный раздел.");
                    message.setReplyMarkup(createMainKeyboard());
                }
                case "отмена" -> {
                    message.setText("Операция отменена.");
                    message.setReplyMarkup(createMainKeyboard());
                    userSession.state = UserState.NONE;
                }
                default -> handleStatefulCommands(userMessage, message, userSession); // Обработка состояний пользователя
            }

            try {
                execute(message); // Отправка сообщения
                logger.info("Message sent: {}", message.getText());
            } catch (TelegramApiException e) {
                logger.error("Ошибка при отправке сообщения", e);
            }
        }
    }

    private void handleStatefulCommands(String userMessage, SendMessage message, UserSession userSession) {
        switch (userSession.state) {
            case WAITING_FOR_GROUP -> handleGroupInput(userMessage, message, userSession); // Обработка ввода группы
            case WAITING_FOR_SUBGROUP -> handleSubgroupInput(userMessage, message, userSession); // Обработка ввода подгруппы
            default -> {
                message.setText("Неизвестная команда. Используйте кнопки на клавиатуре.");
                message.setReplyMarkup(createMainKeyboard());
            }
        }
    }


    private void handleGroupInput(String userMessage, SendMessage message, UserSession userSession) {
        try {
            Integer groupId = groupRepository.findByName(userMessage);
            if (groupId != null) {
                userSession.groupName = userMessage;
                userSession.state = UserState.WAITING_FOR_SUBGROUP;
                message.setText("Группа найдена. \nПожалуйста, укажите номер подгруппы (1 или 2).");
                message.setReplyMarkup(createSubgroupKeyboard());
            } else {
                message.setText("Группа не найдена. Попробуйте ещё раз.");
            }
        } catch (Exception e) {
            logger.error("Ошибка при проверке группы", e);
            message.setText("Произошла ошибка при проверке группы.");
        }
    }

    private void handleSubgroupInput(String userMessage, SendMessage message, UserSession userSession) {
        if ("1".equals(userMessage) || "2".equals(userMessage)) {
            userSession.subgroup = Integer.parseInt(userMessage);
            userSession.state = UserState.NONE;
            try {
                // Опеределяем четность недели
                boolean isEvenWeek = isEvenWeek();
                int weekOdd = isEvenWeek ? 2 : 1;
                // Получаем расписание
                List<Object> schedule = groupSchedule.giveSchedule(userSession.groupName, userSession.subgroup, weekOdd);
                String scheduleText = buildScheduleText(schedule, userSession.groupName, userSession.subgroup);
                message.setText(scheduleText);
                message.setReplyMarkup(createMainKeyboard());
            } catch (Exception e) {
                logger.error("Ошибка при получении расписания", e);
                message.setText("Произошла ошибка при получении расписания.");
            }
        } else {
            message.setText("Пожалуйста, введите 1 или 2 для выбора подгруппы.");
        }
    }

    private String buildScheduleText(List<Object> schedule, String groupName, int subgroup) {
        Map<DayOfWeek, List<Object>> scheduleByDay = new LinkedHashMap<>();
        for (Object item : schedule) {
            DayOfWeek dayOfWeek = null;
            if (item instanceof LessonDTO lesson) {
                dayOfWeek = DayOfWeek.valueOf(String.valueOf(lesson.getDayOfWeek()));
            } else if (item instanceof ReplacementDTO replacement) {
                dayOfWeek = DayOfWeek.valueOf(String.valueOf(replacement.getDatOfWeek()));
            }
            if (dayOfWeek != null) {
                scheduleByDay.computeIfAbsent(dayOfWeek, k -> new ArrayList<>()).add(item);
            }
        }

        StringBuilder scheduleText = new StringBuilder();
        scheduleText.append(String.format("📚 Расписание для группы %s подгруппы %d:\n\n", groupName, subgroup));

        for (Map.Entry<DayOfWeek, List<Object>> entry : scheduleByDay.entrySet()) {
            DayOfWeek dayOfWeek = entry.getKey();
            List<Object> daySchedule = entry.getValue();

            // Получаем русское название дня недели
            String dayOfWeekName = dayOfWeek.label;

            scheduleText.append(String.format("📅 %s:\n", dayOfWeekName));

            for (Object item : daySchedule) {
                if (item instanceof LessonDTO lesson) {
                    scheduleText.append(String.format(
                            "%d. %s 🎓%s 🚪%s\n",
                            lesson.getOrdinal() != null ? lesson.getOrdinal() : 0,
                            lesson.getSubject() != null ? lesson.getSubject() : "Пара отменена ❌",
                            lesson.getTeacher() != null ? lesson.getTeacher() : "Не указано",
                            lesson.getLocation() != null ? lesson.getLocation() : "Не указано"
                    ));
                } else if (item instanceof ReplacementDTO replacement) {
                    scheduleText.append(String.format(
                            "✏ %d. %s 🎓%s 🚪%s\n",
                            replacement.getOrdinal() != null ? replacement.getOrdinal() : 0,
                            replacement.getSubject() != null ? replacement.getSubject() : "Пара отменена ❌",
                            replacement.getTeacher() != null ? replacement.getTeacher() : "Не указано",
                            replacement.getLocation() != null ? replacement.getLocation() : "Не указано"
                    ));
                }
            }

            scheduleText.append("\n");
        }
        return scheduleText.toString();
    }


    private ReplyKeyboardMarkup createMainKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // Автоматическая подгонка клавиатуры под экран

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        // Первая строка с кнопками
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Расписание"));
        row1.add(new KeyboardButton("Преподаватель"));

        // Вторая строка с кнопками
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Кабинет"));
        row2.add(new KeyboardButton("Уведомления"));

        // Третья строка с кнопкой "Справка"
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("Справка"));

        // Добавляем строки в клавиатуру
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);

        keyboardMarkup.setKeyboard(keyboardRows);

        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup createDateKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // Автоматическая подгонка клавиатуры под экран

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        // Первая строка с кнопками
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Всё доступное"));

        // Вторая строка с кнопками
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Сегодня"));
        row2.add(new KeyboardButton("Завтра"));

        // Третья строка с кнопкой "Отмена"
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("Отмена"));

        // Добавляем строки в клавиатуру
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);

        keyboardMarkup.setKeyboard(keyboardRows);

        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup createSubgroupKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // Автоматическая подгонка клавиатуры под экран

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("1"));
        row1.add(new KeyboardButton("2"));

        keyboardRows.add(row1);

        keyboardMarkup.setKeyboard(keyboardRows);

        return keyboardMarkup;
    }

    public static boolean isEvenWeek() {
        LocalDate today = LocalDate.now();

        // Если сегодня воскресенье, сдвигаем на один день вперед
        if (today.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
            today = today.plusDays(1);
        }

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = today.get(weekFields.weekOfWeekBasedYear());
        return weekNumber % 2 == 0;
    }
}