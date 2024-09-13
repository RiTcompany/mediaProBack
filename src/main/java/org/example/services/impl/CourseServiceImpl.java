package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.*;
import org.example.exceptions.ResourceNotFoundException;
import org.example.pojo.*;
import org.example.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final LessonRepository lessonRepository;
    private final UserCourseRepository userCourseRepository;
    private final UserLessonRepository userLessonRepository;

    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream().map(this::convertToCourseDto).collect(Collectors.toList());
    }

    private CourseDto convertToCourseDto(Course course) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, course).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
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
                .isFavourite(userCourse.getIsFavourite())
                .isStarted(userCourse.getIsStarted())
                .isTestPassed(userCourse.getIsTestPassed())
                .isAvailable(isAvailable)
                .accessLevel(course.getAccessLevel())
                .lessons(lessonDtos)
                .build();
    }

    private LessonDto convertToLessonDto(Lesson lesson) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        UserLesson userLesson = userLessonRepository.findByUserAndLesson(user, lesson).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
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
                .isCompleted(userLesson.getIsCompleted())
                .isAvailable(isAvailable)
                .accessLevel(lesson.getAccessLevel().name())
                .isFavourite(userLesson.getIsFavourite())
                .build();
    }

    public Long setFavourite(Long courseId, boolean isFavourite) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, course).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        userCourse.setIsFavourite(isFavourite);
        if (isFavourite) {
            userCourse.setFavouriteSetTime(LocalDateTime.now());
        }
        return userCourseRepository.save(userCourse).getCourse().getId();
    }

    public Long setComplete(Long courseId, boolean isComplete) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, course).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        userCourse.setIsCompleted(isComplete);
        if (isComplete) {
            userCourse.setCompletedSetTime(LocalDateTime.now());
        }
        return userCourseRepository.save(userCourse).getCourse().getId();
    }

    public Long setStarted(Long courseId, boolean isStarted) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, course).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        userCourse.setIsStarted(isStarted);
        if (isStarted) {
            userCourse.setStartTime(LocalDateTime.now());
        }
        return userCourseRepository.save(userCourse).getCourse().getId();
    }

    public TestDto getCourseTest(Long id) {
        Test test = testRepository.findByCourseId(id);
        List<QuestionAnswer> qas = test.getQuestionAnswers();
        return TestDto.builder().name(test.getName()).description(test.getDescription()).courseId(id).questionAnswers(qas).build();
    }

    public FavouritesDto getFavourites() {
        return FavouritesDto.builder()
                .favouriteCourses(userCourseRepository.findAllByIsFavouriteTrue().stream().map(UserCourse::getCourse).map(Course::getId).toList())
                .favouriteLessons(userLessonRepository.findAllByIsFavouriteTrue().stream().map(UserLesson::getLesson).map(Lesson::getId).toList()).build();
    }

    public TestResultDto checkTest(Long id, List<Long> answerIds) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, course).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Test test = testRepository.findByCourseId(id);
        String trueAnswers = test.getTrueAnswers();
        List<Long> trueAnswerIds = Arrays.stream(trueAnswers.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .toList();
        int trueAnswersNumber = 0;
        for (int i = 0; i < answerIds.size(); i++) {
            if (trueAnswerIds.get(i).equals(answerIds.get(i))) {
                trueAnswersNumber++;
            }
        }

        boolean testPassed = trueAnswersNumber == answerIds.size();

        if (testPassed) {
            userCourse.setIsTestPassed(true);
            userCourseRepository.save(userCourse);
        }
        return TestResultDto.builder()
                .testId(test.getId())
                .testName(test.getName())
                .trueAnswersCount(trueAnswersNumber)
                .passed(trueAnswersNumber == trueAnswerIds.size())
                .resultAnswer(trueAnswersNumber == trueAnswerIds.size() ? "Поздравляю вы сдали тест" : null).build();
    }
}
