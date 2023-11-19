/**

 This class represents a music band with all its characteristics.
 */
package common;

import managers.file.CSVSavable;
import result.Result;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MusicBand implements Comparable<MusicBand>, IDAccess, Serializable, CSVSavable {

    /**
     * This field contains an intermediate value for the unique identifier of the group.
     */
    private static long idCounter = 0;

    /**
     * This field contains the unique identifier of the group.
     */
    private long id;

    /**
     * This field contains the name of the group.
     */
    private String name;

    /**
     * This field contains the coordinates of the group.
     */
    private Coordinates coordinates;

    /**
     * This field contains the date of the group's creation.
     */
    private LocalDate creationDate;

    /**
     * This field contains the number of participants in the group.
     */
    private long numberOfParticipants;

    /**
     * This field contains the genre of the group.
     */
    private MusicGenre genre;

    /**
     * The best album of the group.
     */
    private Album bestAlbum;

    /**
     * Creates a new music band with the given parameters.
     *
     * @param name                 the name of the group
     * @param coordinates          the coordinates of the group
     * @param creationDate         the date of the group's creation
     * @param numberOfParticipants the number of participants in the group
     * @param genre                the genre of the group
     * @param bestAlbum            the best album of the group
     */
    public MusicBand(String name, Coordinates coordinates, LocalDate creationDate, long numberOfParticipants, MusicGenre genre, Album bestAlbum) {
        this.id = idCounter++;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.numberOfParticipants = numberOfParticipants;
        this.genre = genre;
        this.bestAlbum = bestAlbum;
    }

    /**
     * Creates a new music band with the given parameters and id.
     *
     * @param id                   the id of the group
     * @param name                 the name of the group
     * @param coordinates          the coordinates of the group
     * @param creationDate         the date of the group's creation
     * @param numberOfParticipants the number of participants in the group
     * @param genre                the genre of the group
     * @param bestAlbum            the best album of the group
     */
    public MusicBand(int id, String name, Coordinates coordinates, LocalDate creationDate, long numberOfParticipants, MusicGenre genre, Album bestAlbum) {
        this.id = id;
        idCounter = Math.max(idCounter, id + 1);
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.numberOfParticipants = numberOfParticipants;
        this.genre = genre;
        this.bestAlbum = bestAlbum;
    }

    /**
     * Sets the value for the idCounter counter.
     *
     * @param idCounter the new value for the idCounter counter
     */
    public static void setIdCounter(long idCounter) {
        MusicBand.idCounter = idCounter;
    }

    /**
     * Creates a new music band with the given name and increments the id by 1.
     *
     * @param name the name of the group
     */
    public MusicBand(String name) {
        this.name = name;
        this.id = idCounter++;
    }

    /**
     * Returns the current value of the idCounter counter.
     *
     * @return the current value of the idCounter counter
     */
    public static long getIdCounter() {
        return idCounter;
    }

    /**
     * Returns the id of the group.
     *
     * @return the id of the group
     */
    @Override
    public long getID() {
        return id;
    }

    /**
     * Set the id of the group.
     * @param id the id of the group
     */

    @Override
    public void setID(long id) {
        this.id = id;
        idCounter = Math.max(idCounter, id + 1);
    }

    /**
     * Returns the name of the group.
     *
     * @return name of the group
     */
    public String getName() {
        return name;
    }
    /**

     Sets the name of the music band.
     @param name the name of the music band
     */
    public void setName(String name) {
        this.name = name;
    }
    /**

     Returns the coordinates of the music band.
     @return the coordinates of the music band
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }
    /**

     Sets the coordinates of the music band.
     @param coordinates the coordinates of the music band
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    /**

     Returns the creation date of the music band.
     @return the creation date of the music band
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }
    /**

     Sets the creation date of the music band.
     @param creationDate the creation date of the music band
     */
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
    /**

     Returns the number of participants in the music band.
     @return the number of participants in the music band
     */
    public long getNumberOfParticipants() {
        return numberOfParticipants;
    }
    /**

     Sets the number of participants in the music band.
     @param numberOfParticipants the number of participants in the music band
     */
    public void setNumberOfParticipants(long numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }
    /**

     Returns the genre of the music band.
     @return the genre of the music band
     */
    public MusicGenre getGenre() {
        return genre;
    }
    /**

     Sets the genre of the music band.
     @param genre the genre of the music band
     */
    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }
    /**

     Returns the best album of the music band.
     @return the best album of the music band
     */
    public Album getBestAlbum() {
        return bestAlbum;
    }
    /**
     *

     Sets the best album of the music band.
     @param bestAlbum the best album of the music band
     */
    public void setBestAlbum(Album bestAlbum) {
        this.bestAlbum = bestAlbum;
    }
    /**

     Compares the music band with another music band.

     @param other the other music band to compare to

     @return an int value indicating which of the two values is greater or smaller, or equal to zero
     */
    @Override
    public int compareTo(MusicBand other) {
        int result = this.name.compareTo(other.name);
        if (result != 0) {
            return result;
        }

        result = this.coordinates.compareTo(other.coordinates);
        if (result != 0) {
            return result;
        }

        result = this.creationDate.compareTo(other.creationDate);
        if (result != 0) {
            return result;
        }

        result = Long.compare(this.numberOfParticipants, other.numberOfParticipants);
        if (result != 0) {
            return result;
        }

        result = this.genre.compareTo(other.genre);
        if (result != 0) {
            return result;
        }

        result = this.bestAlbum.compareTo(other.bestAlbum);
        return result;
    }

    /**

     Returns a string representation of the music band.
     @return a string representation of the music band
     */
    @Override
    public String toString() {
        return "MusicBand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", numberOfParticipants=" + numberOfParticipants +
                ", genre=" + genre +
                ", bestAlbum=" + bestAlbum +
                '}';
    }

    public Result<String> toCSV() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(this.getID()).append(",").
                    append(this.getName()).append(",").
                    append(this.getCoordinates().getX()).append(",").
                    append(this.getCoordinates().getY()).append(",").
                    append(this.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",").
                    append(this.getNumberOfParticipants()).append(",");
            Album album = this.getBestAlbum();
            if (this.getBestAlbum() != null) {
                sb.append(album.getName()).append(",");
                sb.append(album.getTracks()).append(",");
                sb.append(album.getLength()).append(",");
                sb.append(album.getSales()).append(",");
            } else {
                sb.append(",");
            }
            sb.append(this.getGenre());
            return Result.success(sb.toString(), null);
        } catch (Exception e) {
            return Result.failure(e, "Error with parsing CSV format");
        }
    }
}