import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    void Start() throws IOException {
        RomanConverter converter = new RomanConverter();
        Controller controller = new Controller(converter);
        controller.Evaluate(getLine());
    }

    private static String getLine() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine().toUpperCase().trim();
    }

    public static void main(String[] args) throws IOException {
        new Main().Start();
    }
}

enum Operator {
    ADDITION{
        public int action(int a, int b){ return a + b;}
    },
    SUBTRACTION{
        public int action(int a, int b){ return a - b;}
    },
    MULTIPLICATION{
        public int action(int a, int b){ return a * b;}
    },
    DIVISION{
        public int action(int a, int b){ return a / b;}
    };
    public abstract int action(int a, int b);
}

class Numeric {
    String Type;
    Integer Value;
    RomanConverter converter;

    Numeric(String numberRepresentation, RomanConverter converter) throws IOException {
        this.converter = converter;

        try {
            int n = Integer.parseInt(numberRepresentation);
            this.Type = "arabic";
            this.Value = n;

        }catch (Exception e) {
            int n = converter.ConvertToArabic(numberRepresentation);
            if (n == 0) {
                throw new IOException("Недопустимое римское число в качестве операнда");
            }
            this.Type = "roman";
            this.Value = n;
        }
    }
}

class Controller {
    Pattern p;
    RomanConverter converter;

    Controller(RomanConverter conv) {
        p = Pattern.compile("^(\\d+|[IVX]+)\\s+([-+*/])\\s+(\\d+|[IVX]+)$");
        converter = conv;
    }
    void Evaluate(String line) throws IOException {
        Matcher m = p.matcher(line);

        if (!m.find()) {
            throw new IOException("Недопустимый формат строки");
        }

        Numeric a = new Numeric(m.group(1), converter);
        Numeric b = new Numeric(m.group(3), converter);

        if (!a.Type.equals(b.Type)) {
            throw new IOException("используются одновременно разные системы счисления");
        }

        if (a.Value < 0 || b.Value < 0) {
            throw new IOException("отрицательные числа не допускаются к использованию в операндах");
        }

        if (a.Value > 10 || b.Value > 10) {
            throw new IOException("недопускается использовать числа больше 10 в операндах");
        }

        Operator op = getOperator(m.group(2));

        if (op == null) {
            throw new IOException("недопустимая математическая операция");
        }

        int res = op.action(a.Value, b.Value);

        if (res <= 0 && a.Type.equals("roman")) {
            throw new IOException("Отрицательные числа недопустимы в римской системе исчисления");
        }

        if (a.Type.equals("roman")) {
            System.out.println(converter.ConvertToRoman(res));
            return;
        }

        System.out.println(res);
    }

    Operator getOperator(String op) {
        return switch (op) {
            case ("+") -> Operator.ADDITION;
            case ("-") -> Operator.SUBTRACTION;
            case ("*") -> Operator.MULTIPLICATION;
            case ("/") -> Operator.DIVISION;
            default -> null;
        };
    }
}
