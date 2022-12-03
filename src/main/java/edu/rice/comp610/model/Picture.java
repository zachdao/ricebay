package edu.rice.comp610.model;

import edu.rice.comp610.store.PrimaryKey;

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
    private String pictureName;
    private int pictureSequence;
    private byte[] pictureData;
    private UUID auctionId;

    @PrimaryKey(generated = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public int getPictureSequence() {
        return pictureSequence;
    }

    public void setPictureSequence(int pictureSequence) {
        this.pictureSequence = pictureSequence;
    }

    public byte[] getPictureData() {
        return pictureData;
    }

    public void setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
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
                && Objects.equals(pictureName, picture.pictureName)
                && Objects.equals(pictureSequence, picture.pictureSequence)
                && Arrays.equals(pictureData, picture.pictureData)
                && Objects.equals(auctionId, picture.auctionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pictureName, pictureSequence, Arrays.hashCode(pictureData), auctionId);
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", name='" + pictureName + '\'' +
                ", sequenceNum='" + pictureSequence + '\'' +
                ", auctionId=" + auctionId +
                ", image=" + Arrays.toString(pictureData) +
                '}';
    }
}
