package com.piotr.springboot.hotelapp.domain.room;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "number")
    @Min(1)
    private int number;
    @NotNull(message = "Room should have at last one bed!")
    @Column(name = "beds")
    @ElementCollection(targetClass = BedType.class)
    @Enumerated(EnumType.STRING)
    private List<BedType>beds;

    public Room() {
    }

    public Room(Long id, int number, List<BedType> beds) {
        this.id = id;
        this.number = number;
        this.beds = beds;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<BedType> getBeds() {
        return beds;
    }

    public void setBeds(List<BedType> beds) {
        this.beds = beds;
    }

    @Override
    public String toString() {
        return "Number: " + number +
                ", Beds: " + beds;
    }
}
