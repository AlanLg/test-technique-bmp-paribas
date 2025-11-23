package me.alan.reconciliationtransactionsfinancieres;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.net.URI;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ReconciliationTransactionsFinancieresApplicationTests {

//    private static final String DATE_REGEX = "^\\d{4}-([0-9]{2})-(0[1-9]|[12][0-9]|3[01])T([0-9]{2}):([0-5][0-9])(?::([0-5][0-9]))?$";
//
//    @Test
//    @SneakyThrows
//    void checkValidJsonFile() {
//        final URI resourcePath = Objects.requireNonNull(ReconciliationTransactionsFinancieresApplication.class.getResource("/transactions.json")).toURI();
//        final File source = new File(resourcePath);
//
//        final JsonParser parser = new JsonFactory().createParser(source);
//
//        while (parser.nextToken() != JsonToken.END_ARRAY) {
//        for (int i = 0; i < 20; i++) {
//            final JsonToken jsonToken = parser.getCurrentToken();
//
//            if (jsonToken != JsonToken.FIELD_NAME) {
//                continue;
//            }
//
//            final String fieldName = parser.getText();
//
//            parser.nextToken();
//
//            final JsonToken nextJsonToken = parser.getCurrentToken();
//
//            if (nextJsonToken != JsonToken.VALUE_STRING) {
//                continue;
//            }
//
//            final String value = parser.getText();
//            System.out.println(fieldName + ": " + value);
//
//            if ("date".equals(fieldName)) {
//                assertTrue(value.matches(DATE_REGEX));
//            }
//        }
//        parser.close();
//    }
//
}
