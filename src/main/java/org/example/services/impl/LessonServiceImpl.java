package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.*;
import org.example.exceptions.HaveNoAccessLevelException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.pojo.LessonFullDto;
import org.example.repositories.LessonRepository;
import org.example.repositories.UserLessonRepository;
import org.example.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final UserLessonRepository userLessonRepository;

    public LessonFullDto getLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        UserLesson userLesson = userLessonRepository.findByUserAndLesson(user, lesson).orElseGet(() -> userLessonRepository.save(new UserLesson(lesson, user)));
        return toDto(userLesson, lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found")));
    }

    public Long setFavourite(Long lessonId, boolean isFavourite) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        UserLesson userLesson = userLessonRepository.findByUserAndLesson(user, lesson).orElseGet(() -> userLessonRepository.save(new UserLesson(lesson, user)));
        userLesson.setIsFavourite(isFavourite);
        if (isFavourite) {
            userLesson.setFavouriteSetTime(LocalDateTime.now());
        }
        return userLessonRepository.save(userLesson).getId();
    }

    public LessonFullDto setComplete(Long lessonId, boolean isComplete) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        UserLesson userLesson = userLessonRepository.findByUserAndLesson(user, lesson).orElseGet(() -> userLessonRepository.save(new UserLesson(lesson, user)));
        userLesson.setIsCompleted(isComplete);
        if (isComplete) {
            userLesson.setCompletedSetTime(LocalDateTime.now());
        }
        return toDto(userLessonRepository.save(userLesson), lesson);
    }

    public String getPracticeTask(Long id) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        String role = user.getRole().name().substring(5);
        if (role.equals(EAccess.PRO.name()) || role.equals(EAccess.STANDARD.name())) {
            return lessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesson not found")).getPracticeTask();
        } else throw new HaveNoAccessLevelException(role);
    }

    public LessonFullDto toDto(UserLesson userLesson, Lesson lesson) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));

        String role = user.getRole().name().substring(5);
        String accessLevel = lesson.getAccessLevel().name();
        boolean isAvailable;
        if (role.equals(EAccess.PRO.name())) {
            isAvailable = true;
        } else if (role.equals(EAccess.STANDARD.name())) {
            isAvailable = !accessLevel.equals(EAccess.PRO.name());
        } else {
            isAvailable = accessLevel.equals(EAccess.FREE.name());
        }

        if (lesson == null) {
            return null;
        }

        return new LessonFullDto(lesson.getId(), lesson.getCourse() != null ? lesson.getCourse().getId() : null,
                lesson.getName(), lesson.getDescription(), lesson.getContent(), lesson.getPracticeTask(), lesson.getVideoUrl(),
                lesson.getDuration(), userLesson.getIsCompleted(), userLesson.getCompletedSetTime(), lesson.getAccessLevel(),
                userLesson.getIsFavourite(), isAvailable, userLesson.getFavouriteSetTime(), lesson.getTags());
    }

}
