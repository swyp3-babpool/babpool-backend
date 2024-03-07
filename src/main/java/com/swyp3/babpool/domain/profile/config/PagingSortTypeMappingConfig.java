package com.swyp3.babpool.domain.profile.config;

import com.swyp3.babpool.domain.profile.domain.ProfileSortType;
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
                        String convertToDatabaseColumnNameForSorting = order.getProperty();
                        switch (order.getProperty()) {
                            case "NickName":
                                convertToDatabaseColumnNameForSorting = ProfileSortType.NickName.getColumnName();
                                break;
                            case "NewestPofile":
                                convertToDatabaseColumnNameForSorting = ProfileSortType.NewestPofile.getColumnName();
                                break;
                            case "NewestReview":
                                convertToDatabaseColumnNameForSorting = ProfileSortType.NewestReview.getColumnName();
                                break;
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
