package ru.clevertec.newsmanagement.newsservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.newsservice.exception.CustomException;

import java.util.List;

import static ru.clevertec.newsmanagement.newsservice.exception.ExceptionStatus.EMPTY_PAGE;

/**
 This class is an aspect that handles pagination of page results returned by specific service methods.
 */
@Aspect
@Component
@Slf4j
public class PageAspect {
    /**
     * This advice runs after the successful execution of specific service methods that return paginated results,
     * and checks whether the returned list is not null and has a size greater than 0.
     * @param result the list of paginated results returned by the advised method
     * @throws {@link CustomException} if the returned list is null or has a size of 0
     */
    @AfterReturning(value =
            "execution(* ru.clevertec.newsmanagement.newsservice.service.impl.CommentServiceImpl.findComments(long,int,int,String,String)) " +
            "|| execution(* ru.clevertec.newsmanagement.newsservice.service.impl.CommentServiceImpl.findComments(long, ru.clevertec.newsmanagement.newsservice.model.DTO.Comment))" +
            "|| execution(* ru.clevertec.newsmanagement.newsservice.service.impl.NewsServiceImpl.findNews(int,int,String,String)) " +
            "|| execution(* ru.clevertec.newsmanagement.newsservice.service.impl.NewsServiceImpl.findNews(ru.clevertec.newsmanagement.newsservice.model.DTO.News))",
            returning = "result")
    public void afterCommentsPage(List<?> result) throws CustomException {
        if(CollectionUtils.isEmpty(result)) {
            throw new CustomException(EMPTY_PAGE.toString());
        }
    }
}
