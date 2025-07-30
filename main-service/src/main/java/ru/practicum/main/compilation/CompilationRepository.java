package ru.practicum.main.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("""
            SELECT c
            FROM Compilation c
            WHERE LOWER(c.title) = LOWER(:title)
            """)
    Optional<Compilation> findByTitle(@Param("title") String title);

    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
