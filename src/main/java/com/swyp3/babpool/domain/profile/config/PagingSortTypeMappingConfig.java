package com.swyp3.babpool.domain.profile.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
public class PagingSortTypeMappingConfig implements WebMvcConfigurer {

    private static final String DEFAULT_PROFILE_MODIFY_DATE = "profile_modify_date";
    private static final String NAME = "user_name";

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        SortHandlerMethodArgumentResolver sortResolver = new SortHandlerMethodArgumentResolver();
        resolvers.add(new PageableHandlerMethodArgumentResolver(sortResolver) {
            @Override
            public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
                Sort newSort = Sort.unsorted();
                Sort currentSort = pageable.getSort();

                if (currentSort != null && !currentSort.isEmpty()) {
                    for (Sort.Order order : currentSort) {
                        String convertToDatabaseColumnNameForSorting = null;
                        switch (order.getProperty()) {
                            case "이름순":
                                convertToDatabaseColumnNameForSorting = NAME;
                                break;
                            default:
                                convertToDatabaseColumnNameForSorting = DEFAULT_PROFILE_MODIFY_DATE;
                        }

                        Sort.Direction direction = order.getDirection();
                        newSort = Sort.by(Sort.Order.by(convertToDatabaseColumnNameForSorting).with(direction));
                    }
                }
                return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);
            }
        });
    }
}
