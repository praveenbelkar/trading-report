package com.abnamro.report.streaming;

import java.util.ArrayList;
import java.util.List;

public class TradingRecordTestFixture {

    public static final String RECORD_1 = "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012380     688032000092500000000             O";
    public static final String RECORD_2 = "315CL  123400020001SGXDC FUSGX NK    20100910JPY01B 0000000002 0000000000000000000120DUSD000000000060DUSD000000000000DJPY201008200041520     782152000092150000000             O";
    public static final String KEY1 = "CL-4321-0002-0001,SGX-FU-NK-20100910";
    public static final String KEY2 = "CL-1234-0002-0001,SGX-FU-NK-20100910";

    public static List<String> getTestData() {
        List<String> dataList = new ArrayList<>();
        dataList.add(RECORD_1);
        dataList.add(RECORD_1);
        dataList.add(RECORD_1);
        dataList.add(RECORD_2);
        dataList.add(RECORD_2);
        return dataList;
    }
}
