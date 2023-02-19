package ru.clevertec.newsmanagement.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.exception.CustomException;

import java.util.List;

import static ru.clevertec.newsmanagement.handler.DtoHandler.checkPageListExist;

@Aspect
@Component
@Slf4j
public class PageAspect {
    @AfterReturning(value =
            "execution(* ru.clevertec.newsmanagement.service.impl.CommentServiceImpl.findComments(long,int,int,String,String)) " +
            "|| execution(* ru.clevertec.newsmanagement.service.impl.CommentServiceImpl.findComments(long, ru.clevertec.newsmanagement.model.CommentDto))" +
            "|| execution(* ru.clevertec.newsmanagement.service.impl.NewsServiceImpl.findNews(int,int,String,String)) " +
            "|| execution(* ru.clevertec.newsmanagement.service.impl.NewsServiceImpl.findNews(ru.clevertec.newsmanagement.model.NewsDto))",
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
