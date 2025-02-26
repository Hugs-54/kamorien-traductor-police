import java.io.*;
import java.util.*;

public class Main {
    private static Map<String, String> map = new HashMap<>();

    public static void readData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    map.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur : le fichier n'a pu être ouvert");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void printMap() {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " a comme valeur " + entry.getValue());
        }
    }

    public static boolean isVowel(char c) {
        return "aeiou".indexOf(Character.toLowerCase(c)) != -1;
    }

    public static void transform(StringBuilder sentence) {
        for (int i = 0; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            if (Character.isUpperCase(c)) {
                sentence.setCharAt(i, Character.toLowerCase(c));
            }
        }
    }

    public static void append(StringBuilder output, String str){
        if (map.containsKey(str)) {
            output.append(map.get(str));
        }
    }

    public static String translate(String sentence, BufferedWriter outfile) throws IOException {
        StringBuilder output = new StringBuilder();
        int i = 0;

        while (i < sentence.length()) {
            char c1 = sentence.charAt(i);

            if(i + 1 >= sentence.length()){
                append(output,"" + c1);
                break;
            }
            
            char c2 = sentence.charAt(i + 1);     
            String s = "" + c1 + c2;

            if (c1 == ' ') {
                output.append(' ');
                i++;
            } else if (!isVowel(c1) && isVowel(c2)) {
                append(output,s);
                i+=2;
            } else {
                append(output,"" + c1);
                i++;
            }
        }

        if (outfile != null) {
            outfile.write(sentence + " : " + output + "\n");
        }
        return output.toString();
    }


    public static void main(String[] args) {
        BufferedWriter file = null;
        try {
            if (args.length == 1) {
                String outputFile = args[0];
                System.out.println("Nom du fichier de sortie : " + outputFile + ".txt");
                File existingFile = new File(outputFile + ".txt");

                //Read existing lines if file exists
                StringBuilder out = new StringBuilder();
                if (existingFile.exists()) {
                    try (BufferedReader fileIn = new BufferedReader(new FileReader(existingFile))) {
                        String line;
                        while ((line = fileIn.readLine()) != null) {
                            out.append(line).append("\n");
                        }
                    }
                }

                file = new BufferedWriter(new FileWriter(existingFile));
                file.write(out.toString());
            } else {
                System.out.println("Pas de fichier de sortie. Utilisez : java Main nomDuFichier.txt");
            }

            readData();
            Scanner scanner = new Scanner(System.in);
            String sentence;
            do {
                System.out.print("Saisir votre phrase (0 pour quitter) : ");
                sentence = scanner.nextLine();
                if ("0".equals(sentence)) {
                    break;
                }

                StringBuilder sentenceBuilder = new StringBuilder(sentence);
                transform(sentenceBuilder);
                sentence = sentenceBuilder.toString();
                System.out.println("La phrase de départ (formalisé) : " + sentence);
                System.out.println("Se traduit par : " + translate(sentence, file));
            } while (true);

            if (file != null) {
                System.out.println("Fermeture du fichier texte.");
                file.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
