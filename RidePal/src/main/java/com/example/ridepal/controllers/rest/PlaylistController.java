package com.example.ridepal.controllers.rest;

import com.example.ridepal.filters.PlaylistSortField;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.dtos.PlaylistDto;
import com.example.ridepal.services.contracts.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public Page<Playlist> getAllPlaylists(@RequestParam(required = false) String titleFilter,
                                          @RequestParam(required = false) String genreFilter,
                                          @RequestParam(required = false) double minRankFilter,
                                          @RequestParam(required = false) double maxRankFilter,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int sizePerPage,
                                          @RequestParam(defaultValue = "ID") PlaylistSortField sortField,
                                          @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {
        Pageable pageable = PageRequest.of(page, sizePerPage, sortDirection, sortField.getDatabaseFieldName());

        return playlistService.getAllPlaylists(titleFilter, genreFilter, minRankFilter, maxRankFilter, pageable);
    }

    @GetMapping("/{id}")
    public Playlist getPlaylistById(@PathVariable int id) {
        return playlistService.getPlaylistById(id);
    }

    @PostMapping
    public void createPlaylist(@RequestBody PlaylistDto playlistDto){
        Playlist playlist = new Playlist();
        playlist.setTitle(playlistDto.getTitle());
        playlistService.createPlaylist(playlist);
    }

    @PutMapping("/{id}")
    public void updatePlaylist(@PathVariable int id, @RequestBody PlaylistDto playlistDto) {
        Playlist playlist = playlistService.getPlaylistById(id);
        playlist.setTitle(playlistDto.getTitle());
        playlistService.updatePlaylist(playlist);
    }

    @DeleteMapping("/{id}")
    public void deletePlaylist(@PathVariable int id) {
        playlistService.deletePlaylist(id);
    }
}
