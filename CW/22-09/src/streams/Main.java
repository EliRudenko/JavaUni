package streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main
{

    public static void filterMapCollect()
    {
        List<String> names = Arrays.asList("Олександр", "Анна", "Владислава", "Галина");

        List<String> result = names.stream()
                .filter(name -> name.startsWith("А"))
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println(result); // [АННА]
    }

    public static void parallelStreams()
    {
        List<Double> randomNumbers = Stream.generate(Math::random)
                .limit(500)
                .parallel()
                .collect(Collectors.toList());

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double sum = randomNumbers.stream().mapToDouble(Double::doubleValue).sum();
        System.out.println("Сума випадкових чисел: " + sum);
    }

    public static Map<String, Integer> reduceExample() throws Exception {
        URI uri = new URI("https://github.com/sunmeat/storage/raw/refs/heads/main/text/kobzar.txt");
        URL url = uri.toURL();
        try (var reader = new BufferedReader(
                new InputStreamReader(url.openStream(), Charset.forName("Windows-1251")))) {

            Map<String, Integer> wordCount = reader.lines()
                    .flatMap(line -> Arrays.stream(line.split("\\s+")))
                    .reduce(new HashMap<>(),
                            (map, word) -> {
                                map.put(word, map.getOrDefault(word, 0) + 1);
                                return map;
                            },
                            (map1, map2) -> {
                                map2.forEach((key, value) -> map1.merge(key, value, Integer::sum));
                                return map1;
                            });

            return wordCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                    .limit(50)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                            LinkedHashMap::new));
        }
    }

    public static void groupingByExample() {
        List<String> names = Arrays.asList("Armin van Buuren", "Anna Lee", "Above and Beyond", "Cosmic Gate",
                "Ferry Corsten");

        var groupedByInitial = names.stream()
                .collect(Collectors.groupingBy(name -> name.substring(0, 1)));

        System.out.println("Групування: " + groupedByInitial);
    }

    static class Student {
        String name;
        int examScore;
        List<Integer> homeworkScores;

        Student(String name, int examScore, List<Integer> homeworkScores) {
            this.name = name;
            this.examScore = examScore;
            this.homeworkScores = homeworkScores;
        }

        double getAverageHomeworkScore() {
            return homeworkScores.stream().mapToInt(Integer::intValue).average().orElse(0);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void filterStudents() {
        List<Student> students = Arrays.asList(
                new Student("Анна", 8, Arrays.asList(11, 12, 10)),
                new Student("Максим", 6, Arrays.asList(9, 8, 7)),
                new Student("Володимир", 9, Arrays.asList(10, 11, 12)),
                new Student("Світлана", 7, Arrays.asList(10, 10, 9)));

        List<Student> filteredStudents = students.stream()
                .filter(student -> student.examScore > 7)
                .filter(student -> student.getAverageHomeworkScore() > 10)
                .collect(Collectors.toList());

        System.out.println("Студенти, що пройшли фільтр: " + filteredStudents);
    }





    public static void filterLinesFromFile() throws Exception
    {
        URI uri = new URI("https://github.com/sunmeat/storage/raw/refs/heads/main/text/nouns.txt");
        URL url = uri.toURL();

        try (var reader = new BufferedReader(
                new InputStreamReader(url.openStream(), Charset.forName("Windows-1251"))))
        {
            System.out.println("\nНачало на 'A':");
            reader.lines()
                    .filter(line -> line.toUpperCase().startsWith("A") || line.toUpperCase().startsWith("А"))
                    .forEach(System.out::println);
        }
    }


    public static void main(String[] args) throws Exception
    {
        filterMapCollect();
        parallelStreams();

        Map<String, Integer> wordCount = reduceExample();
        wordCount.forEach((word, count) -> System.out.println(word + ": " + count));

        groupingByExample();
        filterStudents();

        filterLinesFromFile();
    }
}
