
import java.util.Random;

public class Cat {
    private final String nick;
    private int hungry;
    private int energy;
    private int happiness;
    private boolean alive = true;

    private final Random rand = new Random();

    public Cat(String nick) {
        this.nick = nick;
        this.hungry = 40;
        this.energy = 70;
        this.happiness = 70;
    }
    
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
        System.out.println(nick + " заснув і мурчить 😴");
        energy = 100;
        hungry = Math.min(100, hungry + 15);
        happiness = Math.max(0, happiness - 5);
    }

    public void eat() {
        if (!alive) return;
        System.out.println(nick + " з апетитом їсть корм 🍖");
        hungry = 0;
        energy = Math.min(100, energy + 15);
        happiness = Math.min(100, happiness + 10);
    }

    public void wash() {
        if (!alive) return;
        System.out.println(nick + " миє лапки і шерсть 🧼");
        happiness = Math.min(100, happiness + 5);
        energy = Math.max(0, energy - 5);
    }

    public String getRequest() {
        if (hungry >= 80) {
            return nick + " нявкає: «Я голодний, погодуй мене!» 🍖";
        } else if (energy <= 25) {
            return nick + " позіхає: «Я хочу спати!» 😴";
        } else if (happiness <= 40) {
            return nick + " нудьгує: «Пограй зі мною!» 🧶";
        } else {
            return nick + " задоволено мурчить і виглядає щасливим 😊";
        }
    }

    public void printStatus() {
        System.out.println("\n---- Статус кота ----");
        System.out.println("Ім'я      : " + nick);
        System.out.println("Голод     : " + bar(hungry));
        System.out.println("Енергія   : " + bar(energy));
        System.out.println("Щастя     : " + bar(happiness));
        System.out.println("---------------------");
        System.out.println(getRequest() + "\n");
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
