package Resources;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface Resource extends Serializable {

    public boolean equals(Resource resource);
    public int getId();

    public void insert();
    public void delete();

    public void update();
    public Reference getReference();

    public String getDestiny();
    public float getPrice();

    public List<Resource> find() throws SQLException;
}
