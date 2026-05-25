package biblioteca.model;

import java.sql.Date;
import biblioteca.util.IdGenerator;

import biblioteca.enums.StatusImprumut;

public class Imprumut implements Printable {
    private int id;
    private int carteId;    
    private int cititorId;
    private Date dataImprumut;
    private Date dataScadenta;
    private Date dataReturnare; 
    private StatusImprumut status;

    public Imprumut(int carteId, int cititorId, Date dataImprumut, Date dataScadenta, Date dataReturnare, StatusImprumut status) {
        this.id = IdGenerator.nextId(Imprumut.class);
        this.carteId = carteId;
        this.cititorId = cititorId;
        this.dataImprumut = dataImprumut;
        this.dataScadenta = dataScadenta;
        this.dataReturnare = dataReturnare;
        this.status = status;
    }

    public Imprumut(int id, int carteId, int cititorId, Date dataImprumut, Date dataScadenta, 
                    Date dataReturnare, StatusImprumut status) {
        this.id = id;
        this.carteId = carteId;
        this.cititorId = cititorId;
        this.dataImprumut = dataImprumut;
        this.dataScadenta = dataScadenta;
        this.dataReturnare = dataReturnare;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getCarteId() {
        return carteId;
    }

    public int getCititorId() {
        return cititorId;
    }

    public Date getDataImprumut() {
        return dataImprumut;
    }

    public Date getDataScadenta() {
        return dataScadenta;
    }

    public Date getDataReturnare() {
        return dataReturnare;
    }

    public StatusImprumut getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCarteId(int carteId) {
        this.carteId = carteId;
    }

    public void setCititorId(int cititorId) {
        this.cititorId = cititorId;
    }

    public void setDataImprumut(Date dataImprumut) {
        this.dataImprumut = dataImprumut;
    }

    public void setDataScadenta(Date dataScadenta) {
        this.dataScadenta = dataScadenta;
    }

    public void setDataReturnare(Date dataReturnare) {
        this.dataReturnare = dataReturnare;
    }

    public void setStatus(StatusImprumut status) {
        this.status = status;
    }

    @Override
    public String printDetails() {
        return String.format("ID=%d: CarteID=%d, CititorID=%d, Imprumutat=%s, Scadenta=%s, Returnare=%s, Status=%s%n",
                id, carteId, cititorId,
                String.valueOf(dataImprumut), String.valueOf(dataScadenta), String.valueOf(dataReturnare),
                (status != null ? status.name() : "null"));
    }
}
