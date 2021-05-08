package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.util.Locale;
import java.util.Scanner;

class CodeFactory {

    public static void readData(StringBuilder data, String in) {
        File file = new File(in);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                data.append(scanner.nextLine());
            }

        } catch (Exception e) {
            System.out.println("No file found: " + in);
        }
    }

    public static String unicode(StringBuilder data, int key, boolean decoding) {
        StringBuilder res = new StringBuilder();
        if (!decoding) {
            for (int i = 0; i < data.length(); i++) {
                String letter = (char)(data.charAt(i) + key) + "";
                res.append(letter);
            }
        } else {
            for (int i = 0; i < data.length(); i++) {
                String letter = (char)(data.charAt(i) - key) + "";
                res.append(letter);
            }
        }
        return res.toString();
    }

    public static String shift(StringBuilder data, int key, boolean decoding) {
        StringBuilder res = new StringBuilder();
        String alph = "abcdefghijklmnopqrstuvwxyz";
        String alphUpper = alph.toUpperCase(Locale.ROOT);
        if (!decoding) {
            for (int i = 0; i < data.length(); i++) {
                if (alph.indexOf(data.charAt(i)) != -1) {
                    String letter = alph.charAt((alph.indexOf(data.charAt(i)) + key) % 26) + "";
                    res.append(letter);
                } else if (alphUpper.indexOf(data.charAt(i)) != -1) {
                    String letter = alphUpper.charAt((alph.indexOf(data.charAt(i)) + key) % 26) + "";
                    res.append(letter);
                } else {
                    res.append(data.charAt(i));
                }
            }
        } else {
            for (int i = 0; i < data.length(); i++) {
                if (alph.indexOf(data.charAt(i)) != -1) {
                    if ((alph.indexOf(data.charAt(i)) - key) < 0) {
                        String letter = alph.charAt((26 + alph.indexOf(data.charAt(i)) - key) % 26) + "";
                        res.append(letter);
                    } else {
                        String letter = alph.charAt((alph.indexOf(data.charAt(i)) - key) % 26) + "";
                        res.append(letter);
                    }

                } else if (alphUpper.indexOf(data.charAt(i)) != -1) {
                    if (alphUpper.indexOf(data.charAt(i)) - key < 0) {
                        String letter = alphUpper.charAt((26 + alphUpper.indexOf(data.charAt(i)) - key) % 26) + "";
                        res.append(letter);
                    } else {
                        String letter = alphUpper.charAt((alphUpper.indexOf(data.charAt(i)) - key) % 26) + "";
                        res.append(letter);
                    }
                } else {
                    res.append(data.charAt(i));
                }
            }
        }
        return res.toString();
    }

    public static void writeToFile(String out, String res) {
        if (out.equals("")) {
            System.out.println(res);
        } else {
            try (FileWriter writer = new FileWriter(out, false)) {
                writer.write(res);
            } catch (Exception e) {
                System.out.printf("An exception occurs %s", e.getMessage());
            }
        }
    }

    public static void getCode (String mode, StringBuilder data, String in, String out, String alg, int key) {
        String res;
        switch (mode) {
            case "enc":
                if (data.toString().equals("") && !in.equals("")) {
                    readData(data, in);
                }

                if (alg.equals("unicode")) {
                    res = unicode(data, key, false);
                } else {
                    res = shift(data, key, false);
                }

                writeToFile(out, res);
                break;
            case "dec":
                if (data.toString().equals("") && !in.equals("")) {
                    readData(data, in);
                }

                if (alg.equals("unicode")) {
                    res = unicode(data, key, true);
                } else {
                    res = shift(data, key, true);
                }

                writeToFile(out, res);
                break;
        }
    }
}


public class Main {
    public static void main(String[] args) {
        String mode = "enc";
        StringBuilder data = new StringBuilder();
        String in = "";
        String out = "";
        String alg = "shift";
        int key = 0;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode":
                    mode = args[i + 1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i + 1]);
                    break;
                case "-data":
                    data = new StringBuilder(args[i + 1]);
                    break;
                case "-in":
                    in = args[i + 1];
                    break;
                case "-out":
                    out = args[i + 1];
                    break;
                case "-alg":
                    alg = args[i + 1];
            }
        }

        CodeFactory.getCode(mode, data, in, out, alg,key);

    }
}
