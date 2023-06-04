import java.util.HashMap;
import java.util.Map;

class RomanConverter {
     HashMap<Integer, String> romanUnits = new HashMap<>();
    RomanConverter() {
        romanUnits.put(1, "I");
        romanUnits.put(2, "II");
        romanUnits.put(3, "III");
        romanUnits.put(4, "IV");
        romanUnits.put(5, "V");
        romanUnits.put(6, "VI");
        romanUnits.put(7, "VII");
        romanUnits.put(8, "VIII");
        romanUnits.put(9, "IX");
        romanUnits.put(10, "X");
    }

    int ConvertToArabic(String number) {
        return romanUnits.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(number))
                .map(Map.Entry::getKey).findFirst().orElse(0);
    }

    String ConvertToRoman(int num) {

        StringBuilder result = new StringBuilder();

        int dozens = GetAmountOfDozens(num);
        int units = GetAmountOfUnits(num);

        if (dozens > 0) {
            RomansDig m = GetRomanMax(num);

            if (m.Value > num) {
                int d = m.Dozen - dozens;

                if (d == 1) {
                    result.append("X");
                    result.append(m.Lit);
                }
            }else {
                int d = dozens - m.Dozen;

                result.append(m.Lit);

                result.append("X".repeat(Math.max(0, d)));
            }

        }


        if (units > 0) {
            result.append(romanUnits.get(units));
        }


        return result.toString();
    }

    RomansDig GetRomanMax(int num) {
        if (num >= 10 && num < 40) {
            return new RomansDig("X", 10, 1);
        }

        if (num >= 40 && num < 90) {
            return new RomansDig("L", 50, 5);
        }

        if (num >= 90) {
            return new RomansDig("C", 100, 10);
        }

        throw new RuntimeException("out of romans range");

    }

    int GetAmountOfDozens(int num) {
        return num / 10;
    }

    int GetAmountOfUnits(int num) {
        return num % 10;
    }


}

class RomansDig {
    String Lit;
    int Value;
    int Dozen;

    RomansDig(String lit, int value, int dozen) {
        Lit = lit;
        Value = value;
        Dozen = dozen;
    }

    @Override
    public String toString() {
        return "Roman [Lit=" + this.Lit + ", Value=" + this.Value + ", Dozen=" + this.Dozen + "]";

    }
}