package com.trekmate.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekmate.dto.*;
import com.trekmate.entity.Favorite;
import com.trekmate.entity.Trek;
import com.trekmate.entity.User;
import com.trekmate.exception.ConflictException;
import com.trekmate.exception.NotFoundException;
import com.trekmate.mapper.FavoriteMapper;
import com.trekmate.repository.FavoriteRepository;
import com.trekmate.repository.TrekRepository;
import com.trekmate.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final TrekRepository trekRepository;
    private final FavoriteMapper favoriteMapper;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, UserRepository userRepository, TrekRepository trekRepository, FavoriteMapper favoriteMapper) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.trekRepository = trekRepository;
        this.favoriteMapper = favoriteMapper;
    }

    @Override
    @Transactional
    public FavoriteMutationResponse add(String email, Long trekId) {
        User user = getUser(email);
        Trek trek = getTrek(trekId);
        if (favoriteRepository.findByUserIdAndTrekId(user.getId(), trekId).isPresent())
            throw new ConflictException("This trek is already a favorite.");
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setTrek(trek);
        Favorite saved = favoriteRepository.save(favorite);
        return new FavoriteMutationResponse(favoriteRepository.countByUserId(user.getId()), favoriteMapper.toResponse(saved));
    }

    @Override
    @Transactional
    public FavoriteCountResponse remove(String email, Long trekId) {
        User user = getUser(email);
        Favorite favorite = favoriteRepository.findByUserIdAndTrekId(user.getId(), trekId).orElseThrow(() -> new NotFoundException("Favorite not found."));
        favoriteRepository.delete(favorite);
        favoriteRepository.flush();
        return new FavoriteCountResponse(favoriteRepository.countByUserId(user.getId()));
    }

    @Override
    public FavoriteListResponse findAll(String email) {
        User user = getUser(email);
        List<FavoriteResponse> favorites = favoriteRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId()).stream().map(favoriteMapper::toResponse).toList();
        return new FavoriteListResponse(favorites.size(), favorites);
    }

    private User getUser(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("User not found."));
    }

    private Trek getTrek(Long trekId) {
        return trekRepository.findById(trekId).orElseThrow(() -> new NotFoundException("Trek not found."));
    }
}
