package cn.xidian.meetingroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.xidian.meetingroom.mapper")
public class MeetingRoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetingRoomApplication.class, args);
	}

}
