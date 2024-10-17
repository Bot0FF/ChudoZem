package com.bot0ff.entity;

import com.bot0ff.entity.enums.LocationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Table(name = "location")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Location implements Serializable {
    @Id
    private Long id;

    @Column(name = "x")
    private int x;

    @Column(name = "y")
    private int y;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "locationType")
    private LocationType locationType;

    @Column(name = "emoji")
    private String emoji;

    @Column(name = "name")
    private String name;

    @Column(name = "ais")
    private List<Long> ais;

    @Column(name = "units")
    private List<Long> units;

    @Column(name = "things")
    private List<Long> things;

    @Column(name = "isWorld")
    private boolean isWorld;

    //id локации, для перехода
    @Column(name = "localityId")
    private Long localityId;
}