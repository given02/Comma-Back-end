package com.team.comma.domain.favorite.track.controller;

import com.team.comma.domain.favorite.track.dto.FavoriteTrackRequest;
import com.team.comma.domain.favorite.track.service.FavoriteTrackService;
import com.team.comma.domain.track.track.dto.TrackRequest;
import com.team.comma.global.common.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/favorite/track")
public class FavoriteTrackController {

    private final FavoriteTrackService favoriteTrackService;

    @PostMapping
    public ResponseEntity createFavoriteTrack(
            @CookieValue String accessToken,
            @RequestBody FavoriteTrackRequest favoriteTrackRequest) throws AccountException {
        return ResponseEntity.ok()
                .body(favoriteTrackService.createFavoriteTrack(accessToken, favoriteTrackRequest));
    }

    @GetMapping
    public ResponseEntity findAllFavoriteTrack(
            @CookieValue String accessToken) throws AccountException {
        return ResponseEntity.ok()
                .body(favoriteTrackService.findAllFavoriteTrack(accessToken));
    }

}
