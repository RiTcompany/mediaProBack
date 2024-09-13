package org.example.repositories;

import lombok.Value;
import org.example.entities.Course;
import org.example.entities.User;
import org.example.entities.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {

    List<UserCourse> findAllByIsFavouriteTrue();

    Optional<UserCourse> findByUserAndCourse(User user, Course course);
}