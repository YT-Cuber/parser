package org.ytcuber.parser;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.processing.Generated;

public class ExcelProperties {

    @Value("${excel.dir}")
    public static String dir;

}
