package model;

import java.util.List;

public class Movie {
    private int id;
    private String title;
    private String description;
    private int duration;

    private List<Director> directors;
    private List<String> genres;
    private List<Actor> actors;

    public Movie(int id, String title, String description, int duration, List<Director> directors, List<String> genres, List<Actor> actors) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.directors = directors;
        this.genres = genres;
        this.actors = actors;
    }

    public Movie(String title, String description, int duration, List<Director> directors, List<String> genres, List<Actor> actors) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.directors = directors;
        this.genres = genres;
        this.actors = actors;
    }

    public Movie(int id, String title, String description, int duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    
}
