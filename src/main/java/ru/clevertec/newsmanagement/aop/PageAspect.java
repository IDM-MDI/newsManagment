package ru.clevertec.newsmanagement.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.exception.CustomException;

import java.util.List;

import static ru.clevertec.newsmanagement.util.DtoUtil.checkPageListExist;

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
            "execution(* ru.clevertec.newsmanagement.service.impl.CommentServiceImpl.findComments(long,int,int,String,String)) " +
            "|| execution(* ru.clevertec.newsmanagement.service.impl.CommentServiceImpl.findComments(long, ru.clevertec.newsmanagement.model.DTO.Comment))" +
            "|| execution(* ru.clevertec.newsmanagement.service.impl.NewsServiceImpl.findNews(int,int,String,String)) " +
            "|| execution(* ru.clevertec.newsmanagement.service.impl.NewsServiceImpl.findNews(ru.clevertec.newsmanagement.model.DTO.News))",
            returning = "result")
    public void afterCommentsPage(List<?> result) throws CustomException {
        try {
            checkPageListExist(result);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getMessage());
        }
    }
}
