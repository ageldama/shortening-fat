package jhyun.jh.repositories;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(name = "urls", uniqueConstraints = @UniqueConstraints())
@Entity
public class Url {
}
