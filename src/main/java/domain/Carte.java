package domain;
public class Carte {
    private int id;
    private String titlu;
    private String autor;
    private String isbn;
    private int stoc;
    private int anPublicare;

    public Carte(int id, String titlu, String autor, String isbn, int stoc, int anPublicare) {
        this.id = id;
        this.titlu = titlu;
        this.autor = autor;
        this.isbn = isbn;
        this.stoc = stoc;
        this.anPublicare = anPublicare;
    }

    public String getTitlu() { return titlu; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public int getStoc() { return stoc; }

    public int getAnPublicare() {return anPublicare; }
    }

