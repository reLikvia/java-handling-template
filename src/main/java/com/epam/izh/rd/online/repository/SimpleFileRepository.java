package com.epam.izh.rd.online.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimpleFileRepository implements FileRepository {
    public final static String PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator;

    /**
     * Метод рекурсивно подсчитывает количество файлов в директории
     *
     * @param path путь до директори
     * @return файлов, в том числе скрытых
     */
    @Override
    public long countFilesInDirectory(String path) {
        Objects.requireNonNull(path, "Path must not be null");
        File file = new File(PATH + path);
        if (!file.exists()) {
            throw new IllegalArgumentException("This file or directory is not exists");
        }
        long counter = 0L;
        if (file.isDirectory()) {
            for (File innerFile : file.listFiles()) {
                if (innerFile.isDirectory()) {
                    counter += countFilesInDirectory(path + File.separator + innerFile.getName());
                } else {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Метод рекурсивно подсчитывает количество папок в директории, считая корень
     *
     * @param path путь до директории
     * @return число папок
     */
    @Override
    public long countDirsInDirectory(String path) {
        Objects.requireNonNull(path, "Path must not be null");
        File file = new File(PATH + path);
        if (!file.exists()) {
            throw new IllegalArgumentException("This file or directory is not exists");
        }
        long counter = 0L;
        if (file.isDirectory()) {
            counter++;
            for (File innerFile : file.listFiles()) {
                counter += countDirsInDirectory(path + File.separator + innerFile.getName());
            }
        }
        return counter;
    }

    /**
     * Метод копирует все файлы с расширением .txt
     *
     * @param from путь откуда
     * @param to   путь куда
     */
    @Override
    public void copyTXTFiles(String from, String to) throws IOException {
        Objects.requireNonNull(from, "Path must not be null");
        Objects.requireNonNull(to, "Path must not be null");
        File pathFrom = new File(from);
        File pathTo = new File(to);
        if (!pathFrom.exists()) {
            throw new IllegalArgumentException("This file or directory is not exists");
        }
        if (Files.isRegularFile(pathFrom.toPath()) && from.endsWith(".txt")) {
            if (pathTo.exists()) {
                if (pathTo.isDirectory()) {
                    Files.copy(pathFrom.toPath(), Paths.get(to + File.separator + pathFrom.getName()));
                } else {
                    Files.copy(pathFrom.toPath(), pathTo.toPath());
                }
            } else {
                Files.createDirectories(pathTo.toPath().getParent());
                Files.copy(pathFrom.toPath(), pathTo.toPath());
            }
        } else if (Files.isDirectory(pathFrom.toPath())) {
            for (File innerFile : pathFrom.listFiles()) {
                copyTXTFiles(from + File.separator + innerFile.getName(), to + File.separator + innerFile.getName());
            }
        }
    }

    /**
     * Метод создает файл на диске с расширением txt
     *
     * @param path путь до нового файла
     * @param name имя файла
     * @return был ли создан файл
     */
    @Override
    public boolean createFile(String path, String name) {
        File file = new File(PATH + path + File.separator + name);
        try {
            file.getParentFile().mkdirs();
            return file.createNewFile();
        } catch (IOException e) {
            if (file.exists()) {
                file.delete();
            }
            return false;
        }
    }

    /**
     * Метод считывает тело файла .txt из папки src/main/resources
     *
     * @param fileName имя файла
     * @return контент
     */
    @Override
    public String readFileFromResources(String fileName) {
        File file = new File(PATH + fileName);
        if (Files.isRegularFile(file.toPath())) {
            try {
                return Files.readAllLines(file.toPath()).stream().collect(Collectors.joining());
            } catch (IOException ignore) {
                // DO NOTHING
                return null;
            }
        }
        return null;
    }
}
