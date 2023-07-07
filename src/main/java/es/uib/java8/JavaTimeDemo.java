package es.uib.java8;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaTimeDemo {

  public static void main(String[] args) {

    Date date = new Date();
    log.info("java.util.Date: {}", date);

    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    log.info("java.sql.Date: {}", sqlDate);

    java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
    log.info("java.sql.Timestamp: {}", timestamp);

    LocalDate localDate = LocalDate.now();
    log.info("java.time.LocalDate: {}", localDate);

    LocalTime localTime = LocalTime.now();
    log.info("java.time.LocalTime: {}", localTime);

    LocalDateTime localDateTime = LocalDateTime.now();
    log.info("java.time.LocalDateTime: {}", localDateTime);

    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    log.info("java.time.ZonedDateTime: {}", zonedDateTime);

    log.info("------");
    log.info("Convert java.util.Date to java.time.*");
    log.info("java.time.LocalDate: {}", date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate());
    log.info("java.time.LocalTime: {}", date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalTime());
    log.info("java.time.LocalDateTime: {}", date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime());
    log.info("java.time.ZonedDateTime: {}", date.toInstant()
        .atZone(ZoneId.systemDefault()));

    log.info("------");
    log.info("Convert java.time.* to java.util.Date");
    // Es como decir, convert java.time.* -> ZonedDateTime -> to
    // java.util.Date
    log.info("java.util.Date: {}", Date.from(localDate.atStartOfDay(ZoneId.systemDefault())
        .toInstant()));
    log.info("java.util.Date: {}", Date.from(localDateTime.atZone(ZoneId.systemDefault())
        .toInstant()));
    log.info("java.util.Date: {}", Date.from(zonedDateTime.toInstant()));

    log.info("------");
    log.info("Convert java.sql.Date to java.time.LocalDate (toInstant not supported, SQL Dates have no time!)");
    log.info("java.time.LocalDate: {}", sqlDate.toLocalDate());

    log.info("------");
    log.info("Convert java.time.* to java.sql.Date");
    // Es como decir, convert java.time.* -> LocalDate -> to java.sql.Date
    log.info("java.sql.Date: {}", java.sql.Date.valueOf(localDate));
    // En realidad es un atajopara...
    log.info("java.sql.Date: {}", new java.sql.Date(java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault())
        .toInstant())
        .getTime()));
    log.info("java.sql.Date: {}", java.sql.Date.valueOf(localDateTime.toLocalDate()));
    log.info("java.sql.Date: {}", new java.sql.Date(java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault())
        .toInstant())
        .getTime()));

    log.info("------");
    log.info("Convert java.sql.Timestamp to java.time.*");
    log.info("java.time.LocalDate: {}", timestamp.toLocalDateTime()
        .toLocalDate());
    log.info("java.time.LocalDateTime: {}", timestamp.toLocalDateTime());
    log.info("java.time.ZonedDateTime: {}", timestamp.toLocalDateTime()
        .atZone(ZoneId.systemDefault()));

    log.info("------");
    log.info("Convert java.time.* to java.sql.Timestamp");
    // Es como decir, convert java.time.* -> LocalDateTime/Instant -> to
    // java.sql.Timestamp
    log.info("java.sql.Timestamp: {}", Timestamp.valueOf(localDate.atStartOfDay()));
    // En realidad es un atajo para...
    log.info("java.sql.Timestamp: {}", Timestamp.from(localDate.atStartOfDay(ZoneId.systemDefault())
        .toInstant()));

    log.info("------");
    log.info("LocalDate -> LocalDateTime");
    log.info("java.time.LocalDateTime: {}", localDate.atStartOfDay());
    log.info("java.time.LocalDateTime: {}", localDate.atTime(localTime));
    log.info("java.time.LocalDateTime: {}", localDate.atTime(LocalTime.now()));
    // ..

    log.info("------");
    log.info("LocalDateTime -> ZonedDateTime");
    log.info("java.time.ZonedDateTime: {}", localDateTime.atZone(ZoneId.systemDefault()));
    log.info("java.time.ZonedDateTime: {}", localDateTime.atZone(ZoneId.of("GMT+2")));
    log.info("java.time.ZonedDateTime: {}", localDateTime.atZone(ZoneId.of("America/New_York")));
    // ..

    log.info("------");
    log.info("Date arithmetic");
    log.info("java.time.LocalDate: {}", localDate);
    log.info("java.time.LocalDate: {}", localDate.plusDays(1));
    log.info("java.time.LocalDate: {}", localDate.minusMonths(1));
    log.info("java.time.LocalDate: {}", localDate.plusWeeks(1));
    log.info("java.time.LocalDate: {}", localDate.minusMonths(1));
    log.info("java.time.LocalDate: {}", localDate.isBefore(localDate.plusDays(1)));
    log.info("java.time.LocalDate: {}", localDate.isBefore(localDate.minusMonths(1)));
    log.info("java.time.LocalDate: {}", localDate.isAfter(localDate.plusWeeks(1)));
    log.info("java.time.LocalDate: {}", localDate.isAfter(localDate.minusMonths(1)));
    // ..

    log.info("------");
    log.info("Changing Timezones");
    zonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
    log.info("java.time.ZonedDateTime: {}", zonedDateTime);
  }
}
