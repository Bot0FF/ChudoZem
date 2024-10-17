package com.bot0ff.util;

import com.bot0ff.entity.Location;
import com.bot0ff.repository.LocationRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

//Инициализация полной карты с распределением на области
@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class MapImageService {
    public static String[][] allLocations;
    private final LocationRepository locationRepository;

    //после запуска приложения загружает из БД все локации и собирает массив с названиями локаций
    @Scheduled(initialDelay = 1000, fixedDelay=Long.MAX_VALUE)
    public void initializeMap() {
        List<Location> allLocationsList = locationRepository.findAll();

        allLocations = new String[allLocationsList.size() / 10][allLocationsList.size() / 10];
        for(Location location: allLocationsList) {
            allLocations[location.getY()][location.getX()] = location.getEmoji();
        }
        log.info("Load Map completed success");
    }
}
