package entity.adventure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adventurers {

    private final List<Adventurer> adventurers = new ArrayList<>();

    private final HashMap<Integer, Adventurer> adventurerIdIndex = new HashMap<>();

    private final HashMap<String, Adventurer> adventurerNameIndex = new HashMap<>();

    public void addAdventurer(Adventurer adventurer) {
        adventurers.add(adventurer);
        adventurerIdIndex.put(adventurer.getId(), adventurer);
        adventurerNameIndex.put(adventurer.getName(), adventurer);
    }

    public Adventurer getAdventurerById(int id) {
        return adventurerIdIndex.get(id);
    }

    public Adventurer getAdventurerByName(String name) {
        return adventurerNameIndex.get(name);
    }

    public String getNameById(int id) {
        return adventurerIdIndex.get(id).getName();
    }
}
