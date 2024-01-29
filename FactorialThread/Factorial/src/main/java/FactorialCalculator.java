import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.concurrent.Executors;

/*Представлено два разных вариант решения задачи */
public class FactorialCalculator {
    public static void main(String[] args) throws IOException {

        doVariantOne("D:/java/FactorialThread/Factorial/src/main/resources/Numbers.txt");
        doVariantTwo("D:/java/FactorialThread/Factorial/src/main/resources/Numbers.txt");

    }

    public static BigInteger doFactorial(int number) throws IllegalAccessException {
        if (number <= 0) {
            throw new IllegalAccessException("You need a natural number");
        }
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= number; i++)
            result = result.multiply(BigInteger.valueOf(i));
        return result;
    }

    private static void doVariantTwo(String filePath) {
        while (true) {
            try {
                Scanner fs = new Scanner(new FileReader(filePath));
                fs.useDelimiter(" ");
                try (var threadPool = Executors.newThreadPerTaskExecutor(Thread::new)) {
                    fs.tokens()
                            .mapToInt(Integer::valueOf)
                            .forEach(number -> threadPool.submit(
                                            () -> System.out.printf("%d! = %d%n", number, doFactorial(number))
                                    )
                            );
                    return;
                }
            } catch (FileNotFoundException e) {
                System.out.println("Invalid path. Try again.");
            }
        }
    }

    public static void doVariantOne(String filePath) throws IOException {

        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytes);
        fis.close();
        String[] valueStr = new String(bytes).trim().split("\\s+");
        int[] tall = new int[valueStr.length];
        for (int i = 0; i < valueStr.length; i++)
            tall[i] = Integer.parseInt(valueStr[i]);
        for (var number : tall) {
            new Thread(() -> {
                try {
                    System.out.println(number + "! = " + doFactorial(number));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
