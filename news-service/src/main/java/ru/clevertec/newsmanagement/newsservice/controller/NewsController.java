package ru.clevertec.newsmanagement.newsservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsmanagement.newsservice.model.DTO;
import ru.clevertec.newsmanagement.newsservice.service.NewsService;

import static ru.clevertec.newsmanagement.exceptionservice.util.JsonUtil.toJson;
import static ru.clevertec.newsmanagement.newsservice.util.QueryParameterUtil.getNewsByQuery;
import static ru.clevertec.newsmanagement.newsservice.util.QueryParameterUtil.getUser;

/**
 * REST controller for News operations.
 * @author Dayanch
 */
@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Validated
public class NewsController {

    private final NewsService service;

    /**
     * API Point made for returning news page.
     * @param page representation of pageable
     * @return A JSON representation of the news found
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "News",
            description = "API Point made for return news page"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News found"
    )
    public String findNews(@Parameter(description = "Pageable") @Valid Pageable page) {
        return toJson(service.findNews(page));
    }



    /**
     * API Point made for returning news by ID.
     * @param id The ID of the news to retrieve (min: 1)
     * @return A JSON representation of the news found
     */
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "News by ID",
            description = "API Point made for return news by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News found"
    )
    public String findNews(@PathVariable @Min(1) long id) {
        return toJson(service.findNews(id));
    }


    /**
     * API Point made for returning news by search query.
     * @param request The HttpServletRequest containing the search query
     * @return A JSON representation of the news found
     */
    @GetMapping(value = "/search",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "News by search",
            description = "API Point made for return news by search"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News found"
    )
    public String findNews(@Parameter(description = "News search by")HttpServletRequest request) {
        return toJson(service.findNews(getNewsByQuery(request.getParameterMap())));
    }


    /**
     * API Point made for saving news.
     * @param news The DTO.News object representing the news to save
     * @return A JSON representation of the news saved
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Save News",
            description = "API Point made for saving news"
    )
    @ApiResponse(
            responseCode = "201",
            description = "News created"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public String saveNews(@RequestBody @Valid DTO.News news, HttpServletRequest request) {
        return toJson(service.saveNews(news,getUser(request)));
    }


    /**
     * Updates the news with the specified ID.
     *
     * @param id    the ID of the news to update
     * @param news  the new news data to update
     * @return      a JSON representation of the updated news
     */
    @PutMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update News",
            description = "API Point made for updating news"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News updated"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public String updateNews(@PathVariable @Min(1) long id,
                              @RequestBody @Valid DTO.News news,
                             HttpServletRequest request) {
        return toJson(service.updateNews(id,news, getUser(request)));
    }


    /**
     * Deletes the news with the specified ID.
     *
     * @param id    the ID of the news to delete
     * @return      a response entity indicating that the news was deleted successfully
     */
    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Delete News",
            description = "API Point made for deleting news"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News deleted"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteNews(@PathVariable @Min(1) long id, HttpServletRequest request) {
        service.deleteNews(id, getUser(request));
        return ResponseEntity.ok("The news successfully was deleted");
    }
}
