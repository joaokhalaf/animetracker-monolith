package br.com.animetracker.AniTracker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import br.com.animetracker.AniTracker.model.Anime;
import br.com.animetracker.AniTracker.model.FavoriteList;
import br.com.animetracker.AniTracker.security.CustomUserDetails;
import br.com.animetracker.AniTracker.service.FavoriteListService;
import br.com.animetracker.AniTracker.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Favorite Lists", description = "Endpoints para gerenciar listas personalizadas de animes favoritos")
public class FavoriteListController {

    @Autowired
    private FavoriteListService favoriteListService;

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/favorite-lists")
    public String favoriteLists(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Long userId = userDetails.getUserId();
        List<FavoriteList> lists = favoriteListService.getUserFavoriteLists(userId);

        model.addAttribute("favoriteLists", lists);
        model.addAttribute("username", userDetails.getUsername());

        return "favorite-lists";
    }

    @GetMapping("/favorite-lists/{id}")
    public String viewFavoriteList(@PathVariable("id") Long listId,
                                  Model model,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Long userId = userDetails.getUserId();
        Optional<FavoriteList> favoriteListOpt = favoriteListService.getFavoriteList(listId, userId);

        if (!favoriteListOpt.isPresent()) {
            return "redirect:/favorite-lists";
        }

        FavoriteList favoriteList = favoriteListOpt.get();
        Set<Anime> animes = favoriteListService.getAnimesInList(listId, userId);

        model.addAttribute("favoriteList", favoriteList);
        model.addAttribute("animeList", animes);
        model.addAttribute("username", userDetails.getUsername());
        List<Anime> generalFavorites = favoriteService.getUserFavorites(userId);
        Map<Long, Boolean> favoritesMap = new HashMap<>();

        for (Anime anime : animes) {
            favoritesMap.put(anime.getId(), false);
        }
        for (Anime anime : generalFavorites) {
            favoritesMap.put(anime.getId(), true);
        }
        model.addAttribute("favorites", favoritesMap);

        return "favorite-list-detail";
    }

    @PostMapping("/api/favorite-lists/create")
    @ResponseBody
    @Operation(summary = "Criar lista de favoritos", description = "Cria uma nova lista personalizada de animes favoritos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista criada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Map<String, Object>> createFavoriteList(
            @Parameter(description = "Nome da lista") @RequestParam("name") String name,
            @Parameter(description = "Descrição da lista") @RequestParam("description") String description,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();

        if (userDetails == null) {
            response.put("success", false);
            response.put("message", "Not authenticated");
            return ResponseEntity.status(401).body(response);
        }

        Long userId = userDetails.getUserId();

        try {
            FavoriteList list = favoriteListService.createFavoriteList(userId, name, description);
            response.put("success", true);
            response.put("message", "Favorite list created successfully");
            response.put("listId", list.getId());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

@PutMapping("/api/favorite-lists/{id}/update")
@ResponseBody
@Operation(summary = "Atualizar lista de favoritos", description = "Atualiza nome e descrição de uma lista existente")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista atualizada com sucesso"),
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
})
public ResponseEntity<Map<String, Object>> updateFavoriteList(
        @Parameter(description = "ID da lista") @PathVariable("id") Long listId,
        @Parameter(description = "Novo nome da lista") @RequestParam("name") String name,
        @Parameter(description = "Nova descrição da lista") @RequestParam("description") String description,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

    Map<String, Object> response = new HashMap<>();

    if (userDetails == null) {
        response.put("success", false);
        response.put("message", "Not authenticated");
        return ResponseEntity.status(401).body(response);
    }

    Long userId = userDetails.getUserId();

    try {
        favoriteListService.updateFavoriteList(listId, userId, name, description);
        response.put("success", true);
        response.put("message", "Favorite list updated successfully");
        return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
        response.put("success", false);
        response.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}

    @DeleteMapping("/api/favorite-lists/{id}/delete")
    @ResponseBody
    @Operation(summary = "Deletar lista de favoritos", description = "Remove uma lista de favoritos permanentemente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista deletada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public ResponseEntity<Map<String, Object>> deleteFavoriteList(
            @Parameter(description = "ID da lista a ser deletada") @PathVariable("id") Long listId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();

        if (userDetails == null) {
            response.put("success", false);
            response.put("message", "Not authenticated");
            return ResponseEntity.status(401).body(response);
        }

        Long userId = userDetails.getUserId();
        boolean success = favoriteListService.deleteFavoriteList(listId, userId);

        response.put("success", success);
        response.put("message", success ? "Favorite list deleted successfully" : "Failed to delete favorite list");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/favorite-lists/add-anime")
    @ResponseBody
    @Operation(summary = "Adicionar anime à lista", description = "Adiciona um anime a uma lista de favoritos específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Anime adicionado à lista com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public ResponseEntity<Map<String, Object>> addAnimeToList(
            @Parameter(description = "ID da lista") @RequestParam("listId") Long listId,
            @Parameter(description = "ID do anime") @RequestParam("animeId") Long animeId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();

        if (userDetails == null) {
            response.put("success", false);
            response.put("message", "Not authenticated");
            return ResponseEntity.status(401).body(response);
        }

        Long userId = userDetails.getUserId();
        boolean success = favoriteListService.addAnimeToList(listId, animeId, userId);

        response.put("success", success);
        response.put("message", success ? "Anime added to list successfully!" : "Failed to add anime to list");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/favorite-lists/{id}/remove-anime")
    @ResponseBody
    @Operation(summary = "Remover anime da lista", description = "Remove um anime de uma lista de favoritos específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Anime removido da lista com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public ResponseEntity<Map<String, Object>> removeAnimeFromList(
            @Parameter(description = "ID da lista") @PathVariable("id") Long listId,
            @Parameter(description = "ID do anime a ser removido") @RequestParam("animeId") Long animeId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();

        if (userDetails == null) {
            response.put("success", false);
            response.put("message", "Not authenticated");
            return ResponseEntity.status(401).body(response);
        }

        Long userId = userDetails.getUserId();
        boolean success = favoriteListService.removeAnimeFromList(listId, animeId, userId);

        response.put("success", success);
        response.put("message", success ? "Anime removed from list" : "Failed to remove anime from list");

        return ResponseEntity.ok(response);
    }
}