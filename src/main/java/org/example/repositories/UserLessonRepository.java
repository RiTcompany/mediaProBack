package org.example.repositories;

import org.example.entities.Lesson;
import org.example.entities.User;
import org.example.entities.UserLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLessonRepository extends JpaRepository<UserLesson, Long> {
    List<UserLesson> findAllByUserAndIsFavouriteTrue(User currentUser);

    Optional<UserLesson> findByUserAndLesson(User user, Lesson lesson);
}