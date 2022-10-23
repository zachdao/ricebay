package edu.rice.comp610.model;

import java.util.Objects;
import java.util.UUID;

public class Rating {

    private UUID id;
    private Double rating;
    private UUID rater_id;
    private UUID seller_id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRater_id() {
        return rater_id;
    }

    public void setRater_id(UUID rater_id) {
        this.rater_id = rater_id;
    }

    public UUID getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(UUID seller_id) {
        this.seller_id = seller_id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    /**
     * Followed another example, needs to be checked (A. Hyatt 2022/10/22)
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating1 = (Rating) o;
        return Objects.equals(id, rating1.id)
                && Objects.equals(rating, rating1.rating)
                && Objects.equals(rater_id, rating1.rater_id)
                && Objects.equals(seller_id, rating1.seller_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, rater_id, seller_id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", rater_id='" + rater_id + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", rating=" + rating +
                '}';
    }
}
