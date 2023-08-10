import java.io.*;
        import java.util.ArrayList;
        import java.util.List;

public class MergeSortFiles {
    private static final String INTEGER_TYPE = "-i";
    private static final String STRING_TYPE = "-s";

    public static void main(String[] args) {
        try {
            String sortMode = "-a";
            String dataType = "";
            String outputFile = "";

            List<File> inputFiles = new ArrayList<>();

            // Анализ аргументов командной строки
            int count = 3;
            if (args.length >= 3) {
                if (!(args[0].equals("-a")) && !(args[0].equals("-d"))) {
                    dataType = args[0];
                    outputFile = args[1];
                    count = 2;
                } else {
                    sortMode = args[0];
                    dataType = args[1];
                    outputFile = args[2];
                }
                for (int i = count; i < args.length; i++) {
                    inputFiles.add(new File("resources/" + args[i]));
                }

            } else {
                System.out.println("Invalid arguments. Usage: java MergeSortFiles [-a/-d] [-i/-s] output.txt input1.txt input2.txt ...");
                System.exit(1);
            }

            // Проверка режима сортировки
            boolean isAscending = true;
            if (sortMode.equals("-d")) {
                isAscending = false;
            } else if (!sortMode.equals("-a")) {
                System.out.println("Invalid sort mode. Use -a for ascending or -d for descending.");
                System.exit(1);
            }

            // Проверка типа данных
            boolean isInteger = false;
            if (dataType.equals(INTEGER_TYPE)) {
                isInteger = true;

            } else if (!dataType.equals(STRING_TYPE)) {
                System.out.println("Invalid data type. Use -i for integers or -s for strings.");
                System.exit(1);
            }

            //Объединение и сортировка входных файлов
            List<Object> mergedList = new ArrayList<>();

            for (File file : inputFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {

                        if (!line.isEmpty()) {
                            if (!isInteger) {
                                mergedList.add(line);
                            } else {
                                if (isNumeric(line)) {
                                    try {
                                        mergedList.add(Integer.parseInt(line));
                                    } catch (NumberFormatException e) {
                                        System.out.println("This string is not an integer, so changed to a string");
                                        isInteger = false;
                                        mergedList.add(line);
                                    }
                                }
                            }
                        }
                    }
                    reader.close();
                }
            }
            mergeSort(mergedList, outputFile, isAscending);
            System.out.println("Sorting is complete. The output data is written to the resources/ folder in the file: " + outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            //System.out.println("This string is not an integer, so skipped"+ s);
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static void mergeSort(List<Object> mergedList, String outputFile, boolean isAscending) throws IOException {
        // Сортировка объединенных списков с помощью сортировки слиянием
        sort(mergedList, 0, mergedList.size() - 1, isAscending);

        //Запись отсортированных данных в выходной файл
        BufferedWriter writer = new BufferedWriter(new FileWriter("resources/" + outputFile));
        for (Object line : mergedList) {
            writer.write(String.valueOf(line));
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    public static void sort(List<Object> list, int start, int end, boolean isAscending) {
        if (start < end) {
            int mid = (start + end) / 2;
            sort(list, start, mid, isAscending);
            sort(list, mid + 1, end, isAscending);
            merge(list, start, mid, end, isAscending);
        }
    }

    public static void merge(List<Object> list, int start, int mid, int end, boolean isAscending) {
        List<Object> temp = new ArrayList<>();
        int i = start;
        int j = mid + 1;

        while (i <= mid && j <= end) {
            Object a = list.get(i);
            Object b = list.get(j);

            if (isAscending) {
                if (compare(a, b) <= 0) {
                    temp.add(list.get(i));
                    i++;
                } else {
                    temp.add(list.get(j));
                    j++;
                }
            } else {
                if (compare(a, b) >= 0) {
                    temp.add(list.get(i));
                    i++;
                } else {
                    temp.add(list.get(j));
                    j++;
                }
            }
        }

        while (i <= mid) {
            temp.add(list.get(i));
            i++;
        }
        while (j <= end) {
            temp.add(list.get(j));
            j++;
        }
        for (int k = start; k <= end; k++) {
            list.set(k, temp.get(k - start));
        }
    }

    public static int compare(Object a, Object b) {
        if (a instanceof Integer && b instanceof Integer) {
            return ((Integer) a).compareTo((Integer) b);
        } else if (a instanceof String && b instanceof String) {
            return ((String) a).compareTo((String) b);
        } else {
            throw new IllegalArgumentException("Invalid comparison.");
        }
    }

}

