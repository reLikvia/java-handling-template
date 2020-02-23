package com.epam.izh.rd.online.service;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleRegExpService implements RegExpService {
    static final String PATH = "src" + File.separator + "main" + File.separator + "resources";

    /**
     * Метод должен читать файл sensitive_data.txt (из директории resources) и маскировать в нем конфиденциальную информацию.
     * Номер счета должен содержать только первые 4 и последние 4 цифры (1234 **** **** 5678). Метод должен содержать регулярное
     * выражение для поиска счета.
     *
     * @return обработанный текст
     */
    @Override
    public String maskSensitiveData() {
        String line = null;
        StringBuilder result = new StringBuilder();
        try (FileReader fileReader = new FileReader(PATH + File.separator + "sensitive_data.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                result.append(maskFilter(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

        private String maskFilter (String line) {
        if (line == null) return null;
        Pattern pattern = Pattern.compile("(\\d{4}\\s?(?<tag>\\d{4}\\s?\\d{4})\\s?\\d{4})");
        Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                line = line.replace(matcher.group("tag"),"**** ****");
            }
        return line;
    }

    /**
     * Метод должен считыввать файл sensitive_data.txt (из директории resources) и заменять плейсхолдер ${payment_amount} и ${balance} на заданные числа. Метод должен
     * содержать регулярное выражение для поиска плейсхолдеров
     *
     * @return обработанный текст
     */
    @Override
    public String replacePlaceholders(double paymentAmount, double balance) {
        String line = null;
        StringBuilder result = new StringBuilder();
        try (FileReader fileReader = new FileReader(PATH + File.separator + "sensitive_data.txt");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                result.append(placeHolderFilter(line, paymentAmount, balance));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private String placeHolderFilter(String line, double paymentAmount, double balance) {
        if (line == null) return null;
        Pattern pattern = Pattern.compile(".*(?<paymentAmount>\\$\\{payment_amount\\}).*(?<balance>\\$\\{balance\\}).*");
        Matcher matcher = pattern.matcher(line);
        String foundedAmount = null;
        String foundedBalance = null;
        while (matcher.find()) {
            foundedAmount = matcher.group("paymentAmount");
            foundedBalance = matcher.group("balance");
            line = line.replace(foundedAmount, String.format("%.0f", paymentAmount));
            line = line.replace(foundedBalance, String.format("%.0f", balance));
        }
        return line;
    }
}
