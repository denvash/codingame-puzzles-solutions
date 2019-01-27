import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// https://www.codingame.com/training/medium/scrabble

class Solution {
  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int N = in.nextInt(); //Get the size of the dictionary
    if (in.hasNextLine()) {
      in.nextLine();
    }
    List<String> dictionary = new ArrayList<>();
    for (int i = 0; i < N; i++) { //Fill the dictionary
      dictionary.add(in.nextLine());
    }
    String SevenLetters = in.nextLine(); //Get the 7 letters two scrabble
    List<Letter> letters = new ArrayList<>(); //Make 7 Letters objects
    for (int i = 0; i < SevenLetters.length(); i++) {
      letters.add(new Letter(SevenLetters.charAt(i)));
    }
    int bestScore = 0;
    String bestWord = dictionary.get(N - 1);
    int indexOfBest = N - 1;
    boolean isLetterFound = false;

    /*
    For every word @ dictionary, go over its letters and decide if it possible to compose a valid word.
    If you able to compose a word, calculate its score, and decide if it's the best so far.
     */
    for (String word : dictionary) {
      for (int i = 0; i < word.length(); i++) {
        isLetterFound = false;
        for (Letter letter : letters) {
          if (letter.c == word.charAt(i) && !letter.status) {
            letter.status = true;
            isLetterFound = true;
            break;
          }
        }
        if (!isLetterFound) break;
      }
      if (isLetterFound) {
        int score = calcScore(letters);
        int currentIndex = dictionary.indexOf(word);
        if ((currentIndex < indexOfBest && score == bestScore) || score >bestScore) {
          bestScore = score;
          bestWord = word;
          indexOfBest = currentIndex;
        }
      }
      resetLetters(letters); // reset all letters before next iteration
    }
    System.out.println(bestWord);
  }

  private static int calcScore(final List<Letter> letters) {
    int res = 0;
    for (Letter letter : letters) {
      if (letter.status) {
        res += letter.toPoints();
      }
    }
    return res;
  }

  private static void resetLetters(List<Letter> letters) {
    for (Letter letter : letters) {
      letter.status = false;
    }
  }

  static class Letter {
    private final char c;
    private boolean status = false;

    Letter(final char c) {
      this.c = c;
    }

    int toPoints() {
      int points = 1;
      if (c == 'd' || c == 'g') {
        points = 2;
      } else if (c == 'b' || c == 'c' || c == 'm' || c == 'p') {
        points = 3;
      } else if (c == 'f' || c == 'h' || c == 'v' || c == 'w' || c == 'y') {
        points = 4;
      } else if (c == 'k') {
        points = 5;
      } else if (c == 'j' || c == 'x') {
        points = 8;
      } else if (c == 'q' || c == 'z') {
        points = 10;
      }
      return points;
    }
  }
}