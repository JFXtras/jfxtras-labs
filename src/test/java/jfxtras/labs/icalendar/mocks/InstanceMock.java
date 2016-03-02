package jfxtras.labs.icalendar.mocks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Comparator;

public class InstanceMock
{    
    public final static Comparator<InstanceMock> INSTANCE_MOCK_COMPARATOR = (i1, i2) -> 
    {
        LocalDateTime ld1 = (i1.getStartTemporal().isSupported(ChronoUnit.NANOS)) ? LocalDateTime.from(i1.getStartTemporal()) : LocalDate.from(i1.getStartTemporal()).atStartOfDay();
        LocalDateTime ld2 = (i2.getStartTemporal().isSupported(ChronoUnit.NANOS)) ? LocalDateTime.from(i2.getStartTemporal()) : LocalDate.from(i2.getStartTemporal()).atStartOfDay();
        return ld1.compareTo(ld2);
    };

    public boolean isWholeDay() { return ! getStartTemporal().isSupported(ChronoUnit.NANOS); }
    
    private Temporal startTemporal;
    public Temporal getStartTemporal() { return startTemporal; }
    public void setStartTemporal(Temporal value) { startTemporal = value; }
    public InstanceMock withStartTemporal(Temporal value) { setStartTemporal(value); return this; }

    private Temporal endTemporal;
    public Temporal getEndTemporal() { return endTemporal; }
    public void setEndTemporal(Temporal value) { endTemporal = value; }
    public InstanceMock withEndTemporal(Temporal value) { setEndTemporal(value); return this; }
    
    private String summary;
    public String getSummary() { return summary; }
    public void setSummary(String value) { summary = value; }
    public InstanceMock withSummary(String value) { setSummary(value); return this; }
    
    public static boolean isEqualTo(InstanceMock i1, InstanceMock i2)
    {
        boolean startEquals = i1.getStartTemporal().equals(i2.getStartTemporal());
        boolean endEquals = i1.getEndTemporal().equals(i2.getEndTemporal());
        boolean summaryEquals = i1.getSummary().equals(i2.getSummary());
        return startEquals && endEquals && summaryEquals;
    }
    
    @Override
    public String toString()
    {
        return super.toString()
             + ", "
             + this.getStartTemporal()
             + " - "
             + this.getEndTemporal()
             ;
    }
}
