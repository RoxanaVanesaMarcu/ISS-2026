package domain;

public abstract class Utilizator {
    protected int id;
    protected String username;
    protected String parola;
    protected String email;
    protected String rol;


    public Utilizator(int id, String username, String parola, String email, String rol) {
        this.id = id;
        this.username = username;
        this.parola = parola;
        this.email = email;
        this.rol = rol;
    }

    public String getUsername() { return username; }
    public String getParola() { return parola; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
}