package Resources;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface Resource extends Serializable {

    /**
     * @param resource
     * @return boolean
     */
    boolean equals(Resource resource);

    /**
     * @return int
     */
    int getId();

    /**
     *
     */
    void setId(int id);


    /**
     *
     */
    void insert();

    /**
     *
     */
    void delete();

    /**
     *
     */
    void update();

    /**
     * @return Reference
     */
    Reference getReference();

    /**
     * @return String
     */
    String getDestiny();

    /**
     * @return
     */
    float getPrice();

    /**
     * @return  List<Resource>
     * @throws SQLException
     */
    List<Resource> find() throws SQLException;

    /**
     *
     */
    void buy();
}
