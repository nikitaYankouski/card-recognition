package com.hudman.CardRecognition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Main {
    static List<List<String>> coordinatesPoints;

    static void initCoordinates() {
        coordinatesPoints = new ArrayList<>();
        coordinatesPoints.add(Arrays.asList("2", "9", "7", "11", "6", "14", "5", "17", "6", "19", "7", "20", "10", "18",
                "14", "15", "17", "11", "20", "8", "24", "14", "24", "20", "24"));
        coordinatesPoints.add(Arrays.asList("3", "10", "5", "14", "5", "20", "5", "18", "8", "16", "10", "14", "13",
                "18", "15", "20", "17", "20", "21", "17", "24", "14", "24", "9", "22"));
        coordinatesPoints.add(Arrays.asList("4", "9", "18", "11", "15", "13", "12", "17", "8", "19", "6", "20", "8", "20",
                "12", "20", "16", "20", "19", "23", "19", "20", "24", "16", "19", "11", "19"));
        coordinatesPoints.add(Arrays.asList("5", "20", "5", "15", "5", "11", "5", "10", "8", "10", "13", "13", "13",
                "17", "13", "20", "15", "21", "19", "19", "23", "15", "24", "9", "22", "9", "5", "9", "14", "21", "4"));
        coordinatesPoints.add(Arrays.asList("6", "20", "6", "18", "5", "14", "5", "11", "7", "10", "9", "9", "12", "9",
                "14", "9", "20", "11", "22", "8", "14", "16", "24", "19", "23", "21", "20", "21", "17", "20", "15", "16", "13", "13", "24"));
        coordinatesPoints.add(Arrays.asList("7", "9", "5", "13", "5", "19", "5", "20", "8", "18", "11", "17", "14",
                "14", "19", "12", "22"));
        coordinatesPoints.add(Arrays.asList("8", "10", "9", "10", "7", "14", "5", "19", "6", "20", "8", "20", "11", "17",
                "14", "13", "14", "10", "11", "20", "16", "21", "20", "20", "23", "15", "24", "10", "22", "9", "19"));
        coordinatesPoints.add(Arrays.asList("9", "9", "10", "10", "7", "15", "5", "19", "6", "21", "9", "21", "14", "18",
                "15", "14", "16", "10", "14", "19", "22", "15", "24", "10", "23"));
        coordinatesPoints.add(Arrays.asList("10", "5", "5", "8", "5", "9", "7", "9", "10", "9", "14", "9", "18", "9",
                "23", "14", "13", "22", "4", "29", "17"));
        coordinatesPoints.add(Arrays.asList("J", "18", "5", "18", "8", "18", "12", "18", "16", "18", "19", "17", "22",
                "14", "24", "10", "24", "8", "22"));
        coordinatesPoints.add(Arrays.asList("Q", "9", "14", "9", "11", "11", "8", "14", "6", "18", "5", "22", "6", "25",
                "8", "27", "11", "27", "14", "26", "19", "21", "18", "27", "23", "21", "23", "18", "24", "12", "22", "10", "19"));
        coordinatesPoints.add(Arrays.asList("K", "9", "5", "9", "9", "9", "16", "9", "22", "13", "15", "17", "12", "19",
                "9", "22", "6", "16", "15", "19", "18", "21", "21", "24", "24"));
        coordinatesPoints.add(Arrays.asList("A", "6", "24", "7", "20", "23", "23", "21", "19", "11", "12", "19", "13",
                "12", "19", "17", "19", "14", "6"));
    }

    static <T> Consumer<T> throwingConsumerWrapper(ThrowingConsumer<T, Exception> throwingConsumer) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public static void cardDefinition(BufferedImage bufferedImage, String fileName) {
        System.out.print(fileName + ": ");
        Stream.of(145, 216, 288, 359, 432).filter(y -> bufferedImage.getSubimage(y, 588, 58, 82).getRGB(31, 36) == -1 ||
                bufferedImage.getSubimage(y, 588, 58, 82).getRGB(31, 36) == -8882056)
                .forEachOrdered(y -> System.out.print(recognition(bufferedImage.getSubimage(y, 588, 58, 82))));
        System.out.println("\n");
    }

    static String recognition(BufferedImage bufferedImage) {
        char suit;
        for (List<String> arrayListValue : coordinatesPoints) {
            for (int i = 1; i < arrayListValue.size(); i += 2) {
                if (bufferedImage.getRGB(Integer.parseInt(arrayListValue.get(i)), Integer.parseInt(arrayListValue.get(i + 1))) == -1 ||
                        bufferedImage.getRGB(Integer.parseInt(arrayListValue.get(i)), Integer.parseInt(arrayListValue.get(i + 1))) == -8882056)
                    break;
                if (i + 2 == arrayListValue.size()) {
                    suit = bufferedImage.getRGB(40, 63) == -3323575 || bufferedImage.getRGB(40, 63) == -10477022 ?
                            bufferedImage.getRGB(40, 49) == -1 || bufferedImage.getRGB(40, 49) == -8882056? 'h' : 'd' :
                            bufferedImage.getRGB(42, 47) == -1 || bufferedImage.getRGB(42, 47) == -8882056? 's' : 'c';
                    return arrayListValue.get(0) + suit;
                }
            }
        }
        return "";
    }

    public static void main(String[] args){
        initCoordinates();
        List<BufferedImage> bufferedImageList = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(Paths.get(args[0]))) {
            walk.map(Object::toString).filter(f -> f.endsWith(".png")).forEach(throwingConsumerWrapper(file -> {
                bufferedImageList.add(ImageIO.read(new File(file)));
                cardDefinition(bufferedImageList.get(bufferedImageList.size() - 1), file);
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}