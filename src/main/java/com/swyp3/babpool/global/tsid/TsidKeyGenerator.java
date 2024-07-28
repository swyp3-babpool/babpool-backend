package com.swyp3.babpool.global.tsid;

import com.github.f4b6a3.tsid.TsidFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TsidKeyGenerator {

    @Value("${property.tsid.node}")
    private int nodeNumber;

    private final TsidFactory tsidFactory = new TsidFactory(nodeNumber);

    public long generateTsid() {
        return tsidFactory.create().toLong();
    }

    @PostConstruct
    public void init() {
        System.out.println("TsidKeyGenerator component is created");
    }

}
