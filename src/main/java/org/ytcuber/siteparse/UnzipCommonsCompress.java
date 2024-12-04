//package org.ytcuber.siteparse;
//
//import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
//import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
//import org.springframework.stereotype.Component;
//
//import java.io.*;
//import java.util.zip.ZipException;
//
//@Component
//public class UnzipCommonsCompress {
//    public static void main(String[] args) throws IOException {
//        String zipFilePath = "zamena";
//        File zipFile = new File("./mainexcel/zip/" + zipFilePath + ".zip");
//        File destDir = new File("./replace/");
//
//        unzip(zipFile, destDir);
//        System.out.println("Распаковка завершена успешно!");
//    }
//
//    public static void unzip(File zipFile, File destDir) throws IOException {
//        if (!destDir.exists()) {
//            destDir.mkdirs();
//        }
//
//        try (ZipArchiveInputStream zipIn = new ZipArchiveInputStream(new FileInputStream(zipFile))) {
//            ZipArchiveEntry entry;
//            while ((entry = zipIn.getNextZipEntry()) != null) {
//                String filePath = destDir + File.separator + entry.getName();
//                if (entry.isDirectory()) {
//                    new File(filePath).mkdirs();
//                } else {
//                    extractFile(zipIn, filePath);
//                }
//            }
//        } catch (ZipException e) {
//            System.err.println("Ошибка при обработке ZIP-архива: " + e.getMessage());
//        }
//    }
//
//    private static void extractFile(ZipArchiveInputStream zipIn, String filePath) throws IOException {
//        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
//            byte[] bytesIn = new byte[4096];
//            int read;
//            while ((read = zipIn.read(bytesIn)) != -1) {
//                bos.write(bytesIn, 0, read);
//            }
//        }
//    }
//}
