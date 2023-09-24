package com.team.comma.spotify.service;

import com.neovisionaries.i18n.CountryCode;
import com.team.comma.global.common.dto.MessageResponse;
import com.team.comma.spotify.dto.SearchArtistResponse;
import com.team.comma.spotify.support.SpotifyAuthorization;
import com.team.comma.spotify.support.SpotifySearchCommand;
import com.team.comma.spotify.dto.SearchTrackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import javax.security.auth.login.AccountException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.team.comma.global.common.constant.ResponseCodeEnum.REQUEST_SUCCESS;
import static com.team.comma.spotify.dto.SearchTrackResponse.createTrackResponse;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SpotifyAuthorization spotifyAuthorization;
    private final SpotifySearchCommand spotifySearchCommand;

    public MessageResponse searchArtistList(String artistName , String token) throws AccountException {
        SpotifyApi spotifyApi = spotifyAuthorization.getSpotifyApi();
        SearchArtistsRequest searchArtistsRequest = spotifyApi.searchArtists(artistName).market(CountryCode.KR).build();
        Object executeResult = spotifySearchCommand.executeCommand(searchArtistsRequest);

        if(executeResult instanceof SpotifyApi) {
            return searchArtistList(artistName , token);
        }

        Paging<Artist> artistsPaging = (Paging<Artist>) executeResult;

        ArrayList<SearchArtistResponse> result = new ArrayList<>();
        for (Artist artist : artistsPaging.getItems()) {
            result.add(SearchArtistResponse.createArtistResponse(artist));
        }

        return MessageResponse.of(REQUEST_SUCCESS , result);
    }

    public Artist getArtistByArtistId(String artistId) {
        SpotifyApi spotifyApi = spotifyAuthorization.getSpotifyApi();
        GetArtistRequest getArtistRequest = spotifyApi.getArtist(artistId).build();

        Object executeResult = spotifySearchCommand.executeCommand(getArtistRequest);
        if(executeResult instanceof SpotifyApi) {
            return getArtistByArtistId(artistId);
        }

        return (Artist) executeResult;
    }

    public MessageResponse searchTrackList(String trackName , String token) throws AccountException {
        SpotifyApi spotifyApi = spotifyAuthorization.getSpotifyApi();
        SearchTracksRequest searchTrackRequest = spotifyApi.searchTracks(trackName).market(CountryCode.KR).build();

        Object executeResult = spotifySearchCommand.executeCommand(searchTrackRequest);
        if(executeResult instanceof SpotifyApi) {
            return searchTrackList(trackName , token);
        }

        Paging<Track> artistsPaging = (Paging<Track>) executeResult;
        ArrayList<SearchTrackResponse> result = new ArrayList<>();
        for (Track track : artistsPaging.getItems()) {
            result.add(createTrackResponse(track));
        }

        return MessageResponse.of(REQUEST_SUCCESS , result);
    }

    @Transactional
    public Track getTrackByTrackId(String trackId) {
        SpotifyApi spotifyApi = spotifyAuthorization.getSpotifyApi();
        GetTrackRequest getTrackRequest = spotifyApi.getTrack(trackId).build();

        Object executeResult = spotifySearchCommand.executeCommand(getTrackRequest);
        if(executeResult instanceof SpotifyApi) {
            return getTrackByTrackId(trackId);
        }

        Track track = (Track) executeResult;

        return (Track) executeResult;
    }

    public MessageResponse searchGenreList() {
        SpotifyApi spotifyApi = spotifyAuthorization.getSpotifyApi();
        GetAvailableGenreSeedsRequest genres = spotifyApi.getAvailableGenreSeeds().build();

        Object executeResult = spotifySearchCommand.executeCommand(genres);
        if(executeResult instanceof SpotifyApi) {
            return searchGenreList();
        }

        String[] result = (String[]) executeResult;
        return MessageResponse.of(REQUEST_SUCCESS , result);
    }

    public MessageResponse searchArtistListByYear(int offset) {
        int year = LocalDate.now().getYear();
        SpotifyApi spotifyApi = spotifyAuthorization.getSpotifyApi();
        SearchArtistsRequest artists = spotifyApi.searchArtists(String.format("year:%d", year))
                .offset(offset)
                .limit(10)
                .market(CountryCode.KR)
                .build();

        Object executeResult = spotifySearchCommand.executeCommand(artists);
        if(executeResult instanceof SpotifyApi) {
            return searchArtistListByYear(offset);
        }

        Paging<Artist> artistPaging = (Paging<Artist>) executeResult;
        ArrayList<String> artistNames = new ArrayList<>();
        for (Artist artist : artistPaging.getItems()) {
            artistNames.add(artist.getName());
        }

        return MessageResponse.of(REQUEST_SUCCESS ,artistNames);
    }

}
