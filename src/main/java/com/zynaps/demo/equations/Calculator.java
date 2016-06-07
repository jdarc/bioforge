package com.zynaps.demo.equations;

class Calculator {

    private static final char[] OPERANDS = {'/', '*', '!', '+'};

    public static double evaluate(String formula) {
        String tokenized = formula.replace('-', '!');
        for (char operand : OPERANDS) {
            int indexOf = tokenized.indexOf(operand);
            while (indexOf > 0) {
                int idx1 = findFirst(tokenized, indexOf);
                int idx2 = findSecond(tokenized, indexOf);
                double val1 = Double.parseDouble(tokenized.substring(idx1, indexOf));
                double val2 = Double.parseDouble(tokenized.substring(indexOf + 1, idx2 + 1));

                if (operand == '/') {
                    val1 /= val2;
                } else if (operand == '*') {
                    val1 *= val2;
                } else if (operand == '+') {
                    val1 += val2;
                } else if (operand == '!') {
                    val1 -= val2;
                }

                tokenized = tokenized.substring(0, idx1) + val1 + tokenized.substring(idx2 + 1);

                indexOf = tokenized.indexOf(operand);
            }
        }

        return Double.parseDouble(tokenized);
    }

    private static int findFirst(String formula, int indexOf) {
        for (int i = indexOf; i > 0; i--) {
            char c = formula.charAt(i - 1);
            if (c == '+' || c == '!' || c == '*' || c == '/') {
                return i;
            }
        }
        return 0;
    }

    private static int findSecond(String formula, int indexOf) {
        for (int i = indexOf + 1; i < formula.length() - 1; i++) {
            char c = formula.charAt(i + 1);
            if (c == '+' || c == '!' || c == '*' || c == '/') {
                return i;
            }
        }
        return formula.length() - 1;
    }
}
