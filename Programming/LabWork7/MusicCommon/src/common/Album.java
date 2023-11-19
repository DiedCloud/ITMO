/**
 * The Album class represents a music album and contains information about its name, number of tracks, length, and sales.
 */
package common;

import java.io.Serializable;

public class Album implements Serializable {

    static final float epsilon = 0.01f;

    private String name;

    private long tracks;

    private Long length;

    private Float sales;

    /**
     * Constructor creates a new Album object with specified parameters.
     *
     * @param name   the name of the album
     * @param tracks the number of tracks in the album
     * @param length the duration of the album in milliseconds
     * @param sales  the sales of the album in dollars
     */
    public Album(String name, long tracks, Long length, Float sales) {
        this.name = name;
        this.tracks = tracks;
        this.length = length;
        this.sales = sales;
    }

    /**
     * This method returns the name of the album.
     *
     * @return the name of the album
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the number of tracks in the album.
     *
     * @return the number of tracks in the album
     */
    public Long getTracks() {
        return tracks;
    }

    /**
     * This method returns the duration of the album in milliseconds.
     *
     * @return the duration of the album in milliseconds
     */
    public Long getLength() {
        return length;
    }

    /**
     * This method returns the sales of the album in dollars.
     *
     * @return the sales of the album in dollars
     */
    public Float getSales() {
        return sales;
    }

    /**
     * This method sets the name of the album.
     *
     * @param name the name of the album
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method sets the number of tracks in the album.
     *
     * @param tracks the number of tracks in the album
     */
    public void setTracks(long tracks) {
        this.tracks = tracks;
    }

    /**
     * This method sets the duration of the album in milliseconds.
     *
     * @param length the duration of the album in milliseconds
     */
    public void setLength(Long length) {
        this.length = length;
    }

    /**
     * This method sets the sales of the album in dollars.
     *
     * @param sales the sales of the album in dollars
     */
    public void setSales(Float sales) {
        this.sales = sales;
    }

    /**
     * This method overrides the toString() method and returns a string representation of the Album object.
     *
     * @return a string representation of the Album object
     */
    @Override
    public String toString() {
        return "common.Album{" +
                "name='" + name + '\'' +
                ", tracks=" + tracks +
                ", length=" + length +
                ", sales=" + sales +
                '}';
    }

    /**
     * This method compares the current Album object with the passed Album object.
     * Comparison is made based on the following criteria:
     * Name of the album (name)
     * Number of tracks in the album (tracks)
     * Duration of the album (length)
     @param otherAlbum The Album object to compare with the current Album object.
     @return true if the two objects are equal based on the above criteria, false otherwise.
     */
    public boolean equals(Album otherAlbum) {
        if (otherAlbum == null) {
            return false;
        }
        if (this == otherAlbum) {
            return true;
        }
        if (!this.name.equals(otherAlbum.name)) {
            return false;
        }
        if (this.tracks != otherAlbum.tracks) {
            return false;
        }
        return this.length.equals(otherAlbum.length);
    }

    /**
     * Method compares the current Album object with the passed Album object.
     * Comparison is made based on the following criteria:
     * Name of the album (name)
     * Number of tracks (tracks)
     * Duration of the album (length)
     * Sales of album (sales)
     *
     * @param bestAlbum The Album object to compare with the current Album object.
     * @return number less than 0 if the current Album object is less than the passed Album object,
     *        number greater than 0 if the current Album object is greater than the passed Album object,
     *        0 if the two objects are equal based on the above criteria.
     */
    public int compareTo(Album bestAlbum) {
        int result = this.name.compareTo(bestAlbum.name);
        if (result != 0) {
            return result;
        }

        result = Long.compare(this.tracks, bestAlbum.tracks);
        if (result != 0) {
            return result;
        }

        result = Long.compare(this.length, bestAlbum.length);
        if (result != 0) {
            return result;
        }

        if (Math.abs(this.sales - bestAlbum.sales) < epsilon) {
            return 0;
        } else if (this.sales < bestAlbum.sales) {
            return -1;
        } else {
            return 1;
        }
    }
}
