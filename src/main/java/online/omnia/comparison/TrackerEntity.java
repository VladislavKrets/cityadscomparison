package online.omnia.comparison;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lollipop on 12.07.2017.
 */
@Table(name = "trackers")
@Entity
public class TrackerEntity {
    @Id
    @Column(name = "prefix")
    private String prefix;
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "domain", length = 100)
    private String domain;
    @Column(name = "ip", length = 20)
    private String ip;
    @Column(name = "modulename", length = 50)
    private String moduleName;

    public TrackerEntity() {
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }

    public String getIp() {
        return ip;
    }

    public String getModuleName() {
        return moduleName;
    }
}
