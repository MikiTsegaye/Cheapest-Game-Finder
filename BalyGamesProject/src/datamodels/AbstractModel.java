package datamodels;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractModel<ID extends Serializable> implements Serializable {
    public abstract ID getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractModel<?> that = (AbstractModel<?>) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}