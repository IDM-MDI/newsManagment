package ru.clevertec.newsmanagement.handler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.clevertec.newsmanagement.exception.CustomException;

import java.util.List;
import java.util.Objects;

import static ru.clevertec.newsmanagement.exception.ExceptionStatus.EMPTY_PAGE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListHandler {
    public static void checkPageListExist(List<?> page) throws CustomException {
        if(Objects.isNull(page) || page.isEmpty()) {
            throw new CustomException(EMPTY_PAGE.toString());
        }
    }
 }
