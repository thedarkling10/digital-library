package biblioteca.model;

import biblioteca.enums.TipAbonament;
import biblioteca.util.IdGenerator;

public class Abonament implements Calculable, Printable {
    private int id;
    private String cititorId;
    private TipAbonament tip;
    private int dataInceput;
    private int dataExpirare;
    private int limitaImprumuturi;

    public Abonament() {
    }

    public Abonament(String cititorId, TipAbonament tip, int dataInceput, int dataExpirare, int limitaImprumuturi) {
        this.id = IdGenerator.nextId(Abonament.class);
        this.cititorId = cititorId;
        this.tip = tip;
        this.dataInceput = dataInceput;
        this.dataExpirare = dataExpirare;
        this.limitaImprumuturi = limitaImprumuturi;
    }

    public Abonament(int id, String cititorId, TipAbonament tip, int dataInceput, int dataExpirare, int limitaImprumuturi) {
        this.id = id;
        this.cititorId = cititorId;
        this.tip = tip;
        this.dataInceput = dataInceput;
        this.dataExpirare = dataExpirare;
        this.limitaImprumuturi = limitaImprumuturi;
    }

    public int getId() {
        return id;
    }

    public String getCititorId() {
        return cititorId;
    }

    public TipAbonament getTip() {
        return tip;
    }

    public int getDataInceput() {
        return dataInceput;
    }

    public int getDataExpirare() {
        return dataExpirare;
    }

    public int getLimitaImprumuturi() {
        return limitaImprumuturi;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCititorId(String cititorId) {
        this.cititorId = cititorId;
    }

    public void setTip(TipAbonament tip) {
        this.tip = tip;
    }

    public void setDataInceput(int dataInceput) {
        this.dataInceput = dataInceput;
    }

    public void setDataExpirare(int dataExpirare) {
        this.dataExpirare = dataExpirare;
    }

    public void setLimitaImprumuturi(int limitaImprumuturi) {
        this.limitaImprumuturi = limitaImprumuturi;
    }

    @Override
    public double calculeazaRecenzie() {
        int zileRamase = this.dataExpirare - this.dataInceput;
        System.out.println("Zile ramase pana la expirarea abonamentului: " + zileRamase);
        return zileRamase;
    }

    public boolean isExpired(int dataActuala) {
        return dataActuala > this.dataExpirare;
    }

    public int getRemainingDays(int dataActuala) {
        return Math.max(0, this.dataExpirare - dataActuala);
    }

    @Override
    public String printDetails() {
        return String.format("ID=%d: CititorID=%s, Tip=%s, Inceput=%d, Expirare=%d, Limita=%d%n",
                id, cititorId, tip != null ? tip.name() : "null", dataInceput, dataExpirare, limitaImprumuturi);
    }
}
