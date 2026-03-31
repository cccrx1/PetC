package com.pethealth.system.repository;

import com.pethealth.system.entity.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SlideRepository extends JpaRepository<Slide, Long> {
    List<Slide> findByActiveTrueOrderByOrderIndexAsc();
}
