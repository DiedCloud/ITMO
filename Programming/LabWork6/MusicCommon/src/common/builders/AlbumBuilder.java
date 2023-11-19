package common.builders;

import common.Album;

import java.io.Serializable;

/**

 The AlbumBuilder class is a builder class for objects of the Album class.
 */
public class AlbumBuilder implements Buildable<Album>, Serializable {
    private String name;
    private long tracks;
    private Long length;
    private Float sales;

    /**

     A method for setting the name of the album.
     @param name - The name of the album
     @return Returns the current instance of the AlbumBuilder class
     */
    public AlbumBuilder setName(String name) {
        this.name = name;
        return this;
    }
    /**

     A method for setting the number of tracks in the album.
     @param tracks - The number of tracks in the album
     @return Returns the current instance of the AlbumBuilder class
     */
    public AlbumBuilder setTracks(Long tracks) {
        if (tracks <= 0)
            throw new IllegalArgumentException("The number of tracks in the album cannot be negative.");
        this.tracks = tracks;
        return this;
    }
    /**

     A method for setting the length of the album in milliseconds.
     @param length - The length of the album in milliseconds
     @return Returns the current instance of the AlbumBuilder class
     */
    public AlbumBuilder setLength(Long length) {
        if (length <= 0)
            throw new IllegalArgumentException("The length of the album cannot be negative.");
        this.length = length;
        return this;
    }
    /**

     A method for setting the number of sales of the album.
     @param sales - The number of sales of the album
     @return Returns the current instance of the AlbumBuilder class
     */
    public AlbumBuilder setSales(Float sales) {
        if(sales <= 0)
            throw new IllegalArgumentException("The number of sales of the album cannot be negative.");

        this.sales = sales;
        return this;
    }
    /**

     A method that creates a new Album object with the set field values.
     @return Returns a new Album object with the set field values.
     */

    @Override
    public Album build() {
        return new Album(name, tracks, length, sales);
    }
}