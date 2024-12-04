//package org.ytcuber.siteparse;
//
//import jakarta.annotation.PostConstruct;
//import org.apache.hc.client5.http.cookie.BasicCookieStore;
//import org.apache.hc.client5.http.cookie.CookieStore;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.*;
//import java.net.URI;
//
//@Component
//public class FolderDownloader {
//
//    private static final String DOWNLOAD_URL = "https://newlms.magtu.ru/mod/folder/download_folder.php";
//    private static final String SAVE_PATH = "./mainexcel/replacement/downloaded_folder.zip"; // Путь для сохранения
//
//    @PostConstruct
//    private void init() {
//        System.out.println("Можно написать инициализацию, но я не буду");
//    }
//
//    public void downloadFolder() {
//        try {
//            // Формируем URI
//            URI uri = new URI(DOWNLOAD_URL + "?id=219250");
//
//            // Подготавливаем путь для сохранения файла
//            File file = new File(SAVE_PATH);
//            File parentDir = file.getParentFile();
//            if (!parentDir.exists() && !parentDir.mkdirs()) {
//                throw new RuntimeException("Не удалось создать директории: " + parentDir.getAbsolutePath());
//            }
//
//            // Настроим CookieStore для хранения cookies
//            CookieStore cookieStore = new BasicCookieStore();
//
//            // Создаем HttpClient с поддержкой cookieStore
//            CloseableHttpClient client = HttpClients.custom()
//                    .setDefaultCookieStore(cookieStore)
//                    .build();
//
//            // Оборачиваем HttpClient в фабрику для RestTemplate
//            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
//            RestTemplate restTemplate = new RestTemplate(factory);
//
//            // Выполняем запрос с использованием exchange()
//            ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, null, byte[].class);
//
//            // Получаем статус-код
//            int statusCode = response.getStatusCodeValue();
//            System.out.println("Status Code: " + statusCode);
//            System.out.println("Headers: " + response.getHeaders());
//
//            // Проверяем статус-код
//            if (statusCode != 200) {
//                throw new RuntimeException("Ошибка запроса. Код: " + statusCode);
//            }
//
//            // Получаем тип контента
//            String contentType = response.getHeaders().getContentType().toString();
//            System.out.println("Content-Type: " + contentType);
//
//            // Если тип не ZIP, сохраняем ответ для отладки
//            if (!contentType.equalsIgnoreCase("application/zip")) {
//                try (InputStream inputStream = response.getBody() != null ? new ByteArrayInputStream(response.getBody()) : null;
//                     OutputStream debugOutput = new FileOutputStream("./response_debug.html")) {
//
//                    if (inputStream != null) {
//                        byte[] buffer = new byte[1024];
//                        int bytesRead;
//                        while ((bytesRead = inputStream.read(buffer)) != -1) {
//                            debugOutput.write(buffer, 0, bytesRead);
//                        }
//                    }
//                }
//                throw new RuntimeException("Ошибка: Ожидался ZIP-архив, но получен " + contentType);
//            }
//
//            // Сохраняем ZIP-архив в файл
//            try (InputStream inputStream = new ByteArrayInputStream(response.getBody());
//                 OutputStream outputStream = new FileOutputStream(file)) {
//
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//            }
//
//            System.out.println("Папка успешно загружена в: " + file.getAbsolutePath());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
