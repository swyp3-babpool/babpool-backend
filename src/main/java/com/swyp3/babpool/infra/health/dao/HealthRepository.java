package com.swyp3.babpool.infra.health.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthRepository {

    Integer countAll();

}
