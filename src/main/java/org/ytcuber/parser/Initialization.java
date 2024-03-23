package org.ytcuber.parser;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.model.Group;
import org.ytcuber.repository.GroupRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class Initialization {

    @Autowired
    private GroupRepository groupRepository;

    @PostConstruct
    public void init(){
        try {
            // Указываем URL-адрес веб-страницы
            Document doc = Jsoup.connect("https://newlms.magtu.ru/mod/folder/view.php?id=1223699").get();

            // Извлекаем все элементы с определенным селектором
            Elements elements = doc.select("a");

            boolean startPrinting = false;
            // Перебираем все извлеченные элементы и выводим их на экран
            for (Element element : elements) {
                String title = element.text(); // Название файла
                String link = element.absUrl("href"); // Ссылка на скачивание файла

                if (link.equals("https://newlms.magtu.ru/pluginfile.php/1936752/mod_folder/content/0/%D0%98%D0%A1%D0%BF%D0%92-20-1.xlsx?forcedownload=1")) {
                    startPrinting = true;
                }

                if (title.equals("◄ ОТДЕЛЕНИЕ № 1 «ОБЩЕОБРАЗОВАТЕЛЬНАЯ ПОДГОТОВКА»")) {
                    break;
                }

                if (startPrinting && !title.endsWith(".pdf")) {
                    System.out.println(new String(title.getBytes(StandardCharsets.UTF_8)));
                    // System.out.println("Ссылка: " + link);

                    // Скачиваем файл
                    try (InputStream in = new URL(link).openStream()) {
                        Files.copy(in, Path.of("C:/My_Space/More/Java/parser/mainexcel/squad2/" + title), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Group groups = new Group();
                    if (title.endsWith(".xlsx")) {
                        title = title.substring(0, title.length() - 5);
                    }
                    groups.setTitle(title);
                    groups.setSquad(2);

                    groupRepository.save(groups);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

