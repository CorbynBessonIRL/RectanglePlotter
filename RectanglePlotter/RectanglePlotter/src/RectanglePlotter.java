/*
 * Rectangle Plotter
 * -----------------
 * A console program that lets users draw up to 5 filled rectangles on a
 * 40x20 text grid. Rectangles are specified by an anchor corner (x, y),
 * width, and height.
 */

import GridWorld.Grid;
import java.util.Scanner;


public class RectanglePlotter {

    // Grid dimension constants
    private static final int GRID_WIDTH = 40;
    private static final int GRID_HEIGHT = 20;
    private static final char FILL_CHAR = ' ';
    private static final char RECT_CHAR = '*';

    // Maximum number of rectangles
    private static final int MAX_RECTANGLES = 5;

    // Axis label constants
    private static final int X_LABEL_INTERVAL = 5;
    private static final int Y_LABEL_INTERVAL = 5;

    // Program title
    private static final String TITLE = "Rectangle Plotter";

    private static Scanner keyboard = new Scanner(System.in);

    /**
     * Main method - entry point of the program
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        printTitle();
        printInstructions();

        Grid paper = Grid.create(GRID_HEIGHT, GRID_WIDTH, FILL_CHAR);

        int rectangleCount = 0;
        boolean continueDrawing = true;

        while (continueDrawing && rectangleCount < MAX_RECTANGLES) {
            if (getUserWantsToDraw(rectangleCount)) {
                drawRectangle(paper);
                rectangleCount++;
            } else {
                continueDrawing = false;
            }
        }

        if (rectangleCount == MAX_RECTANGLES) {
            System.out.println("\nMaximum number of rectangles (" + MAX_RECTANGLES + ") reached.");
        }

        System.out.println("\nFinal grid with " + rectangleCount + " rectangle(s):");
        displayGrid(paper);

        keyboard.close();
    }

    /**
     * Prints the program title banner
     */
    private static void printTitle() {
        System.out.println("=======================================");
        System.out.println("  " + TITLE);
        System.out.println("=======================================\n");
    }

    /**
     * Prints instructions for using the program
     */
    private static void printInstructions() {
        System.out.println("This program allows you to draw up to " + MAX_RECTANGLES + " rectangles");
        System.out.println("on a " + GRID_WIDTH + "x" + GRID_HEIGHT + " grid.");
        System.out.println();
        System.out.println("For each rectangle, you will specify:");
        System.out.println("  - Top-left corner X coordinate (0-" + (GRID_WIDTH - 1) + ")");
        System.out.println("  - Top-left corner Y coordinate (0-" + (GRID_HEIGHT - 1) + ")");
        System.out.println("  - Width (must fit on grid)");
        System.out.println("  - Height (must fit on grid)");
        System.out.println();
    }

    /**
     * Asks the user if they want to draw a rectangle
     * 
     * @param currentCount the current number of rectangles drawn
     * @return true if user wants to draw, false otherwise
     */
    private static boolean getUserWantsToDraw(int currentCount) {
        if (currentCount == 0) {
            System.out.print("Would you like to draw a rectangle? (yes/no): ");
        } else {
            System.out.print("\nWould you like to draw another rectangle? (yes/no): ");
        }

        String response = keyboard.nextLine().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }

    /**
     * Prompts user for rectangle parameters and draws it on the grid
     * Includes input validation for all parameters
     * 
     * @param paper the Grid to draw on
     */
    private static void drawRectangle(Grid paper) {
        System.out.println("\nEnter rectangle details:");

        int x = getValidCoordinate("X coordinate (top-left corner)", 0, GRID_WIDTH - 1);
        int y = getValidCoordinate("Y coordinate (top-left corner)", 0, GRID_HEIGHT - 1);
        int width = getValidDimension("Width", x, GRID_WIDTH);
        int height = getValidDimension("Height", y, GRID_HEIGHT);

        fillRectangle(paper, x, y, width, height);

        System.out.println("\nRectangle drawn at (" + x + ", " + y + ") with width " +
                width + " and height " + height + ".");
    }

    /**
     * Gets a valid coordinate value from the user
     * 
     * @param prompt the prompt message to display
     * @param min    minimum valid value
     * @param max    maximum valid value
     * @return valid coordinate value
     */
    private static int getValidCoordinate(String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print("  " + prompt + " (" + min + "-" + max + "): ");

            if (keyboard.hasNextInt()) {
                value = keyboard.nextInt();
                keyboard.nextLine(); // consume newline

                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("  Error: Value must be between " + min + " and " + max + ".");
                }
            } else {
                keyboard.nextLine(); // consume invalid input
                System.out.println("  Error: Please enter a valid integer.");
            }
        }
    }

    /**
     * Gets a valid dimension (width or height) from the user
     * 
     * @param prompt       the prompt message to display
     * @param startPos     the starting position (x or y coordinate)
     * @param maxDimension maximum dimension of the grid (width or height)
     * @return valid dimension value
     */
    private static int getValidDimension(String prompt, int startPos, int maxDimension) {
        int value;
        int maxAllowed = maxDimension - startPos;

        while (true) {
            System.out.print("  " + prompt + " (1-" + maxAllowed + "): ");

            if (keyboard.hasNextInt()) {
                value = keyboard.nextInt();
                keyboard.nextLine(); // consume newline

                if (value >= 1 && value <= maxAllowed) {
                    return value;
                } else if (value < 1) {
                    System.out.println("  Error: " + prompt + " must be at least 1.");
                } else {
                    System.out.println("  Error: Rectangle would extend beyond grid boundary.");
                    System.out.println("         Maximum " + prompt.toLowerCase() + " from position " +
                            startPos + " is " + maxAllowed + ".");
                }
            } else {
                keyboard.nextLine(); // consume invalid input
                System.out.println("  Error: Please enter a valid integer.");
            }
        }
    }

    /**
     * Fills a rectangle on the grid with the rectangle character
     * 
     * @param paper  the Grid to draw on
     * @param x      top-left corner x coordinate
     * @param y      top-left corner y coordinate
     * @param width  rectangle width
     * @param height rectangle height
     */
    private static void fillRectangle(Grid paper, int x, int y, int width, int height) {
        for (int row = y; row < y + height; row++) {
            for (int col = x; col < x + width; col++) {
                paper.mark(row, col, RECT_CHAR);
            }
        }
    }

    /**
     * Displays the grid with axis labels and borders
     * 
     * @param paper the Grid to display
     */
    private static void displayGrid(Grid paper) {
        System.out.println();

        // Draw top axis with labels
        drawTopAxis();

        // Draw grid rows with left axis labels
        for (int row = GRID_HEIGHT - 1; row >= 0; row--) {
            drawLeftAxisLabel(row);
            drawGridRow(paper, row);
            System.out.println();
        }

        // Draw bottom axis with labels
        drawBottomAxis();
    }

    /**
     * Draws the top axis label line
     */
    private static void drawTopAxis() {
        System.out.print("^");
        System.out.println();
    }

    /**
     * Draws the left axis label for a given row
     * 
     * @param row the row number
     */
    private static void drawLeftAxisLabel(int row) {
        if (row % Y_LABEL_INTERVAL == 0 || row == GRID_HEIGHT - 1) {
            System.out.printf("%2d +", row);
        } else {
            System.out.print("   |");
        }
    }

    /**
     * Draws a single row of the grid
     * 
     * @param paper the Grid to read from
     * @param row   the row number to draw
     */
    private static void drawGridRow(Grid paper, int row) {
        for (int col = 0; col < GRID_WIDTH; col++) {
            System.out.print(paper.get(row, col));
        }
    }

    /**
     * Draws the bottom axis with labels and markers
     */
    private static void drawBottomAxis() {
        // Draw the baseline
        System.out.print(" 0 +");
        for (int i = 0; i < GRID_WIDTH / X_LABEL_INTERVAL; i++) {
            System.out.print("====+");
        }
        System.out.println(">");

        // Draw the numeric labels
        System.out.print("   ");
        for (int i = 0; i <= GRID_WIDTH; i += X_LABEL_INTERVAL) {
            if (i == 0) {
                System.out.printf(" %d", i);
            } else {
                System.out.printf("    %d", i);
            }
        }
        System.out.println();
    }
}