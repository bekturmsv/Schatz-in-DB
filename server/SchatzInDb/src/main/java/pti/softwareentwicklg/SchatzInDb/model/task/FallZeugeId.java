package pti.softwareentwicklg.SchatzInDb.model.task;

import java.io.Serializable;
import java.util.Objects;

public class FallZeugeId implements Serializable {
    private Integer fallId;
    private Integer zeugeId;

    public FallZeugeId() {
    }

    public FallZeugeId(Integer fallId, Integer zeugeId) {
        this.fallId = fallId;
        this.zeugeId = zeugeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FallZeugeId that)) return false;
        return Objects.equals(fallId, that.fallId) &&
                Objects.equals(zeugeId, that.zeugeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fallId, zeugeId);
    }
}