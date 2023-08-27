package com.team.comma.domain.favorite.artist.domain;

import com.team.comma.domain.user.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "favorite_artist_tb")
public class FavoriteArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String artistName;

    @Column(length = 100)
    private String artistImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static FavoriteArtist buildFavoriteArtist(User user, String artistName) {
        return FavoriteArtist.builder()
                .id(1L)
                .artistName(artistName)
                .artistImageUrl("url")
                .user(user)
                .build();
    }
}
