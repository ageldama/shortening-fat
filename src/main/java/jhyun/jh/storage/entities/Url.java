package jhyun.jh.storage.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@ApiModel("Saved URL and its ID value")
@Data
@Builder
@Table(name = "urls", uniqueConstraints = {
        @UniqueConstraint(columnNames = "url")
})
@Entity
public class Url {
    @ApiModelProperty(value = "생성된 ID", required = false)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ApiModelProperty(value = "원본URL", required = true)
    @NonNull
    @Column(name = "url")
    private String url;
}
