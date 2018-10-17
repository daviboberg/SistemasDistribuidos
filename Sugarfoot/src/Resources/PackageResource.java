package Resources;

import java.sql.SQLException;
import java.util.List;

public class PackageResource implements Resource {

    public Hotel hotel;
    public Airplane aiplane_going;
    public Airplane airplane_return;

    @Override
    public boolean equals(Resource resource) {
        return false;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void insert() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }

    @Override
    public Reference getReference() {
        return null;
    }

    @Override
    public String getDestiny() {
        return null;
    }

    @Override
    public float getPrice() {
        return 0;
    }

    @Override
    public List<Resource> find() throws SQLException {
        return null;
    }
}
