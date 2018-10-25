import java.util.Arrays;

public class Decrypt {

    public static void main(String[] args) {
        String problem1 = "33, 14, 14,51,0,48,53,55,0,58,50,45,24,7,21,4,31,60,45,0,49,30,16,9,56,37,38,49,10,56,46,15,0,51,28,19,57,10,50,53,30,0,21,49,26,5,16,43,28,17,49,20,38,60,50,37,18,29,32,49,72,5,20,47,13,0,54,28,23,73,5,66,72,60,0,59,64,49,24,15,42,17,32,74,45,19,69,51,32,18,60,46,58,74,10,57,60,19,0";
        int[][] key1 = { {-3,-2,4}, {-1,0,1}, {2,1,-2}};

        int[][] problem1Answer = matrixSolver(problem1, 3, 33, key1);
        printMatrix(problem1Answer);
        translator(problem1Answer);

        System.out.println();
        System.out.println();
        String problem2 = "62, 32, 45, 0, 49, 33, 44, 21, 0, 47, 49, 26, 24, 15, 38, 28, 32, 45, 51, 26, 6, 49, 24, 36, 37, 3, 165, 84, 115, 0, 124, 90, 112, 55, 0, 122, 132, 65, 67, 45, 105, 70, 90, 120, 135, 65, 17, 127, 63, 93, 97, 9";
        int[][] key2 = { {1, 0}, {0, 1}};

        int[][] key22 = {{-5, 2}, {3, -1}};
        printMatrix(matrixSolver(problem2, 2, 26, key2));
        translator(matrixSolver(problem2, 2, 26, key22));

        System.out.println();
        System.out.println();

        String problem3 = "34, 54, 23, 54, 63, 57, 87, 36, 105, 63, 100, 9, 55, 117, 115, 0, 169,\n" +
                "67, 65, 124, 45, 83, 135, 25, 134, 63, 79, 99, 100, 27, 107, 43, 50, 124,\n" +
                "39, 48, 160, 27, 90, 137, 0, 72, 122, 80, 114, 158, 104, 69, 10, 99, 70,\n" +
                "54, 13, 18, 9, 21, 23, 21, 32, 13, 42, 25, 40, 3, 19, 46, 45, 0, 63, 25,\n" +
                "22, 48, 15, 29, 50, 10, 51, 21, 31, 38, 40, 9, 40, 16, 17, 46, 13, 19, 60,\n" +
                "9, 33, 52, 0, 27, 45, 32, 43, 59, 41, 26, 4, 37, 25, 18";

        printMatrix(matrixSolver(problem3, 2, 52, key2));
        //there's 104 numbers, so it's either a 2x2 or a 4x4

        System.out.println();

        int[][] tester = { {34, 54, 23, 54, 0}, {79, 99, 100, 27, 0}, {13, 18, 9, 21, 0}, {31, 38, 40, 9, 0}};
        int[][] testerSmall = {{34, 54, 0}, {13,18,0}};
        //printMatrix(rref(tester));

        int[][] identity4 = { {1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};

        int[][] key3 = {{2, -5}, {-1, 3}};
        int[][] problem3Answer = matrixSolver(problem3, 2, 52, key3);

        printMatrix(problem3Answer);
        translator(problem3Answer);

        //double[][] key3 = {{1, -(2/9)}, {0, 1}};

        //printMatrix(matrixSolver(problem3, 2, 52, key3));

        bruteForce(testerSmall, problem3);

        System.out.println("As seen here, the matrix that is correct is: \n"
                + "CRACKING A CODE WITHOUT MUCH INFORMATION IS MUCH MORE DIFFICULT BUT THAT DOES NOT MEAN IT IS IMPOSSIBLE \n" +
                "a = 2 :: b = -5 :: c = -1 :: d = 3");



    }

    public static int[][] mod (int[][] startMatrix) {
        int[][] endMatrix = startMatrix;
        for (int i = 0; i < startMatrix.length; ++i) {
            for (int j = 0; j < startMatrix[i].length; ++j) {
                endMatrix[i][j] = endMatrix[i][j]%26;
            }
        }
        return endMatrix;
    }

    //this attempts to brute force crack the code by multiplying the string by a whole bunch of matrices
    //the numbers it tries can be altered, I have them set to 20 right now,  because larger numbers
    //take a whole lot of time to run. That's one of the two major downsides of this method:
    //use large numbers in your key matrix and we're going to be running our laptop all week trying to crack it
    //or use decimals and it's similarly difficult.
    //each "." is when it tries a new value of b, each enter is when it tries a new value of a.
    public static void bruteForce (int[][] startMatrix, String problemString) {
        //build the testing matrix
        int[][] tester = new int[startMatrix.length][startMatrix.length+1];



        for(int i = 0; i < tester.length; ++i) {
            for (int j = 0; j < tester[0].length-1; ++j) {
                tester[i][j] = startMatrix[i][j];
            }
        }

        int stringLength = howMany(problemString);

        System.out.println();
        System.out.print("Attempting to brute force crack matrix ");

        double[][] holder = new double[2][2];
        double[][] solved;
        for (int a = -20; a <= 20; ++a) {
            holder[0][0] = a;
            for (int b = -20; b <= 20; ++b) {
                holder[0][1] = b;
                for (int c = -20; c <=20; ++c) {
                    holder[1][0] = c;
                    for (int d = -20; d <=20; ++d) {
                        holder[1][1] = d;


                        solved = matrixSolver(problemString, 2, stringLength/2, holder);

                        boolean isLetters = true;
                        for (int i = 0; i < stringLength/2; ++i) {
                            //this checks to make sure each number in the matrix is either a space or
                            //a letter. This still results in some odd outputs, but a human can easily sort through those.
                            if(solved[0][i] < 0 || solved[0][i] > 27 || solved[1][i] < 0 || solved[1][i] > 27) {
                                isLetters = false;
                            }
                        }
                        if (isLetters == true) {
                            System.out.println();
                            translator(solved);
                            System.out.println();
                            System.out.println("a = " + a + " :: b = " + b + " :: c = " + c + " :: d = " + d);
                        }
                    }
                }

                System.out.print(".");

            }
            System.out.println();
        }
        System.out.println("All attempts checked!");

}

    //This brute force solver is supposed to attempt to solve stuff via row reducing for each
    //possible letter for the first two rows in the data, then multiply the key matrix drawn from that
    //by the original string to see if it decodes it, but it's running into some errors with
    //rounding errors. Darn doubles.
    //ultimately this doesn't work but I thought I might as well leave it in anyway.
    public static void bruteForcebyLetter (int[][] startMatrix, String problemString) {
        //build the testing matrix
        int[][] tester = new int[startMatrix.length][startMatrix.length+1];



        for(int i = 0; i < tester.length; ++i) {
            for (int j = 0; j < tester[0].length-1; ++j) {
                tester[i][j] = startMatrix[i][j];
            }
        }


        double[][] holder = new double[2][3];
        for (int a = 1; a <= 26; ++a) {
        tester[0][2] = a;
        for (int b = 1; b <= 26; ++b) {
            tester[1][2] = b;

            holder = rref(tester);

                printMatrix(holder);
                System.out.println("a = " + a + ":: b = " + b);
                int[][] trial = {{(int)holder[0][2], (int)holder[1][2]}, {0, 1}};


                    translator(matrixSolver(problemString, 2, 52, trial));



            }

            System.out.print(".");

        }
        System.out.println("All attempts checked!");

    }

    //row reduces a 2x3 matrix
    public static double[][] rref (int[][] startMatrix) {
        double[][] endMatrix = new double[startMatrix.length][startMatrix[0].length];

        //build matrix
        for (int i = 0; i < startMatrix.length; ++i) {
            for (int j = 0; j < startMatrix[i].length; ++j) {
                endMatrix[i][j] = startMatrix[i][j];
            }
        }

        for (int a = 0; a < endMatrix[0].length-1; ++a) {
            //make first pivot
            double div = endMatrix[a][a];
            for (int i = 0; i < endMatrix[0].length; ++i) {
                endMatrix[a][i] = endMatrix[a][i] / div;
            }
            //zero out other rows
            for (int b = 0; b < endMatrix[0].length-1; ++b) {
                if (b != a) {
                    double mult = endMatrix[b][a];
                    for (int i = 0; i < endMatrix[0].length; ++i) {
                        endMatrix[b][i] = endMatrix[b][i] - (mult * endMatrix[a][i]);
                    }
                }
            }

        }

        return endMatrix;
    }


    //note: each number in the number string has to be seperated with a comma.
    //White spaces are fine as they'll automatically be removed.
    //this assumes the sent string works with the size of the matrix.
    public static int[][] matrixSolver (String number, int rows, int columns, int[][] key) {
        int[][] startMatrix = new int[rows][columns];

        int counter = 0;
        String eliminate = "[,]";
        String[] tokens = number.split(eliminate);
//        for (int i = 0; i < tokens.length; ++i){
//            System.out.print(tokens[i] + " ");
//        }

        //build matrix
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                //takes the String at counter's slot, turns it into an int, then sets it in the matrix
                startMatrix[i][j] = Integer.parseInt(tokens[counter].replaceAll("\\s+",""));
                ++counter;
            }
        }

        int[][] endMatrix = new int[rows][columns];


        //multiply matrix
        for (int i = 0; i<rows; ++i) {
            for (int j = 0; j <columns; ++j) {
                int c = 0;

                for (int k = 0; k<rows; ++k) {
                    c = c + key[i][k]*startMatrix[k][j];
                }

                endMatrix[i][j] = c;
            }
        }

        return endMatrix;
    }

    //note: each number in the number string has to be seperated with a comma.
    //White spaces are fine as they'll automatically be removed.
    //this assumes the sent string works with the size of the matrix.
    public static double[][] matrixSolver (String number, int rows, int columns, double[][] key) {
        int[][] startMatrix = new int[rows][columns];

        int counter = 0;
        String eliminate = "[,]";
        String[] tokens = number.split(eliminate);
//        for (int i = 0; i < tokens.length; ++i){
//            System.out.print(tokens[i] + " ");
//        }

        //build matrix
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                //takes the String at counter's slot, turns it into an int, then sets it in the matrix
                startMatrix[i][j] = Integer.parseInt(tokens[counter].replaceAll("\\s+",""));
                ++counter;
            }
        }

        double[][] endMatrix = new double[rows][columns];


        //multiply matrix
        for (int i = 0; i<rows; ++i) {
            for (int j = 0; j <columns; ++j) {
                double c = 0;

                for (int k = 0; k<rows; ++k) {
                    c = c + key[i][k]*startMatrix[k][j];
                }

                endMatrix[i][j] = c;
            }
        }

        return endMatrix;
    }


    //Translates a matrix into alphabetical form and prints it.
    public static void translator (int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        if (matrix[0][0]%26 > 26 || matrix[0][0] < 0) {
            return;
        }
        for (int i = 0; i<rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                int k = matrix[i][j]%26;
                char c = (char) (k + 64);
                if (c == '@') {
                    System.out.print(" ");
                } else {
                    System.out.print(c);
                }
            }
        }
    }

    //Translates a matrix into alphabetical form and prints it.
    public static void translator (double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        for (int i = 0; i<rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                double k = matrix[i][j];
                char c = (char) (k + 64);
                if (c == '@') {
                    System.out.print(" ");
                } else {
                    System.out.print(c);
                }
            }
        }
    }

    //prints the matrix. Notably, it's configured to format best with
    //matracies with values no greater than 99.
    public static void printMatrix (int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        for (int i = 0; i<rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                System.out.print(matrix[i][j]);
                if (matrix[i][j] <= 9) {
                    System.out.print("   ");
                } else if (matrix[i][j] <= 99) {
                    System.out.print("  ");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public static void printMatrix (double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        for (int i = 0; i<rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                System.out.print(matrix[i][j]);
                if (matrix[i][j] <= 9) {
                    System.out.print("   ");
                } else if (matrix[i][j] <= 99) {
                    System.out.print("  ");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    //returns the number of numbers in a string
    public static int howMany (String string) {
        String eliminate = "[,]";
        String[] tokens = string.split(eliminate);
        return tokens.length;
    }

}
