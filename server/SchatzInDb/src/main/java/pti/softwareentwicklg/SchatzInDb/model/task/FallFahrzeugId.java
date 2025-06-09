package pti.softwareentwicklg.SchatzInDb.model.task;

import java.io.Serializable;
import java.util.Objects;

public class FallFahrzeugId implements Serializable {
    private Integer fallId;
    private Integer fahrzeugId;

    public FallFahrzeugId() {
    }

    public FallFahrzeugId(Integer fallId, Integer fahrzeugId) {
        this.fallId = fallId;
        this.fahrzeugId = fahrzeugId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FallFahrzeugId that)) return false;
        return Objects.equals(fallId, that.fallId) &&
                Objects.equals(fahrzeugId, that.fahrzeugId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fallId, fahrzeugId);
    }
}