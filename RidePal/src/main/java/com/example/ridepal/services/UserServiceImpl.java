package com.example.ridepal.services;

import com.example.ridepal.exceptions.DuplicateEntityException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.models.User;
import com.example.ridepal.repositories.UserRepository;
import com.example.ridepal.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import static com.example.ridepal.filters.specifications.UserSpecifications.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Page<User> findAll(String usernameFilter, String firstNameFilter, String lastNameFilter, String emailFilter, Pageable pageable) {
        Specification<User> filters = Specification.where(StringUtils.isEmptyOrWhitespace(usernameFilter) ? null : username(usernameFilter))
                .and(StringUtils.isEmptyOrWhitespace(firstNameFilter) ? null : firstName(firstNameFilter))
                .and(StringUtils.isEmptyOrWhitespace(lastNameFilter) ? null : lastName(lastNameFilter))
                .and(StringUtils.isEmptyOrWhitespace(emailFilter) ? null : email(emailFilter));
        return userRepository.findAll(filters, pageable);
    }

    @Override
    public User findById(int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }
        return user;
    }


    @Override
    public User create(User user) {
        checkUsernameUniqueness(user);
        checkEmailUniqueness(user);
        return userRepository.save(user);
    }

    @Override
    public void update(User user) {
        checkUsernameUniqueness(user);
        checkEmailUniqueness(user);
        userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }
        userRepository.delete(user);
    }

    private void checkUsernameUniqueness(User user) {
        User repositoryUser = userRepository.findByUsername(user.getUsername());
        if (repositoryUser != null) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }
    }

    private void checkEmailUniqueness(User user) {
        User repositoryUser = userRepository.findByEmail(user.getEmail());
        if (repositoryUser != null) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
    }
}
