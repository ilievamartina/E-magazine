package mk.ukim.finki.emagazine.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Data
@Entity
public class Photo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;
    private Long size;
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] data;
    private String base64;

    public Photo(String name, Long size, byte[] data) {
        this.name = name;
        this.size = size;
        this.data = data;
    }

    public Photo() {
    }


}
