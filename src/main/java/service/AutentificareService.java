package service;

import domain.Utilizator;
import repository.UserRepository;

public class AutentificareService {
    private UserRepository userRepo = new UserRepository();
    public Utilizator login(String username, String parolaSimpla) {
        Utilizator user = userRepo.getUserByUsername(username);

        if (user != null) {

            if (user.getParola().equals(parolaSimpla)) {
                return user;
            }
        }
        return null;
    }
}