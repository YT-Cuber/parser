//package org.ytcuber.parser;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//public class ParseSite {
//
//    public static void main(String[] args) {
//        try {
//            // Указываем URL-адрес веб-страницы
//            String url = "https://newlms.magtu.ru/course/view.php?id=26619&section=5";
//
//            // Подключаемся к веб-странице и получаем документ
//            Document document = Jsoup.connect(url).get();
//
//            // Извлекаем все элементы с веб-страницы
//            Elements elements = document.body().select("*");
//
//            boolean startPrinting = false;
//
//            for (Element element : elements) {
//                // Если мы находим строку "СПО - ОЧНАЯ ФОРМА", начинаем выводить ссылки
//                if (element.text().equals("СПО - ОЧНАЯ ФОРМА")) {
//                    startPrinting = true;
//                }
//
//                // Если мы находим строку "ДОПы", прекращаем выводить ссылки
//                if (element.text().equals("ДОПы")) {
//                    break;
//                }
//
//                // Если мы начали выводить ссылки, выводим все ссылки после строки "СПО - ОЧНАЯ ФОРМА"
//                if (startPrinting && element.tagName().equals("a")) {
//                    System.out.println(new String(element.text().getBytes(StandardCharsets.UTF_8))); //Названия
//                    System.out.println(element.attr("href")); //Ссылки
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
//
//
