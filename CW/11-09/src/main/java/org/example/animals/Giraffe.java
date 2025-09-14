package org.example.animals;

import org.example.annotations.Mammal;
import org.example.annotations.Habitat;

@Mammal(sound = "uuuu uuuu uuuu", color = 0xFFA844)
@Habitat(place = "Savannah")
@Habitat(place = "Zoo")
public class Giraffe {
    private String name = "Longneck";

    public void eatLeaves() {
        System.out.println(name + " is eating leaves...");
    }
}
