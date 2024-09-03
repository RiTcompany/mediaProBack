package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Lesson;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repositories.LessonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl {

    private final LessonRepository lessonRepository;

    public Lesson getLesson(Long lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
    }

    public Lesson setFavourite(Long lessonId, boolean isFavourite) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        lesson.setIsFavourite(isFavourite);
        if (isFavourite) {
            lesson.setFavouriteSetTime(LocalDateTime.now());
        }
        return lessonRepository.save(lesson);
    }

    public Lesson setComplete(Long lessonId, boolean isComplete) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        lesson.setIsCompleted(isComplete);
        if (isComplete) {
            lesson.setCompletedSetTime(LocalDateTime.now());
        }
        return lessonRepository.save(lesson);
    }
}
