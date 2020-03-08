package com.lvairductcare.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.lvairductcare.app.model.CalendarData;

public interface CalendarDataRepository extends CrudRepository<CalendarData, Long> {
    List<CalendarData> findByPhone(String phone);
    List<CalendarData> findByAppointmentday(String appointmentday);

//    @Query("SELECT a FROM Article a WHERE a.title=:title and a.category=:category")
//    List<CalendarData> fetchArticles(@Param("title") String title, @Param("category") String category); 
}