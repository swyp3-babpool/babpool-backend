package com.swyp3.babpool.domain.profile.application.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@ToString
@Slf4j
@Getter
public class ProfileKeywordsResponse {
    private Map<String,String[]> keywords;

    public ProfileKeywordsResponse(String keywords) throws IOException {
        //JSON_expression을 사용하면 응답이 json 문자열로 반환돼서 바로 응답으로 보내면 이스케이프 처리가 된 채로 보내짐. "->\"
        //그래서 따로 처리가 필요
        ObjectMapper mapper = new ObjectMapper();
        // JSON 문자열을 Map<String, String[]> 타입으로 변환
        Map<String, String[]> map = mapper.readValue(keywords, new TypeReference<Map<String, String[]>>() {});
        this.keywords=map;
    }
}
