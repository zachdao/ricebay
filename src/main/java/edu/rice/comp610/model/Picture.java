package edu.rice.comp610.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * A picture in the RiceBay system.
 *
 * <p>Represents a photo that is attached to an auction item.</p>
 */

public class Picture {
    private int id;
    private String name;
    private int sequenceNum;
    private byte[] image;
    private UUID auctionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public UUID getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(UUID auctionId) {
        this.auctionId = auctionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(id, picture.id)
                && Objects.equals(name, picture.name)
                && Objects.equals(sequenceNum, picture.sequenceNum)
                && Arrays.equals(image, picture.image)
                && Objects.equals(auctionId, picture.auctionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sequenceNum, Arrays.hashCode(image), auctionId);
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sequenceNum='" + sequenceNum + '\'' +
                ", auctionId=" + auctionId +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
