package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.*;
import org.example.exceptions.ResourceNotFoundException;
import org.example.pojo.CourseDto;
import org.example.pojo.FavouritesDto;
import org.example.pojo.LessonDto;
import org.example.pojo.TestDto;
import org.example.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final TestRepository testRepository;
    private final LessonRepository lessonRepository;

    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream().map(this::convertToCourseDto).collect(Collectors.toList());
    }

    private CourseDto convertToCourseDto(Course course) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));

        List<LessonDto> lessonDtos = course.getLessons().stream()
                .map(this::convertToLessonDto)
                .collect(Collectors.toList());

        String role = user.getRole().name().substring(5);
        String accessLevel = course.getAccessLevel().name();
        boolean isAvailable;
        if (role.equals(EAccess.PRO.name())) {
            isAvailable = true;
        } else if (role.equals(EAccess.STANDARD.name())) {
            isAvailable = !accessLevel.equals(EAccess.PRO.name());
        } else {
            isAvailable = accessLevel.equals(EAccess.FREE.name());
        }

        return CourseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .urlImg(course.getUrlImg())
                .duration(course.getDuration())
                .isFavourite(course.getIsFavourite())
                .isStarted(course.getIsStarted())
                .isAvailable(isAvailable)
                .accessLevel(course.getAccessLevel())
                .lessons(lessonDtos)
                .build();
    }

    private LessonDto convertToLessonDto(Lesson lesson) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
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
        return LessonDto.builder()
                .id(lesson.getId())
                .courseId(lesson.getCourse().getId())
                .name(lesson.getName())
                .duration(lesson.getDuration())
                .description(lesson.getDescription())
                .isCompleted(lesson.getIsCompleted())
                .isAvailable(isAvailable)
                .accessLevel(lesson.getAccessLevel().name())
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

    public TestDto getCourseTest(Long id) {
        Test test = testRepository.findByCourseId(id);
        List<QuestionAnswer> qas = test.getQuestionAnswers();
        return TestDto.builder().name(test.getName()).description(test.getDescription()).courseId(id).questionAnswers(qas).build();
    }

    public FavouritesDto getFavourites() {
        return FavouritesDto.builder()
                .favouriteCourses(courseRepository.findAllByIsFavouriteTrue().stream().map(Course::getId).toList())
                .favouriteLessons(lessonRepository.findAllByIsFavouriteTrue().stream().map(Lesson::getId).toList()).build();
    }
}
