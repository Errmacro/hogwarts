package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
}
