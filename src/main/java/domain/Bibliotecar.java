package domain;

public class Bibliotecar extends Utilizator {
    private String codAccesSpecial = "biblioteca1234";

    public Bibliotecar(int id, String username, String parola, String email) {

        super(id, username, parola, email, "Bibliotecar");
    }

    public boolean verificaCod(String codIntrodus) {
        return this.codAccesSpecial.equals(codIntrodus);
    }
}