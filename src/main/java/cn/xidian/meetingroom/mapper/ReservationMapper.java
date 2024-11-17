package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.Reservation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {
    Reservation selectReservationById(int reservationId);
    // Additional CRUD methods
}
