package com.zynaps.demo.equations;

import java.util.Scanner;

class Program {

    public static void main(String[] args) {
        System.out.println("Genetic Algorithm - Equations");
        System.out.println();
        System.out.println("How many large numbers, please specify 1, 2, 3, or 4...");

        var input = new Scanner(System.in);
        var game = new Game(input.nextInt());
        System.out.println(game.describe());

        input.nextLine();
        System.out.println("Press enter key to solve...");
        input.nextLine();

        game.run();
    }
}
