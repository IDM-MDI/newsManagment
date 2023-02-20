package ru.clevertec.newsmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;
import ru.clevertec.newsmanagement.service.CommentService;

import static ru.clevertec.newsmanagement.util.DtoUtil.toJson;
import static ru.clevertec.newsmanagement.util.JwtSecurityUtil.getUsernameByContext;
import static ru.clevertec.newsmanagement.util.QueryParameterUtil.getCommentByQuery;


/**
 * This class represents the API endpoints for managing news comments. It handles GET, POST, PUT, and DELETE requests
 * related to news comments. The controller uses CommentService for managing comment data and provides various API
 * points for fetching, searching, saving, updating, and deleting comments.
 * @author Dayanch
 */
@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    /**
     * GET endpoint for retrieving comments associated with a news post.
     * @param news The ID of the news post to retrieve comments for.
     * @param page The page number of comments to retrieve. Default is 0.
     * @param size The number of comments per page to retrieve. Default is 10.
     * @param filter The field to sort comments by. Default is "id".
     * @param direction The direction to sort comments. "asc" for ascending or "desc" for descending. Default is "asc".
     * @return A JSON string representation of the comments for the specified news post.
     * @throws CustomException If the news post or comments cannot be found.
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
                                            @Parameter(description = "Page number(def: 0,min: 0)")
                                                @RequestParam(defaultValue = "0") @Min(0) int page,
                                            @Parameter(description = "Page size(def: 10, min: 1)")
                                                @RequestParam(defaultValue = "10") @Min(1) int size,
                                            @Parameter(description = "Filter by field(def: id)")
                                                @RequestParam(defaultValue = "id") @NotBlank String filter,
                                            @Parameter(description = "asc or desc(def: asc)")
                                                @RequestParam(defaultValue = "asc") @NotBlank String direction) throws CustomException {
        return toJson(service.findComments(news,page,size,filter,direction));
    }


    /**
     * GET endpoint for retrieving a comment associated with a news post by its ID.
     * @param news The ID of the news post to retrieve the comment from.
     * @param id The ID of the comment to retrieve.
     * @return A JSON string representation of the comment.
     * @throws CustomException If the news post or comment cannot be found.
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
                                  @Parameter(description = "Comment ID") @PathVariable @Min(1) long id) throws CustomException {
        return toJson(service.findComments(news,id));
    }


    /**
     * GET endpoint for searching comments associated with a news post.
     * @param news The ID of the news post to search comments in.
     * @param request The HTTP request containing query parameters for the search.
     * @return A JSON string representation of the comments matching the search criteria.
     * @throws CustomException If the news post or comments cannot be found.
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
                                 HttpServletRequest request) throws CustomException {
        return toJson(service.findComments(news, getCommentByQuery(request.getParameterMap())));
    }


    /**
     * Saves a comment for a news ID.
     * @param news the ID of the news article for which the comment is being saved
     * @param comment the comment to be saved
     * @return a JSON representation of the saved comment
     * @throws CustomException if there is an error while saving the comment
     */
    @PostMapping(value = "/{news}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
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
                                   @RequestBody @Valid DTO.Comment comment) throws CustomException {
        return toJson(service.saveComment(news,getUsernameByContext(),comment));
    }


    /**
     * Update News Comment.
     * API Point made for updating a news comment.
     *
     * @param news ID of the news to which the comment belongs.
     * @param id ID of the comment to be updated.
     * @param comment DTO representing the updated comment.
     * @return JSON representation of the updated comment.
     * @throws CustomException if the news or comment is not found, or the user is not authorized to update the comment.
     */
    @PutMapping(value = "/{news}/comment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
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
                              @RequestBody @Valid DTO.Comment comment) throws CustomException {
        return toJson(service.updateComment(news,id,getUsernameByContext(),comment));
    }


    /**
     * Delete News Comment.
     * API Point made for deleting a news comment.
     *
     * @param news ID of the news to which the comment belongs.
     * @param id ID of the comment to be deleted.
     * @return HTTP response entity with a success message if the comment was deleted.
     * @throws CustomException if the news or comment is not found, or the user is not authorized to delete the comment.
     */
    @DeleteMapping(value = "/{news}/comment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
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
                                                @Parameter(description = "Comment ID") @PathVariable @Min(1) long id) throws CustomException {
        service.deleteComment(id,news,getUsernameByContext());
        return ResponseEntity.ok("The comment successfully was deleted");
    }
}
