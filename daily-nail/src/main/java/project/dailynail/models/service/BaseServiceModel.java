package project.dailynail.models.service;

public abstract class BaseServiceModel {
    private String id;

    public BaseServiceModel() {
    }

    public String getId() {
        return id;
    }

    public BaseServiceModel setId(String id) {
        this.id = id;
        return this;
    }
}
