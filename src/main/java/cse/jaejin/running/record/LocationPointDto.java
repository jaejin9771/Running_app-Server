package cse.jaejin.running.record;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class LocationPointDto {
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;
}
