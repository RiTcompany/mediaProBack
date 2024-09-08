package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.EAccess;
import org.example.entities.Lesson;
import org.example.entities.User;
import org.example.exceptions.HaveNoAccessLevelException;
import org.example.exceptions.InvalidRoleException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.pojo.LessonDto;
import org.example.pojo.LessonFullDto;
import org.example.repositories.LessonRepository;
import org.example.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    public LessonFullDto getLesson(Long lessonId) {
        return toDto(lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found")));
    }

    public Long setFavourite(Long lessonId, boolean isFavourite) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        lesson.setIsFavourite(isFavourite);
        if (isFavourite) {
            lesson.setFavouriteSetTime(LocalDateTime.now());
        }
        return lessonRepository.save(lesson).getId();
    }

    public LessonFullDto setComplete(Long lessonId, boolean isComplete) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        lesson.setIsCompleted(isComplete);
        if (isComplete) {
            lesson.setCompletedSetTime(LocalDateTime.now());
        }
        return toDto(lessonRepository.save(lesson));
    }

    public String getPracticeTask(Long id) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        String role = user.getRole().name().substring(5);
        if (role.equals(EAccess.PRO.name()) || role.equals(EAccess.STANDARD.name())) {
            return lessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesson not found")).getPracticeTask();
        } else throw new HaveNoAccessLevelException(role);
    }

    public static LessonFullDto toDto(Lesson lesson) {
        if (lesson == null) {
            return null;
        }

        return new LessonFullDto(lesson.getId(), lesson.getCourse() != null ? lesson.getCourse().getId() : null,
                lesson.getName(), lesson.getDescription(), lesson.getContent(), lesson.getPracticeTask(), lesson.getVideoUrl(),
                lesson.getDuration(), lesson.getIsCompleted(), lesson.getCompletedSetTime(), lesson.getAccessLevel(),
                lesson.getIsFavourite(), lesson.getFavouriteSetTime(), lesson.getTags());
    }

}
