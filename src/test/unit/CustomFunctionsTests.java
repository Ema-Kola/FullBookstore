package test.unit;

import model.CustomFunctions;
import java.util.Date;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


public class CustomFunctionsTests {


    @ParameterizedTest(name = "Date({0}, {1}, {2})")
    @CsvSource({
            "15,    6,   9999, 15-06-9999",
            "15,    6,   0001, 15-06-0001",
            "15,    1,   1942, 15-01-1942",
            "15,   12,   1942, 15-12-1942",
            "1,     6,   1942, 01-06-1942",
            "31,    7,   1942, 31-07-1942",
            "30,    6,   1942, 30-06-1942",
            "28,    2,   2003, 28-02-2003",
            "29,    2,   2004, 29-02-2004",
    })
    public void testConvertDate_validDate(int d, int m, int y, String s) {
        Date date =  CustomFunctions.convertDate(s);
        Assertions.assertEquals(d, date.getDate());
        Assertions.assertEquals(m, date.getMonth()+1);
        Assertions.assertEquals(y-1900, date.getYear());

    }
    @ParameterizedTest(name = "Date({0}")
    @CsvSource({
            "15-06-0000",
            "15-06-10000",
            "15-00-1942",
            "15-13-1942",
            "31-06-1942",
            "32-07-1942",
            "00-06-1942",
            "29-02-2003",

    })
    public void testConvertDate_invalidDate(String date) {
        Assertions.assertNull(CustomFunctions.convertDate(date)); // Invalid day

    }
}
