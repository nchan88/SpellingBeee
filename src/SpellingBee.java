import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {
    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void letters(String input, String lettersLeft)
    {
        if(input.length() == letters.length())
        {
            words.add(input);
            return;
        }
        //iterating through my remaining letters not used so that use all possible combinations
        for (int i = 0; i < lettersLeft.length(); i++)
        {
            //This takes out the letter that we are adding to input and updates lettersLeft.
            //The substring function is (inclusive, exclusive), so I can get the same substring minus i by concatenating two strings before and after i
            String updatedLettersLeft = lettersLeft.substring(0, i) + lettersLeft.substring(i + 1);
            String updatedInput = input + lettersLeft.charAt(i);
            letters(updatedInput, updatedLettersLeft);
            if (updatedInput.length() > 0)
            {
                words.add(updatedInput);
            }
        }

    }
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        letters("", letters);
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        mergeSort(words);
    }

    public void mergeSort(ArrayList<String> words)
    {
        //Base case.
        if (words.size() <= 1)
        {
            return;
        }
        int mid = words.size() / 2;
        //Here I am breaking my ArrayList into two even arrays.
        ArrayList<String> left = new ArrayList<>();
        ArrayList<String> right = new ArrayList<>();

        //I use for loops to add the first and second halves of my arrayList into separate ArrayLists.
        for (int i = 0; i < mid; i++)
        {
            left.add(words.get(i));
        }
        for (int i = mid; i < words.size(); i++)
        {
            right.add(words.get(i));
        }
        //Recursive calls
        mergeSort(left);
        mergeSort(right);
        int i = 0;
        int j = 0;
        while (i < left.size() && j < right.size())
        {
            //Swaps if out of order.
            if (left.get(i).compareTo(right.get(j)) < 0)
            {
                words.set(i + j, left.get(i));
                i++;
            }
            else
            {
                words.set(i + j, right.get(j));
                j++;
            }
        }
        //Since it is already sorted, when one array gets to the end we can just append the rest.
        while (i < left.size())
        {
            words.set(i + j, left.get(i));
            i++;
        }
        while (j < right.size())
        {
            words.set(i + j, right.get(j));
            j++;
        }
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
//         YOUR CODE HERE
        for (int i = 0; i < words.size(); i++)
        {
            //If the word is not found in Dictionary, this removes the word from the ArrayList
            if(!search(words.get(i), 0, DICTIONARY_SIZE - 1))
            {
                words.remove(i);
                i--;
            }
        }
    }

    public boolean search(String target, int low, int high)
    {
        //Base case, false when no match is found.
        if (low > high)
        {
            return false;
        }
        int med = (low + high) / 2;
        //Checks if it is too high or too low, and recurses accordingly in the new range.
        int check = target.compareTo(DICTIONARY[med]);
        if (check == 0)
        {
            return true;
        }
        else if (check < 0)
        {
            return search(target, low, med - 1);
        }
        else
        {
            return search(target, med + 1, high);
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
