package com.swyp3.babpool.global.tsid;

import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.tsid.TsidFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Slf4j
class TsidGenerateTest {

    @Mock
    private TsidFactory tsidFactory;

    @InjectMocks
    private TsidKeyGenerator tsidKeyGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tsidKeyGenerator.init();
    }

    @Test
    void generateTsidForMigrateDB() {
        for(int i = 1; i <= 73; i++){
            long l = tsidKeyGenerator.generateTsid();
            System.out.println(l);
        }
    }

    @Test
    void generateTsidWithComponent(){
        long l = tsidKeyGenerator.generateTsid();
        log.info("TSID : {}", l);
        assertThat(l).isNotNull();
        assertThat(String.valueOf(l).length()).isGreaterThanOrEqualTo(18);
    }

}