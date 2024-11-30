package org.ytcuber.parser;

import org.springframework.beans.factory.annotation.Value;

public class ExcelProperties {
    @Value("${excel.dir}")
    public static String dir;
}
