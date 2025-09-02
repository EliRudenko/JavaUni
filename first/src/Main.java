import java.util.*;


abstract class Animal
{
    protected String name;
    protected boolean hungry = true;
    protected boolean tired = false;

    public Animal(String name) { this.name = name; }

    public abstract void makeSound();

    public void feed()
    {
        if (hungry)
        {
            hungry = false;
            System.out.println(name + " eats.");
        }
        else { System.out.println(name + " is not hungry."); }
    }

    public void rest()
    {
        if (tired)
        {
            tired = false;
            System.out.println(name + " rests.");
        }
        else { System.out.println(name + " is not tired."); }
    }

    protected void updateState(Random rand)
    {
        if (rand.nextBoolean()) hungry = true;
        if (rand.nextBoolean()) tired = true;
    }
}


interface Pet { void play(Random rand); }


class Dog extends Animal implements Pet
{
    public Dog(String name) { super(name); }

    @Override
    public void makeSound() { System.out.println(name + ": Woof!"); }

    @Override
    public void play(Random rand)
    {
        if (hungry)
        {
            System.out.println(name + " wants food.");
        }
        else if (tired) { System.out.println(name + " is too tired."); }
        else
        {
            System.out.println(name + " plays with a stick.");
            updateState(rand);
        }
    }
}

class Cat extends Animal implements Pet
{
    public Cat(String name) { super(name); }

    @Override
    public void makeSound() { System.out.println(name + ": Meow!"); }

    @Override
    public void play(Random rand)
    {
        if (hungry) { System.out.println(name + " wants food."); }
        else if (tired) { System.out.println(name + " is too tired."); }
        else
        {
            System.out.println(name + " plays with a laser.");
            updateState(rand);
        }
    }
}


class Parrot extends Animal implements Pet
{
    public Parrot(String name) { super(name); }

    @Override
    public void makeSound() { System.out.println(name + ": Hello!"); }

    @Override
    public void play(Random rand)
    {
        if (hungry) { System.out.println(name + " wants seeds."); }
        else if (tired) { System.out.println(name + " is too tired."); }
        else
        {
            System.out.println(name + " jumps on a perch.");
            updateState(rand);
        }
    }
}


class ZooManager
{
    private final List<Animal> animals = new ArrayList<>();
    private final Random rand = new Random();

    public void addAnimal(Animal animal)
    {
        animals.add(animal);
        System.out.println(animal.name + " added.");
    }

    public void feedAll() { for (Animal a : animals) a.feed(); }

    public void restAll() { for (Animal a : animals) a.rest(); }

    public void playWithAll()
    {
        for (Animal a : animals) { if (a instanceof Pet pet) pet.play(rand); }
    }

    public void listenAll() { for (Animal a : animals) a.makeSound(); }
}


public class Main
{
    public static void main(String[] args)
    {
        ZooManager zoo = new ZooManager();

        zoo.addAnimal(new Dog("Buddy"));
        zoo.addAnimal(new Cat("Molly"));
        zoo.addAnimal(new Parrot("Kiki"));

        System.out.println("\nSounds ---");
        zoo.listenAll();

        System.out.println("\nPlay ---");
        zoo.playWithAll();

        System.out.println("\nFeed ---");
        zoo.feedAll();

        System.out.println("\nRest ---");
        zoo.restAll();

        System.out.println("\nPlay Again ---");
        zoo.playWithAll();
    }
}