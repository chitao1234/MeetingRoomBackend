package cn.xidian.meetingroom.mapper;

import cn.xidian.meetingroom.model.Reservation;
import cn.xidian.meetingroom.model.ReservationExample;
import cn.xidian.meetingroom.model.ReservationWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReservationMapper {
    long countByExample(ReservationExample example);

    int deleteByExample(ReservationExample example);

    int deleteByPrimaryKey(Long reservationId);

    int insert(ReservationWithBLOBs row);

    int insertSelective(ReservationWithBLOBs row);

    List<ReservationWithBLOBs> selectByExampleWithBLOBs(ReservationExample example);

    List<Reservation> selectByExample(ReservationExample example);

    ReservationWithBLOBs selectByPrimaryKey(Long reservationId);

    int updateByExampleSelective(@Param("row") ReservationWithBLOBs row, @Param("example") ReservationExample example);

    int updateByExampleWithBLOBs(@Param("row") ReservationWithBLOBs row, @Param("example") ReservationExample example);

    int updateByExample(@Param("row") Reservation row, @Param("example") ReservationExample example);

    int updateByPrimaryKeySelective(ReservationWithBLOBs row);

    int updateByPrimaryKeyWithBLOBs(ReservationWithBLOBs row);

    int updateByPrimaryKey(Reservation row);
}