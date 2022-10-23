package edu.rice.comp610.model;

import java.util.Objects;
import java.util.UUID;

public class PaymentDetail {

    private UUID id;
    private String zelle_id;
    private UUID owner_id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getZelle_id() {
        return zelle_id;
    }

    public void setId(String zelle_id) {
        this.zelle_id = zelle_id;
    }

    public UUID getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(UUID owner_id) {
        this.owner_id = owner_id;
    }

    /**
     * Followed another example, needs to be checked (A. Hyatt 2022/10/22)
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDetail paymentDetail = (PaymentDetail) o;
        return Objects.equals(id, paymentDetail.id)
                && Objects.equals(zelle_id, paymentDetail.zelle_id)
                && Objects.equals(owner_id, paymentDetail.owner_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zelle_id, owner_id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", zelle_id='" + zelle_id + '\'' +
                ", owner_id='" + owner_id +
                '}';
    }
}
