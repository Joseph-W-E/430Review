package review.problem_10;

/**
 * Created by jellio on 3/8/2017.
 */
public class ParserUtil {

    private static Parser parser;
    private static String filename = "grammar.txt";

    // singleton
    public static Parser getParser() {

        if (parser != null) {

            return parser; // (**)

        } else {

            synchronized (ParserUtil.class) {

                // recheck while holding mutex so we don't create it twice
                if (parser == null)
                    parser = new Parser(filename);

                return parser;

            }

        }

    }

}
