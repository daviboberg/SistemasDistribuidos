package Resources;

import java.io.Serializable;

public interface Resource extends Serializable {

    public boolean equals(Resource resource);
    public int getId();

    public void insert();
    public void delete();

    public void update();
    public Reference getReference();
}
