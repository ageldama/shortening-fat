package jhyun.jh.storage.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@ApiModel("Saved URL and its ID value")
@Builder
@ToString
@EqualsAndHashCode
@Table(name = "urls", uniqueConstraints = {
        @UniqueConstraint(columnNames = "url")
})
@Entity
public class Url {
    @Getter
    @Setter
    @ApiModelProperty(value = "생성된 ID", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Getter
    @Setter
    @ApiModelProperty(value = "원본URL", required = true)
    @NonNull
    @Column(name = "url")
    private String url;

    public Url() {
    }

    public Url(Integer id, String url) {
        this.id = id;
        this.url = url;
    }

}
