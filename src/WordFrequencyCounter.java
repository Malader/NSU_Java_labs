import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class WordFrequencyCounter {
    public static void main(String[] args) {
        // Проверка наличия аргумента, содержащего имя файла
        if (args.length == 0) {
            System.out.println("Не указано имя файла.");
            return;
        }
        String fileName = args[0];
        try {
            // Чтение файла и подсчёт частот слов
            Map<String, Integer> wordFrequencies = getWordFrequencies(fileName);
            // Получение упорядоченного списка слов и их частот
            List<WordFrequency> sortedWordFrequencies = getSortedWordFrequencies(wordFrequencies);
            // Создание и запись в CSV-файл
            String csvFileName = "NSU_Java_labs/Out.csv";
            writeCsvFile(csvFileName, sortedWordFrequencies);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
    private static Map<String, Integer> getWordFrequencies(String fileName) throws IOException {
        Map<String, Integer> wordFrequencies = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Каждая строка разбивается на слова с помощью этого метода (split)
                // Используется регулярное выражение, для разделения строк на слова
                String[] words = line.split("[^\\p{L}\\p{Digit}]+");
                for (String word : words) {
                    wordFrequencies.merge(word.toLowerCase(), 1, Integer::sum);
                }
            }
        }
        // Каждое слово добавляется в карту 'wordFrequencies', где ключом является слово в нижнем регистре,
        // А значением - его частота
        return wordFrequencies;
    }

    private static List<WordFrequency> getSortedWordFrequencies(Map<String, Integer> wordFrequencies) {
        List<WordFrequency> sortedWordFrequencies = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordFrequencies.entrySet()) {
            sortedWordFrequencies.add(new WordFrequency(entry.getKey(), entry.getValue()));
        }
        // Список сортируется по убыванию частоты
        Collections.sort(sortedWordFrequencies);
        return sortedWordFrequencies;
    }

    private static void writeCsvFile(String fileName, List<WordFrequency> wordFrequencies) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Word    Frequency     Frequency (%)\n");
            int totalWords = wordFrequencies.stream().mapToInt(WordFrequency::frequency).sum();
            for (WordFrequency wf : wordFrequencies) {
                writer.append(String.format("%s     %d     %.2f%%\n", wf.word(), wf.frequency(),
                        ((double) wf.frequency() / totalWords) * 100));
            }
        }
    }

    private record WordFrequency(String word, int frequency) implements Comparable<WordFrequency> {

        @Override
            public int compareTo(WordFrequency other) {
                return Integer.compare(other.frequency, this.frequency);
            }
        }
}

