package model;
public class Question {
    public int id;
    public String question;
    public String a, b, c, d;
    public char correct;

    public Question(int id, String question, String a, String b, String c, String d, char correct) {
        this.id = id;
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.correct = correct;
    }
}
