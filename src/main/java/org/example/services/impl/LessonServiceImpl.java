package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.*;
import org.example.enums.EAccess;
import org.example.enums.ERole;
import org.example.enums.ESubscriptionType;
import org.example.exceptions.HaveNoAccessLevelException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.pojo.*;
import org.example.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl {

    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final UserLessonRepository userLessonRepository;
    private final UserCourseRepository userCourseRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionBenefitRepository subscriptionBenefitRepository;

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
        UserLesson userLesson = userLessonRepository.findByUserAndLesson(user, lesson)
                .orElseGet(() -> userLessonRepository.save(new UserLesson(lesson, user)));
        UserCourse userCourse = userCourseRepository.findByUserAndCourse(user, lesson.getCourse())
                .orElseGet(() -> userCourseRepository.save(new UserCourse(lesson.getCourse(), user)));
        userCourse.setIsStarted(true);
        userLesson.setIsCompleted(isComplete);
        if (isComplete) {
            userLesson.setCompletedSetTime(LocalDateTime.now());
        }
        userCourseRepository.save(userCourse);
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

        return LessonFullDto.builder()
                .id(lesson.getId())
                .courseId(lesson.getCourse() != null ? lesson.getCourse().getId() : null)
                .name(lesson.getName())
                .description(lesson.getDescription())
                .content(lesson.getContent())
                .practiceTask(lesson.getPracticeTask())
                .videoUrl(lesson.getVideoUrl())
                .duration(lesson.getDuration())
                .isCompleted(userLesson.getIsCompleted())
                .completedSetTime(userLesson.getCompletedSetTime())
                .accessLevel(lesson.getAccessLevel())
                .isFavourite(userLesson.getIsFavourite())
                .isAvailable(isAvailable)
                .favouriteSetTime(userLesson.getFavouriteSetTime())
                .tags(lesson.getTags())
                .isAvailableForPracticeTaskCheck(user.getRole().equals(ERole.ROLE_PRO))
                .build();
    }

    public CollectStarsInfo addLessonToStreak(Long id, LocalDateTime dateTime) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        UserLesson userLesson = userLessonRepository.findByUserAndLesson(user, lesson)
                .orElseGet(() -> userLessonRepository.save(new UserLesson(lesson, user)));
        userLesson.setIsCompleted(true);
        userLesson.setCompletedSetTime(dateTime);
        user.addStreak();
        if (user.getStreak() == 3) {
            user.addStars();
            user.setStreak(0);
        }
        userLessonRepository.save(userLesson);
        userRepository.save(user);
        return CollectStarsInfo.builder()
                .currentLessonStreak(user.getRole().equals(ERole.ROLE_FREE) ? user.getStreak() : null)
                .targetLessonStreak(3)
                .currentStarsCount(user.getRole().equals(ERole.ROLE_FREE) ? user.getStars() : null)
                .targetStarsCount(21)
                .expiresAt(user.getRole().equals(ERole.ROLE_FREE) ? user.getSubscriptionExpiresAt() : null)
                .featureDiscount(0.1).build();
    }

    public CollectStarsInfo getStreakAndStarsInfo() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        return CollectStarsInfo.builder()
                .currentLessonStreak(user.getRole().equals(ERole.ROLE_FREE) ? user.getStreak() : null)
                .targetLessonStreak(3)
                .currentStarsCount(user.getRole().equals(ERole.ROLE_FREE) ? user.getStars() : null)
                .targetStarsCount(21)
                .expiresAt(user.getRole().equals(ERole.ROLE_FREE) ? user.getSubscriptionExpiresAt() : null)
                .featureDiscount(0.1).build();
    }

    public SubscriptionResponse getCurrentSubscription() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        Subscription subscription = subscriptionRepository.findByName(user.getRole().name());
        return SubscriptionResponse.builder()
                .expirationDate(user.getSubscriptionExpiresAt())
                .subscription(convertSubscriptionToDto(subscription))
                .build();
    }

    public SubscriptionsInfo getAllSubscriptions() {
        return SubscriptionsInfo.builder().subscriptions(subscriptionRepository.findAll().stream().map(this::convertSubscriptionToDto).toList()).discount(0.1).build();
    }

    private SubscriptionDto convertSubscriptionToDto(Subscription subscription) {
        List<SubscriptionBenefit> benefits = subscriptionBenefitRepository.findAll();
        List<SubscriptionBenefitDto> benefitDtos = benefits.stream()
                .map(benefit -> convertBenefitToDto(benefit, subscription.getESubscriptionType()))
                .toList();
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .name(subscription.getName())
                .price(subscription.getPrice())
                .description(subscription.getDescription())
                .eSubscriptionType(subscription.getESubscriptionType())
                .benefits(benefitDtos)
                .build();
    }

    private SubscriptionBenefitDto convertBenefitToDto(SubscriptionBenefit subscriptionBenefit, ESubscriptionType currentType) {
        boolean isAvailable;
        if (currentType.equals(ESubscriptionType.PRO)) {
            isAvailable = true;
        } else if (currentType.equals(ESubscriptionType.STANDARD)) {
            isAvailable = !subscriptionBenefit.getSubscriptionType().equals(ESubscriptionType.PRO);
        } else {
            isAvailable = subscriptionBenefit.getSubscriptionType().equals(ESubscriptionType.FREE);
        }
        return SubscriptionBenefitDto.builder()
                .id(subscriptionBenefit.getId())
                .benefitName(subscriptionBenefit.getBenefitName())
                .isAvailable(isAvailable)
                .build();
    }

    public SubscriptionAddInfo addSubscription(SubscriptionDto subscriptionDto, LocalDateTime dateTime) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        user.setSubscriptionId(subscriptionDto.getId());
        user.setRole(ERole.valueOf("ROLE_" + subscriptionDto.getName()));
        user.setSubscriptionExpiresAt(dateTime.plusDays(30));
        userRepository.save(user);
        return SubscriptionAddInfo.builder()
                .subscription(subscriptionDto)
                .hasTelegramId(user.hasTelegramId())
                .build();
    }

    public Long addTgId(TgAddRequest request) throws NoPermissionException {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + SecurityContextHolder.getContext().getAuthentication().getName()));
        if (request.getEmail().equals(user.getEmail())) {
            user.setTgId(request.getTgId());
            return userRepository.save(user).getId();
        } else {
            throw new NoPermissionException();
        }
    }
}
