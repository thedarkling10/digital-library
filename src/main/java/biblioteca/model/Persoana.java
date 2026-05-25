package biblioteca.model;

import biblioteca.util.IdGenerator;

public abstract class Persoana {
    private final int id;
    private String nume, prenume;
    private int varsta;

    protected Persoana(String nume, String prenume, int varsta) {
        if(nume == null || nume.isBlank()){
            throw new IllegalArgumentException("Completati numele de familie!");
        }
        if(prenume == null || prenume.isBlank()){
            throw new IllegalArgumentException("Completati prenumele!");
        }
        if(varsta <= 0){
            throw new IllegalArgumentException("Completati varsta!");
        }

        this.id = IdGenerator.nextId(getClass());
        this.nume = nume;
        this.prenume = prenume;
        this.varsta = varsta;
    }

    protected Persoana(int id, String nume, String prenume, int varsta) {
        if(id <= 0){
            throw new IllegalArgumentException("Id-ul nu poate fi negativ sau zero!");
        }
        if(nume == null || nume.isBlank()){
            throw new IllegalArgumentException("Completati numele de familie!");
        }
        if(prenume == null || prenume.isBlank()){
            throw new IllegalArgumentException("Completati prenumele!");
        }
        if(varsta <= 0){
            throw new IllegalArgumentException("Completati varsta!");
        }

        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.varsta = varsta;
    }

    public int getId(){
        return id;
    }

    public String getNume(){
        return nume;
    }

    public String getPrenume(){
        return prenume;
    }

    public int getVarsta(){
        return varsta;
    }
}


