package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Course;
import org.example.entities.Lesson;
import org.example.exceptions.ResourceNotFoundException;
import org.example.pojo.CourseDto;
import org.example.pojo.LessonDto;
import org.example.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl {

    private final CourseRepository courseRepository;

    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream().map(this::convertToCourseDto).collect(Collectors.toList());
    }

    private CourseDto convertToCourseDto(Course course) {
        List<LessonDto> lessonDtos = course.getLessons().stream()
                .map(this::convertToLessonDto)
                .collect(Collectors.toList());

        return CourseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .urlImg(course.getUrlImg())
                .duration(course.getDuration())
                .isFavourite(course.getIsFavourite())
                .isStarted(course.getIsStarted())
                .isAvailable(course.getIsAvailable())
                .lessons(lessonDtos)
                .build();
    }

    private LessonDto convertToLessonDto(Lesson lesson) {
        return LessonDto.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .duration(lesson.getDuration())
                .description(lesson.getDescription())
                .isCompleted(lesson.getIsCompleted())
                .accessability(lesson.getAccessability())
                .isFavourite(lesson.getIsFavourite())
                .build();
    }

    public Course setFavourite(Long courseId, boolean isFavourite) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.setIsFavourite(isFavourite);
        if (isFavourite) {
            course.setFavouriteSetTime(LocalDateTime.now());
        }
        return courseRepository.save(course);
    }

    public Course setComplete(Long courseId, boolean isComplete) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.setIsStarted(isComplete);
        if (isComplete) {
            course.setStartTime(LocalDateTime.now());
        }
        return courseRepository.save(course);
    }
}
