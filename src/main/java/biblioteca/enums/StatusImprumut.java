package biblioteca.enums;

public enum StatusImprumut {
    ACTIVE("Activ"),
    RETURNED("Returnat"),
    OVERDUE("Intarziat");

    private final String status;
    private StatusImprumut(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
