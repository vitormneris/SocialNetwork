package com.socialnetwork.socialnetwork.services;

import com.socialnetwork.socialnetwork.infrastructure.entities.UserEntity;
import com.socialnetwork.socialnetwork.infrastructure.repositories.UserRepository;
import com.socialnetwork.socialnetwork.services.exceptions.DatabaseException;
import com.socialnetwork.socialnetwork.services.exceptions.FieldBlankException;
import com.socialnetwork.socialnetwork.services.exceptions.InvalidFormatException;
import com.socialnetwork.socialnetwork.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;
    private final UploadImageService uploadImageService;
    private final CheckFiledService checkFiledService;
    private final CheckDatabaseService checkDatabaseService;

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    public UserEntity findById(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserEntity insert(UserEntity userEntity, MultipartFile image) {

        try {
            checkFiledService.checkFieldUser(userEntity);
            checkFiledService.checkFieldImage(image);

            checkDatabaseService.emailExists(userEntity.getLogin());
            checkDatabaseService.usernameExists(userEntity.getUsername());

            userEntity.setUrlImage(uploadImageService.uploadImage(userEntity.getUsername(), image));
            userEntity.setPassword(new BCryptPasswordEncoder().encode(userEntity.getPassword()));
            return repository.save(userEntity);

        } catch (InvalidFormatException e) {
            throw new InvalidFormatException(e.getMessage());
        } catch (FieldBlankException e) {
            throw new FieldBlankException(e.getMessage());
        } catch (IOException e) {
            throw new InvalidFormatException("Not was possible to save the image.");
        } catch (DatabaseException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
