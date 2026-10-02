package org.example.notificationsystem3728.model;

import jakarta.persistence.*;

@Entity
@Table(name = "list_subscribers")
public class ListSubscribers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String division;
    private String rankValue;
    private String family;
    private String phoneNumber;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_id")
    private ListAlarm alarm;


    public ListSubscribers() {

    }

    public ListSubscribers(Long id, String division, String rankValue, String family, String phoneNumber, boolean enabled, ListAlarm alarm) {
        this.id = id;
        this.division = division;
        this.rankValue = rankValue;
        this.family = family;
        this.phoneNumber = phoneNumber;
        this.enabled = enabled;
        this.alarm = alarm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getRankValue() {
        return rankValue;
    }

    public void setRankValue(String rankValue) {
        this.rankValue = rankValue;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ListAlarm getAlarm() {
        return alarm;
    }

    public void setAlarm(ListAlarm alarm) {
        this.alarm = alarm;
    }
}
