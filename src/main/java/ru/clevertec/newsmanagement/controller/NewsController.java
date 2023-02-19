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
import ru.clevertec.newsmanagement.service.NewsService;

import static ru.clevertec.newsmanagement.util.DtoUtil.toJson;
import static ru.clevertec.newsmanagement.util.JwtSecurityUtil.getUsernameByContext;
import static ru.clevertec.newsmanagement.util.QueryParameterUtil.getNewsByQuery;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService service;

    @GetMapping
    @Operation(
            summary = "News",
            description = "API Point made for return news page"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News found"
    )
    public String findNews(@Parameter(description = "Page number(def: 0,min: 0)")
                                    @RequestParam(defaultValue = "0") @Min(0) int page,
                                  @Parameter(description = "Page size(def: 10, min: 1)")
                                    @RequestParam(defaultValue = "10") @Min(1) int size,
                                  @Parameter(description = "Filter by field(def: id)")
                                    @RequestParam(defaultValue = "id") @NotBlank String filter,
                                  @Parameter(description = "asc or desc(def: asc)")
                                    @RequestParam(defaultValue = "asc") @NotBlank String direction) throws CustomException {
        return toJson(service.findNews(page,size,filter,direction));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "News by ID",
            description = "API Point made for return news by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News found"
    )
    public String findNews(@PathVariable @Min(1) long id) throws CustomException {
        return toJson(service.findNews(id));
    }

    @GetMapping("/search")
    @Operation(
            summary = "News by search",
            description = "API Point made for return news by search"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News found"
    )
    public String findNews(@Parameter(description = "News search by")HttpServletRequest request) throws CustomException {
        return toJson(service.findNews(getNewsByQuery(request.getParameterMap())));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_JOURNALIST')")
    @Operation(
            summary = "Save News",
            description = "API Point made for saving news"
    )
    @ApiResponse(
            responseCode = "201",
            description = "News created"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public String saveNews(@RequestBody @Valid DTO.News news) throws CustomException {
        return toJson(service.saveNews(getUsernameByContext(),news));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_JOURNALIST')")
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
                              @RequestBody @Valid DTO.News news) throws CustomException {
        return toJson(service.updateNews(id,getUsernameByContext(),news));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_JOURNALIST')")
    @Operation(
            summary = "Delete News",
            description = "API Point made for deleting news"
    )
    @ApiResponse(
            responseCode = "200",
            description = "News deleted"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteNews(@PathVariable @Min(1) long id) throws CustomException {
        service.deleteNews(id,getUsernameByContext());
        return ResponseEntity.ok("The news successfully was deleted");
    }
}
