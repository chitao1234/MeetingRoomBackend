package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.Reservation;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Date;

@Mapper
public interface ReservationMapper {
    Reservation selectReservationById(Long reservationId);
    List<Reservation> selectReservationsByUserId(Long userId);
    List<Reservation> selectReservationsByMeetingRoomId(Long meetingRoomId);
    List<Reservation> selectReservationsByDateRange(Long meetingRoomId, Date startDate, Date endDate);
    void insertReservation(Reservation reservation);
    void updateReservation(Reservation reservation);
    void deleteReservation(Long reservationId);
}
