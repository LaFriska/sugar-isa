package xyz.haroldgao.sugarisa.parser;

public class ParserTest {

    public static void main(String[] args) {
        var instructions = Parser.parse("!r4;!r3;");
        System.out.println(instructions.size());
    }



}
