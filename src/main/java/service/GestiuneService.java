package service;

import domain.*;
import repository.BookRepository;
import repository.UserRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

public class GestiuneService {
    private BookRepository bookRepo = new BookRepository();
    private UserRepository userRepo = new UserRepository();




    public boolean inregistrareUtilizator(String username, String email, String parolaSimpla, String rol, Utilizator admin) {

        if (!(admin instanceof Bibliotecar)) {
            return false;
        }


        String parolaDeSalvat = parolaSimpla;

        Utilizator nou;
        if ("Cititor".equalsIgnoreCase(rol)) {
            nou = new Cititor(0, username, parolaDeSalvat, email);
        } else {
            nou = new Bibliotecar(0, username, parolaDeSalvat, email);
        }

        return userRepo.addUser(nou);
    }


    public List<Carte> obtineToateCartile() { return bookRepo.getAllBooks(); }
    public List<Carte> cauta(String criteriu, String text) { return bookRepo.searchBooks(criteriu, text); }
    public boolean adaugaCarte(Carte c, Utilizator user) { return (user instanceof Bibliotecar) ? bookRepo.addBook(c) : false; }
    public boolean modificaStoc(int id, int stoc, Utilizator user) { return (user instanceof Bibliotecar) ? bookRepo.updateStock(id, stoc) : false; }
    public boolean stergeCarte(int id, Utilizator user) { return (user instanceof Bibliotecar) ? bookRepo.deleteBook(id) : false; }
}