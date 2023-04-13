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
import ru.clevertec.newsmanagement.newsservice.service.CommentService;
import ru.clevertec.newsmanagement.newsservice.util.QueryParameterUtil;

import static ru.clevertec.newsmanagement.exceptionservice.util.JsonUtil.toJson;
import static ru.clevertec.newsmanagement.newsservice.util.QueryParameterUtil.getCommentByQuery;


/**
 * This class represents the API endpoints for managing news comments. It handles GET, POST, PUT, and DELETE requests
 * related to news comments. The controller uses CommentService for managing comment data and provides various API
 * points for fetching, searching, saving, updating, and deleting comments.
 * @author Dayanch
 */
@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService service;

    /**
     * API Point made for returning news page.
     * @param page representation of pageable
     * @return A JSON representation of the comment found
     */
    @GetMapping(value = "/{news}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "News Comments",
            description = "API Point made for return comment page by news"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comments found"
    )
    public String getNewsComment(@Parameter(description = "News ID")
                                     @PathVariable long news,
                                 @Parameter(description = "Pageable")
                                 @Valid Pageable page) {
        return toJson(service.findComments(news,page));
    }


    /**
     * GET endpoint for retrieving a comment associated with a news post by its ID.
     * @param news The ID of the news post to retrieve the comment from.
     * @param id The ID of the comment to retrieve.
     * @return A JSON string representation of the comment.
     */
    @GetMapping(value = "/{news}/comment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "News Comment by ID",
            description = "API Point made for return comment by ID & news"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comments found"
    )
    public String getComment(@Parameter(description = "News ID") @PathVariable @Min(1) long news,
                                  @Parameter(description = "Comment ID") @PathVariable @Min(1) long id) {
        return toJson(service.findComments(news,id));
    }


    /**
     * GET endpoint for searching comments associated with a news post.
     * @param news The ID of the news post to search comments in.
     * @param request The HTTP request containing query parameters for the search.
     * @return A JSON string representation of the comments matching the search criteria.
     */
    @GetMapping(value = "/{news}/comment/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "News Comments by search",
            description = "API Point made for return comment by search query parameters"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comments found"
    )
    public String getComment(@Parameter(description = "News ID") @PathVariable long news,
                                 HttpServletRequest request) {
        return toJson(service.findComments(news, getCommentByQuery(request.getParameterMap())));
    }


    /**
     * Saves a comment for a news ID.
     * @param news the ID of the news article for which the comment is being saved
     * @param comment the comment to be saved
     * @return a JSON representation of the saved comment
     */
    @PostMapping(value = "/{news}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Save News Comment",
            description = "API Point made for saving news comment"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Comment created"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public String saveComment(@Parameter(description = "News ID") @PathVariable @Min(1) long news,
                                   @RequestBody @Valid DTO.Comment comment,
                              HttpServletRequest request) {
        return toJson(service.saveComment(news,comment, QueryParameterUtil.getUser(request)));
    }


    /**
     * Update News Comment.
     * API Point made for updating a news comment.
     *
     * @param news ID of the news to which the comment belongs.
     * @param id ID of the comment to be updated.
     * @param comment DTO representing the updated comment.
     * @return JSON representation of the updated comment.
     */
    @PutMapping(value = "/{news}/comment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update News Comment",
            description = "API Point made for updating news comment"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comment updated"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public String updateNews(@Parameter(description = "News ID") @PathVariable @Min(1) long news,
                                 @Parameter(description = "Comment ID") @PathVariable @Min(1) long id,
                              @RequestBody @Valid DTO.Comment comment,
                             HttpServletRequest request) {
        return toJson(service.updateComment(news,id,comment,QueryParameterUtil.getUser(request)));
    }


    /**
     * Delete News Comment.
     * API Point made for deleting a news comment.
     *
     * @param news ID of the news to which the comment belongs.
     * @param id ID of the comment to be deleted.
     * @return HTTP response entity with a success message if the comment was deleted.
     */
    @DeleteMapping(value = "/{news}/comment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Delete News Comment",
            description = "API Point made for deleting news comment"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comment deleted"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteComment(@Parameter(description = "News ID") @PathVariable @Min(1) long news,
                                                @Parameter(description = "Comment ID") @PathVariable @Min(1) long id,
                                                HttpServletRequest request) {
        service.deleteComment(id,news, QueryParameterUtil.getUser(request));
        return ResponseEntity.ok("The comment successfully was deleted");
    }
}
