
import java.util.Random;

public class Cat
{
    private final String nick;
    private int hungry;     // 0 - ситий, 100 - голодний
    private int energy;     // 0 - виснажений, 100 - бадьорий
    private int happiness;  // 0 - сумний, 100 - щасливий
    private boolean alive = true;

    private final Random rand = new Random();

    // ==== Конструктор ====
    public Cat(String nick) {
        this.nick = nick;
        this.hungry = 40;
        this.energy = 70;
        this.happiness = 70;
    }

    // ==== Методи ====
    public void play() {
        if (!alive) return;
        if (hungry >= 90) {
            System.out.println(nick + " занадто голодний, щоб гратися.");
            return;
        }
        if (energy <= 20) {
            System.out.println(nick + " занадто втомився для ігор.");
            return;
        }
        System.out.println(nick + " весело ганяється за клубочком 🧶");
        energy -= 20;
        hungry += 15;
        happiness = Math.min(100, happiness + 20);
    }

    public void sleep() {
        if (!alive) return;
        System.out.println(nick + " заснув і солодко мурчить 😴");
        energy = 100;
        hungry = Math.min(100, hungry + 15);
        happiness = Math.max(0, happiness - 5);
    }

    public void eat() {
        if (!alive) return;
        System.out.println(nick + " їсть корм 🍖 і мурчить.");
        hungry = 0;
        energy = Math.min(100, energy + 15);
        happiness = Math.min(100, happiness + 10);
    }

    public void hunt() {
        if (!alive) return;
        System.out.println(nick + " полює на іграшкову мишку 🐭...");
        energy = Math.max(0, energy - 10);
        hungry = Math.min(100, hungry + 5);

        if (rand.nextBoolean()) {
            System.out.println("Успіх! " + nick + " спіймав мишку!");
            happiness = Math.min(100, happiness + 15);
        } else {
            System.out.println("Мишка втекла... " + nick + " сумує 😿");
            happiness = Math.max(0, happiness - 5);
        }
    }

    public void wash() {
        if (!alive) return;
        System.out.println(nick + " вилизує шерсть 🧼");
        happiness = Math.min(100, happiness + 5);
        energy = Math.max(0, energy - 5);
    }

    public void liveOneCycle() {
        if (hungry > 80) {
            eat();
        } else if (energy < 30) {
            sleep();
        } else {
            int action = rand.nextInt(3);
            switch (action) {
                case 0 -> play();
                case 1 -> hunt();
                case 2 -> wash();
            }
        }
        normalizeStats();
    }

    private void normalizeStats() {
        hungry = Math.max(0, Math.min(100, hungry));
        energy = Math.max(0, Math.min(100, energy));
        happiness = Math.max(0, Math.min(100, happiness));
    }

    public void printStatus() {
        System.out.println("\n---- Статус кота ----");
        System.out.println("Ім'я      : " + nick);
        System.out.println("Голод     : " + bar(hungry));
        System.out.println("Енергія   : " + bar(energy));
        System.out.println("Щастя     : " + bar(happiness));
        System.out.println("---------------------\n");
    }

    private String bar(int value) {
        int filled = value / 10;
        int empty = 10 - filled;
        return "[" + "█".repeat(filled) + " ".repeat(empty) + "] " + value + "%";
    }

    public String getNick() {
        return nick;
    }
}
