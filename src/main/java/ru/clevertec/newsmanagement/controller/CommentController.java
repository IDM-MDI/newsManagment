package ru.clevertec.newsmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
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
import ru.clevertec.newsmanagement.model.CommentDto;
import ru.clevertec.newsmanagement.service.CommentService;

import java.util.List;

import static ru.clevertec.newsmanagement.handler.JwtSecurityHandler.getUsernameByContext;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @GetMapping("/{news}/comment")
    @Operation(
            summary = "News Comments",
            description = "API Point made for return comment page by news"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comments found"
    )
    public List<CommentDto> getNewsComment(@Parameter(description = "News ID")
                                               @PathVariable long news,
                                           @Parameter(description = "Page number(def: 0,min: 0)")
                                                @RequestParam(defaultValue = "0") @Min(0) int page,
                                           @Parameter(description = "Page size(def: 10, min: 1)")
                                                @RequestParam(defaultValue = "10") @Min(1) int size,
                                           @Parameter(description = "Filter by field(def: id)")
                                                @RequestParam(defaultValue = "id") @NotBlank String filter,
                                           @Parameter(description = "asc or desc(def: asc)")
                                                @RequestParam(defaultValue = "asc") @NotBlank String direction) throws CustomException {
        return service.findComments(news,page,size,filter,direction);
    }
    @GetMapping("/{news}/comment/{id}")
    @Operation(
            summary = "News Comment by ID",
            description = "API Point made for return comment by ID & news"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Comments found"
    )
    public CommentDto getComment(@Parameter(description = "News ID") @PathVariable @Min(1) long news,
                                 @Parameter(description = "Comment ID") @PathVariable @Min(1) long id) throws CustomException {
        return service.findComment(news,id);
    }

    @PostMapping("/{news}/comment")
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
    public CommentDto saveComment(@Parameter(description = "News ID") @PathVariable @Min(1) long news,
                                  @RequestBody @Valid CommentDto comment) throws CustomException {
        return service.saveComment(news,getUsernameByContext(),comment);
    }
    @PutMapping("/{news}/comment/{id}")
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
    public CommentDto updateNews(@Parameter(description = "News ID") @PathVariable @Min(1) long news,
                                 @Parameter(description = "Comment ID") @PathVariable @Min(1) long id,
                              @RequestBody @Valid CommentDto comment) throws CustomException {
        return service.updateComment(news,id,getUsernameByContext(),comment);
    }
    @DeleteMapping("/{news}/comment/{id}")
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
