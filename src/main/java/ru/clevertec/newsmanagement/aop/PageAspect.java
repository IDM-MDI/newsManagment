package ru.clevertec.newsmanagement.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.exception.CustomException;

import java.util.List;

import static ru.clevertec.newsmanagement.handler.ListHandler.checkPageListExist;

@Aspect
@Component
@Slf4j
public class PageAspect {
    @AfterReturning(value = "execution(* ru.clevertec.newsmanagement.service.impl.CommentServiceImpl.findComments(*)) " +
            "|| execution(* ru.clevertec.newsmanagement.service.impl.NewsServiceImpl.findNews(int,int,String,String))",
            returning = "result")
    public void afterCommentsPage(List<?> result) throws CustomException {
        checkPageListExist(result);
    }
}
