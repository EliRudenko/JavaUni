package org.example.storage;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MutableClassToInstanceMap;
import org.example.model.Cat;
import org.example.model.Dog;

public class ZooStorage
{
    private final ClassToInstanceMap<Object> animals = MutableClassToInstanceMap.create();

    private final ImmutableList<String> visitors = ImmutableList.of("Лена", "Егор", "Саша");

    public void addCat(Cat cat) { animals.putInstance(Cat.class, cat); }

    public void addDog(Dog dog) { animals.putInstance(Dog.class, dog); }

    public Cat getCat() { return animals.getInstance(Cat.class); }

    public Dog getDog() { return animals.getInstance(Dog.class); }

    public ImmutableList<String> getVisitors() { return visitors; }
}
